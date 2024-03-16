package net.tiagofar78.prisonescape.game.prisonbuilding;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;

import net.tiagofar78.prisonescape.game.PrisonEscapeItem;
import net.tiagofar78.prisonescape.managers.ConfigManager;

public class PrisonBuilding {
	
	private PrisonEscapeLocation _prisonTopLeftCorner;
	private PrisonEscapeLocation _prisonBottomRightCorner;
	private List<PrisonEscapeLocation> _prisionersSpawnLocations;
	private List<PrisonEscapeLocation> _policeSpawnLocations;
	private PrisonEscapeLocation _solitaryLocation;
	private PrisonEscapeLocation _solitaryExitLocation;
	private Hashtable<PrisonEscapeLocation, PrisonEscapeLocation> _prisionersSecretPassageLocations;
	private Hashtable<PrisonEscapeLocation, PrisonEscapeLocation> _policeSecretPassageLocations;
	
	private List<Chest> _chests;
	private List<PrisonEscapeLocation> _metalDetectorsLocations;
	
	public PrisonBuilding(PrisonEscapeLocation reference) {
		ConfigManager config = ConfigManager.getInstance();
		
		_prisonTopLeftCorner = addReferenceLocation(reference, config.getPrisonTopLeftCornerLocation());
		_prisonBottomRightCorner = addReferenceLocation(reference, config.getPrisonBottomRightCornerLocation());
		
		_prisionersSpawnLocations = new ArrayList<>();
		for (PrisonEscapeLocation loc : config.getPrisionersSpawnLocations()) {
			_prisionersSpawnLocations.add(addReferenceLocation(reference, loc));
		}
		
		_policeSpawnLocations = new ArrayList<>();
		for (PrisonEscapeLocation loc : config.getPoliceSpawnLocations()) {
			_policeSpawnLocations.add(addReferenceLocation(reference, loc));
		}
		
		_solitaryLocation = addReferenceLocation(reference, config.getSolitaryLocation());
		_solitaryExitLocation = addReferenceLocation(reference, config.getSolitaryExitLocation());
		
		_prisionersSecretPassageLocations = new Hashtable<>();
		for (Entry<PrisonEscapeLocation, PrisonEscapeLocation> entry : config.getPrisionersSecretPassageLocations().entrySet()) {
			_prisionersSecretPassageLocations.put(addReferenceLocation(reference, entry.getKey()),
					addReferenceLocation(reference, entry.getValue()));
		}
		
		_policeSecretPassageLocations = new Hashtable<>();
		for (Entry<PrisonEscapeLocation, PrisonEscapeLocation> entry : config.getPoliceSecretPassageLocations().entrySet()) {
			_policeSecretPassageLocations.put(addReferenceLocation(reference, entry.getKey()), 
					addReferenceLocation(reference, entry.getValue()));
		}
	}
	
	private PrisonEscapeLocation addReferenceLocation(PrisonEscapeLocation reference, PrisonEscapeLocation loc) {
		return new PrisonEscapeLocation(loc.getX() + reference.getX(),
				loc.getY() + reference.getX(),
				loc.getZ() + reference.getZ());
	}
	
	public boolean isOutsidePrison(PrisonEscapeLocation loc) {
		return loc.getX() > _prisonTopLeftCorner.getX() || _prisonBottomRightCorner.getX() < loc.getX() ||
				loc.getY() > _prisonTopLeftCorner.getY() || _prisonBottomRightCorner.getY() < loc.getY() ||
				loc.getZ() > _prisonTopLeftCorner.getZ() || _prisonBottomRightCorner.getZ() < loc.getZ();
	}
	
	public PrisonEscapeLocation getPoliceSpawnLocation(int index) {
		return _policeSpawnLocations.get(index);
	}
	
	public PrisonEscapeLocation getPlayerCellLocation(int index) {
		return  _prisionersSpawnLocations.get(index);
	}
	
	public PrisonEscapeLocation getSolitaryLocation() {
		return _solitaryLocation;
	}
	
	public PrisonEscapeLocation getSolitaryExitLocation() {
		return _solitaryExitLocation;
	}
	
	public PrisonEscapeLocation getSecretPassageDestinationLocation(PrisonEscapeLocation entranceBlock, boolean isPrisioner) {
		Hashtable<PrisonEscapeLocation, PrisonEscapeLocation> secretPassageLocations =
				isPrisioner ? _prisionersSecretPassageLocations : _policeSecretPassageLocations; 
		
		return secretPassageLocations.get(entranceBlock);
	}

	public boolean checkIfMetalDetectorTriggered(PrisonEscapeLocation location, List<PrisonEscapeItem> playerItems) {
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
