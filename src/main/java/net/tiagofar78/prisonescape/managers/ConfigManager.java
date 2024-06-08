package net.tiagofar78.prisonescape.managers;

import net.tiagofar78.prisonescape.PEResources;
import net.tiagofar78.prisonescape.dataobjects.ItemProbability;
import net.tiagofar78.prisonescape.game.prisonbuilding.regions.SquaredRegion;

import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;

public class ConfigManager {

    private static ConfigManager instance = new ConfigManager();

    public static ConfigManager getInstance() {
        return instance;
    }

    private double _prisionerRatio;
    private double _officerRatio;
    private int _minimumPlayers;
    private int _maxPlayers;
    private int _secondsInSolitary;
    private int _daysAmount;
    private int _dayDuration;
    private int _nightDuration;
    private int _waitingPhaseDuration;
    private int _fullLobbyWaitDuration;
    private int _finishedPhaseDuration;
    private int _delayBetweenAnnouncements;
    private int _speedDuration;
    private int _speedLevel;
    private int _startingBalance;
    private int _trapLimit;
    private int _sensorLimit;
    private int _radarLimit;
    private int _energyDrinkLimit;
    private int _cameraLimit;
    private int _trapPrice;
    private int _sensorPrice;
    private int _radarPrice;
    private int _energyDrinkPrice;
    private int _cameraPrice;
    private int _helicopterSpawnDelay;
    private int _helicopterDepartureDelay;
    private int _radarDuration;
    private double _soundDetectorRange;
    private int _tradeRequestTimeout;

    private List<String> _availableLanguages;
    private String _defaultLanguage;

    private String _teamChatPrefix;

    private String _cameraSkinSignature;
    private String _cameraSkinTexture;

    private String _worldName;
    private Location _referenceBlock;
    private Location _leavingLocation;
    private Location _waitingLocation;
    private Location _prisonUpperCornerLocation;
    private Location _prisonLowerCornerLocation;
    private List<SquaredRegion> _regions;
    private List<Location> _prisionersSpawnLocation;
    private List<Location> _policeSpawnLocation;
    private Location _solitaryLocation;
    private Location _solitaryExitLocation;
    private Location _helicopterExitLocation;
    private Location _helicopterJoinLocation;
    private Location _helicopterUpperLocation;
    private Location _helicopterLowerLocation;
    private Location _afterEscapeLocation;
    private Hashtable<Location, Location> _prisionersSecretPassageLocations;
    private Hashtable<Location, Location> _policeSecretPassageLocations;
    private List<Location> _vaultsLocations;
    private String _vaultsDirection;
    private List<Location> _chestsLocations;
    private List<Location> _goldenDoorsLocations;
    private List<Location> _grayDoorsLocations;
    private List<Location> _codeDoorsLocations;
    private List<Location> _cellDoorsLocations;
    private List<Location> _wallCornersLocations;
    private List<List<String>> _wallCrackFormats;
    private List<String> _mazeFormat;
    private Location _mazeUpperCornerLocation;
    private List<List<Location>> _fencesLocations;
    private List<Location> _ventsLocations;
    private List<Location> _metalDetectorLocations;

    private Hashtable<String, List<ItemProbability>> _regionsChestContents;

    private double _commonItemsProbability;
    private double _rareItemsProbability;

    private int _chestSize;

