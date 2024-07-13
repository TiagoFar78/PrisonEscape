package net.tiagofar78.prisonescape.managers;

import net.tiagofar78.prisonescape.PEResources;
import net.tiagofar78.prisonescape.dataobjects.ItemProbability;
import net.tiagofar78.prisonescape.game.prisonbuilding.regions.Region;
import net.tiagofar78.prisonescape.game.prisonbuilding.regions.SquaredRegion;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map.Entry;

public class ConfigManager {

    private static ConfigManager instance;

    public static boolean load() {
        instance = new ConfigManager();
        return instance.isValid();
    }

    public static ConfigManager getInstance() {
        return instance;
    }

    private double _prisonerRatio;
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
    private int _searchLimit;
    private int _trapPrice;
    private int _sensorPrice;
    private int _radarPrice;
    private int _energyDrinkPrice;
    private int _cameraPrice;
    private int _searchPrice;
    private int _helicopterSpawnDelay;
    private int _helicopterDepartureDelay;
    private int _radarDuration;
    private int _trapDuration;
    private int _blindingDistance;
    private int _blindnessSeconds;
    private double _soundDetectorRange;
    private int _tradeRequestTimeout;
    private int _missionsPerDay;
    private int _missionMoneyReward;
    private int _differencesAmount;
    private int _sequenceSize;

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
    private List<String> _missionsRegions;
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
    private List<List<String>> _wallCrackFormats;
    private List<String> _mazeFormat;
    private Location _mazeUpperCornerLocation;
    private List<List<Location>> _fencesLocations;
    private List<Location> _ventsLocations;
    private List<List<Location>> _metalDetectorLocations;

    private Hashtable<String, List<ItemProbability>> _regionsChestContents;
    private List<ItemProbability> _packagesItemProbabilities;

    private double _commonItemsProbability;
    private double _rareItemsProbability;

    private int _chestSize;

    public ConfigManager() {
        YamlConfiguration config = PEResources.getYamlConfiguration();

        _prisonerRatio = config.getDouble("PrisonersRatio");
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
        _searchLimit = config.getInt("SearchLimit");
        _trapPrice = config.getInt("TrapPrice");
        _sensorPrice = config.getInt("SensorPrice");
        _radarPrice = config.getInt("RadarPrice");
        _energyDrinkPrice = config.getInt("EnergyDrinkPrice");
        _cameraPrice = config.getInt("CameraPrice");
        _searchPrice = config.getInt("SearchPrice");
        _helicopterSpawnDelay = config.getInt("HelicopterSpawnDelay");
        _helicopterDepartureDelay = config.getInt("HelicopterDepartureDelay");
        _radarDuration = config.getInt("RadarDuration");
        _trapDuration = config.getInt("TrapDuration");
        _blindingDistance = config.getInt("BlindingDistance");
        _blindnessSeconds = config.getInt("BlindnessSeconds");
        _soundDetectorRange = config.getDouble("SoundDetectorRange");
        _tradeRequestTimeout = config.getInt("TradeRequestTimeout");
        _missionsPerDay = config.getInt("MissionsPerDay");
        _missionMoneyReward = config.getInt("MissionMoneyReward");
        _differencesAmount = config.getInt("DifferencesAmount");
        _sequenceSize = config.getInt("SequenceSize");

        _availableLanguages = config.getStringList("AvailableLanguages");
        _defaultLanguage = config.getString("DefaultLanguage");

        _teamChatPrefix = config.getString("TeamChatPrefix");

        _cameraSkinSignature = config.getString("CameraSkinSignature");
        _cameraSkinTexture = config.getString("CameraSkinTexture");

        _worldName = config.getString("WorldName");
        World world = Bukkit.getWorld(_worldName);
        _referenceBlock = createLocation(config, "ReferenceBlock", world);
        _leavingLocation = createLocation(config, "LeavingLocation", world);
        _waitingLocation = createLocation(config, "WaitingLocation", world);
        _prisonUpperCornerLocation = createLocation(config, "PrisonTopLeftCornerLocation", world);
        _prisonLowerCornerLocation = createLocation(config, "PrisonBottomRightCornerLocation", world);
        _regions = createRegionsList(config, world);
        _missionsRegions = config.getStringList("MissionsRegions");
        _prisonersSpawnLocation = createLocationList(config, "PrisonersSpawnLocations", world);
        _policeSpawnLocation = createLocationList(config, "PoliceSpawnLocations", world);
        _solitaryLocation = createLocation(config, "SolitaryLocation", world);
        _solitaryExitLocation = createLocation(config, "SolitaryExitLocation", world);
        _helicopterExitLocation = createLocation(config, "Helicopter.ExitLocation", world);
        _helicopterJoinLocation = createLocation(config, "Helicopter.JoinLocation", world);
        _helicopterUpperLocation = createLocation(config, "Helicopter.UpperLocation", world);
        _helicopterLowerLocation = createLocation(config, "Helicopter.LowerLocation", world);
        _afterEscapeLocation = createLocation(config, "AfterEscapeLocation", world);
        _prisonersSecretPassageLocations = createLocationsMap(config, "PrisonersSecretPassagesLocation", world);
        _policeSecretPassageLocations = createLocationsMap(config, "PoliceSecretPassagesLocation", world);
        _vaultsLocations = createLocationList(config, "VaultsLocations", world);
        _vaultsDirection = config.getString("VaultsDirection");
        _chestsLocations = createLocationListList(config, "ChestsLocations", world);
        _goldenDoorsLocations = createLocationList(config, "GoldenDoorsLocations", world);
        _grayDoorsLocations = createLocationList(config, "GrayDoorsLocations", world);
        _codeDoorsLocations = createLocationList(config, "CodeDoorsLocations", world);
        _cellDoorsLocations = createLocationList(config, "CellDoorsLocations", world);
        _wallCornersLocations = createLocationList(config, "WallCorners", world);
        _wallCrackFormats = createStringListList(config, "WallCrackFormats");
        _mazeFormat = config.getStringList("Maze.Format");
        _mazeUpperCornerLocation = createLocation(config, "Maze.UpperCornerLocation", world);
        _fencesLocations = createLocationPairList(config, "Fences", world);
        _ventsLocations = createLocationList(config, "Vents", world);
        _metalDetectorLocations = createLocationPairList(config, "MetalDetectors", world);

        _regionsChestContents = createRegionsChestContentsMap(config);
        _packagesItemProbabilities = createItemProbabilitiesList(config, "PackageItemProbabilities");

        _commonItemsProbability = config.getDouble("CommonItemsProbability");
        _rareItemsProbability = config.getDouble("RareItemsProbability");

        _chestSize = config.getInt("ChestSize");
    }

