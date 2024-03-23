package net.tiagofar78.prisonescape.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.tiagofar78.prisonescape.bukkit.BukkitMessageSender;
import net.tiagofar78.prisonescape.bukkit.BukkitScheduler;
import net.tiagofar78.prisonescape.bukkit.BukkitTeleporter;
import net.tiagofar78.prisonescape.game.phases.Finished;
import net.tiagofar78.prisonescape.game.phases.Phase;
import net.tiagofar78.prisonescape.game.phases.Waiting;
import net.tiagofar78.prisonescape.game.prisonbuilding.PrisonBuilding;
import net.tiagofar78.prisonescape.game.prisonbuilding.PrisonEscapeLocation;
import net.tiagofar78.prisonescape.managers.ConfigManager;
import net.tiagofar78.prisonescape.managers.GameManager;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

public class PrisonEscapeGame {
	
	private static final int TICKS_PER_SECOND = 20;
	private static final String POLICE_TEAM_NAME = "Police";
	private static final String PRISIONERS_TEAM_NAME = "Prisioners";
	
	private Settings _settings;
	
	private int _currentDay;
	private DayPeriod _dayPeriod;
	private PrisonBuilding _prison;
	
	private List<PrisonEscapePlayer> _playersOnLobby;
	private PrisonEscapeTeam _policeTeam;
	private PrisonEscapeTeam _prisionersTeam;
	
	private Phase _phase;
	
	public PrisonEscapeGame(String mapName, PrisonEscapeLocation referenceBlock) {
		_settings = new Settings();
		
		_currentDay = 0;
		_prison = new PrisonBuilding(referenceBlock);
		
		_playersOnLobby = new ArrayList<>();
		_policeTeam = new PrisonEscapeTeam(POLICE_TEAM_NAME);
		_prisionersTeam = new PrisonEscapeTeam(PRISIONERS_TEAM_NAME);
		
		startWaitingPhase();
	}
	
//	#########################################
//	#                 Lobby                 #
//	#########################################
	
	/**
	* @return      0 if success<br> 
	* 				-1 if already on game<br>
	* 				-2 if already started<br>
	* 				-3 if the lobby is full
	*/
	public int playerJoin(String playerName) {		
		if (isPlayerOnGame(playerName)) {
			return -1;
		}

		if (_phase.hasGameStarted()) {
			return -2;
		}
		
		ConfigManager config = ConfigManager.getInstance();
		if (_playersOnLobby.size() >= config.getMaxPlayers()) {
			return -3;
		}
		
		PrisonEscapePlayer player = new PrisonEscapePlayer(playerName);
		_playersOnLobby.add(player);
		
		BukkitTeleporter.teleport(player, _prison.getWaitingLobbyLocation());
		return 0;
	}

	/**
	* @return      0 if success<br> 
	* 				-1 if game has not started <br>
	* 				-2 if already on game<br>
	* 				-3 if player never on game<br>
	*/
	public int playerRejoin(String playerName) {
		if (!_phase.hasGameStarted()) {
			return -1;
		}

		if (isPlayerOnGame(playerName)) {
			return -2;
		}

		PrisonEscapePlayer player = getPlayerOnPoliceTeam(playerName);
		if (player != null) {
			teleportPoliceToSpawnPoint(player);
		}
		else {
			player = getPlayerOnPrisionersTeam(playerName);
			if (player == null) {
				return -3;
			}
			teleportPrisionerToSpawnPoint(player);
		}

		_playersOnLobby.add(player);
		return 0;
	}
	
	/**
	* @return      0 if success<br>
	* 				-1 if not on game
	*/
	public int playerLeft(String playerName) {		
		if (!isPlayerOnGame(playerName)) {
			return -1;
		}
		
		PrisonEscapePlayer player = null;
		for (int i = 0; i < _playersOnLobby.size(); i++) {
			if (_playersOnLobby.get(i).getName().equals(playerName)) {
				player = _playersOnLobby.get(i);
				_playersOnLobby.remove(i);
				break;
			}
		}
		
		teleportToLeavingLocation(player);
		return 0;
	}
	
