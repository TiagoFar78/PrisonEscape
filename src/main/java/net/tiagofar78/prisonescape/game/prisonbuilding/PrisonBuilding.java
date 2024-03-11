package net.tiagofar78.prisonescape.game.prisonbuilding;

import java.util.List;

import org.bukkit.Location;

public class PrisonBuilding {
	
	private List<Chest> _chests;
	private List<MetalDetector> _metalDetectors;
	
	public boolean isOutsidePrison(Location loc) {
		return true;
	}

}
