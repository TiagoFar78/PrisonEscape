package net.tiagofar78.prisonescape.game.prisonbuilding;

import net.tiagofar78.prisonescape.bukkit.BukkitWorldEditor;
import net.tiagofar78.prisonescape.game.Prisioner;
import net.tiagofar78.prisonescape.game.PrisonEscapePlayer;
import net.tiagofar78.prisonescape.game.prisonbuilding.doors.CodeDoor;
import net.tiagofar78.prisonescape.game.prisonbuilding.doors.Door;
import net.tiagofar78.prisonescape.game.prisonbuilding.doors.GoldenDoor;
import net.tiagofar78.prisonescape.game.prisonbuilding.doors.GrayDoor;
import net.tiagofar78.prisonescape.game.prisonbuilding.regions.Region;
import net.tiagofar78.prisonescape.game.prisonbuilding.regions.SquaredRegion;
import net.tiagofar78.prisonescape.managers.ConfigManager;

import org.bukkit.block.Block;

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
    private PrisonEscapeLocation _helicopterExitLocation;
    private PrisonEscapeLocation _helicopterJoinLocation;
    private Hashtable<String, PrisonEscapeLocation> _prisionersSecretPassageLocations;
    private Hashtable<String, PrisonEscapeLocation> _policeSecretPassageLocations;

    private List<Vault> _vaults;
    private List<PrisonEscapeLocation> _vaultsLocations;

    private Hashtable<String, Chest> _chests;
    private Hashtable<String, Door> _doors;
    private List<PrisonEscapeLocation> _metalDetectorsLocations;
    private Wall _wall;

    private Maze _maze;
    private List<Obstacle> _obstacles;

    private Helicopter _helicopter;

