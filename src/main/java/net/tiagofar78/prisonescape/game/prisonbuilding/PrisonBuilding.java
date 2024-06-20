package net.tiagofar78.prisonescape.game.prisonbuilding;

import net.tiagofar78.prisonescape.PEResources;
import net.tiagofar78.prisonescape.bukkit.BukkitWorldEditor;
import net.tiagofar78.prisonescape.game.PEPlayer;
import net.tiagofar78.prisonescape.game.Prisoner;
import net.tiagofar78.prisonescape.game.prisonbuilding.doors.CellDoor;
import net.tiagofar78.prisonescape.game.prisonbuilding.doors.CodeDoor;
import net.tiagofar78.prisonescape.game.prisonbuilding.doors.Door;
import net.tiagofar78.prisonescape.game.prisonbuilding.doors.GoldenDoor;
import net.tiagofar78.prisonescape.game.prisonbuilding.doors.GrayDoor;
import net.tiagofar78.prisonescape.game.prisonbuilding.placeables.Camera;
import net.tiagofar78.prisonescape.game.prisonbuilding.placeables.SoundDetector;
import net.tiagofar78.prisonescape.game.prisonbuilding.placeables.Trap;
import net.tiagofar78.prisonescape.game.prisonbuilding.regions.Region;
import net.tiagofar78.prisonescape.game.prisonbuilding.regions.SquaredRegion;
import net.tiagofar78.prisonescape.managers.ConfigManager;

import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;

public class PrisonBuilding {

    private static final String PRISON_REGION_NAME = "PRISON";

    private Location _reference;

    private Location _waitingLobbyLocation;
    private Region _prison;
    private List<Region> _regions;
    private List<Location> _prisonersSpawnLocations;
    private List<Location> _policeSpawnLocations;
    private Location _solitaryLocation;
    private Location _solitaryExitLocation;
    private Location _helicopterExitLocation;
    private Location _helicopterJoinLocation;
    private Location _afterEscapeLocation;
    private Hashtable<Location, Location> _prisonersSecretPassageLocations;
    private Hashtable<Location, Location> _policeSecretPassageLocations;
    private List<Location> _metalDetectorsLocations;

    private List<Vault> _vaults;

    private Hashtable<Location, Chest> _chests;
    private Hashtable<Location, Door> _doors;
    private List<CellDoor> _cellDoors;
    private Wall _wall;

    private Maze _maze;
    private List<Obstacle> _obstacles;

    private Helicopter _helicopter;

    private List<Camera> _cameras;
    private List<SoundDetector> _soundDetectors;
    private List<Trap> _traps;

//  #########################################
//  #              Constructor              #
//  #########################################

