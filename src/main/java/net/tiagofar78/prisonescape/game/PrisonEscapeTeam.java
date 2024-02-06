package net.tiagofar78.prisonescape.game;

import java.util.List;

public class PrisonEscapeTeam {
	
	private List<PrisonEscapePlayer> _players;
	
	public boolean isOnTeam(String playerName) {
		for (PrisonEscapePlayer player : _players) {
			if (player.getName().equals(playerName)) {
				return true;
			}
		}
		
		return false;
	}

}
