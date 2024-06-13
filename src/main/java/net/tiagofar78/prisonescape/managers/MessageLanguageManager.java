package net.tiagofar78.prisonescape.managers;

import net.tiagofar78.prisonescape.PEResources;

import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class MessageLanguageManager {

    public static boolean load() {
        // Just needs to enter this class to initialize static values
        return true;
    }

    private static Hashtable<String, MessageLanguageManager> instance = initializeLanguageMessages();

    private static Hashtable<String, MessageLanguageManager> initializeLanguageMessages() {
        ConfigManager config = ConfigManager.getInstance();

        Hashtable<String, MessageLanguageManager> languagesMessages = new Hashtable<>();

        List<String> availableLanguages = config.getAvailableLanguages();
        String defaultLanguage = config.getDefaultLanguage();

        if (availableLanguages.size() == 0) {
            availableLanguages.add(defaultLanguage);
        }

        for (String language : availableLanguages) {
            languagesMessages.put(language, new MessageLanguageManager(language));
        }

        return languagesMessages;
    }

    public static MessageLanguageManager getInstance(String language) {
        if (language == null) {
            language = ConfigManager.getInstance().getDefaultLanguage();
        }

        return instance.get(language);
    }

    public static MessageLanguageManager getInstanceByPlayer(String playerName) {
        String language = MessageLanguageManager.getPlayerLanguage(playerName);
        if (language == null) {
            language = ConfigManager.getInstance().getDefaultLanguage();
        }

        return MessageLanguageManager.getInstance(language);
    }

    private static String getPlayerLanguage(String playerName) {
        return ConfigManager.getInstance().getDefaultLanguage();
    }

//  #######################################
//  #               BossBar               #
//  #######################################

    private String _bossBarDayTitle;
    private String _bossBarNightTitle;

//  ########################################
//  #              Scoreboard              #
//  ########################################

    private String _sbDisplayName;
    private String _sideBarLastLine;
    private String _guardSideBarBalanceLine;
    private String _guardSideBarSoundDetectorLine;

//	#######################################
//	#                Items                #
//	#######################################

    private Hashtable<String, String> _itemsNames;
    private Hashtable<String, List<String>> _itemsLores;
    private String _itemMetalicProperty;
    private String _itemIllegalProperty;
    private String _itemLoreLine;

//	#######################################
//	#              Inventory              #
//	#######################################

    private String _containerName;
    private String _vaultTitle;
    private String _vaultHiddenGlassName;
    private String _tradeTitle;
    private String _tradeInvalidWoolName;
    private String _tradeAcceptWoolName;
    private String _tradeAcceptedWoolName;
    private String _tradeNotAcceptedGlassName;
    private String _tradeAcceptedGlassName;
    private String _craftingMenuTitle;
    private String _missingItemsWoolName;
    private String _confirmCraftWoolName;

//	########################################
//	#                 Chat                 #
//	########################################

    private String _generalMessage;
    private String _policeTeamMessage;
    private String _prisonerTeamMessage;

//	########################################
//	#               Warnings               #
//	########################################

    private String _successfullyStartedGameMessage;
    private String _successfullyForceStartedGameMessage;
    private String _successfullyForceStoppedGameMessage;
    private String _successfullyStoppedGameMessage;
    private String _selectedPrisonersTeamMessage;
    private String _selectedPoliceTeamMessage;
    private String _removedTeamPreferenceMessage;
    private String _prisonerGameStartedMessage;
    private String _policeGameStartedMessage;
    private String _policeOpenVaultMessage;
    private String _prisonerOtherVaultMessage;
    private String _policeFoundIllegalItemsMessage;
    private String _prisonerFoundIllegalItemsMessage;
    private String _policeNoIllegalItemsFoundMessage;
    private String _prisonerNoIllegalItemsFoundMessage;
    private String _policeCanNotOpenChestMessage;
    private String _chestAlreadyOpenedMessage;
    private String _fullInventoryMessage;
    private String _notWantedPlayerMessage;
    private String _policeInspectedMessage;
    private String _prisonerInspectedMessage;
    private String _canOnlyFixHolesMessage;
    private String _reachedItemLimitMessage;
    private String _notEnoughMoneyMessage;
    private String _successfullyBoughtItemMessage;
    private String _cannotDropThatItemMessage;
    private String _helicopterOnTheWayMessage;
    private String _noCellPhoneCoverageMessage;
    private String _cameraPlacedMessage;
    private String _noCamerasPlacedMessage;
    private String _sneakToLeaveCameraMessage;
    private String _soundDetectorPlacedMessage;
    private String _invalidSoundDetectorLocMessage;
    private String _tradeAlreadyAcceptedMessage;
    private String _tradeRequestSentMessage;
    private String _tradeRequestReceivedMessage;
    private String _craftingItemsMissingMessage;
    private String _codeFoundMessage;

//	########################################
//	#             Announcements            #
//	########################################

    private List<String> _gameStartingAnnouncementMessage;
    private String _prisonerArrested;
    private String _prisonerFreedOfSolitary;
    private String _prisonersWonTitle;
    private String _prisonersWonSubtitle;
    private String _policeWonTitle;
    private String _policeWonSubtitle;
    private String _victoryWord;
    private String _defeatWord;
    private List<String> _gameResultMessage;
    private String _playerEscapedMessage;
    private String _gameCancelledNotEnoughPlayers;
    private String _newDayTitleMessage;
    private String _newDaySubtitleMessage;
    private String _nightTitleMessage;
    private String _nightSubtitleMessage;
    private String _successfullyJoinedGameMessage;
    private String _successfullyLeftGameMessage;
    private String _successfullyRejoinedGameMessage;

//	########################################
//	#                Errors                #
//	########################################

    private String _notAllowedMessage;
    private String _onlyPlayersCanUseThisCommandMessage;

    private String _gameAlreadyStartedMessage;
    private String _gameNotStartedYetMessage;
    private String _gameAlreadyOngoingMessage;
    private String _gameHasNotStartedUseJoinInsteadMessage;
    private String _gameIsNotInFinishedPhaseMessage;
    private String _lobbyIsFullMessage;

    private String _playerAlreadyJoinedMessage;
    private String _playerNotOnLobbyMessage;
    private String _playerWasNeverInGameMessage;

//	########################################
//	#                Usages                #
//	########################################

    private List<String> _usageMessage;
    private String _startCommandUsage;
    private String _forceStartCommandUsage;
    private String _joinCommandUsage;
    private String _leaveCommandUsage;
    private String _forceStopCommandUsage;
    private String _rejoinCommandUsage;
    private String _stopCommandUsage;

    private MessageLanguageManager(String language) {
        YamlConfiguration messages = PEResources.getYamlLanguage(language);

        String bossBarPath = "BossBar.";
        _bossBarDayTitle = createMessage(messages.getString(bossBarPath + "DayTitle"));
        _bossBarNightTitle = createMessage(messages.getString(bossBarPath + "NightTitle"));

        String scoreboardPath = "Scoreboard.";
        _sbDisplayName = createMessage(messages.getString(scoreboardPath + "DisplayName"));
        _sideBarLastLine = createMessage(messages.getString(scoreboardPath + "LastLine"));
        _guardSideBarBalanceLine = createMessage(messages.getString(scoreboardPath + "GuardSideBar.BalanceLine"));
        _guardSideBarSoundDetectorLine = createMessage(
                messages.getString(scoreboardPath + "GuardSideBar.SoundDectorLine")
        );

        _itemsNames = new Hashtable<>();
        _itemsLores = new Hashtable<>();

        String itemsPath = "Items";
        for (String itemConfigName : getItemsConfigNames(messages, itemsPath)) {
            String itemPath = itemsPath + "." + itemConfigName;

            String itemName = messages.getString(itemPath + ".Name");
            if (itemName != null) {
                _itemsNames.put(itemConfigName, createMessage(itemName));
            }

            List<String> itemLore = messages.getStringList(itemPath + ".Lore");
            if (itemLore != null) {
                _itemsLores.put(itemConfigName, createMessage(itemLore));
            }
        }

        String itemsPropertiesPath = "ItemsProperties.";
        _itemMetalicProperty = createMessage(messages.getString(itemsPropertiesPath + "Metalic"));
        _itemIllegalProperty = createMessage(messages.getString(itemsPropertiesPath + "Illegal"));
        _itemLoreLine = createMessage(messages.getString(itemsPropertiesPath + "LoreLine"));

        _containerName = createMessage(messages.getString("Inventory.Chest.Title"));
        _vaultTitle = createMessage(messages.getString("Inventory.Vault.Title"));
        _vaultHiddenGlassName = createMessage(messages.getString("Inventory.Vault.HiddenGlass.Name"));
        _tradeTitle = createMessage(messages.getString("Inventory.Trade.Title"));
        _tradeInvalidWoolName = createMessage(messages.getString("Inventory.Trade.InvalidWool.Name"));
        _tradeAcceptWoolName = createMessage(messages.getString("Inventory.Trade.AcceptWool.Name"));
        _tradeAcceptedWoolName = createMessage(messages.getString("Inventory.Trade.AcceptedWool.Name"));
        _tradeNotAcceptedGlassName = createMessage(messages.getString("Inventory.Trade.NotAcceptedGlass.Name"));
        _tradeAcceptedGlassName = createMessage(messages.getString("Inventory.Trade.AcceptedGlass.Name"));
        _craftingMenuTitle = createMessage(messages.getString("Inventory.Crafting.Title"));
        _missingItemsWoolName = createMessage(messages.getString("Inventory.Crafting.MissingItemsWool.Name"));
        _confirmCraftWoolName = createMessage(messages.getString("Inventory.Crafting.ConfirmCraftWool.Name"));

        String messagePath = "Messages.";
        _generalMessage = createMessage(messages.getString(messagePath + "GeneralMessage"));
        _policeTeamMessage = createMessage(messages.getString(messagePath + "PoliceTeamMessage"));
        _prisonerTeamMessage = createMessage(messages.getString(messagePath + "PrisonerTeamMessage"));

        String warningPath = messagePath + "Warnings.";
        _successfullyStartedGameMessage = createMessage(messages.getString(warningPath + "StartedGame"));
        _successfullyForceStartedGameMessage = createMessage(messages.getString(warningPath + "ForceStartedGame"));
        _successfullyForceStoppedGameMessage = createMessage(messages.getString(warningPath + "ForceStoppedGame"));
        _successfullyStoppedGameMessage = createMessage(messages.getString(warningPath + "StoppedGame"));
        _selectedPrisonersTeamMessage = createMessage(messages.getString(warningPath + "SelectedPrisonersTeam"));
        _selectedPoliceTeamMessage = createMessage(messages.getString(warningPath + "SelectedPoliceTeam"));
        _removedTeamPreferenceMessage = createMessage(messages.getString(warningPath + "SelectedRandomTeam"));
        _prisonerGameStartedMessage = createMessage(messages.getString(warningPath + "PrisonerGameStart"));
        _policeGameStartedMessage = createMessage(messages.getString(warningPath + "PoliceGameStart"));
        _policeOpenVaultMessage = createMessage(messages.getString(warningPath + "PoliceOpenVault"));
        _prisonerOtherVaultMessage = createMessage(messages.getString(warningPath + "PrisonerOtherVault"));
        _policeFoundIllegalItemsMessage = createMessage(messages.getString(warningPath + "PoliceFoundIllegalItems"));
        _prisonerFoundIllegalItemsMessage = createMessage(
                messages.getString(warningPath + "PrisonerFoundIllegalItems")
        );
        _policeNoIllegalItemsFoundMessage = createMessage(
                messages.getString(warningPath + "PoliceNoIllegalItemsFound")
        );
        _prisonerNoIllegalItemsFoundMessage = createMessage(
                messages.getString(warningPath + "PrisonerNoIllegalItemsFound")
        );
        _policeCanNotOpenChestMessage = createMessage(messages.getString(warningPath + "PoliceCanNotOpenChest"));
        _chestAlreadyOpenedMessage = createMessage(messages.getString(warningPath + "ChestAlreadyOpened"));
        _fullInventoryMessage = createMessage(messages.getString(warningPath + "FullInventory"));
        _notWantedPlayerMessage = createMessage(messages.getString(warningPath + "NotWantedPlayer"));
        _policeInspectedMessage = createMessage(messages.getString(warningPath + "PoliceInspected"));
        _prisonerInspectedMessage = createMessage(messages.getString(warningPath + "PrisonerInspected"));
        _canOnlyFixHolesMessage = createMessage(messages.getString(warningPath + "CanOnlyFixHoles"));
        _reachedItemLimitMessage = createMessage(messages.getString(warningPath + "ReachedItemLimit"));
        _notEnoughMoneyMessage = createMessage(messages.getString(warningPath + "NotEnoughMoney"));
        _successfullyBoughtItemMessage = createMessage(messages.getString(warningPath + "SuccessfullyBoughtItem"));
        _cannotDropThatItemMessage = createMessage(messages.getString(warningPath + "CannotDropThatItem"));
        _helicopterOnTheWayMessage = createMessage(messages.getString(warningPath + "HelicopterOnTheWay"));
        _noCellPhoneCoverageMessage = createMessage(messages.getString(warningPath + "NoCellPhoneCoverage"));
        _cameraPlacedMessage = createMessage(messages.getString(warningPath + "CameraPlaced"));
        _noCamerasPlacedMessage = createMessage(messages.getString(warningPath + "NoCamerasPlaced"));
        _sneakToLeaveCameraMessage = createMessage(messages.getString(warningPath + "SneakToLeaveCamera"));
        _soundDetectorPlacedMessage = createMessage(messages.getString(warningPath + "SoundDetectorPlaced"));
        _invalidSoundDetectorLocMessage = createMessage(messages.getString(warningPath + "InvalidSoundDetectorLoc"));
        _tradeAlreadyAcceptedMessage = createMessage(messages.getString(warningPath + "TradeAlreadyAccepted"));
        _tradeRequestSentMessage = createMessage(messages.getString(warningPath + "TradeRequestSent"));
        _tradeRequestReceivedMessage = createMessage(messages.getString(warningPath + "TradeRequestReceived"));
        _craftingItemsMissingMessage = createMessage(messages.getString(warningPath + "CraftingItemsMissing"));
        _codeFoundMessage = createMessage(messages.getString(warningPath + "CodeFound"));

        String announcementPath = messagePath + "Announcements.";
        _gameStartingAnnouncementMessage = createMessage(messages.getStringList(announcementPath + "GameStarting"));
        _prisonerArrested = createMessage(messages.getString(announcementPath + "PrisonerArrested"));
        _prisonerFreedOfSolitary = createMessage(messages.getString(announcementPath + "PrisonerFreedOfSolitary"));
        _prisonersWonTitle = createMessage(messages.getString(announcementPath + "PrisonersWonTitle"));
        _prisonersWonSubtitle = createMessage(messages.getString(announcementPath + "PrisonersWonSubtitle"));
        _policeWonTitle = createMessage(messages.getString(announcementPath + "PoliceWonTitle"));
        _policeWonSubtitle = createMessage(messages.getString(announcementPath + "PoliceWonSubtitle"));
        _victoryWord = messages.getString(announcementPath + "VictoryWord");
        _defeatWord = messages.getString(announcementPath + "DefeatWord");
        _gameResultMessage = createMessage(messages.getStringList(announcementPath + "GameResult"));
        _playerEscapedMessage = createMessage(messages.getString(announcementPath + "PlayerEscaped"));
        _gameCancelledNotEnoughPlayers = createMessage(
                messages.getString(announcementPath + "GameCancelledFewPlayers")
        );
        _newDayTitleMessage = createMessage(messages.getString(announcementPath + "NewDayTitle"));
        _newDaySubtitleMessage = createMessage(messages.getString(announcementPath + "NewDaySubtitle"));
        _nightTitleMessage = createMessage(messages.getString(announcementPath + "NightTitle"));
        _nightSubtitleMessage = createMessage(messages.getString(announcementPath + "NightSubtitle"));
        _successfullyJoinedGameMessage = createMessage(messages.getString(announcementPath + "JoinedGame"));
        _successfullyLeftGameMessage = createMessage(messages.getString(announcementPath + "LeftGame"));
        _successfullyRejoinedGameMessage = createMessage(messages.getString(announcementPath + "RejoinedGame"));

        String errorPath = messagePath + "Errors.";
        _notAllowedMessage = createMessage(messages.getString(errorPath + "NotAllowed"));
        _onlyPlayersCanUseThisCommandMessage = createMessage(messages.getString(errorPath + "CommandForPlayers"));
        _gameAlreadyStartedMessage = createMessage(messages.getString(errorPath + "GameAlreadyStarted"));
        _gameNotStartedYetMessage = createMessage(messages.getString(errorPath + "GameNotStartedYet"));
        _gameAlreadyOngoingMessage = createMessage(messages.getString(errorPath + "GameAlreadyOngoing"));
        _gameHasNotStartedUseJoinInsteadMessage = createMessage(messages.getString(errorPath + "GameIsStillWaiting"));
        _gameIsNotInFinishedPhaseMessage = createMessage(messages.getString(errorPath + "GameIsNotFinished"));
        _lobbyIsFullMessage = createMessage(messages.getString(errorPath + "LobbyIsFull"));
        _playerAlreadyJoinedMessage = createMessage(messages.getString(errorPath + "AlreadyJoined"));
        _playerNotOnLobbyMessage = createMessage(messages.getString(errorPath + "NotOnLobby"));
        _playerWasNeverInGameMessage = createMessage(messages.getString(errorPath + "NeverInGame"));

        String usagePath = messagePath + "Usages.";
        _usageMessage = createMessage(messages.getStringList(usagePath + "General"));
        _startCommandUsage = createMessage(messages.getString(usagePath + "Start"));
        _forceStartCommandUsage = createMessage(messages.getString(usagePath + "ForceStart"));
        _forceStopCommandUsage = createMessage(messages.getString(usagePath + "ForceStop"));
        _joinCommandUsage = createMessage(messages.getString(usagePath + "Join"));
        _leaveCommandUsage = createMessage(messages.getString(usagePath + "Leave"));
        _rejoinCommandUsage = createMessage(messages.getString(usagePath + "Rejoin"));
        _stopCommandUsage = createMessage(messages.getString(usagePath + "Stop"));
    }

    private String createMessage(String rawMessage) {
        return rawMessage.replace("&", "§");
    }

    private List<String> createMessage(List<String> rawMessage) {
        List<String> message = new ArrayList<>(rawMessage);

        for (int i = 0; i < message.size(); i++) {
            message.set(i, message.get(i).replace("&", "§"));
        }

        return message;
    }

//  #######################################
//  #               BossBar               #
//  #######################################

    public String getBossBarDayTitle(int day) {
        return _bossBarDayTitle.replace("{DAY}", Integer.toString(day));
    }

    public String getBossBarNightTitle(int day) {
        return _bossBarNightTitle.replace("{DAY}", Integer.toString(day));
    }

//  ########################################
//  #              Scoreboard              #
//  ########################################

    public String getScoreboardDisplayName() {
        return _sbDisplayName;
    }

    public String getSideBarLastLine() {
        return _sideBarLastLine;
    }

    public String getGuardSideBarBalanceLine(int balance) {
        return _guardSideBarBalanceLine.replace("{BALANCE}", Integer.toString(balance));
    }

    public String getGuardSideBarSoundDetectorLine() {
        return _guardSideBarSoundDetectorLine;
    }

//	#######################################
//	#                Items                #
//	#######################################

    private List<String> getItemsConfigNames(YamlConfiguration messages, String itemsPath) {
        return messages.getConfigurationSection(itemsPath).getKeys(true).stream().filter(
                key -> !key.contains(".")
        ).toList();
    }

    public String getItemName(String configName) {
        return _itemsNames.get(configName);
    }

    public List<String> getItemLore(String configName) {
        return new ArrayList<>(_itemsLores.get(configName));
    }

    public List<String> getItemPropertiesLore(boolean isMetalic, boolean isIllegal) {
        List<String> itemPropertiesLore = new ArrayList<>();

        if (isMetalic) {
            itemPropertiesLore.add(_itemLoreLine.replace("{PROPERTY}", _itemMetalicProperty));
        }

        if (isIllegal) {
            itemPropertiesLore.add(_itemLoreLine.replace("{PROPERTY}", _itemIllegalProperty));
        }

        return itemPropertiesLore;
    }

//	#######################################
//	#              Inventory              #
//	#######################################

    public String getContainerName() {
        return _containerName;
    }

    public String getVaultTitle() {
        return _vaultTitle;
    }

    public String getVaultHiddenGlassName() {
        return _vaultHiddenGlassName;
    }

    public String getTradeTitle() {
        return _tradeTitle;
    }

    public String getTradeInvalidWoolName() {
        return _tradeInvalidWoolName;
    }

    public String getTradeAcceptWoolName() {
        return _tradeAcceptWoolName;
    }

    public String getTradeAcceptedWoolName() {
        return _tradeAcceptedWoolName;
    }

    public String getTradeNotAcceptedGlassName() {
        return _tradeNotAcceptedGlassName;
    }

    public String getTradeAcceptedGlassName() {
        return _tradeAcceptedGlassName;
    }

    public String getCraftingMenuTitle() {
        return _craftingMenuTitle;
    }

    public String getMissingItemsWoolName() {
        return _missingItemsWoolName;
    }

    public String getConfirmCraftWoolName() {
        return _confirmCraftWoolName;
    }

//	########################################
//	#                Chat                #
//	########################################

    public String getGeneralMessage(String playerName, String message) {
        return _generalMessage.replace("{PLAYER}", playerName).replace("{MESSAGE}", message);
    }

    public String getPoliceTeamMessage(String playerName, String message) {
        return _policeTeamMessage.replace("{PLAYER}", playerName).replace("{MESSAGE}", message);
    }

    public String getPrisonerTeamMessage(String playerName, String message) {
        return _prisonerTeamMessage.replace("{PLAYER}", playerName).replace("{MESSAGE}", message);
    }

//	########################################
//	#               Warnings               #
//	########################################

    public String getSuccessfullyStartedGameMessage() {
        return _successfullyStartedGameMessage;
    }

    public String getSuccessfullyForceStartedGameMessage() {
        return _successfullyForceStartedGameMessage;
    }

    public String getSuccessfullyForceStoppedGameMessage() {
        return _successfullyForceStoppedGameMessage;
    }

    public String getSuccessfullyStoppedGameMessage() {
        return _successfullyStoppedGameMessage;
    }

    public String getSelectedPrisonersTeamMessage() {
        return _selectedPrisonersTeamMessage;
    }

    public String getSelectedPoliceTeamMessage() {
        return _selectedPoliceTeamMessage;
    }

    public String getRemovedTeamPreferenceMessage() {
        return _removedTeamPreferenceMessage;
    }

    public String getPrisonerGameStartedMessage() {
        return _prisonerGameStartedMessage;
    }

    public String getPoliceGameStartedMessage() {
        return _policeGameStartedMessage;
    }

    public String getPoliceOpenVaultMessage() {
        return _policeOpenVaultMessage;
    }

    public String getPrisonerOtherVaultMessage() {
        return _prisonerOtherVaultMessage;
    }

    public String getPoliceFoundIllegalItemsMessage(String vaultOwner) {
        return _policeFoundIllegalItemsMessage.replace("{PLAYER}", vaultOwner);
    }

    public String getPrisonerFoundIllegalItemsMessage() {
        return _prisonerFoundIllegalItemsMessage;
    }

    public String getPoliceNoIllegalItemsFoundMessage() {
        return _policeNoIllegalItemsFoundMessage;
    }

    public String getPrisonerNoIllegalItemsFoundMessage() {
        return _prisonerNoIllegalItemsFoundMessage;
    }

    public String getPoliceCanNotOpenChestMessage() {
        return _policeCanNotOpenChestMessage;
    }

    public String getChestAlreadyOpenedMessage() {
        return _chestAlreadyOpenedMessage;
    }

    public String getFullInventoryMessage() {
        return _fullInventoryMessage;
    }

    public String getNotWantedPlayerMessage() {
        return _notWantedPlayerMessage;
    }

    public String getPoliceInspectedMessage(String playerName) {
        return _policeInspectedMessage.replace("{PLAYER}", playerName);
    }

    public String getPrisonerInspectedMessage() {
        return _prisonerInspectedMessage;
    }

    public String getCanOnlyFixHolesMessage() {
        return _canOnlyFixHolesMessage;
    }

    public String getReachedItemLimitMessage() {
        return _reachedItemLimitMessage;
    }

    public String getNotEnoughMoneyMessage() {
        return _notEnoughMoneyMessage;
    }

    public String getSuccessfullyBoughtItemMessage(int balance) {
        return _successfullyBoughtItemMessage.replace("{BALANCE}", Integer.toString(balance));
    }

    public String getCannotDropThatItemMessage() {
        return _cannotDropThatItemMessage;
    }

    public String getHelicopterOnTheWayMessage(int delay) {
        return _helicopterOnTheWayMessage.replace("{SECONDS}", Integer.toString(delay));
    }

    public String getNoCellPhoneCoverageMessage() {
        return _noCellPhoneCoverageMessage;
    }

    public String getCameraPlacedMessage() {
        return _cameraPlacedMessage;
    }

    public String getNoCamerasPlacedMessage() {
        return _noCamerasPlacedMessage;
    }

    public String getSneakToLeaveCameraMessage() {
        return _sneakToLeaveCameraMessage;
    }

    public String getSoundDetectorPlacedMessage() {
        return _soundDetectorPlacedMessage;
    }

    public String getInvalidSoundDetectorLocMessage() {
        return _invalidSoundDetectorLocMessage;
    }

    public String getTradeAlreadyAcceptedMessage() {
        return _tradeAlreadyAcceptedMessage;
    }

    public String getTradeRequestSentMessage(String playerName) {
        return _tradeRequestSentMessage.replace("{PLAYER}", playerName);
    }

    public String getTradeRequestReceivedMessage(String playerName, int time) {
        return _tradeRequestReceivedMessage.replace("{PLAYER}", playerName).replace("{TIME}", Integer.toString(time));
    }

    public String getCraftingItemsMissingMessage() {
        return _craftingItemsMissingMessage;
    }

    public String getCodeFoundMessage() {
        return _codeFoundMessage;
    }

//	########################################
//	#             Announcements            #
//	########################################

    public List<String> getGameStartingAnnouncementMessage(int remainingTime, int playersOnLobby) {
        List<String> message = new ArrayList<>(_gameStartingAnnouncementMessage);

        for (int i = 0; i < message.size(); i++) {
            message.set(
                    i,
                    message.get(i).replace("{SECONDS}", Integer.toString(remainingTime)).replace(
                            "{PLAYERS}",
                            Integer.toString(playersOnLobby)
                    )
            );
        }

        return message;
    }

    public String getPrisonerArrested(String playerName) {
        return _prisonerArrested.replace("{PLAYER}", playerName);
    }

    public String getPrisonerFreedOfSolitary() {
        return _prisonerFreedOfSolitary;
    }

    public String getPrisonersWonTitle() {
        return _prisonersWonTitle;
    }

    public String getPrisonersWonSubtitle() {
        return _prisonersWonSubtitle;
    }

    public String getPoliceWonTitle() {
        return _policeWonTitle;
    }

    public String getPoliceWonSubtitle(int playersInPrison) {
        return _policeWonSubtitle;
    }

    public List<String> getGameResultMessage(boolean isWinner) {
        String result = isWinner ? _victoryWord : _defeatWord;

        List<String> message = new ArrayList<>(_gameResultMessage);
        for (int i = 0; i < message.size(); i++) {
            message.set(i, message.get(i).replace("{RESULT}", result));
        }

        return message;
    }

    public String getPlayerEscapedMessage(String playerName) {
        return _playerEscapedMessage.replace("{PLAYER}", playerName);
    }

    public String getGameCancelledFewPlayersMessage() {
        return _gameCancelledNotEnoughPlayers;
    }

    public String getNewDayTitleMessage(int day) {
        return _newDayTitleMessage.replace("{DAYNUMBER}", String.valueOf(day));
    }

    public String getNewDaySubtitleMessage() {
        return _newDaySubtitleMessage;
    }

    public String getNightTitleMessage() {
        return _nightTitleMessage;
    }

    public String getNightSubtitleMessage() {
        return _nightSubtitleMessage;
    }

    public String getSuccessfullyJoinedGameMessage(String playerName, int playerNumber, int maxPlayers) {
        return _successfullyJoinedGameMessage.replace("{PLAYER}", playerName).replace(
                "{PLAYERNUMBER}",
                Integer.toString(playerNumber)
        ).replace("{MAXPLAYERS}", Integer.toString(maxPlayers));
    }

    public String getSuccessfullyLeftGameMessage(String playerName, int playerNumber, int maxPlayers) {
        return _successfullyLeftGameMessage.replace("{PLAYER}", playerName).replace(
                "{PLAYERNUMBER}",
                Integer.toString(playerNumber)
        ).replace("{MAXPLAYERS}", Integer.toString(maxPlayers));
    }

    public String getSuccessfullyRejoinedGameMessage(String playerName, int playerNumber, int maxPlayers) {
        return _successfullyRejoinedGameMessage.replace("{PLAYER}", playerName).replace(
                "{PLAYERNUMBER}",
                Integer.toString(playerNumber)
        ).replace("{MAXPLAYERS}", Integer.toString(maxPlayers));
    }

//	########################################
//	#                Errors                #
//	########################################

    public String getNotAllowedMessage() {
        return _notAllowedMessage;
    }

    public String getOnlyPlayersCanUseThisCommandMessage() {
        return _onlyPlayersCanUseThisCommandMessage;
    }

    public String getGameAlreadyStartedMessage() {
        return _gameAlreadyStartedMessage;
    }

    public String getGameNotStartedYetMessage() {
        return _gameNotStartedYetMessage;
    }

    public String getGameAlreadyOngoingMessage() {
        return _gameAlreadyOngoingMessage;
    }

    public String getGameHasNotStartedUseJoinInsteadMessage() {
        return _gameHasNotStartedUseJoinInsteadMessage;
    }

    public String getGameHasNotFinishedMessage() {
        return _gameIsNotInFinishedPhaseMessage;
    }

    public String getLobbyIsFullMessage() {
        return _lobbyIsFullMessage;
    }

    public String getPlayerAlreadyJoinedMessage() {
        return _playerAlreadyJoinedMessage;
    }

    public String getPlayerNotOnLobbyMessage() {
        return _playerNotOnLobbyMessage;
    }

    public String getPlayerWasNeverInGameMessage() {
        return _playerWasNeverInGameMessage;
    }

//	########################################
//	#                Usages                #
//	########################################

    public List<String> getUsage() {
        return new ArrayList<>(_usageMessage);
    }

    public String getStartCommandUsage() {
        return _startCommandUsage;
    }

    public String getForceStartCommandUsage() {
        return _forceStartCommandUsage;
    }

    public String getJoinCommandUsage() {
        return _joinCommandUsage;
    }

    public String getLeaveCommandUsage() {
        return _leaveCommandUsage;
    }

    public String getForceStopCommandUsage() {
        return _forceStopCommandUsage;
    }

    public String getRejoinCommandUsage() {
        return _rejoinCommandUsage;
    }

    public String getStopCommandUsage() {
        return _stopCommandUsage;
    }

}