    public PrisonBuilding(Location reference) {
        ConfigManager config = ConfigManager.getInstance();

        _reference = reference;

        Location prisonUpperCorner = config.getPrisonUpperCornerLocation().add(reference);
        Location prisonLowerCorner = config.getPrisonLowerCornerLocation().add(reference);
        _prison = new SquaredRegion(PRISON_REGION_NAME, false, true, prisonUpperCorner, prisonLowerCorner);

        _regions = createRegionsList(reference, config.getRegions());

        _waitingLobbyLocation = config.getWaitingLobbyLocation().add(reference);

        _prisonersSpawnLocations = createLocationsList(reference, config.getPrisonersSpawnLocations());
        _policeSpawnLocations = createLocationsList(reference, config.getPoliceSpawnLocations());

        _solitaryLocation = config.getSolitaryLocation().add(reference);
        _solitaryExitLocation = config.getSolitaryExitLocation().add(reference);

        _helicopterExitLocation = config.getHelicopterExitLocation().add(reference);
        _helicopterJoinLocation = config.getHelicopterJoinLocation().add(reference);

        _afterEscapeLocation = config.getAfterEscapeLocation().add(reference);

        _prisonersSecretPassageLocations = createLocationsMap(reference, config.getPrisonersSecretPassageLocations());
        _policeSecretPassageLocations = createLocationsMap(reference, config.getPoliceSecretPassageLocations());

        _vaults = new ArrayList<>();

        _chests = new Hashtable<>();
        for (Location loc : config.getChestsLocations()) {
            String regionName = getRegionName(loc);
            if (regionName == null) {
                regionName = "Default";
            }
            _chests.put(loc.add(reference), new Chest(regionName));
        }

        _doors = new Hashtable<>();
        for (Location loc : config.getGoldenDoorsLocations()) {
            Location referenceLoc = loc.add(reference);
            GoldenDoor goldenDoor = new GoldenDoor(referenceLoc);
            _doors.put(referenceLoc, goldenDoor);
            _doors.put(referenceLoc.clone().add(0, 1, 0), goldenDoor);
        }
        for (Location loc : config.getGrayDoorsLocations()) {
            Location referenceLoc = loc.add(reference);
            GrayDoor grayDoor = new GrayDoor(referenceLoc);
            _doors.put(referenceLoc, grayDoor);
            _doors.put(referenceLoc.clone().add(0, 1, 0), grayDoor);
        }
        for (Location loc : config.getCodeDoorsLocations()) {
            Location referenceLoc = loc.add(reference);
            CodeDoor codeDoor = new CodeDoor(referenceLoc);
            _doors.put(referenceLoc, codeDoor);
            _doors.put(referenceLoc.clone().add(0, 1, 0), codeDoor);
        }

        _cellDoors = new ArrayList<CellDoor>();
        for (Location loc : config.getCellDoorsLocations()) {
            Location referenceLoc = loc.add(reference);
            CellDoor door = new CellDoor(referenceLoc);
            _cellDoors.add(door);
        }

        _wall = new Wall();

        _obstacles = new ArrayList<>();

        _maze = new Maze();
        List<Dirt> dirts = _maze.buildMaze(config.getMazeUpperCornerLocation().add(reference), config.getMazeFormat());
        _obstacles.addAll(dirts);

        for (List<Location> pair : createLocationsPairList(reference, config.getFencesLocations())) {
            Fence fence = new Fence(pair.get(0), pair.get(1));
            fence.generate();
            _obstacles.add(fence);
        }

        for (Location location : createLocationsList(reference, config.getVentsLocations())) {
            Vent vent = new Vent(location);
            vent.generate();
            _obstacles.add(vent);
        }

        _metalDetectorsLocations = new ArrayList<>();
        for (Location location : config.getMetalDetectorLocations()) {
            _metalDetectorsLocations.add(location.add(reference));
        }

        Location helicopterUpperLocation = config.getHelicopterUpperLocation().add(reference);
        Location helicopterLowerLocation = config.getHelicopterLowerLocation().add(reference);
        _helicopter = new Helicopter(helicopterUpperLocation, helicopterLowerLocation);
        _helicopter.departed();

        _cameras = new ArrayList<>();
        _soundDetectors = new ArrayList<>();
        _traps = new ArrayList<>();
    }

    private List<Location> createLocationsList(
            Location reference,
            List<Location> locs
    ) {
        List<Location> list = new ArrayList<>();

        for (Location loc : locs) {
            list.add(loc.add(reference));
        }

        return list;
    }

    private List<List<Location>> createLocationsPairList(
            Location reference,
            List<List<Location>> pairs
    ) {
        List<List<Location>> list = new ArrayList<>();

        for (List<Location> pair : pairs) {
            list.add(createLocationsList(reference, pair));
        }

        return list;
    }

    private Hashtable<Location, Location> createLocationsMap(Location reference, Hashtable<Location, Location> locs) {
        Hashtable<Location, Location> map = new Hashtable<>();

        for (Entry<Location, Location> entry : locs.entrySet()) {
            Location key = entry.getKey().add(reference);
            Location value = entry.getValue().add(reference);
            map.put(key, value);
        }

        return map;
    }

