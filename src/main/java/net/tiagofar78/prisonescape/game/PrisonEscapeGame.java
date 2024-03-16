package net.tiagofar78.prisonescape.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import net.tiagofar78.prisonescape.PrisonEscape;
import net.tiagofar78.prisonescape.game.phases.Finished;
import net.tiagofar78.prisonescape.game.phases.Ongoing;
import net.tiagofar78.prisonescape.game.phases.Phase;
import net.tiagofar78.prisonescape.game.phases.Waiting;
import net.tiagofar78.prisonescape.game.prisonbuilding.PrisonBuilding;
import net.tiagofar78.prisonescape.game.prisonbuilding.PrisonEscapeLocation;
import net.tiagofar78.prisonescape.managers.ConfigManager;
import net.tiagofar78.prisonescape.managers.TeleportManager;

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

		if (_phase.hasGameStarted()) {
			return -2;
		}
		
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

	/**
	* @return      0 if success<br> 
	* 				-1 if cannot start game
	*/
	public int startGame() {
		if (!_phase.hasGameStarted() && hasMinimumPlayersToStart()) {
			startOngoingPhase();
			return 0;
		}
		return -1;
	}
	
//	########################################
//	#                Phases                #
//	########################################
	
	private void startWaitingPhase() {
		_phase.next();
	}
	
	private void startOngoingPhase() {
		distributePlayersPerTeam();

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
		if (_phase.isClockStopped()) {
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
		if (_phase.isClockStopped()) {
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
	
	public void playerMove(String playerName, PrisonEscapeLocation loc) {
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
		
		if (_prison.checkIfMetalDetectorTriggered(loc, player.getInventory())) {
			// TODO: Do beep
		}
	}
	
	public void playerTouch(String attackerName, String attackedName, PrisonEscapeItem item) {	
		PrisonEscapePlayer toucher = getPrisonEscapePlayer(attackerName);
		if (toucher == null) {
			return;
		}
		
		PrisonEscapePlayer touched = getPrisonEscapePlayer(attackedName);
		if (touched == null) {
			return;
		}
		
		if (_phase.isClockStopped()) {
			return;
		}
		
		if (_prisionersTeam.isOnTeam(toucher)) {
			return;
		}
		
		if (item == PrisonEscapeItem.HANDCUFS) {
			if (touched.isWanted()) {
				arrestPlayer(touched, toucher);
			}
		}
		else if (item == PrisonEscapeItem.SEARCH) {
			if (touched.hasIllegalItems()) {
				touched.setWanted();
			}
		}
	}
	
//	########################################
//	#                Arrest                #
//	########################################
	
	private void arrestPlayer(PrisonEscapePlayer arrested, PrisonEscapePlayer arrester) {
		TeleportManager.teleport(arrested, _prison.getSolitaryLocation());
		
		// TODO warn players
		
		Bukkit.getScheduler().runTaskLater(PrisonEscape.getPrisonEscape(), new Runnable() {
			
			@Override
			public void run() {
				if (_phase.isClockStopped()) {
					return;
				}
				
				arrested.removeWanted();
				
				// TODO warn arrested
				
				if (_dayPeriod == DayPeriod.DAY) {
					TeleportManager.teleport(arrested, _prison.getSolitaryExitLocation());
				}
				else if (_dayPeriod == DayPeriod.NIGHT) {
					int playerIndex = _prisionersTeam.getPlayerIndex(arrested);
					TeleportManager.teleport(arrested, _prison.getPlayerCellLocation(playerIndex));
				}
			}
		}, TICKS_PER_SECOND * _settings.getSecondsInSolitary());
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

	private boolean hasMinimumPlayersToStart() {
		return _players.size() >= ConfigManager.getInstance().getMinimumPlayers();
	}

	private void distributePlayersPerTeam() {
		int numberOfPlayers = _players.size();
		int requiredPrisioners = (int) Math.round(
				numberOfPlayers * ConfigManager.getInstance().getPrisionerRatio()
		);
		int requiredOfficers = (int) Math.round(
				numberOfPlayers * ConfigManager.getInstance().getOfficerRatio()
		);

		Collections.shuffle(_players);
		List<PrisonEscapePlayer> remainingPlayers = new ArrayList<>();

		for (PrisonEscapePlayer player : _players) {
			TeamPreference preference = player.getPreference();

			if (preference == TeamPreference.POLICE && requiredOfficers != 0) {
				_policeTeam.addMember(player);
				requiredOfficers--;
			} else if (preference == TeamPreference.PRISIONERS && requiredPrisioners != 0) {
				_prisionersTeam.addMember(player);
				requiredPrisioners--;
			} else {
				remainingPlayers.add(player);
			}
		}

		for (PrisonEscapePlayer player : remainingPlayers) {
			if (requiredPrisioners != 0) {
				_prisionersTeam.addMember(player);
				requiredPrisioners--;
			} else {
				_policeTeam.addMember(player);
				requiredOfficers--;
			}
		}
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
