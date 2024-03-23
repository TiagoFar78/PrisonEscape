package net.tiagofar78.prisonescape.managers;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.bukkit.configuration.file.YamlConfiguration;

import net.tiagofar78.prisonescape.PrisonEscapeResources;

public class MessageLanguageManager {
	
	private static Hashtable<String, MessageLanguageManager> instance = initializeLanguageMessages();
	
	private static Hashtable<String, MessageLanguageManager> initializeLanguageMessages() {
		ConfigManager config = ConfigManager.getInstance();
		
		Hashtable<String, MessageLanguageManager> languagesMessages = new Hashtable<>();
		
		List<String> availableLanguages = config.getAvailableLanguages();
		String defaultLanguage = config.getDefaultLanguage();
		
		if (availableLanguages.size() == 0) {
			availableLanguages.add(defaultLanguage);
		}
		
		for (String language : availableLanguages) {
			languagesMessages.put(language, new MessageLanguageManager(language));
		}
		
		return languagesMessages;
	}
	
	public static MessageLanguageManager getInstance(String language) {
		if (language == null) {
			language = ConfigManager.getInstance().getDefaultLanguage();
		}
		
		return instance.get(language);
	}
	
	public static MessageLanguageManager getInstanceByPlayer(String playerName) {
		String language = MessageLanguageManager.getPlayerLanguage(playerName);
		return MessageLanguageManager.getInstance(language);
	}
	
	private static String getPlayerLanguage(String playerName) {
		return ConfigManager.getInstance().getDefaultLanguage();
	}
	
//	#######################################
//	#              Inventory              #
//	#######################################
	
	private String _containerName;
	
//	########################################
//	#               Warnings               #
//	########################################
	
	private String _successfullyStartedGameMessage;
	private String _successfullyForceStartedGameMessage;
	private String _successfullyJoinedGameMessage;
	private String _successfullyLeftGameMessage;
	private String _successfullyForceStoppedGameMessage;
	private String _successfullyRejoinedGameMessage;
	private String _successfullyStoppedGameMessage;

//	########################################
//	#             Announcements            #
//	########################################
	
	private List<String> _gameStartingAnnouncementMessage;
	private String _prisionerArrested;
	private String _prisionerFreedOfSolitary;
	private String _prisionersWonTitle;
	private String _prisionersWonSubtitle;
	private String _policeWonTitle;
	private String _policeWonSubtitle;
	private String _victoryWord;
	private String _defeatWord;
	private List<String> _gameResultMessage;
	
//	########################################
//	#                Errors                #
//	########################################
	
	private String _notAllowedMessage;
	private String _onlyPlayersCanUseThisCommandMessage;
	
	private String _gameAlreadyStartedMessage;
	private String _gameNotStartedYetMessage;
	private String _gameAlreadyOngoingMessage;
	private String _gameHasNotStartedUseJoinInsteadMessage;
	private String _gameIsNotInFinishedPhaseMessage;
	private String _lobbyIsFullMessage;

	private String _playerAlreadyJoinedMessage;
	private String _playerNotOnLobbyMessage;
	private String _playerWasNeverInGameMessage;
	
//	########################################
//	#                Usages                #
//	########################################
	
	private List<String> _usageMessage;
	private String _startCommandUsage;
	private String _forceStartCommandUsage;
	private String _joinCommandUsage;
	private String _leaveCommandUsage;
	private String _forceStopCommandUsage;
	private String _rejoinCommandUsage;
	private String _stopCommandUsage;
	
