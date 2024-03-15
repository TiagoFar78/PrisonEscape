package net.tiagofar78.prisonescape.game.prisonbuilding;

import java.util.List;

import org.bukkit.Location;

import net.tiagofar78.prisonescape.game.PrisonEscapeItem;
import net.tiagofar78.prisonescape.game.PrisonEscapePlayer;

public class PrisonBuilding {
	
	private List<Chest> _chests;
	private List<Location> _metalDetectorsLocations;
	
	public boolean isOutsidePrison(Location loc) {
		return true;
	}
	
	public void sendPlayerToSolitary(PrisonEscapePlayer player) {
		
	}
	
	public void sendPlayerToCell(PrisonEscapePlayer player) {
		
	}
	
	public void takePlayerFromSolitary(PrisonEscapePlayer player) {
		// TODO should just send the player to the front of the solitary door and anything else.
	}

	public boolean checkIfMetalDetectorTriggered(Location location, List<PrisonEscapeItem> playerItems) {
		if (_metalDetectorsLocations.contains(location)) {
			for (PrisonEscapeItem item : playerItems) {
				if (item.isMetal()) {
					return true;
				}
			}
		}
		return false;
	}
}
