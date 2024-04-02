package net.tiagofar78.prisonescape.managers;

import net.tiagofar78.prisonescape.PrisonEscapeResources;
import net.tiagofar78.prisonescape.game.prisonbuilding.PrisonEscapeLocation;

import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

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
    private PrisonEscapeLocation _prisonTopLeftCornerLocation;
    private PrisonEscapeLocation _prisonBottomRightCornerLocation;
    private List<PrisonEscapeLocation> _restrictedAreasBottomRightCornerLocations;
    private List<PrisonEscapeLocation> _restrictedAreasTopLeftCornerLocations;
    private List<PrisonEscapeLocation> _prisionersSpawnLocation;
    private List<PrisonEscapeLocation> _policeSpawnLocation;
    private PrisonEscapeLocation _solitaryLocation;
    private PrisonEscapeLocation _solitaryExitLocation;
    private Hashtable<PrisonEscapeLocation, PrisonEscapeLocation> _prisionersSecretPassageLocations;
    private Hashtable<PrisonEscapeLocation, PrisonEscapeLocation> _policeSecretPassageLocations;
    private List<PrisonEscapeLocation> _vaultsLocations;
    private List<PrisonEscapeLocation> _chestsLocations;

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
        _prisonTopLeftCornerLocation = createLocation(config, "PrisonTopLeftCornerLocation");
        _prisonBottomRightCornerLocation = createLocation(config, "PrisonBottomRightCornerLocation");
        _restrictedAreasBottomRightCornerLocations = createLocationList(
                config,
                "PrisonOfficeBottomRightCornerLocations"
        );
        _restrictedAreasTopLeftCornerLocations = createLocationList(config, "PrisonOfficeTopLeftCornerLocations");
        _prisionersSpawnLocation = createLocationList(config, "PrisionersSpawnLocations");
        _policeSpawnLocation = createLocationList(config, "PoliceSpawnLocations");
        _solitaryLocation = createLocation(config, "SolitaryLocation");
        _solitaryExitLocation = createLocation(config, "SolitaryExitLocation");
        _prisionersSecretPassageLocations = createLocationsMap(config, "PrisionersSecretPassagesLocation");
        _policeSecretPassageLocations = createLocationsMap(config, "PoliceSecretPassagesLocation");
        _vaultsLocations = createLocationList(config, "VaultsLocations");
        _chestsLocations = createLocationList(config, "ChestsLocations");

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
        return _referenceBlock;
    }

    public PrisonEscapeLocation getLeavingLocation() {
        return _leavingLocation;
    }

    public PrisonEscapeLocation getWaitingLobbyLocation() {
        return _waitingLocation;
    }

    public PrisonEscapeLocation getPrisonTopLeftCornerLocation() {
        return _prisonTopLeftCornerLocation;
    }

    public PrisonEscapeLocation getPrisonBottomRightCornerLocation() {
        return _prisonBottomRightCornerLocation;
    }

    public List<PrisonEscapeLocation> getRestrictedAreasBottomRightCornerLocations() {
        return _restrictedAreasBottomRightCornerLocations;
    }

    public List<PrisonEscapeLocation> getRestrictedAreasTopLeftCornerLocations() {
        return _restrictedAreasTopLeftCornerLocations;
    }

    public List<PrisonEscapeLocation> getPrisionersSpawnLocations() {
        return _prisionersSpawnLocation;
    }

    public List<PrisonEscapeLocation> getPoliceSpawnLocations() {
        return _policeSpawnLocation;
    }

    public PrisonEscapeLocation getSolitaryLocation() {
        return _solitaryLocation;
    }

    public PrisonEscapeLocation getSolitaryExitLocation() {
        return _solitaryExitLocation;
    }

    public Hashtable<PrisonEscapeLocation, PrisonEscapeLocation> getPrisionersSecretPassageLocations() {
        return _prisionersSecretPassageLocations;
    }

    public Hashtable<PrisonEscapeLocation, PrisonEscapeLocation> getPoliceSecretPassageLocations() {
        return _policeSecretPassageLocations;
    }

    public List<PrisonEscapeLocation> getVaultsLocations() {
        return _vaultsLocations;
    }

    public List<PrisonEscapeLocation> getChestsLocations() {
        return _chestsLocations;
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
}
