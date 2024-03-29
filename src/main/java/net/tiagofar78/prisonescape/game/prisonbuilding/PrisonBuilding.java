package net.tiagofar78.prisonescape.game.prisonbuilding;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;

import net.tiagofar78.prisonescape.bukkit.BukkitWorldEditor;
import net.tiagofar78.prisonescape.game.PrisonEscapeItem;
import net.tiagofar78.prisonescape.game.PrisonEscapePlayer;
import net.tiagofar78.prisonescape.managers.ConfigManager;

public class PrisonBuilding {
	
	private PrisonEscapeLocation _waitingLobbyLocation;
	private PrisonEscapeLocation _prisonTopLeftCorner;
	private PrisonEscapeLocation _prisonBottomRightCorner;
	private List<PrisonEscapeLocation> _prisionersSpawnLocations;
	private List<PrisonEscapeLocation> _policeSpawnLocations;
	private List<PrisonEscapeLocation> _restrictedAreasBottomRightCornerLocations;
	private List<PrisonEscapeLocation> _restrictedAreasTopLeftCornerLocations;
	private PrisonEscapeLocation _solitaryLocation;
	private PrisonEscapeLocation _solitaryExitLocation;
	private Hashtable<PrisonEscapeLocation, PrisonEscapeLocation> _prisionersSecretPassageLocations;
	private Hashtable<PrisonEscapeLocation, PrisonEscapeLocation> _policeSecretPassageLocations;
	
	private List<Vault> _vaults;
	private List<PrisonEscapeLocation> _vaultsLocations;
	
	private List<Chest> _chests;
	private List<PrisonEscapeLocation> _metalDetectorsLocations;

	public PrisonBuilding(PrisonEscapeLocation reference) {
		ConfigManager config = ConfigManager.getInstance();

		_waitingLobbyLocation = addReferenceLocation(reference, config.getWaitingLobbyLocation());
		_prisonTopLeftCorner = addReferenceLocation(reference, config.getPrisonTopLeftCornerLocation());
		_prisonBottomRightCorner = addReferenceLocation(reference, config.getPrisonBottomRightCornerLocation());

		_restrictedAreasBottomRightCornerLocations = new ArrayList<>();
		for (PrisonEscapeLocation loc : config.getRestrictedAreasBottomRightCornerLocations()) {
    		_restrictedAreasBottomRightCornerLocations.add(addReferenceLocation(reference, loc));
		}

		_restrictedAreasTopLeftCornerLocations = new ArrayList<>();
		for (PrisonEscapeLocation loc : config.getRestrictedAreasTopLeftCornerLocations()) {
			_restrictedAreasTopLeftCornerLocations.add(addReferenceLocation(reference, loc));
		}
		
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
		
		_vaults = new ArrayList<>();
		_vaultsLocations = new ArrayList<>();
		for (PrisonEscapeLocation location : config.getVaultsLocations()) {
			_vaultsLocations.add(addReferenceLocation(reference, location));
		}
		
		_chests = new ArrayList<>();
		_metalDetectorsLocations = new ArrayList<>();
	}
	
	private PrisonEscapeLocation addReferenceLocation(PrisonEscapeLocation reference, PrisonEscapeLocation loc) {
		return new PrisonEscapeLocation(loc.getX() + reference.getX(),
				loc.getY() + reference.getX(),
				loc.getZ() + reference.getZ());
	}
	
	public boolean isOutsidePrison(PrisonEscapeLocation loc) {
		return loc.getX() > _prisonTopLeftCorner.getX() || loc.getX() < _prisonBottomRightCorner.getX() ||
				loc.getY() > _prisonTopLeftCorner.getY() || loc.getY() < _prisonBottomRightCorner.getY() ||
				loc.getZ() > _prisonTopLeftCorner.getZ() || loc.getZ() < _prisonBottomRightCorner.getZ();
	}

	public boolean isInRestrictedAreas(PrisonEscapeLocation loc) {
		for (int i = 0; i < _restrictedAreasTopLeftCornerLocations.size(); i++) {
			PrisonEscapeLocation bottomRight = _restrictedAreasBottomRightCornerLocations.get(i);
			PrisonEscapeLocation topLeft = _restrictedAreasTopLeftCornerLocations.get(i);

			if (loc.getX() > bottomRight.getX() && loc.getX() < topLeft.getX() &&
				loc.getZ() > bottomRight.getZ() && loc.getZ() < topLeft.getZ()) {
				return true;
			}
		}
		return false;
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

	public void reloadChests() {
		for (Chest chest : _chests) {
			chest.reload();
		}
	}
	
//	#########################################
//	#                 Vault                 #
//	#########################################
	
	public void addVaults(List<PrisonEscapePlayer> prisioners) {
		for (int i = 0; i < prisioners.size(); i++) {
			_vaults.add(new Vault(prisioners.get(i)));
			
			String signText = prisioners.get(i).getName();
			PrisonEscapeLocation vaultLocation = _vaultsLocations.get(i);
			BukkitWorldEditor.addSignAboveVault(vaultLocation, signText);
			BukkitWorldEditor.addVault(vaultLocation);
		}
	}
	
	public Vault getVault(int index) {
		return _vaults.get(index);
	}
	
	public int getVaultIndex(PrisonEscapeLocation location) {
		for (int i = 0; i < _vaultsLocations.size(); i++) {
			if (_vaultsLocations.get(i).equals(location)) {
				return i;
			}
		}
		
		return -1;
	}
	
	public void deleteVaults() {
		for (PrisonEscapeLocation location : _vaultsLocations) {
			BukkitWorldEditor.deleteVaultAndRespectiveSign(location);
		}
	}
	
//	#########################################
//	#               Locations               #
//	#########################################
	
	public PrisonEscapeLocation getWaitingLobbyLocation() {
		return _waitingLobbyLocation;
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
}
