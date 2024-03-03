package net.tiagofar78.prisonescape.game;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.tiagofar78.prisonescape.PrisonEscape;

public class PrisonEscapeGame {
	
	private static final int TICKS_PER_SECOND = 20;
	
	private Settings _settings;
	
	private int _currentDay;
	private DayPeriod _dayPeriod; 
	private PrisonBuilding _prison;
	
	private List<PrisonEscapePlayer> _players;
	
	private PrisonEscapeTeam _policeTeam;
	private PrisonEscapeTeam _prisionersTeam;
	
	private String _state;
	
	public PrisonEscapeGame(String mapName, Location referenceBlock, String hostName) {
		_settings = new Settings();
		
		_currentDay = 0;
		
		_players = new ArrayList<>();
		
		_policeTeam = new PrisonEscapeTeam();
		_prisionersTeam = new PrisonEscapeTeam();
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
	
//	########################################
//	#                Phases                #
//	########################################
	
	private void startWaitingPhase() {
		// Nothing
	}
	
	private void startOngoingPhase() {
		startDay();
		
		distributePlayersPerTeams();
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
	
	private void startFinishedPhase() {
		
	}	
	
//	########################################
//	#                 Time                 #
//	########################################
	
	private void startDay() {
		_dayPeriod = DayPeriod.DAY;
		_currentDay++;
		
		// TODO check if it was finished
		
		// TODO reload chests
		
		Bukkit.getScheduler().runTaskLater(PrisonEscape.getPrisonEscape(), new Runnable() {
			
			@Override
			public void run() {
				startNight();
			}
		}, _settings.getDayDuration() * TICKS_PER_SECOND);
	}
	
	private void startNight() {
		// TODO check if it was finished
		
		_dayPeriod = DayPeriod.NIGHT;
		
		Bukkit.getScheduler().runTaskLater(PrisonEscape.getPrisonEscape(), new Runnable() {
			
			@Override
			public void run() {
				if (_currentDay == _settings.getDays()) {
					startFinishedPhase(); // TODO cops won
				}
				else {
					startDay();
				}
			}
		}, _settings.getNightDuration() * TICKS_PER_SECOND);
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
