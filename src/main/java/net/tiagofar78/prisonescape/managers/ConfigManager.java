package net.tiagofar78.prisonescape.managers;

import net.tiagofar78.prisonescape.PrisonEscapeResources;
import net.tiagofar78.prisonescape.dataobjects.ItemProbability;
import net.tiagofar78.prisonescape.game.PrisonEscapeItem;
import net.tiagofar78.prisonescape.game.prisonbuilding.PrisonEscapeLocation;
import net.tiagofar78.prisonescape.game.prisonbuilding.regions.SquaredRegion;

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

    private List<String> _availableLanguages;
    private String _defaultLanguage;

    private String _teamChatPrefix;

    private String _worldName;
    private PrisonEscapeLocation _referenceBlock;
    private PrisonEscapeLocation _leavingLocation;
    private PrisonEscapeLocation _waitingLocation;
    private PrisonEscapeLocation _prisonUpperCornerLocation;
    private PrisonEscapeLocation _prisonLowerCornerLocation;
    private List<SquaredRegion> _regions;
    private List<PrisonEscapeLocation> _prisionersSpawnLocation;
    private List<PrisonEscapeLocation> _policeSpawnLocation;
    private PrisonEscapeLocation _solitaryLocation;
    private PrisonEscapeLocation _solitaryExitLocation;
    private Hashtable<PrisonEscapeLocation, PrisonEscapeLocation> _prisionersSecretPassageLocations;
    private Hashtable<PrisonEscapeLocation, PrisonEscapeLocation> _policeSecretPassageLocations;
    private List<PrisonEscapeLocation> _vaultsLocations;
    private List<PrisonEscapeLocation> _chestsLocations;
    
    private Hashtable<String, List<ItemProbability>> _regionsChestContents;

    private double _commonItemsProbability;
    private double _rareItemsProbability;

    private int _chestSize;

    public ConfigManager() {
        YamlConfiguration config = PrisonEscapeResources.getYamlConfiguration();

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

        _availableLanguages = config.getStringList("AvailableLanguages");
        _defaultLanguage = config.getString("DefaultLanguage");

        _teamChatPrefix = config.getString("TeamChatPrefix");

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
        _prisionersSecretPassageLocations = createLocationsMap(config, "PrisionersSecretPassagesLocation");
        _policeSecretPassageLocations = createLocationsMap(config, "PoliceSecretPassagesLocation");
        _vaultsLocations = createLocationList(config, "VaultsLocations");
        _chestsLocations = createLocationList(config, "ChestsLocations");
        
        _regionsChestContents = createRegionsChestContentsMap(config);

        _commonItemsProbability = config.getDouble("CommonItemsProbability");
        _rareItemsProbability = config.getDouble("RareItemsProbability");

        _chestSize = config.getInt("ChestSize");
    }

    private PrisonEscapeLocation createLocation(YamlConfiguration config, String path) {
        int x = config.getInt(path + ".X");
        int y = config.getInt(path + ".Y");
        int z = config.getInt(path + ".Z");

        return new PrisonEscapeLocation(x, y, z);
    }

    private List<PrisonEscapeLocation> createLocationList(YamlConfiguration config, String path) {
        List<PrisonEscapeLocation> list = new ArrayList<>();

        List<String> filteredKeys = config.getKeys(true)
                .stream()
                .filter(key -> key.startsWith(path) && key.lastIndexOf(".") == path.length())
                .toList();

        for (String key : filteredKeys) {
            list.add(createLocation(config, key));
        }

        return list;
    }

    private Hashtable<PrisonEscapeLocation, PrisonEscapeLocation> createLocationsMap(
            YamlConfiguration config,
            String path
    ) {
        Hashtable<PrisonEscapeLocation, PrisonEscapeLocation> map = new Hashtable<>();

        List<String> filteredKeys = config.getKeys(true)
                .stream()
                .filter(key -> key.startsWith(path) && key.lastIndexOf(".") == path.length())
                .toList();

        for (String key : filteredKeys) {
            map.put(createLocation(config, key + ".Key"), createLocation(config, key + ".Value"));
        }

        return map;
    }

    private List<SquaredRegion> createRegionsList(YamlConfiguration config) {
        List<SquaredRegion> list = new ArrayList<>();

        List<String> regionsNamesPaths = config.getKeys(true)
                .stream()
                .filter(key -> key.startsWith("Regions.") && key.lastIndexOf(".") == "Regions".length())
                .toList();

        for (String regionNamePath : regionsNamesPaths) {
            String name = regionNamePath.substring("Regions.".length());
            boolean isRestricted = config.getBoolean(regionNamePath + ".IsRestricted");

            List<String> regionsPaths = config.getKeys(true)
                    .stream()
                    .filter(
                            key -> key.startsWith(regionNamePath + ".") && key.lastIndexOf(".") == regionNamePath
                                    .length() && !key.contains("IsRestricted")
                    )
                    .toList();

            for (String regionPath : regionsPaths) {
                PrisonEscapeLocation upperCornerLocation = createLocation(config, regionPath + ".UpperCorner");
                PrisonEscapeLocation lowerCornerLocation = createLocation(config, regionPath + ".LowerCorner");

                list.add(new SquaredRegion(name, isRestricted, upperCornerLocation, lowerCornerLocation));
            }
        }

        return list;
    }
    
    private Hashtable<String, List<ItemProbability>> createRegionsChestContentsMap(YamlConfiguration config) {
        Hashtable<String, List<ItemProbability>> map = new Hashtable<>();
        
        String chestsContentsPath = "ChestsContents";
        
        List<String> paths = config.getKeys(true).stream().filter(key -> key.startsWith(chestsContentsPath) && key.lastIndexOf(".") != chestsContentsPath.length()).toList();
        for (String path : paths) {
            int lastIndexOfDot = path.lastIndexOf(".");
            String regionName = path.substring(chestsContentsPath.length(), lastIndexOfDot);
            
            List<ItemProbability> itemsProbabilities = map.get(regionName);
            if (itemsProbabilities == null) {
                itemsProbabilities = new ArrayList<>();
                map.put(regionName, itemsProbabilities);
            }
            
            String itemName = path.substring(lastIndexOfDot);
            double probability = config.getDouble(path);
            itemsProbabilities.add(new ItemProbability(PrisonEscapeItem.valueOf(itemName), probability));
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

    public List<String> getAvailableLanguages() {
        return _availableLanguages;
    }

    public String getDefaultLanguage() {
        return _defaultLanguage;
    }

    public String getTeamChatPrefix() {
        return _teamChatPrefix;
    }

    public String getWorldName() {
        return _worldName;
    }

    @Deprecated
    public PrisonEscapeLocation getReferenceBlock() {
        return createLocationCopy(_referenceBlock);
    }

    public PrisonEscapeLocation getLeavingLocation() {
        return createLocationCopy(_leavingLocation);
    }

    public PrisonEscapeLocation getWaitingLobbyLocation() {
        return createLocationCopy(_waitingLocation);
    }

    public PrisonEscapeLocation getPrisonUpperCornerLocation() {
        return createLocationCopy(_prisonUpperCornerLocation);
    }

    public PrisonEscapeLocation getPrisonLowerCornerLocation() {
        return createLocationCopy(_prisonLowerCornerLocation);
    }

    public List<SquaredRegion> getRegions() {
        return createRegionsListCopy(_regions);
    }

    public List<PrisonEscapeLocation> getPrisionersSpawnLocations() {
        return createLocationsListCopy(_prisionersSpawnLocation);
    }

    public List<PrisonEscapeLocation> getPoliceSpawnLocations() {
        return createLocationsListCopy(_policeSpawnLocation);
    }

    public PrisonEscapeLocation getSolitaryLocation() {
        return createLocationCopy(_solitaryLocation);
    }

    public PrisonEscapeLocation getSolitaryExitLocation() {
        return createLocationCopy(_solitaryExitLocation);
    }

    public Hashtable<PrisonEscapeLocation, PrisonEscapeLocation> getPrisionersSecretPassageLocations() {
        return createLocationsMapCopy(_prisionersSecretPassageLocations);
    }

    public Hashtable<PrisonEscapeLocation, PrisonEscapeLocation> getPoliceSecretPassageLocations() {
        return createLocationsMapCopy(_policeSecretPassageLocations);
    }

    public List<PrisonEscapeLocation> getVaultsLocations() {
        return createLocationsListCopy(_vaultsLocations);
    }

    public List<PrisonEscapeLocation> getChestsLocations() {
        return createLocationsListCopy(_chestsLocations);
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

    private PrisonEscapeLocation createLocationCopy(PrisonEscapeLocation location) {
        return new PrisonEscapeLocation(location);
    }

    private List<PrisonEscapeLocation> createLocationsListCopy(List<PrisonEscapeLocation> locations) {
        List<PrisonEscapeLocation> list = new ArrayList<>();

        for (PrisonEscapeLocation location : locations) {
            list.add(createLocationCopy(location));
        }

        return list;
    }

    private Hashtable<PrisonEscapeLocation, PrisonEscapeLocation> createLocationsMapCopy(
            Hashtable<PrisonEscapeLocation, PrisonEscapeLocation> locations
    ) {
        Hashtable<PrisonEscapeLocation, PrisonEscapeLocation> map = new Hashtable<>();

        for (Entry<PrisonEscapeLocation, PrisonEscapeLocation> entry : locations.entrySet()) {
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
            list.add(new ItemProbability(itemProbability.getItem(), itemProbability.getProbability()));
        }
        
        return list;
    }

}
