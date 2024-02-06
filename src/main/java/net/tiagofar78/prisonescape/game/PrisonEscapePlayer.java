package net.tiagofar78.prisonescape.game;

public class PrisonEscapePlayer {
	
	private String _name;
	private TeamPreference _preference;
	
	public PrisonEscapePlayer(String name) {
		_name = name;
		_preference = TeamPreference.RANDOM;
	}
	
	public String getName() {
		return _name;
	}
	
	public TeamPreference getPreference() {
		return _preference;
	}

}
