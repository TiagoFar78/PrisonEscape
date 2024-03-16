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

	public void addMember(PrisonEscapePlayer player) {
		_players.add(player);
	}
	
	public int getPlayerIndex(PrisonEscapePlayer player) {
		for (int i = 0; i < _players.size(); i++) {
			if (_players.get(i).equals(player)) {
				return i;
			}
		}
		return -1;
	}
	
	public boolean isOnTeam(PrisonEscapePlayer player) {
		return _players.contains(player);
	}
	
	public int countArrestedPlayers() {
		return (int) _players.stream().filter(player -> !player.hasEscaped() && player.isOnline()).count();
	}

}
