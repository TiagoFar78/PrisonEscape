package net.tiagofar78.prisonescape.game.prisonbuilding;

import net.tiagofar78.prisonescape.bukkit.BukkitWorldEditor;
import net.tiagofar78.prisonescape.game.PrisonEscapePlayer;
import net.tiagofar78.prisonescape.managers.ConfigManager;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;

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
    private Hashtable<String, PrisonEscapeLocation> _prisionersSecretPassageLocations;
    private Hashtable<String, PrisonEscapeLocation> _policeSecretPassageLocations;

    private List<Vault> _vaults;
    private List<PrisonEscapeLocation> _vaultsLocations;

    private Hashtable<String, Chest> _chests;
    private List<PrisonEscapeLocation> _metalDetectorsLocations;

//  #########################################
//  #              Constructor              #
//  #########################################

    public PrisonBuilding(PrisonEscapeLocation reference) {
        ConfigManager config = ConfigManager.getInstance();

        _waitingLobbyLocation = addReferenceLocation(reference, config.getWaitingLobbyLocation());
        _prisonTopLeftCorner = addReferenceLocation(reference, config.getPrisonTopLeftCornerLocation());
        _prisonBottomRightCorner = addReferenceLocation(reference, config.getPrisonBottomRightCornerLocation());

        _restrictedAreasBottomRightCornerLocations = createLocationsList(
                reference,
                config.getRestrictedAreasBottomRightCornerLocations()
        );
        _restrictedAreasTopLeftCornerLocations = createLocationsList(
                reference,
                config.getRestrictedAreasTopLeftCornerLocations()
        );

        _prisionersSpawnLocations = createLocationsList(reference, config.getPrisionersSpawnLocations());
        _policeSpawnLocations = createLocationsList(reference, config.getPoliceSpawnLocations());

        _solitaryLocation = addReferenceLocation(reference, config.getSolitaryLocation());
        _solitaryExitLocation = addReferenceLocation(reference, config.getSolitaryExitLocation());

        _prisionersSecretPassageLocations = createLocationsMap(reference, config.getPrisionersSecretPassageLocations());
        _policeSecretPassageLocations = createLocationsMap(reference, config.getPoliceSecretPassageLocations());

        _vaults = new ArrayList<>();
        _vaultsLocations = createLocationsList(reference, config.getVaultsLocations());

        _chests = new Hashtable<>();
        for (PrisonEscapeLocation loc : config.getChestsLocations()) {
            _chests.put(addReferenceLocation(reference, loc).createKey(), new Chest());
        }

        _metalDetectorsLocations = new ArrayList<>();
    }

    private PrisonEscapeLocation addReferenceLocation(PrisonEscapeLocation reference, PrisonEscapeLocation loc) {
        return new PrisonEscapeLocation(
                loc.getX() + reference.getX(),
                loc.getY() + reference.getX(),
                loc.getZ() + reference.getZ()
        );
    }

    private List<PrisonEscapeLocation> createLocationsList(
            PrisonEscapeLocation reference,
            List<PrisonEscapeLocation> locs
    ) {
        List<PrisonEscapeLocation> list = new ArrayList<>();

        for (PrisonEscapeLocation loc : locs) {
            list.add(addReferenceLocation(reference, loc));
        }

        return list;
    }

    private Hashtable<String, PrisonEscapeLocation> createLocationsMap(
            PrisonEscapeLocation reference,
            Hashtable<PrisonEscapeLocation, PrisonEscapeLocation> locs
    ) {
        Hashtable<String, PrisonEscapeLocation> map = new Hashtable<>();

        for (Entry<PrisonEscapeLocation, PrisonEscapeLocation> entry : locs.entrySet()) {
            String key = addReferenceLocation(reference, entry.getKey()).createKey();
            PrisonEscapeLocation value = addReferenceLocation(reference, entry.getValue());
            map.put(key, value);
        }

        return map;
    }

//  #########################################
//  #            Metal Detectors            #
//  #########################################

    public boolean isInRestrictedAreas(PrisonEscapeLocation loc) {
        for (int i = 0; i < _restrictedAreasTopLeftCornerLocations.size(); i++) {
            PrisonEscapeLocation bottomRight = _restrictedAreasBottomRightCornerLocations.get(i);
            PrisonEscapeLocation topLeft = _restrictedAreasTopLeftCornerLocations.get(i);

            if (loc.getX() > bottomRight.getX() && loc.getX() < topLeft.getX() && loc.getZ() > bottomRight.getZ() && loc
                    .getZ() < topLeft.getZ()) {
                return true;
            }
        }
        return false;
    }

    public boolean checkIfMetalDetectorTriggered(PrisonEscapeLocation location, PrisonEscapePlayer player) {
        return _metalDetectorsLocations.contains(location) && player.hasMetalItems();
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

//  #########################################
//  #                 Chest                 #
//  #########################################

    public void reloadChests() {
        for (Chest chest : _chests.values()) {
            chest.reload();
        }
    }

    public Chest getChest(PrisonEscapeLocation location) {
        return _chests.get(location.createKey());
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
        return _prisionersSpawnLocations.get(index);
    }

    public PrisonEscapeLocation getSolitaryLocation() {
        return _solitaryLocation;
    }

    public PrisonEscapeLocation getSolitaryExitLocation() {
        return _solitaryExitLocation;
    }

    public PrisonEscapeLocation getSecretPassageDestinationLocation(
            PrisonEscapeLocation location,
            boolean isPrisioner
    ) {
        Hashtable<String, PrisonEscapeLocation> secretPassageLocations =
                isPrisioner ? _prisionersSecretPassageLocations : _policeSecretPassageLocations;

        return secretPassageLocations.get(location.createKey());
    }

    public boolean isOutsidePrison(PrisonEscapeLocation loc) {
        return loc.getX() > _prisonTopLeftCorner.getX() || loc.getX() < _prisonBottomRightCorner.getX() || loc
                .getY() > _prisonTopLeftCorner.getY() || loc.getY() < _prisonBottomRightCorner.getY() || loc
                        .getZ() > _prisonTopLeftCorner.getZ() || loc.getZ() < _prisonBottomRightCorner.getZ();
    }
}