    public ConfigManager() {
        YamlConfiguration config = PEResources.getYamlConfiguration();

        _prisionerRatio = config.getDouble("PrisionersRatio");
        _officerRatio = config.getDouble("PoliceRatio");
        _minimumPlayers = config.getInt("MinPlayers");
        _maxPlayers = config.getInt("MaxPlayers");
        _waitingPhaseDuration = config.getInt("WaitingPhaseDuration");
        _fullLobbyWaitDuration = config.getInt("FullLobbyWaitDuration");
        _finishedPhaseDuration = config.getInt("FinishedPhaseDuration");
        _delayBetweenAnnouncements = config.getInt("DelayBetweenAnnouncements");
        _secondsInSolitary = config.getInt("SecondsInSolitary");
        _daysAmount = config.getInt("DaysAmount");
        _dayDuration = config.getInt("DayDuration");
        _nightDuration = config.getInt("NightDuration");
        _speedDuration = config.getInt("SpeedDuration");
        _speedLevel = config.getInt("SpeedLevel");
        _startingBalance = config.getInt("StartingBalance");
        _trapLimit = config.getInt("TrapLimit");
        _sensorLimit = config.getInt("SensorLimit");
        _radarLimit = config.getInt("RadarLimit");
        _energyDrinkLimit = config.getInt("EnergyDrinkLimit");
        _cameraLimit = config.getInt("CameraLimit");
        _trapPrice = config.getInt("TrapPrice");
        _sensorPrice = config.getInt("SensorPrice");
        _radarPrice = config.getInt("RadarPrice");
        _energyDrinkPrice = config.getInt("EnergyDrinkPrice");
        _cameraPrice = config.getInt("CameraPrice");
        _helicopterSpawnDelay = config.getInt("HelicopterSpawnDelay");
        _helicopterDepartureDelay = config.getInt("HelicopterDepartureDelay");
        _radarDuration = config.getInt("RadarDuration");
        _soundDetectorRange = config.getDouble("SoundDetectorRange");
        _tradeRequestTimeout = config.getInt("TradeRequestTimeout");

        _availableLanguages = config.getStringList("AvailableLanguages");
        _defaultLanguage = config.getString("DefaultLanguage");

        _teamChatPrefix = config.getString("TeamChatPrefix");

        _cameraSkinSignature = config.getString("CameraSkinSignature");
        _cameraSkinTexture = config.getString("CameraSkinTexture");

        _worldName = config.getString("WorldName");
        _referenceBlock = createLocation(config, "ReferenceBlock");
        _leavingLocation = createLocation(config, "LeavingLocation");
        _waitingLocation = createLocation(config, "WaitingLocation");
        _prisonUpperCornerLocation = createLocation(config, "PrisonTopLeftCornerLocation");
        _prisonLowerCornerLocation = createLocation(config, "PrisonBottomRightCornerLocation");
        _regions = createRegionsList(config);
        _prisionersSpawnLocation = createLocationList(config, "PrisionersSpawnLocations");
        _policeSpawnLocation = createLocationList(config, "PoliceSpawnLocations");
        _solitaryLocation = createLocation(config, "SolitaryLocation");
        _solitaryExitLocation = createLocation(config, "SolitaryExitLocation");
        _helicopterExitLocation = createLocation(config, "Helicopter.ExitLocation");
        _helicopterJoinLocation = createLocation(config, "Helicopter.JoinLocation");
        _helicopterUpperLocation = createLocation(config, "Helicopter.UpperLocation");
        _helicopterLowerLocation = createLocation(config, "Helicopter.LowerLocation");
        _afterEscapeLocation = createLocation(config, "AfterEscapeLocation");
        _prisionersSecretPassageLocations = createLocationsMap(config, "PrisionersSecretPassagesLocation");
        _policeSecretPassageLocations = createLocationsMap(config, "PoliceSecretPassagesLocation");
        _vaultsLocations = createLocationList(config, "VaultsLocations");
        _vaultsDirection = config.getString("VaultsDirection");
        _chestsLocations = createLocationList(config, "ChestsLocations");
        _goldenDoorsLocations = createLocationList(config, "GoldenDoorsLocations");
        _grayDoorsLocations = createLocationList(config, "GrayDoorsLocations");
        _codeDoorsLocations = createLocationList(config, "CodeDoorsLocations");
        _cellDoorsLocations = createLocationList(config, "CellDoorsLocations");
        _wallCornersLocations = createLocationList(config, "WallCorners");
        _wallCrackFormats = createStringListList(config, "WallCrackFormats");
        _mazeFormat = config.getStringList("Maze.Format");
        _mazeUpperCornerLocation = createLocation(config, "Maze.UpperCornerLocation");
        _fencesLocations = createLocationPairList(config, "Fences");
        _ventsLocations = createLocationList(config, "Vents");
        _metalDetectorLocations = createLocationList(config, "MetalDetectors");

        _regionsChestContents = createRegionsChestContentsMap(config);

        _commonItemsProbability = config.getDouble("CommonItemsProbability");
        _rareItemsProbability = config.getDouble("RareItemsProbability");

        _chestSize = config.getInt("ChestSize");
    }

