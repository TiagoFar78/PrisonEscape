package net.tiagofar78.prisonescape.managers;

import net.tiagofar78.prisonescape.PEResources;
import net.tiagofar78.prisonescape.game.prisonbuilding.regions.SquaredRegion;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;

public class MapManager {

    public static boolean load() {
        instance = initialize();
        return true;
    }

    private static Hashtable<String, MapManager> instance = initialize();

    private static Hashtable<String, MapManager> initialize() {
        ConfigManager config = ConfigManager.getInstance();

        Hashtable<String, MapManager> maps = new Hashtable<>();

        List<String> availableMaps = config.getAvailableMaps();
        for (String map : availableMaps) {
            maps.put(map, new MapManager(map));
        }

        return maps;
    }

    public static MapManager getInstance(String language) {
        return instance.get(language);
    }

    private Location _waitingLocation;
    private Location _prisonUpperCornerLocation;
    private Location _prisonLowerCornerLocation;
    private List<SquaredRegion> _regions;
    private List<Location> _prisonersSpawnLocation;
    private List<Location> _policeSpawnLocation;
    private Location _solitaryLocation;
    private Location _solitaryExitLocation;
    private Location _helicopterExitLocation;
    private Location _helicopterJoinLocation;
    private Location _helicopterUpperLocation;
    private Location _helicopterLowerLocation;
    private Location _afterEscapeLocation;
    private Hashtable<Location, Location> _prisonersSecretPassageLocations;
    private Hashtable<Location, Location> _policeSecretPassageLocations;
    private List<Location> _vaultsLocations;
    private String _vaultsDirection;
    private List<List<Location>> _chestsLocations;
    private List<Location> _goldenDoorsLocations;
    private List<Location> _grayDoorsLocations;
    private List<Location> _codeDoorsLocations;
    private List<Location> _cellDoorsLocations;
    private List<Location> _wallCornersLocations;
    private List<String> _mazeFormat;
    private Location _mazeUpperCornerLocation;
    private List<List<Location>> _fencesLocations;
    private List<Location> _ventsLocations;
    private List<List<Location>> _metalDetectorLocations;
    private Location _mapTopLeftCornerLocation;
    private Location _mapBottomRightCornerLocation;
    private BufferedImage _image;

    private MapManager(String mapName) {
        YamlConfiguration map = PEResources.getYamlMap(mapName);

        World world = Bukkit.getWorld(ConfigManager.getInstance().getWorldName());
        _waitingLocation = createLocation(map, "WaitingLocation", world);
        _prisonUpperCornerLocation = createLocation(map, "PrisonTopLeftCornerLocation", world);
        _prisonLowerCornerLocation = createLocation(map, "PrisonBottomRightCornerLocation", world);
        _prisonersSpawnLocation = createLocationList(map, "PrisonersSpawnLocations", world);
        _policeSpawnLocation = createLocationList(map, "PoliceSpawnLocations", world);
        _solitaryLocation = createLocation(map, "SolitaryLocation", world);
        _solitaryExitLocation = createLocation(map, "SolitaryExitLocation", world);
        _helicopterExitLocation = createLocation(map, "Helicopter.ExitLocation", world);
        _helicopterJoinLocation = createLocation(map, "Helicopter.JoinLocation", world);
        _helicopterUpperLocation = createLocation(map, "Helicopter.UpperLocation", world);
        _helicopterLowerLocation = createLocation(map, "Helicopter.LowerLocation", world);
        _afterEscapeLocation = createLocation(map, "AfterEscapeLocation", world);
        _prisonersSecretPassageLocations = createLocationsMap(map, "PrisonersSecretPassagesLocation", world);
        _policeSecretPassageLocations = createLocationsMap(map, "PoliceSecretPassagesLocation", world);
        _vaultsLocations = createLocationList(map, "VaultsLocations", world);
        _vaultsDirection = map.getString("VaultsDirection");
        _chestsLocations = createLocationListList(map, "ChestsLocations", world);
        _goldenDoorsLocations = createLocationList(map, "GoldenDoorsLocations", world);
        _grayDoorsLocations = createLocationList(map, "GrayDoorsLocations", world);
        _codeDoorsLocations = createLocationList(map, "CodeDoorsLocations", world);
        _cellDoorsLocations = createLocationList(map, "CellDoorsLocations", world);
        _regions = createRegionsList(map, world);
        _wallCornersLocations = createLocationList(map, "WallCorners", world);
        _mazeFormat = map.getStringList("Maze.Format");
        _mazeUpperCornerLocation = createLocation(map, "Maze.UpperCornerLocation", world);
        _fencesLocations = createLocationPairList(map, "Fences", world);
        _ventsLocations = createLocationList(map, "Vents", world);
        _metalDetectorLocations = createLocationPairList(map, "MetalDetectors", world);
        _mapTopLeftCornerLocation = createLocation(map, "Map.TopLeftCornerLocation", world);
        _mapBottomRightCornerLocation = createLocation(map, "Map.BottomRightCornerLocation", world);
        _image = PEResources.getMapImage(mapName);
    }

