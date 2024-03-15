package net.tiagofar78.prisonescape.managers;

import java.util.List;

public class ConfigManager {

	private static ConfigManager instance = new ConfigManager();

	public static ConfigManager getInstance() {
		return instance;
	}

	public String getContainerName() {
		return null; // TODO
	}

	public Double getPrisionerRatio() {
		return 0.0; // TODO
	}

	public Double getOfficerRatio() {
		return 0.0; // TODO
	}
	
	public String getWorldName() {
		return null; // TODO
	}
	
	public List<String> getAvailableLanguages() {
		return null; // TODO
	}
}
