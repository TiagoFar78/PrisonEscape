package net.tiagofar78.prisonescape.game;

public class Settings {

	private static final double PRISIONERS_RATIO = 0.64;
	private static final double GUARDS_RATIO = 0.36;

	public Settings() {
		
	}

	public int getRequiredPrisioners(int numberOfPlayers) {
		return (int) Math.round(numberOfPlayers * PRISIONERS_RATIO);
	}

	public int getRequiredOfficers(int numberOfPlayers) {
		return (int) Math.round(numberOfPlayers * GUARDS_RATIO);
	}
	
	public int getSecondsInSolitary() {
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
