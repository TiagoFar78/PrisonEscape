package net.tiagofar78.prisonescape.managers;

import java.util.Hashtable;
import java.util.List;

import net.tiagofar78.prisonescape.game.prisonbuilding.PrisonEscapeLocation;

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

	public int getMinimumPlayers() {
		return 0; // TODO
	}
	
	public int getMaxPlayers() {
		return 0; // TODO
	}
	
	public int getWaitingPhaseDuration() {
		return 0; // TODO
	}
	
	public int getFullLobbyWaitDuration() {
		return 0; // TODO
	}
	
	public int getDelayBetweenAnnouncements() {
		return 0; // TODO
	}
	
	public String getWorldName() {
		return null; // TODO
	}
	
	public List<String> getAvailableLanguages() {
		return null; // TODO
	}

	public String getDefaultLanguage() { 
		return null; // TODO
	}
	
	@Deprecated
	public PrisonEscapeLocation getReferenceBlock() {
		return null; // TODO
	}
	
	public PrisonEscapeLocation getLeavingLocation() {
		return null; // TODO
	}
	
	public PrisonEscapeLocation getWaitingLobbyLocation() {
		return null; // TODO
	}
	
	public PrisonEscapeLocation getPrisonTopLeftCornerLocation() {
		return null; // TODO
	}
	
	public PrisonEscapeLocation getPrisonBottomRightCornerLocation() {
		return null; // TODO
	}
	
	public List<PrisonEscapeLocation> getPrisionersSpawnLocations() {
		return null; // TODO
	}
	
	public List<PrisonEscapeLocation> getPoliceSpawnLocations() {
		return null; // TODO
	}
	
	public PrisonEscapeLocation getSolitaryLocation() {
		return null; // TODO
	}
	
	public PrisonEscapeLocation getSolitaryExitLocation() {
		return null; // TODO
	}
	
	public Hashtable<PrisonEscapeLocation, PrisonEscapeLocation> getPrisionersSecretPassageLocations() {
		return null; // TODO
	}
	
	public Hashtable<PrisonEscapeLocation, PrisonEscapeLocation> getPoliceSecretPassageLocations() {
		return null; // TODO
	}

	public int getCommonItemsProbability() {
		return 0; // TODO
	}

	public int getRareItemsProbability() {
		return 0; // TODO
	}

	public int getChestSize() {
		return 0; // TODO
	}
}