	private MessageLanguageManager(String language) {
		YamlConfiguration messages = PrisonEscapeResources.getYamlLanguage(language);
		
		_containerName = createMessage(messages.getString("Inventory.Chest.Title"));
		
		String messagePath = "Messages.";
		String warningPath = messagePath + "Warnings.";
		_successfullyStartedGameMessage = createMessage(messages.getString(warningPath + "StartedGame"));
		_successfullyForceStartedGameMessage = createMessage(messages.getString(warningPath + "ForceStartedGame"));
		_successfullyJoinedGameMessage = createMessage(messages.getString(warningPath + "JoinedGame"));
		_successfullyLeftGameMessage = createMessage(messages.getString(warningPath + "LeftGame"));
		_successfullyForceStoppedGameMessage = createMessage(messages.getString(warningPath + "ForceStoppedGame"));
		_successfullyRejoinedGameMessage = createMessage(messages.getString(warningPath + "RejoinedGame"));
		_successfullyStoppedGameMessage = createMessage(messages.getString(warningPath + "StoppedGame"));
		
		String announcementPath = messagePath + "Announcements.";
		_gameStartingAnnouncementMessage = createMessage(messages.getStringList(announcementPath + "GameStarting"));
		_prisionerArrested = createMessage(messages.getString(announcementPath + "PrisionerArrested"));
		_prisionerFreedOfSolitary = createMessage(messages.getString(announcementPath + "PrisionerFreedOfSolitary"));
		_prisionersWonTitle = createMessage(messages.getString(announcementPath + "PrisionersWonTitle"));
		_prisionersWonSubtitle = createMessage(messages.getString(announcementPath + "PrisionersWonSubtitle"));
		_policeWonTitle = createMessage(messages.getString(announcementPath + "PoliceWonTitle"));
		_policeWonSubtitle = createMessage(messages.getString(announcementPath + "PoliceWonSubtitle"));
		_victoryWord = messages.getString(announcementPath + "VictoryWord");
		_defeatWord = messages.getString(announcementPath + "DefeatWord");
		_gameResultMessage = createMessage(messages.getStringList(announcementPath + "GameResultMessage"));
		
		String errorPath = messagePath + "Errors.";
		_notAllowedMessage = createMessage(messages.getString(errorPath + "NotAllowed"));
		_onlyPlayersCanUseThisCommandMessage = createMessage(messages.getString(errorPath + "CommandForPlayers"));
		_gameAlreadyStartedMessage = createMessage(messages.getString(errorPath + "GameAlreadyStarted"));
		_gameNotStartedYetMessage = createMessage(messages.getString(errorPath + "GameNotStartedYet"));
		_gameAlreadyOngoingMessage = createMessage(messages.getString(errorPath + "GameAlreadyOngoing"));
		_gameHasNotStartedUseJoinInsteadMessage = createMessage(messages.getString(errorPath + "GameIsStillWaiting"));
		_gameIsNotInFinishedPhaseMessage = createMessage(messages.getString(errorPath + "GameIsNotFinished"));
		_lobbyIsFullMessage = createMessage(messages.getString(errorPath + "LobbyIsFull"));
		_playerAlreadyJoinedMessage = createMessage(messages.getString(errorPath + "AlreadyJoined"));
		_playerNotOnLobbyMessage = createMessage(messages.getString(errorPath + "NotOnLobby"));
		_playerWasNeverInGameMessage = createMessage(messages.getString(errorPath + "NeverInGame"));
		
		String usagePath = messagePath + "Usage.";
		_usageMessage = createMessage(messages.getStringList(usagePath + "General"));
		_startCommandUsage = createMessage(messages.getString(usagePath + "Start"));
		_forceStartCommandUsage = createMessage(messages.getString(usagePath + "ForceStart"));
		_forceStopCommandUsage = createMessage(messages.getString(usagePath + "ForceStop"));
		_joinCommandUsage = createMessage(messages.getString(usagePath + "Join"));
		_leaveCommandUsage = createMessage(messages.getString(usagePath + "Leave"));
		_rejoinCommandUsage = createMessage(messages.getString(usagePath + "Rejoin"));
		_stopCommandUsage = createMessage(messages.getString(usagePath + "Stop"));
	}
	
	private String createMessage(String rawMessage) {
		return rawMessage.replace("&", "§");
	}
	
	private List<String> createMessage(List<String> rawMessage) {
		List<String> message = new ArrayList<>(rawMessage);
		
		for (int i = 0; i < message.size(); i++) {
			message.set(i, message.get(i).replace("&", "§"));
		}
		
		return message;
	}
	
//	#######################################
//	#              Inventory              #
//	#######################################

	public String getContainerName() {
		return _containerName;
	}
	
//	########################################
//	#               Warnings               #
//	########################################
	
	public String getSuccessfullyStartedGameMessage() {
		return _successfullyStartedGameMessage;
	}
	
