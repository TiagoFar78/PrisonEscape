package net.tiagofar78.prisonescape.game;

import java.util.List;

public class PrisonEscapeTeam {
	
	private String _name;
	private List<PrisonEscapePlayer> _players;
	
	public PrisonEscapeTeam(String name) {
		this._name = name;
	}
	
	public String getName() {
		return _name;
	}

	public void add(PrisonEscapePlayer player) {
		_players.add(player);
	}
	
	public boolean isOnTeam(PrisonEscapePlayer player) {
		return _players.contains(player);
	}
	
	public int countArrestedPlayers() {
		return (int) _players.stream().filter(player -> !player.hasEscaped() && player.isOnline()).count();
	}

}