	private boolean isPlayerOnGame(String playerName) {
		for (int i = 0; i < _playersOnLobby.size(); i++) {
			if (_playersOnLobby.get(i).getName().equals(playerName)) {
				return true;
			}
		}
		
		return false;
	}

	private PrisonEscapePlayer getPlayerOnPoliceTeam(String playerName) {
		for (int i = 0; i < _policeTeam.getSize(); i++) {
			if (_policeTeam.getMember(i).getName().equals(playerName)) {
				return _policeTeam.getMember(i);
			}
		}
		
		return null;
	}

	private PrisonEscapePlayer getPlayerOnPrisionersTeam(String playerName) {
		for (int i = 0; i < _prisionersTeam.getSize(); i++) {
			if (_prisionersTeam.getMember(i).getName().equals(playerName)) {
				return _prisionersTeam.getMember(i);
			}
		}
		
		return null;
	}
	
//	########################################
//	#              Admin zone              #
//	########################################
	
	/**
	* @return      0 if successful<br> 
	* 				-1 if already started ongoing phase
	*/
	public int forceStart() {
		if (_phase.hasGameStarted()) {
			return -1;
		}
		
		startOngoingPhase();
		return 0;
	}

	public void forceStop() {
		_phase = new Finished();
		disableGame();
	}

	/**
	* @return      0 if successful<br> 
	* 				-1 if not in finished phase
	*/
	public int stop() {
		if (!_phase.hasGameEnded()) {
			return -1;
		}

		disableGame();
		return 0;
	}
	
//	########################################
//	#                Phases                #
//	########################################
	
	private void startWaitingPhase() {
		_phase = new Waiting();
		
		ConfigManager config = ConfigManager.getInstance();
		
		runWaitingPhaseScheduler(config.getWaitingPhaseDuration(), true);
	}
	
	private void runWaitingPhaseScheduler(int remainingSeconds, boolean isFirst) {
		ConfigManager config = ConfigManager.getInstance();
		
		BukkitScheduler.runSchedulerLater(new Runnable() {
			
			@Override
			public void run() {
				if (remainingSeconds == 0) {
					return;
				}
				
				if (remainingSeconds % config.getDelayBetweenAnnouncements() == 0) {
					List<String> playersNames = BukkitMessageSender.getOnlinePlayersNames();
					for (String playerName : playersNames) {
						MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(playerName);
						
						List<String> announcement = messages
								.getGameStartingAnnouncementMessage(remainingSeconds, _playersOnLobby.size());
						BukkitMessageSender.sendChatMessage(playerName, announcement);
					}
				}
				
				int fullLobbyWaitDuration = config.getFullLobbyWaitDuration();
				if (_playersOnLobby.size() == config.getMaxPlayers() && remainingSeconds > fullLobbyWaitDuration) {
					runWaitingPhaseScheduler(fullLobbyWaitDuration, false);
				}
				
				runWaitingPhaseScheduler(remainingSeconds - 1, false);
			}
		}, isFirst ? 0 : 1 * TICKS_PER_SECOND);
	}
	
	private void startOngoingPhase() {
		distributePlayersPerTeam();

		_phase = _phase.next();
		
		startDay();
	}
	
	private void startFinishedPhase(PrisonEscapeTeam winnerTeam) {
		_phase = _phase.next();		
		
		boolean prisionersWon = winnerTeam.getName().equals(_prisionersTeam.getName());
		int playersInPrison = _prisionersTeam.countArrestedPlayers();
		
		for (PrisonEscapePlayer player : _playersOnLobby) {
			MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(player.getName());
			
			String title;
			String subtitle;
			if (prisionersWon) {
				title = messages.getPrisionersWonTitle();
				subtitle = messages.getPrisionersWonSubtitle();
			}
			else {
				title = messages.getPoliceWonTitle();
				subtitle = messages.getPoliceWonSubtitle(playersInPrison);
			}
			
			List<String> resultMessage = messages.getGameResultMessage(winnerTeam.isOnTeam(player));
			
			BukkitMessageSender.sendTitleMessage(player.getName(), title, subtitle);
			BukkitMessageSender.sendChatMessage(player, resultMessage);
		}
		
		int finishedPhaseDuration = ConfigManager.getInstance().getFinishedPhaseDuration();
		BukkitScheduler.runSchedulerLater(new Runnable() {
			
			@Override
			public void run() {
				disableGame();
			}
		}, finishedPhaseDuration * TICKS_PER_SECOND);
	}
	
