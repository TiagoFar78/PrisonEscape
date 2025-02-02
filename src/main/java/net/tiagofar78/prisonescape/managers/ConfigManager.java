package net.tiagofar78.prisonescape.managers;

import net.tiagofar78.prisonescape.PEResources;
import net.tiagofar78.prisonescape.dataobjects.ItemProbability;
import net.tiagofar78.prisonescape.missions.SortMission;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class ConfigManager {

    private static ConfigManager instance;

    public static boolean load() {
        instance = new ConfigManager();
        return instance.isValid();
    }

    public static ConfigManager getInstance() {
        return instance;
    }

    private int _maxGames;
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
    private int _sortSize;

    private List<String> _availableLanguages;
    private String _defaultLanguage;
    private List<String> _availableMaps;

    private String _teamChatPrefix;

    private String _cameraSkinSignature;
    private String _cameraSkinTexture;

    private String _worldName;
    private Location _leavingLocation;
    private List<String> _missionsRegions;
    private List<List<String>> _wallCrackFormats;

    private Hashtable<String, List<ItemProbability>> _regionsChestContents;
    private List<ItemProbability> _packagesItemProbabilities;

    private double _commonItemsProbability;
    private double _rareItemsProbability;

    private int _chestSize;

    public ConfigManager() {
        YamlConfiguration config = PEResources.getYamlConfiguration();

        _maxGames = config.getInt("MaxSimultaneousGames");
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
        _sortSize = config.getInt("SortSize");

        _availableLanguages = config.getStringList("AvailableLanguages");
        _defaultLanguage = config.getString("DefaultLanguage");
        _availableMaps = config.getStringList("AvailableMaps");

        _teamChatPrefix = config.getString("TeamChatPrefix");

        _cameraSkinSignature = config.getString("CameraSkinSignature");
        _cameraSkinTexture = config.getString("CameraSkinTexture");

        _worldName = config.getString("WorldName");
        _leavingLocation = createLocation(config, "LeavingLocation");
        _missionsRegions = config.getStringList("MissionsRegions");
        _wallCrackFormats = createStringListList(config, "WallCrackFormats");

        _regionsChestContents = createRegionsChestContentsMap(config);
        _packagesItemProbabilities = createItemProbabilitiesList(config, "PackageItemProbabilities");

        _commonItemsProbability = config.getDouble("CommonItemsProbability");
        _rareItemsProbability = config.getDouble("RareItemsProbability");

        _chestSize = config.getInt("ChestSize");
    }

    private Location createLocation(YamlConfiguration config, String path) {
        String worldName = config.getString(path + ".World");
        int x = config.getInt(path + ".X");
        int y = config.getInt(path + ".Y");
        int z = config.getInt(path + ".Z");

        return new Location(Bukkit.getWorld(worldName), x, y, z);
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

    public int getMaxGames() {
        return _maxGames;
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

    public int getSortSize() {
        return _sortSize;
    }

    public List<String> getAvailableLanguages() {
        return new ArrayList<>(_availableLanguages);
    }

    public String getDefaultLanguage() {
        return _defaultLanguage;
    }

    public List<String> getAvailableMaps() {
        return new ArrayList<>(_availableMaps);
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

    public Location getLeavingLocation() {
        return createLocationCopy(_leavingLocation);
    }

    public List<String> getMissionsRegions() {
        return new ArrayList<>(_missionsRegions);
    }

    public List<List<String>> getWallCrackFormats() {
        return createStringListListCopy(_wallCrackFormats);
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
        return isSortMissionSizeValid();
    }

    private boolean isSortMissionSizeValid() {
        if (_sortSize <= 2 || _sortSize >= SortMission.ITEMS.length) {
            throw new IllegalArgumentException("Sort size must be between 3 and " + SortMission.ITEMS.length);
        }

        return true;
    }

}