    private Location createLocation(YamlConfiguration config, String path, World world) {
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

    private List<ItemProbability> createItemProbabilitiesList(YamlConfiguration config, String path) {
        List<ItemProbability> itemsProbabilities = new ArrayList<>();

        List<String> paths = config.getKeys(true).stream().filter(
                key -> key.startsWith(path + ".") && key.lastIndexOf(".") == path.length()
        ).toList();

        for (String itemPath : paths) {
            String itemName = itemPath.substring(itemPath.lastIndexOf(".") + 1);
            double probability = config.getDouble(itemPath);
            itemsProbabilities.add(new ItemProbability(itemName, probability));
        }

        return itemsProbabilities;
    }

    public Double getPrisonerRatio() {
        return _prisonerRatio;
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

    public int getSearchLimit() {
        return _searchLimit;
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

    public int getSearchPrice() {
        return _searchPrice;
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

    public int getTrapDuration() {
        return _trapDuration;
    }

    public int getBlindingDistance() {
        return _blindingDistance;
    }

    public int getBlindnessSeconds() {
        return _blindnessSeconds;
    }

    public int getTradeRequestTimeout() {
        return _tradeRequestTimeout;
    }

    public int getMissionsPerDay() {
        return _missionsPerDay;
    }

    public int getMissionsMoneyReward() {
        return _missionMoneyReward;
    }

    public int getDifferencesAmount() {
        return _differencesAmount;
    }

    public int getSequenceSize() {
        return _sequenceSize;
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

    public List<String> getMissionsRegions() {
        return new ArrayList<>(_missionsRegions);
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

    public List<List<Location>> getMetalDetectorLocations() {
        return createLocationsPairListCopy(_metalDetectorLocations);
    }

    public List<ItemProbability> getChestContents(String regionName) {
        if (!_regionsChestContents.containsKey(regionName)) {
            return null;
        }

        return createItemProbabilityListCopy(_regionsChestContents.get(regionName));
    }

    public List<ItemProbability> getPackageItemProbabilities() {
        return createItemProbabilityListCopy(_packagesItemProbabilities);
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

//  ########################################
//  #               Is Valid               #
//  ########################################

    private boolean isValid() {
        return areMissionsRegionsValid();
    }

    private boolean areMissionsRegionsValid() {
        for (String regionName : _missionsRegions) {
            boolean regionExists = false;
            for (Region region : _regions) {
                if (region.getName().equals(regionName)) {
                    regionExists = true;
                }
            }

            if (!regionExists) {
                throw new IllegalArgumentException("There is no region named " + regionName);
            }
        }

        return true;
    }

}