	private void disableGame() {
		for (PrisonEscapePlayer player : _playersOnLobby) {
			teleportToLeavingLocation(player);
		}
		
		GameManager.removeGame();
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
		
		_prison.reloadChests();
		
		BukkitScheduler.runSchedulerLater(new Runnable() {
			
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
		
		BukkitScheduler.runSchedulerLater(new Runnable() {
			
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
		teleportToSolitary(arrested);

		for (PrisonEscapePlayer player : _playersOnLobby) {
			MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(player.getName());
			String announcement = messages.getPrisionerArrested(arrested.getName());
			BukkitMessageSender.sendChatMessage(player.getName(), announcement);
		}
		
		BukkitScheduler.runSchedulerLater(new Runnable() {
			
			@Override
			public void run() {
				if (_phase.isClockStopped()) {
					return;
				}
				
				arrested.removeWanted();
				
				MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(arrested.getName());
				BukkitMessageSender.sendChatMessage(arrested.getName(), messages.getPrisionerFreedOfSolitary());
				
				if (_dayPeriod == DayPeriod.DAY) {
					teleportToSolitaryExit(arrested);
				}
				else if (_dayPeriod == DayPeriod.NIGHT) {
					teleportPrisionerToSpawnPoint(arrested);
				}
			}
		}, TICKS_PER_SECOND * _settings.getSecondsInSolitary());
	}
	
//	########################################
//	#                 Util                 #
//	########################################
	
	private PrisonEscapePlayer getPrisonEscapePlayer(String playerName) {
		for (PrisonEscapePlayer player : _playersOnLobby) {
			if (player.getName().equals(playerName)) {
				return player;
			}
		}
		
		return null;
	}

	private void distributePlayersPerTeam() {
		int numberOfPlayers = _playersOnLobby.size();
		int requiredPrisioners = (int) Math.round(
				numberOfPlayers * ConfigManager.getInstance().getPrisionerRatio()
		);
		int requiredOfficers = (int) Math.round(
				numberOfPlayers * ConfigManager.getInstance().getOfficerRatio()
		);

		Collections.shuffle(_playersOnLobby);
		List<PrisonEscapePlayer> remainingPlayers = new ArrayList<>();

		for (PrisonEscapePlayer player : _playersOnLobby) {
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

	private void teleportPoliceToSpawnPoint(PrisonEscapePlayer player) {
		int playerIndex = _policeTeam.getPlayerIndex(player);
		BukkitTeleporter.teleport(player, _prison.getPoliceSpawnLocation(playerIndex));
	}

	private void teleportPrisionerToSpawnPoint(PrisonEscapePlayer player) {
		int playerIndex = _prisionersTeam.getPlayerIndex(player);
		BukkitTeleporter.teleport(player, _prison.getPlayerCellLocation(playerIndex));
	}

	private void teleportToSolitary(PrisonEscapePlayer player) {
		BukkitTeleporter.teleport(player, _prison.getSolitaryLocation());
	}

	private void teleportToSolitaryExit(PrisonEscapePlayer player) {
		BukkitTeleporter.teleport(player, _prison.getSolitaryExitLocation());
	}
	
	private void teleportToLeavingLocation(PrisonEscapePlayer player) {
		BukkitTeleporter.teleport(player, ConfigManager.getInstance().getLeavingLocation());
	}
	
}