    private List<Region> createRegionsList(Location reference, List<SquaredRegion> regions) {
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

    public String getRegionName(Location location) {
        for (Region region : _regions) {
            if (region.contains(location)) {
                return region.getName();
            }
        }

        return null;
    }

    public boolean hasCellPhoneCoverage(Location location) {
        for (Region region : _regions) {
            if (region.contains(location)) {
                return region.canCallHelicopter();
            }
        }

        return true;
    }

    public boolean isInRestrictedArea(Location loc) {
        for (Region region : _regions) {
            if (region.contains(loc)) {
                return region.isRestricted();
            }
        }

        return true;
    }

//	#########################################
//	#                 Vault                 #
//	#########################################

    public void addVaults(List<Prisoner> prisoners) {
        ConfigManager config = ConfigManager.getInstance();
        List<Location> vaultsLocations = createLocationsList(_reference, config.getVaultsLocations());

        for (int i = 0; i < prisoners.size(); i++) {
            _vaults.add(new Vault(prisoners.get(i), vaultsLocations.get(i)));
        }
    }

    public Vault getVault(int index) {
        return _vaults.get(index);
    }

    public int getVaultIndex(Location location) {
        for (int i = 0; i < _vaults.size(); i++) {
            if (_vaults.get(i).isIn(location)) {
                return i;
            }
        }

        return -1;
    }

    public void deleteVaults() {
        for (Vault vault : _vaults) {
            vault.deleteVaultAndRespectiveSignFromWorld();
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

    public Chest getChest(Location location) {
        return _chests.get(location);
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
        List<Location> crackedBlocksLocations = explodedBlocks.stream().filter(
                b -> b.getType() == BukkitWorldEditor.CRACKED_BLOCK
        ).map(b -> b.getLocation()).map(
                l -> new Location(PEResources.getWorld(), l.getBlockX(), l.getBlockY(), l.getBlockZ())
        ).toList();

        _wall.crackedBlocksExploded(crackedBlocksLocations);
    }

    public void placeBomb(Location location) {
        BukkitWorldEditor.placeTNT(location);
    }

    public WallCrack getWallCrack(Location location) {
        return _wall.getAffectedCrack(location);
    }

//  #########################################
//  #               Obstacles               #
//  #########################################

    public Obstacle getObstacle(Location location) {
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

    public Door getDoor(Location location) {
        return _doors.get(location);
    }

    public void openCellDoors() {
        for (CellDoor door : _cellDoors) {
            door.open();
        }
    }

    public void closeCellDoors() {
        for (CellDoor door : _cellDoors) {
            door.close();
        }
    }

//  ########################################
//  #              Helicopter              #
//  ########################################

    public Helicopter getHelicopter(Location location) {
        return _helicopter.contains(location) ? _helicopter : null;
    }

    public void callHelicopter() {
        _helicopter.call();
    }

//  #########################################
//  #              Placeables               #
//  #########################################

    public void deletePlaceables() {
        deleteCameras();
        deleteSoundDetectors();
        deleteTraps();
    }

//  #########################################
//  #                Cameras                #
//  #########################################

    public List<Camera> getCameras() {
        return _cameras;
    }

    public void addCamera(Location location) {
        _cameras.add(new Camera(location));
    }

    private void deleteCameras() {
        for (Camera camera : _cameras) {
            camera.delete();
        }
    }

//  ########################################
//  #            Sound Detector            #
//  ########################################

    public int countSoundDetectors() {
        return _soundDetectors.size();
    }

    public void addSoundDetector(Location location) {
        _soundDetectors.add(new SoundDetector(_soundDetectors.size(), location));
    }

    public List<SoundDetector> getSoundDetectors() {
        return _soundDetectors;
    }

    private void deleteSoundDetectors() {
        for (SoundDetector soundDetector : _soundDetectors) {
            soundDetector.delete();
        }
    }

//  #########################################
//  #                 Traps                 #
//  #########################################

    public List<Trap> getTraps() {
        return _traps;
    }

    public int addTrap(Location location) {
        Trap trap = new Trap(location);
        if (!trap.wasPlaced()) {
            return 1;
        }

        _traps.add(new Trap(location));
        return 0;
    }

    public void checkIfWalkedOverTrap(Location location, PEPlayer player) {
        int locX = location.getBlockX();
        int locY = location.getBlockY();
        int locZ = location.getBlockZ();

        for (Trap trap : _traps) {
            Location trapLocation = trap.getLocation();
            int trapX = trapLocation.getBlockX();
            int trapY = trapLocation.getBlockY();
            int trapZ = trapLocation.getBlockZ();

            if (trapX == locX && (trapY - 1 <= locY || locY <= trapY) && trapZ == locZ) {
                trap.triggerTrap(player);
                _traps.remove(trap);
                break;
            }
        }

    }

    private void deleteTraps() {
        for (Trap trap : _traps) {
            trap.delete();
        }
    }

//  #########################################
//  #            Metal Detectors            #
//  #########################################

    public boolean checkIfWalkedOverMetalDetector(Location location) {
        return _metalDetectorsLocations.contains(location);
    }

//	#########################################
//	#               Locations               #
//	#########################################

    public Location getWaitingLobbyLocation() {
        return _waitingLobbyLocation;
    }

    public Location getPoliceSpawnLocation(int index) {
        return _policeSpawnLocations.get(index);
    }

    public Location getPlayerCellLocation(int index) {
        return _prisonersSpawnLocations.get(index);
    }

    public Location getSolitaryLocation() {
        return _solitaryLocation;
    }

    public Location getSolitaryExitLocation() {
        return _solitaryExitLocation;
    }

    public Location getHelicopterExitLocation() {
        return _helicopterExitLocation;
    }

    public Location getHelicopterJoinLocation() {
        return _helicopterJoinLocation;
    }

    public Location getAfterEscapeLocation() {
        return _afterEscapeLocation;
    }

    public Location getSecretPassageDestinationLocation(Location location, boolean isPrisoner) {
        Hashtable<Location, Location> secretPassageLocations =
                isPrisoner ? _prisonersSecretPassageLocations : _policeSecretPassageLocations;

        return secretPassageLocations.get(location);
    }

    public boolean isOutsidePrison(Location loc) {
        return !_prison.contains(loc);
    }

}