	public String getSuccessfullyForceStartedGameMessage() {
		return _successfullyForceStartedGameMessage;
	}

	public String getSuccessfullyJoinedGameMessage() {
		return _successfullyJoinedGameMessage;
	}
	
	public String getSuccessfullyLeftGameMessage() {
		return _successfullyLeftGameMessage;
	}

	public String getSuccessfullyForceStoppedGameMessage() {
		return _successfullyForceStoppedGameMessage;
	}

	public String getSuccessfullyRejoinedGameMessage() {
		return _successfullyRejoinedGameMessage;
	}

	public String getSuccessfullyStoppedGameMessage() {
		return _successfullyStoppedGameMessage;
	}

//	########################################
//	#             Announcements            #
//	########################################
	
	public List<String> getGameStartingAnnouncementMessage(int remainingTime, int playersOnLobby) {
		List<String> message = new ArrayList<>(_gameStartingAnnouncementMessage);
		
		for (int i = 0; i < message.size(); i++) {
			
			message.set(i, message.get(i)
					.replace("{TIME}", Integer.toString(remainingTime)
					.replace("{PLAYERS}", Integer.toString(playersOnLobby))));
		}
		
		return message;
	}

	public String getPrisionerArrested(String playerName) {
		return _prisionerArrested.replace("{PLAYER}", playerName);
	}

	public String getPrisionerFreedOfSolitary() {
		return _prisionerFreedOfSolitary;
	}
	
	public String getPrisionersWonTitle() {
		return _prisionersWonTitle;
	}
	
	public String getPrisionersWonSubtitle() {
		return _prisionersWonSubtitle;
	}
	
	public String getPoliceWonTitle() {
		return _policeWonTitle;
	}
	
	public String getPoliceWonSubtitle(int playersInPrison) {
		return _policeWonSubtitle;
	}
	
	public List<String> getGameResultMessage(boolean isWinner) {
		String result = isWinner ? _victoryWord : _defeatWord;
		
		List<String> message = new ArrayList<>(_gameResultMessage);
		for (int i = 0; i < message.size(); i++) {
			message.set(i, message.get(i).replace("{RESULT}", result));
		}
		
		return message;
	}
	
//	########################################
//	#                Errors                #
//	########################################
	
	public String getNotAllowedMessage() {
		return _notAllowedMessage;
	}
	
	public String getOnlyPlayersCanUseThisCommandMessage() {
		return _onlyPlayersCanUseThisCommandMessage;
	}
	
	public String getGameAlreadyStartedMessage() {
		return _gameAlreadyStartedMessage;
	}
	
	public String getGameNotStartedYetMessage() {
		return _gameNotStartedYetMessage;
	}
	
	public String getGameAlreadyOngoingMessage() {
		return _gameAlreadyOngoingMessage;
	}

	public String getGameHasNotStartedUseJoinInsteadMessage() {
		return _gameHasNotStartedUseJoinInsteadMessage;
	}

	public String getGameHasNotFinishedMessage() {
		return _gameIsNotInFinishedPhaseMessage;
	}
	
	public String getLobbyIsFullMessage() {
		return _lobbyIsFullMessage;
	}

	public String getPlayerAlreadyJoinedMessage() {
		return _playerAlreadyJoinedMessage;
	}
	
	public String getPlayerNotOnLobbyMessage() {
		return _playerNotOnLobbyMessage;
	}

	public String getPlayerWasNeverInGameMessage() {
		return _playerWasNeverInGameMessage;
	}
	
//	########################################
//	#                Usages                #
//	########################################
	
	public List<String> getUsage() {
		return _usageMessage;
	}
	
	public String getStartCommandUsage() {
		return _startCommandUsage;
	}
	
	public String getForceStartCommandUsage() {
		return _forceStartCommandUsage;
	}

	public String getJoinCommandUsage() {
		return _joinCommandUsage;
	}
	
	public String getLeaveCommandUsage() {
		return _leaveCommandUsage;
	}

	public String getForceStopCommandUsage() {
		return _forceStopCommandUsage;
	}

	public String getRejoinCommandUsage() {
		return _rejoinCommandUsage;
	}

	public String getStopCommandUsage() {
		return _stopCommandUsage;
	}
}