    private Location createLocation(YamlConfiguration config, String path) {
        int x = config.getInt(path + ".X");
        int y = config.getInt(path + ".Y");
        int z = config.getInt(path + ".Z");

        return new Location(PEResources.getWorld(), x, y, z);
    }

    private List<Location> createLocationList(YamlConfiguration config, String path) {
        List<Location> list = new ArrayList<>();

        List<String> filteredKeys = config.getKeys(true).stream().filter(
                key -> key.startsWith(path) && key.lastIndexOf(".") == path.length()
        ).toList();

        for (String key : filteredKeys) {
            list.add(createLocation(config, key));
        }

        return list;
    }

    private List<List<Location>> createLocationPairList(YamlConfiguration config, String path) {
        List<List<Location>> list = new ArrayList<>();

        List<String> filteredKeys = config.getKeys(true).stream().filter(
                key -> key.startsWith(path) && key.lastIndexOf(".") == path.length()
        ).toList();

        for (String key : filteredKeys) {
            List<Location> pair = new ArrayList<>();
            pair.add(createLocation(config, key + ".UpperCornerLocation"));
            pair.add(createLocation(config, key + ".LowerCornerLocation"));
            list.add(pair);
        }

        return list;
    }

    private List<List<String>> createStringListList(YamlConfiguration config, String path) {
        List<List<String>> list = new ArrayList<>();

        List<String> filteredKeys = config.getKeys(true).stream().filter(
                key -> key.startsWith(path) && key.lastIndexOf(".") == path.length()
        ).toList();

        for (String key : filteredKeys) {
            list.add(config.getStringList(key));
        }

        return list;
    }

    private Hashtable<Location, Location> createLocationsMap(
            YamlConfiguration config,
            String path
    ) {
        Hashtable<Location, Location> map = new Hashtable<>();

        List<String> filteredKeys = config.getKeys(true).stream().filter(
                key -> key.startsWith(path) && key.lastIndexOf(".") == path.length()
        ).toList();

        for (String key : filteredKeys) {
            map.put(createLocation(config, key + ".Key"), createLocation(config, key + ".Value"));
        }

        return map;
    }