    public static Location createLocation(YamlConfiguration config, String path, World world) {
        int x = config.getInt(path + ".X");
        int y = config.getInt(path + ".Y");
        int z = config.getInt(path + ".Z");

        return new Location(world, x, y, z);
    }

    private List<Location> createLocationList(YamlConfiguration config, String path, World world) {
        List<Location> list = new ArrayList<>();

        List<String> filteredKeys = config.getKeys(true).stream().filter(
                key -> key.startsWith(path) && key.lastIndexOf(".") == path.length()
        ).toList();

        for (String key : filteredKeys) {
            list.add(createLocation(config, key, world));
        }

        return list;
    }

    private List<List<Location>> createLocationListList(YamlConfiguration config, String path, World world) {
        List<List<Location>> list = new ArrayList<>();

        List<String> filteredKeys = config.getKeys(true).stream().filter(
                key -> key.startsWith(path) && key.lastIndexOf(".") == path.length()
        ).toList();

        for (String key : filteredKeys) {
            list.add(createLocationList(config, key, world));
        }

        return list;
    }

    private List<List<Location>> createLocationPairList(YamlConfiguration config, String path, World world) {
        List<List<Location>> list = new ArrayList<>();

        List<String> filteredKeys = config.getKeys(true).stream().filter(
                key -> key.startsWith(path) && key.lastIndexOf(".") == path.length()
        ).toList();

        for (String key : filteredKeys) {
            List<Location> pair = new ArrayList<>();
            pair.add(createLocation(config, key + ".UpperCornerLocation", world));
            pair.add(createLocation(config, key + ".LowerCornerLocation", world));
            list.add(pair);
        }

        return list;
    }

    private Hashtable<Location, Location> createLocationsMap(YamlConfiguration config, String path, World world) {
        Hashtable<Location, Location> map = new Hashtable<>();

        List<String> filteredKeys = config.getKeys(true).stream().filter(
                key -> key.startsWith(path) && key.lastIndexOf(".") == path.length()
        ).toList();

        for (String key : filteredKeys) {
            map.put(createLocation(config, key + ".Key", world), createLocation(config, key + ".Value", world));
        }

        return map;
    }

    private List<SquaredRegion> createRegionsList(YamlConfiguration config, World world) {
        List<SquaredRegion> list = new ArrayList<>();

        String regionsPath = "Regions";

        List<String> regionsNamesPaths = config.getKeys(true).stream().filter(
                key -> key.startsWith(regionsPath) && key.lastIndexOf(".") == regionsPath.length()
        ).toList();

        for (String regionNamePath : regionsNamesPaths) {
            String name = regionNamePath.substring(regionsPath.length() + 1);
            boolean isRestricted = config.getBoolean(regionNamePath + ".IsRestricted");
            boolean cutCellPhoneCalls = config.getBoolean(regionNamePath + ".CutCellPhoneCalls");

            List<String> regionsPaths = config.getKeys(true).stream().filter(
                    key -> key.startsWith(regionNamePath + ".") && key.lastIndexOf(".") == regionNamePath.length() &&
                            !key.contains("IsRestricted") && !key.contains("HasCellPhoneCoverage")
            ).toList();

            for (String regionPath : regionsPaths) {
                Location upperCornerLocation = createLocation(config, regionPath + ".UpperCorner", world);
                Location lowerCornerLocation = createLocation(config, regionPath + ".LowerCorner", world);

                list.add(
                        new SquaredRegion(
                                name,
                                isRestricted,
                                !cutCellPhoneCalls,
                                upperCornerLocation,
                                lowerCornerLocation
                        )
                );
            }
        }

        return list;
    }

    public Location getWaitingLobbyLocation() {
        return createLocationCopy(_waitingLocation);
    }

    public Location getPrisonUpperCornerLocation() {
        return createLocationCopy(_prisonUpperCornerLocation);
    }

    public Location getPrisonLowerCornerLocation() {
        return createLocationCopy(_prisonLowerCornerLocation);
    }

