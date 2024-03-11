package net.tiagofar78.prisonescape.game;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.tiagofar78.prisonescape.PrisonEscape;
import net.tiagofar78.prisonescape.game.phases.Phase;
import net.tiagofar78.prisonescape.game.phases.Waiting;
import net.tiagofar78.prisonescape.game.prisonbuilding.PrisonBuilding;

public class PrisonEscapeGame {
	
	private static final int TICKS_PER_SECOND = 20;
	private static final String POLICE_TEAM_NAME = "Police";
	private static final String PRISIONERS_TEAM_NAME = "Prisioners";
	
	private Settings _settings;
	
	private int _currentDay;
	private DayPeriod _dayPeriod;
	private PrisonBuilding _prison;
	
	private List<PrisonEscapePlayer> _players;
	
	private PrisonEscapeTeam _policeTeam;
	private PrisonEscapeTeam _prisionersTeam;
	
	private Phase _phase;
	
	public PrisonEscapeGame(String mapName, Location referenceBlock, String hostName) {
		_settings = new Settings();
		
		_currentDay = 0;
		
		_players = new ArrayList<>();
		
		_policeTeam = new PrisonEscapeTeam(POLICE_TEAM_NAME);
		_prisionersTeam = new PrisonEscapeTeam(PRISIONERS_TEAM_NAME);
		
		_phase = new Waiting();
	}
	
//	#########################################
//	#                 Lobby                 #
//	#########################################
	
	/**
	* @return      0 if success<br> 
	* 				1 if rejoin<br>
	* 				-1 if already on game<br>
	* 				-2 if already started
	*/
	public int playerJoin(Player player) {
		String playerName = player.getName();
		
		if (isPlayerOnGame(playerName)) {
			return -1;
		}
		
		// TODO check if already started -2
		
		// TODO check if is rejoin and take action 1
		
		player.teleport(getWaitingLobbyLocation());
		_players.add(new PrisonEscapePlayer(playerName));
		
		return 0;
	}
	
	/**
	* @return      0 if success<br>
	* 				-1 if not on game
	*/
	public int playerLeft(Player player) {
		String playerName = player.getName();
		
		if (!isPlayerOnGame(playerName)) {
			return -1;
		}
		
		player.teleport(getLeavingLocation());
		for (int i = 0; i < _players.size(); i++) {
			if (_players.get(i).getName().equals(playerName)) {
				_players.remove(i);
				break;
			}
		}
		
		return 0;
	}
	
	private boolean isPlayerOnGame(String playerName) {
		for (int i = 0; i < _players.size(); i++) {
			if (_players.get(i).getName().equals(playerName)) {
				return true;
			}
		}
		
		return false;
	}
	
	private void distributePlayersPerTeams() {
		int preferPrisioner = 0;
		int preferPolice = 0;
		int noPreference = 0;
		
		for (int i = 0; i < _players.size(); i++) {
			TeamPreference preference = _players.get(i).getPreference();
			if (preference == TeamPreference.POLICE) {
				preferPolice++;
			}
			else if (preference == TeamPreference.PRISIONERS) {
				preferPrisioner++;
			}
			else if (preference == TeamPreference.RANDOM) {
				noPreference++;
			}
		}
		
		if (preferPrisioner <= _settings.getMaxPrisioners()) {
			//TODO finish this method
		}
		
		
	}
	
//	########################################
//	#                Phases                #
//	########################################
	
	private void startWaitingPhase() {
		// Nothing
	}
	
	private void startOngoingPhase() {		
		distributePlayersPerTeams();
		
		_phase.next();
		
		startDay();
	}
	
	private void startFinishedPhase(PrisonEscapeTeam winnerTeam) {
		_phase.next();
	}	
	
//	########################################
//	#                 Time                 #
//	########################################
	
	private void startDay() {
		if (_phase.isClockStoped()) {
			return;
		}
		
		_dayPeriod = DayPeriod.DAY;
		_currentDay++;
		
		// TODO reload chests
		
		Bukkit.getScheduler().runTaskLater(PrisonEscape.getPrisonEscape(), new Runnable() {
			
			@Override
			public void run() {
				startNight();
			}
		}, _settings.getDayDuration() * TICKS_PER_SECOND);
	}
	
	private void startNight() {
		if (_phase.isClockStoped()) {
			return;
		}
		
		_dayPeriod = DayPeriod.NIGHT;
		
		Bukkit.getScheduler().runTaskLater(PrisonEscape.getPrisonEscape(), new Runnable() {
			
			@Override
			public void run() {
				if (_currentDay == _settings.getDays()) {
					startFinishedPhase(_policeTeam);
				}
				else {
					startDay();
				}
			}
		}, _settings.getNightDuration() * TICKS_PER_SECOND);
	}
	
//	########################################
//	#                Events                #
//	########################################
	
	public void playerEscaped(String playerName) {
		PrisonEscapePlayer player = getPrisonEscapePlayer(playerName);
		if (player == null) {
			return;
		}
		
		player.escaped();
		
		if (_prisionersTeam.countArrestedPlayers() == 0) {
			startFinishedPhase(_prisionersTeam);
		}
	}
	
	public void playerMove(String playerName, Location loc) {
		PrisonEscapePlayer player = getPrisonEscapePlayer(playerName);
		if (player == null) {
			return;
		}
		
		if (_prison.isOutsidePrison(loc)) {
			player.giveLeavingPrisonItem();
		}
		else {
			player.removeLeavingPrisonItem();
		}
		
		// TODO check if metal detectors are triggered
	}
	
	public void playerAttack(String attackerName, String attackedName) {
		PrisonEscapePlayer attacker = getPrisonEscapePlayer(attackerName);
		if (attacker == null) {
			return;
		}
		
		PrisonEscapePlayer attacked = getPrisonEscapePlayer(attackedName);
		if (attacked == null) {
			return;
		}
		
		// TODO keep going
	}
	
//	########################################
//	#                 Util                 #
//	########################################
	
	private PrisonEscapePlayer getPrisonEscapePlayer(String playerName) {
		for (PrisonEscapePlayer player : _players) {
			if (player.getName().equals(playerName)) {
				return player;
			}
		}
		
		return null;
	}
	
//	#########################################
//	#               Locations               #
//	#########################################
	
	private Location getWaitingLobbyLocation() {
		return null; // TODO
	}
	
	private Location getLeavingLocation() {
		return null; // TODO
	}
	
}
