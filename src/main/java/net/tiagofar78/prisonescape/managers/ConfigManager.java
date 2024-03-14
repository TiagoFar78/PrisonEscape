package net.tiagofar78.prisonescape.managers;

public class ConfigManager {

	private static ConfigManager instance = new ConfigManager();

	public static ConfigManager getInstance() {
		return instance;
	}

	public String getContainerName() {
		return null;
	}

	public Double getPrisionerRatio() {
		return 0.0; // TODO
	}

	public Double getOfficerRatio() {
		return 0.0; // TODO
	}
}
