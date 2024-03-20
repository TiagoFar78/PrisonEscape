package net.tiagofar78.prisonescape.managers;

import java.util.Hashtable;
import java.util.List;

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
	
	public static String getPlayerLanguage(String playerName) {
		return ConfigManager.getInstance().getDefaultLanguage();
	}
	
//	########################################
//	#               Warnings               #
//	########################################
	
	private String _successfullyStartedGameMessage;
	private String _successfullyForceStartedGameMessage;
	private String _successfullyJoinedGameMessage;
	
//	########################################
//	#                Errors                #
//	########################################
	
	private String _notAllowedMessage;
	private String _onlyPlayersCanUseThisCommandMessage;
	
	private String _gameAlreadyStartedMessage;
	private String _gameNotStartedYetMessage;
	private String _gameAlreadyOngoingMessage;

	private String _playerAlreadyJoinedMessage;
	
//	########################################
//	#                Usages                #
//	########################################
	
	private String[] _usageMessage;
	private String _startCommandUsage;
	private String _forceStartCommandUsage;
	private String _joinCommandUsage;
	
	private MessageLanguageManager(String language) {
		// TODO set the attributes values here
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

	public String getPlayerAlreadyJoinedMessage() {
		return _playerAlreadyJoinedMessage;
	}
	
//	########################################
//	#                Usages                #
//	########################################
	
	public String[] getUsage() {
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

}