    private List<SquaredRegion> createRegionsList(YamlConfiguration config) {
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
                Location upperCornerLocation = createLocation(config, regionPath + ".UpperCorner");
                Location lowerCornerLocation = createLocation(config, regionPath + ".LowerCorner");

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

    private Hashtable<String, List<ItemProbability>> createRegionsChestContentsMap(YamlConfiguration config) {
        Hashtable<String, List<ItemProbability>> map = new Hashtable<>();

        String chestsContentsPath = "ChestsContents";

        List<String> paths = config.getKeys(true).stream().filter(
                key -> key.startsWith(chestsContentsPath + ".") && key.lastIndexOf(".") != chestsContentsPath.length()
        ).toList();
        for (String path : paths) {
            int lastIndexOfDot = path.lastIndexOf(".");
            String regionName = path.substring(chestsContentsPath.length() + 1, lastIndexOfDot);

            List<ItemProbability> itemsProbabilities = map.get(regionName);
            if (itemsProbabilities == null) {
                itemsProbabilities = new ArrayList<>();
                map.put(regionName, itemsProbabilities);
            }

            String itemName = path.substring(lastIndexOfDot + 1);
            double probability = config.getDouble(path);
            itemsProbabilities.add(new ItemProbability(itemName, probability));
        }

        return map;
    }

    public Double getPrisionerRatio() {
        return _prisionerRatio;
    }

    public Double getOfficerRatio() {
        return _officerRatio;
    }

    public int getMinimumPlayers() {
        return _minimumPlayers;
    }

    public int getMaxPlayers() {
        return _maxPlayers;
    }

    public int getSecondsInSolitary() {
        return _secondsInSolitary;
    }

    public int getDaysAmount() {
        return _daysAmount;
    }

    public int getDayDuration() {
        return _dayDuration;
    }

    public int getNightDuration() {
        return _nightDuration;
    }

    public int getWaitingPhaseDuration() {
        return _waitingPhaseDuration;
    }

    public int getFullLobbyWaitDuration() {
        return _fullLobbyWaitDuration;
    }

    public int getFinishedPhaseDuration() {
        return _finishedPhaseDuration;
    }

    public int getDelayBetweenAnnouncements() {
        return _delayBetweenAnnouncements;
    }

    public int getSpeedDuration() {
        return _speedDuration;
    }

    public int getSpeedLevel() {
        return _speedLevel;
    }

    public int getStartingBalance() {
        return _startingBalance;
    }

    public int getTrapLimit() {
        return _trapLimit;
    }

    public int getSensorLimit() {
        return _sensorLimit;
    }

    public int getRadarLimit() {
        return _radarLimit;
    }

    public int getEnergyDrinkLimit() {
        return _energyDrinkLimit;
    }

    public int getCameraLimit() {
        return _cameraLimit;
    }

    public int getTrapPrice() {
        return _trapPrice;
    }

    public int getSensorPrice() {
        return _sensorPrice;
    }

    public int getRadarPrice() {
        return _radarPrice;
    }

    public int getEnergyDrinkPrice() {
        return _energyDrinkPrice;
    }

    public int getCameraPrice() {
        return _cameraPrice;
    }

    public int getHelicopterSpawnDelay() {
        return _helicopterSpawnDelay;
    }

    public int getHelicopterDepartureDelay() {
        return _helicopterDepartureDelay;
    }

    public double getSoundDetectorRange() {
        return _soundDetectorRange;
    }

    public int getRadarDuration() {
        return _radarDuration;
    }

    public int getTradeRequestTimeout() {
        return _tradeRequestTimeout;
    }

    public List<String> getAvailableLanguages() {
        return new ArrayList<>(_availableLanguages);
    }

    public String getDefaultLanguage() {
        return _defaultLanguage;
    }

    public String getTeamChatPrefix() {
        return _teamChatPrefix;
    }

    public String getCameraSkinSignature() {
        return _cameraSkinSignature;
    }

    public String getCameraSkinTexture() {
        return _cameraSkinTexture;
    }

    public String getWorldName() {
        return _worldName;
    }

    @Deprecated
    public Location getReferenceBlock() {
        return createLocationCopy(_referenceBlock);
    }

    public Location getLeavingLocation() {
        return createLocationCopy(_leavingLocation);
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

    public List<Location> getPrisionersSpawnLocations() {
        return createLocationsListCopy(_prisionersSpawnLocation);
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

    public Hashtable<Location, Location> getPrisionersSecretPassageLocations() {
        return createLocationsMapCopy(_prisionersSecretPassageLocations);
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

    public List<Location> getChestsLocations() {
        return createLocationsListCopy(_chestsLocations);
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

    public List<List<String>> getWallCrackFormats() {
        return createStringListListCopy(_wallCrackFormats);
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

    public List<Location> getMetalDetectorLocations() {
        return createLocationsListCopy(_metalDetectorLocations);
    }

    public List<ItemProbability> getChestContents(String regionName) {
        if (!_regionsChestContents.containsKey(regionName)) {
            return null;
        }

        return createItemProbabilityListCopy(_regionsChestContents.get(regionName));
    }

    public double getCommonItemsProbability() {
        return _commonItemsProbability;
    }

    public double getRareItemsProbability() {
        return _rareItemsProbability;
    }

    public int getChestSize() {
        return _chestSize;
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

    private List<List<Location>> createLocationsPairListCopy(
            List<List<Location>> locationPairs
    ) {
        List<List<Location>> list = new ArrayList<>();

        for (List<Location> locationPair : locationPairs) {
            list.add(createLocationsListCopy(locationPair));
        }

        return list;
    }

    private Hashtable<Location, Location> createLocationsMapCopy(
            Hashtable<Location, Location> locations
    ) {
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

    private List<ItemProbability> createItemProbabilityListCopy(List<ItemProbability> itemsProbabilities) {
        List<ItemProbability> list = new ArrayList<>();

        for (ItemProbability itemProbability : itemsProbabilities) {
            list.add(new ItemProbability(itemProbability.getItemName(), itemProbability.getProbability()));
        }

        return list;
    }

    private List<List<String>> createStringListListCopy(List<List<String>> strings) {
        List<List<String>> list = new ArrayList<>();

        for (List<String> s : strings) {
            list.add(new ArrayList<String>(s));
        }

        return list;
    }

}
