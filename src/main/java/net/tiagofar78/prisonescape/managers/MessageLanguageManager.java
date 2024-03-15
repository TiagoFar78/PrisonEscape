package net.tiagofar78.prisonescape.managers;

import java.util.Hashtable;
import java.util.List;

public class MessageLanguageManager {
	
	private static final String DEFAULT_LANGUAGE = "English";
	
	private static Hashtable<String, MessageLanguageManager> instance = initializeLanguageMessages();
	
	private static Hashtable<String, MessageLanguageManager> initializeLanguageMessages() {
		ConfigManager config = ConfigManager.getInstance();
		
		Hashtable<String, MessageLanguageManager> languagesMessages = new Hashtable<>();
		
		List<String> availableLanguages = config.getAvailableLanguages();
		
		if (availableLanguages.size() == 0) {
			languagesMessages.put(DEFAULT_LANGUAGE, new MessageLanguageManager(DEFAULT_LANGUAGE));
			return languagesMessages;
		}
		
		for (String language : availableLanguages) {
			languagesMessages.put(language, new MessageLanguageManager(language));
		}
		
		return languagesMessages;
	}
	
	public static MessageLanguageManager getInstance(String language) {
		return instance.get(language);
	}
	
	// TODO define attributes here
	
	private MessageLanguageManager(String language) {
		// TODO set the attributes values here
	}
	
	// TODO getters for attributes here

}
