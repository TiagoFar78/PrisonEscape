package net.tiagofar78.prisonescape.game.prisonbuilding;

import net.tiagofar78.prisonescape.PEResources;
import net.tiagofar78.prisonescape.bukkit.BukkitWorldEditor;
import net.tiagofar78.prisonescape.game.DayPeriod;
import net.tiagofar78.prisonescape.game.Guard;
import net.tiagofar78.prisonescape.game.PEGame;
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
import net.tiagofar78.prisonescape.managers.MapManager;

import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;

public class PrisonBuilding {

    private static final String PRISON_REGION_NAME = "PRISON";

    private PEGame _game;
    private Location _reference;
    private MapManager _map;

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
    private List<MetalDetector> _metalDetectors;

    private List<Vault> _vaults;

    private List<Chest> _chests;
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

    public PrisonBuilding(PEGame game, String mapName, Location reference) {
        MapManager map = MapManager.getInstance(mapName);
        _map = map;

        _game = game;
        _reference = reference;

        Location prisonUpperCorner = map.getPrisonUpperCornerLocation().add(reference);
        Location prisonLowerCorner = map.getPrisonLowerCornerLocation().add(reference);
        _prison = new SquaredRegion(PRISON_REGION_NAME, false, true, prisonUpperCorner, prisonLowerCorner);

        _regions = createRegionsList(reference, map.getRegions());

        _waitingLobbyLocation = map.getWaitingLobbyLocation().add(reference);

        _prisonersSpawnLocations = createLocationsList(reference, map.getPrisonersSpawnLocations());
        _policeSpawnLocations = createLocationsList(reference, map.getPoliceSpawnLocations());

        _solitaryLocation = map.getSolitaryLocation().add(reference);
        _solitaryExitLocation = map.getSolitaryExitLocation().add(reference);

        _helicopterExitLocation = map.getHelicopterExitLocation().add(reference);
        _helicopterJoinLocation = map.getHelicopterJoinLocation().add(reference);

        _afterEscapeLocation = map.getAfterEscapeLocation().add(reference);

        _prisonersSecretPassageLocations = createLocationsMap(reference, map.getPrisonersSecretPassageLocations());
        _policeSecretPassageLocations = createLocationsMap(reference, map.getPoliceSecretPassageLocations());

        _vaults = new ArrayList<>();

        _chests = new ArrayList<>();
        for (List<Location> locs : map.getChestsLocations()) {
            String regionName = getRegionName(locs.get(0));
            if (regionName == null) {
                regionName = "Default";
            }
            _chests.add(new Chest(locs, regionName));
        }

        _doors = new Hashtable<>();
        for (Location loc : map.getGoldenDoorsLocations()) {
            Location referenceLoc = loc.add(reference);
            GoldenDoor goldenDoor = new GoldenDoor(referenceLoc);
            _doors.put(referenceLoc, goldenDoor);
            _doors.put(referenceLoc.clone().add(0, 1, 0), goldenDoor);
        }
        for (Location loc : map.getGrayDoorsLocations()) {
            Location referenceLoc = loc.add(reference);
            GrayDoor grayDoor = new GrayDoor(referenceLoc);
            _doors.put(referenceLoc, grayDoor);
            _doors.put(referenceLoc.clone().add(0, 1, 0), grayDoor);
        }
        for (Location loc : map.getCodeDoorsLocations()) {
            Location referenceLoc = loc.add(reference);
            CodeDoor codeDoor = new CodeDoor(referenceLoc);
            _doors.put(referenceLoc, codeDoor);
            _doors.put(referenceLoc.clone().add(0, 1, 0), codeDoor);
        }

        _cellDoors = new ArrayList<CellDoor>();
        for (Location loc : map.getCellDoorsLocations()) {
            Location referenceLoc = loc.add(reference);
            CellDoor door = new CellDoor(referenceLoc);
            _cellDoors.add(door);
        }

        _wall = new Wall(map);

        _obstacles = new ArrayList<>();

        _maze = new Maze();
        List<Dirt> dirts = _maze.buildMaze(map.getMazeUpperCornerLocation().add(reference), map.getMazeFormat());
        _obstacles.addAll(dirts);

        for (List<Location> pair : createLocationsPairList(reference, map.getFencesLocations())) {
            Fence fence = new Fence(pair.get(0), pair.get(1));
            fence.generate();
            _obstacles.add(fence);
        }

        for (Location location : createLocationsList(reference, map.getVentsLocations())) {
            Vent vent = new Vent(location);
            vent.generate();
            _obstacles.add(vent);
        }

        _metalDetectors = new ArrayList<>();
        for (List<Location> pair : map.getMetalDetectorLocations()) {
            _metalDetectors.add(new MetalDetector(pair.get(0), pair.get(1)));
        }

        Location helicopterUpperLocation = map.getHelicopterUpperLocation().add(reference);
        Location helicopterLowerLocation = map.getHelicopterLowerLocation().add(reference);
        _helicopter = new Helicopter(_game, helicopterUpperLocation, helicopterLowerLocation);
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
//  #               Reference               #
//  #########################################

    public Location getReference() {
        return _reference;
    }

//  #########################################
//  #                Regions                #
//  #########################################

    public Region getRegion(Location location) {
        for (Region region : _regions) {
            if (region.contains(location)) {
                return region;
            }
        }

        return null;
    }

    public Region getRegion(String name) {
        for (Region region : _regions) {
            if (region.getName().equals(name)) {
                return region;
            }
        }

        return null;
    }

    public String getRegionName(Location location) {
        return getRegionName(getRegion(location));
    }

    public String getRegionName(Region region) {
        return region == null ? null : region.getName();
    }

    public boolean hasCellPhoneCoverage(Location location) {
        Region region = getRegion(location);
        return region == null ? true : region.canCallHelicopter();
    }

    public boolean isInRestrictedArea(Location location, DayPeriod dayPeriod) {
        return isRestrictedArea(getRegion(location), dayPeriod);
    }

    public boolean isRestrictedArea(Region region, DayPeriod dayPeriod) {
        boolean isOutsideCell = region == null || !region.getName().equals(PEGame.CELLS_REGION_NAME);
        return region == null || region.isRestricted() || (dayPeriod == DayPeriod.NIGHT && isOutsideCell);
    }

//	#########################################
//	#                 Vault                 #
//	#########################################

    public void addVaults(List<Prisoner> prisoners) {
        List<Location> vaultsLocations = createLocationsList(_reference, _map.getVaultsLocations());

        for (int i = 0; i < prisoners.size(); i++) {
            _vaults.add(new Vault(prisoners.get(i), vaultsLocations.get(i), _map));
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
        for (Chest chest : _chests) {
            chest.reload();
        }
    }

    public Chest getChest(Location location) {
        for (Chest chest : _chests) {
            if (chest.isIn(location)) {
                return chest;
            }
        }

        return null;
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
        _soundDetectors.add(new SoundDetector(_game, _soundDetectors.size(), location));
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

    public int addTrap(Guard player, Location location) {
        Trap trap = new Trap(player, location);
        _traps.add(trap);
        return 0;
    }

    public void checkIfWalkedOverTrap(Location location, PEPlayer player, List<Guard> guards) {
        int locX = location.getBlockX();
        int locY = location.getBlockY();
        int locZ = location.getBlockZ();

        for (Trap trap : _traps) {
            if (trap.isOnTrap(locX, locY, locZ)) {
                trap.triggerTrap(guards, player);
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

    public void checkIfWalkedOverMetalDetector(PEPlayer player, Location locTo, Location locFrom) {
        for (MetalDetector metalDetector : _metalDetectors) {
            if (metalDetector.checkIfDetectedAndTrigger(player, locTo, locFrom)) {
                return;
            }
        }
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
