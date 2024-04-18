package net.tiagofar78.prisonescape.game.prisonbuilding;

import net.tiagofar78.prisonescape.bukkit.BukkitWorldEditor;
import net.tiagofar78.prisonescape.game.PrisonEscapePlayer;
import net.tiagofar78.prisonescape.game.prisonbuilding.doors.GoldenDoor;
import net.tiagofar78.prisonescape.game.prisonbuilding.regions.Region;
import net.tiagofar78.prisonescape.game.prisonbuilding.regions.SquaredRegion;
import net.tiagofar78.prisonescape.managers.ConfigManager;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;

public class PrisonBuilding {

    private static final String PRISON_REGION_NAME = "PRISON";

    private PrisonEscapeLocation _waitingLobbyLocation;
    private Region _prison;
    private List<Region> _regions;
    private List<PrisonEscapeLocation> _prisionersSpawnLocations;
    private List<PrisonEscapeLocation> _policeSpawnLocations;
    private PrisonEscapeLocation _solitaryLocation;
    private PrisonEscapeLocation _solitaryExitLocation;
    private Hashtable<String, PrisonEscapeLocation> _prisionersSecretPassageLocations;
    private Hashtable<String, PrisonEscapeLocation> _policeSecretPassageLocations;

    private List<Vault> _vaults;
    private List<PrisonEscapeLocation> _vaultsLocations;

    private Hashtable<String, Chest> _chests;
    private Hashtable<String, GoldenDoor> _goldenDoors;
    private List<PrisonEscapeLocation> _metalDetectorsLocations;

//  #########################################
//  #              Constructor              #
//  #########################################

    public PrisonBuilding(PrisonEscapeLocation reference) {
        ConfigManager config = ConfigManager.getInstance();

        PrisonEscapeLocation prisonUpperCorner = config.getPrisonUpperCornerLocation().add(reference);
        PrisonEscapeLocation prisonLowerCorner = config.getPrisonLowerCornerLocation().add(reference);
        _prison = new SquaredRegion(PRISON_REGION_NAME, false, prisonUpperCorner, prisonLowerCorner);

        _regions = createRegionsList(reference, config.getRegions());

        _waitingLobbyLocation = config.getWaitingLobbyLocation().add(reference);

        _prisionersSpawnLocations = createLocationsList(reference, config.getPrisionersSpawnLocations());
        _policeSpawnLocations = createLocationsList(reference, config.getPoliceSpawnLocations());

        _solitaryLocation = config.getSolitaryLocation().add(reference);
        _solitaryExitLocation = config.getSolitaryExitLocation().add(reference);

        _prisionersSecretPassageLocations = createLocationsMap(reference, config.getPrisionersSecretPassageLocations());
        _policeSecretPassageLocations = createLocationsMap(reference, config.getPoliceSecretPassageLocations());

        _vaults = new ArrayList<>();
        _vaultsLocations = createLocationsList(reference, config.getVaultsLocations());

        _chests = new Hashtable<>();
        for (PrisonEscapeLocation loc : config.getChestsLocations()) {
            String regionName = getRegionName(loc);
            _chests.put(loc.add(reference).createKey(), new Chest(regionName));
        }

        _goldenDoors = new Hashtable<>();
        for (PrisonEscapeLocation loc : config.getGoldenDoorsLocations()) {
            PrisonEscapeLocation referenceLoc = loc.add(reference);
            GoldenDoor goldenDoor = new GoldenDoor();
            _goldenDoors.put(referenceLoc.createKey(), goldenDoor);
            _goldenDoors.put(referenceLoc.add(0, 1, 0).createKey(), goldenDoor);
        }


        _metalDetectorsLocations = new ArrayList<>();
    }

    private List<PrisonEscapeLocation> createLocationsList(
            PrisonEscapeLocation reference,
            List<PrisonEscapeLocation> locs
    ) {
        List<PrisonEscapeLocation> list = new ArrayList<>();

        for (PrisonEscapeLocation loc : locs) {
            list.add(loc.add(reference));
        }

        return list;
    }

    private Hashtable<String, PrisonEscapeLocation> createLocationsMap(
            PrisonEscapeLocation reference,
            Hashtable<PrisonEscapeLocation, PrisonEscapeLocation> locs
    ) {
        Hashtable<String, PrisonEscapeLocation> map = new Hashtable<>();

        for (Entry<PrisonEscapeLocation, PrisonEscapeLocation> entry : locs.entrySet()) {
            String key = entry.getKey().add(reference).createKey();
            PrisonEscapeLocation value = entry.getValue().add(reference);
            map.put(key, value);
        }

        return map;
    }

    private List<Region> createRegionsList(PrisonEscapeLocation reference, List<SquaredRegion> regions) {
        List<Region> list = new ArrayList<>();

        for (Region region : regions) {
            region.add(reference);
            list.add(region);
        }

        return list;
    }

//  #########################################
//  #                Regions                #
//  #########################################

    public String getRegionName(PrisonEscapeLocation location) {
        for (Region region : _regions) {
            if (region.contains(location)) {
                return region.getName();
            }
        }

        return null;
    }

//  #########################################
//  #            Metal Detectors            #
//  #########################################

    public boolean isInRestrictedArea(PrisonEscapeLocation loc) {
        for (Region region : _regions) {
            if (region.contains(loc)) {
                return region.isRestricted();
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
//	#                 GolderDoor            #
//	#########################################

    public GoldenDoor getGoldenDoor(PrisonEscapeLocation location) {
        return _goldenDoors.get(location.createKey());
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
        return !_prison.contains(loc);
    }
}