//  #########################################
//  #              Constructor              #
//  #########################################

    public PrisonBuilding(PrisonEscapeLocation reference) {
        ConfigManager config = ConfigManager.getInstance();

        PrisonEscapeLocation prisonUpperCorner = config.getPrisonUpperCornerLocation().add(reference);
        PrisonEscapeLocation prisonLowerCorner = config.getPrisonLowerCornerLocation().add(reference);
        _prison = new SquaredRegion(PRISON_REGION_NAME, false, true, prisonUpperCorner, prisonLowerCorner);

        _regions = createRegionsList(reference, config.getRegions());

        _waitingLobbyLocation = config.getWaitingLobbyLocation().add(reference);

        _prisionersSpawnLocations = createLocationsList(reference, config.getPrisionersSpawnLocations());
        _policeSpawnLocations = createLocationsList(reference, config.getPoliceSpawnLocations());

        _solitaryLocation = config.getSolitaryLocation().add(reference);
        _solitaryExitLocation = config.getSolitaryExitLocation().add(reference);

        _helicopterExitLocation = config.getHelicopterExitLocation().add(reference);
        _helicopterJoinLocation = config.getHelicopterJoinLocation().add(reference);

        _prisionersSecretPassageLocations = createLocationsMap(reference, config.getPrisionersSecretPassageLocations());
        _policeSecretPassageLocations = createLocationsMap(reference, config.getPoliceSecretPassageLocations());

        _vaults = new ArrayList<>();
        _vaultsLocations = createLocationsList(reference, config.getVaultsLocations());

        _chests = new Hashtable<>();
        for (PrisonEscapeLocation loc : config.getChestsLocations()) {
            String regionName = getRegionName(loc);
            if (regionName == null) {
                regionName = "Default";
            }
            _chests.put(loc.add(reference).createKey(), new Chest(regionName));
        }

        _doors = new Hashtable<>();
        for (PrisonEscapeLocation loc : config.getGoldenDoorsLocations()) {
            PrisonEscapeLocation referenceLoc = loc.add(reference);
            GoldenDoor goldenDoor = new GoldenDoor();
            _doors.put(referenceLoc.createKey(), goldenDoor);
            _doors.put(referenceLoc.add(0, 1, 0).createKey(), goldenDoor);
        }
        for (PrisonEscapeLocation loc : config.getGrayDoorsLocations()) {
            PrisonEscapeLocation referenceLoc = loc.add(reference);
            GrayDoor grayDoor = new GrayDoor();
            _doors.put(referenceLoc.createKey(), grayDoor);
            _doors.put(referenceLoc.add(0, 1, 0).createKey(), grayDoor);
        }
        for (PrisonEscapeLocation loc : config.getCodeDoorsLocations()) {
            PrisonEscapeLocation referenceLoc = loc.add(reference);
            CodeDoor codeDoor = new CodeDoor();
            _doors.put(referenceLoc.createKey(), codeDoor);
            _doors.put(referenceLoc.add(0, 1, 0).createKey(), codeDoor);
        }

        _metalDetectorsLocations = new ArrayList<>();

        _wall = new Wall();

        _obstacles = new ArrayList<>();

        _maze = new Maze();
        List<Dirt> dirts = _maze.buildMaze(config.getMazeUpperCornerLocation().add(reference), config.getMazeFormat());
        _obstacles.addAll(dirts);

        for (List<PrisonEscapeLocation> pair : createLocationsPairList(reference, config.getFencesLocations())) {
            Fence fence = new Fence(pair.get(0), pair.get(1));
            fence.generate();
            _obstacles.add(fence);
        }

        for (PrisonEscapeLocation location : createLocationsList(reference, config.getVentsLocations())) {
            Vent vent = new Vent(location);
            vent.generate();
            _obstacles.add(vent);
        }

        PrisonEscapeLocation helicopterUpperLocation = config.getHelicopterUpperLocation().add(reference);
        PrisonEscapeLocation helicopterLowerLocation = config.getHelicopterLowerLocation().add(reference);
        _helicopter = new Helicopter(helicopterUpperLocation, helicopterLowerLocation);
        _helicopter.departed();
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

    private List<List<PrisonEscapeLocation>> createLocationsPairList(
            PrisonEscapeLocation reference,
            List<List<PrisonEscapeLocation>> pairs
    ) {
        List<List<PrisonEscapeLocation>> list = new ArrayList<>();

        for (List<PrisonEscapeLocation> pair : pairs) {
            list.add(createLocationsList(reference, pair));
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

    public boolean hasCellPhoneCoverage(PrisonEscapeLocation location) {
        for (Region region : _regions) {
            if (region.contains(location)) {
                return region.canCallHelicopter();
            }
        }

        return true;
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

    public void addVaults(List<Prisioner> prisioners) {
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

//  #########################################
//  #                 Walls                 #
//  #########################################

    public void raiseWall() {
        _wall.raiseFixedWall();
    }

    public void putRandomCracks() {
        _wall.putRandomCracks();
    }

    public void removeExplodedBlocks(List<Block> explodedBlocks) {
        List<PrisonEscapeLocation> crackedBlocksLocations = explodedBlocks.stream()
                .filter(b -> b.getType() == BukkitWorldEditor.CRACKED_BLOCK)
                .map(b -> b.getLocation())
                .map(l -> new PrisonEscapeLocation(l.getBlockX(), l.getBlockY(), l.getBlockZ()))
                .toList();

        _wall.crackedBlocksExploded(crackedBlocksLocations);
    }

    public void placeBomb(PrisonEscapeLocation location) {
        BukkitWorldEditor.placeTNT(location);
    }

    public WallCrack getWallCrack(PrisonEscapeLocation location) {
        return _wall.getAffectedCrack(location);
    }

//  #########################################
//  #               Obstacles               #
//  #########################################

    public Obstacle getObstacle(PrisonEscapeLocation location) {
        for (Obstacle obstacle : _obstacles) {
            if (obstacle.contains(location)) {
                return obstacle;
            }
        }

        return null;
    }

//  #########################################
//  #                 Doors                 #
//  #########################################

    public Door getDoor(PrisonEscapeLocation location) {
        return _doors.get(location.createKey());
    }

//  ########################################
//  #              Helicopter              #
//  ########################################

    public Helicopter getHelicopter(PrisonEscapeLocation location) {
        return _helicopter.contains(location) ? _helicopter : null;
    }

    public void callHelicopter() {
        _helicopter.call();
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

    public PrisonEscapeLocation getHelicopterExitLocation() {
        return _helicopterExitLocation;
    }

    public PrisonEscapeLocation getHelicopterJoinLocation() {
        return _helicopterJoinLocation;
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