    public List<SquaredRegion> getRegions() {
        return createRegionsListCopy(_regions);
    }

    public List<Location> getPrisonersSpawnLocations() {
        return createLocationsListCopy(_prisonersSpawnLocation);
    }

    public List<Location> getPoliceSpawnLocations() {
        return createLocationsListCopy(_policeSpawnLocation);
    }

    public Location getSolitaryLocation() {
        return createLocationCopy(_solitaryLocation);
    }

    public Location getSolitaryExitLocation() {
        return createLocationCopy(_solitaryExitLocation);
    }

    public Location getHelicopterExitLocation() {
        return createLocationCopy(_helicopterExitLocation);
    }

    public Location getHelicopterJoinLocation() {
        return createLocationCopy(_helicopterJoinLocation);
    }

    public Location getHelicopterUpperLocation() {
        return createLocationCopy(_helicopterUpperLocation);
    }

    public Location getHelicopterLowerLocation() {
        return createLocationCopy(_helicopterLowerLocation);
    }

    public Location getAfterEscapeLocation() {
        return createLocationCopy(_afterEscapeLocation);
    }

    public Hashtable<Location, Location> getPrisonersSecretPassageLocations() {
        return createLocationsMapCopy(_prisonersSecretPassageLocations);
    }

    public Hashtable<Location, Location> getPoliceSecretPassageLocations() {
        return createLocationsMapCopy(_policeSecretPassageLocations);
    }

    public List<Location> getVaultsLocations() {
        return createLocationsListCopy(_vaultsLocations);
    }

    public String getVaultsDirection() {
        return _vaultsDirection;
    }

    public List<List<Location>> getChestsLocations() {
        return createLocationsPairListCopy(_chestsLocations);
    }

    public List<Location> getGoldenDoorsLocations() {
        return createLocationsListCopy(_goldenDoorsLocations);
    }

    public List<Location> getGrayDoorsLocations() {
        return createLocationsListCopy(_grayDoorsLocations);
    }

    public List<Location> getCodeDoorsLocations() {
        return createLocationsListCopy(_codeDoorsLocations);
    }

    public List<Location> getCellDoorsLocations() {
        return createLocationsListCopy(_cellDoorsLocations);
    }

    public List<Location> getWallCornersLocations() {
        return createLocationsListCopy(_wallCornersLocations);
    }

    public List<String> getMazeFormat() {
        return new ArrayList<>(_mazeFormat);
    }

    public Location getMazeUpperCornerLocation() {
        return createLocationCopy(_mazeUpperCornerLocation);
    }

    public List<List<Location>> getFencesLocations() {
        return createLocationsPairListCopy(_fencesLocations);
    }

    public List<Location> getVentsLocations() {
        return createLocationsListCopy(_ventsLocations);
    }

    public List<List<Location>> getMetalDetectorLocations() {
        return createLocationsPairListCopy(_metalDetectorLocations);
    }

    public Location getMapTopLeftCornerLocation() {
        return createLocationCopy(_mapTopLeftCornerLocation);
    }

    public Location getMapBottomRightCornerLocation() {
        return createLocationCopy(_mapBottomRightCornerLocation);
    }

    public BufferedImage getImage() {
        return _image;
    }

//  ########################################
//  #                 Copy                 #
//  ########################################

    private Location createLocationCopy(Location location) {
        return location.clone();
    }

    private List<Location> createLocationsListCopy(List<Location> locations) {
        List<Location> list = new ArrayList<>();

        for (Location location : locations) {
            list.add(createLocationCopy(location));
        }

        return list;
    }

    private List<List<Location>> createLocationsPairListCopy(List<List<Location>> locationPairs) {
        List<List<Location>> list = new ArrayList<>();

        for (List<Location> locationPair : locationPairs) {
            list.add(createLocationsListCopy(locationPair));
        }

        return list;
    }

    private Hashtable<Location, Location> createLocationsMapCopy(Hashtable<Location, Location> locations) {
        Hashtable<Location, Location> map = new Hashtable<>();

        for (Entry<Location, Location> entry : locations.entrySet()) {
            map.put(createLocationCopy(entry.getKey()), createLocationCopy(entry.getValue()));
        }

        return map;
    }

    private List<SquaredRegion> createRegionsListCopy(List<SquaredRegion> regions) {
        List<SquaredRegion> list = new ArrayList<>();

        for (SquaredRegion region : regions) {
            list.add(new SquaredRegion(region));
        }

        return list;
    }

}
