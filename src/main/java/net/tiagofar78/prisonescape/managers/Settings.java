package net.tiagofar78.prisonescape.managers;

public class Settings {
	
	private static Settings settings = new Settings();
	
	public static Settings getInstance() {
		return settings;
	}
	
	public int getMaxPrisioners() {
		return 0; // TODO
	}
	
	public int getMaxPoliceOfficers() {
		return 0;
	}
	
	public int getDays() {
		return 0; // TODO
	}
	
	public int getDayDuration() {
		return 0; // TODO
	}
	
	public int getNightDuration() {
		return 0; // TODO
	}

}
