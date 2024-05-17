package net.tiagofar78.prisonescape.game;

import net.tiagofar78.prisonescape.bukkit.BukkitEffectGiver;
import net.tiagofar78.prisonescape.bukkit.BukkitMenu;
import net.tiagofar78.prisonescape.bukkit.BukkitMessageSender;
import net.tiagofar78.prisonescape.bukkit.BukkitScheduler;
import net.tiagofar78.prisonescape.bukkit.BukkitTeleporter;
import net.tiagofar78.prisonescape.bukkit.BukkitWorldEditor;
import net.tiagofar78.prisonescape.game.phases.Finished;
import net.tiagofar78.prisonescape.game.phases.Phase;
import net.tiagofar78.prisonescape.game.phases.Waiting;
import net.tiagofar78.prisonescape.game.prisonbuilding.Chest;
import net.tiagofar78.prisonescape.game.prisonbuilding.Helicopter;
import net.tiagofar78.prisonescape.game.prisonbuilding.Obstacle;
import net.tiagofar78.prisonescape.game.prisonbuilding.PrisonBuilding;
import net.tiagofar78.prisonescape.game.prisonbuilding.PrisonEscapeLocation;
import net.tiagofar78.prisonescape.game.prisonbuilding.Regenerable;
import net.tiagofar78.prisonescape.game.prisonbuilding.SoundDetector;
import net.tiagofar78.prisonescape.game.prisonbuilding.Vault;
import net.tiagofar78.prisonescape.game.prisonbuilding.WallCrack;
import net.tiagofar78.prisonescape.game.prisonbuilding.doors.ClickDoorReturnAction;
import net.tiagofar78.prisonescape.game.prisonbuilding.doors.Door;
import net.tiagofar78.prisonescape.items.FunctionalItem;
import net.tiagofar78.prisonescape.items.Item;
import net.tiagofar78.prisonescape.items.SearchItem;
import net.tiagofar78.prisonescape.items.ToolItem;
import net.tiagofar78.prisonescape.kits.PoliceKit;
import net.tiagofar78.prisonescape.kits.PrisionerKit;
import net.tiagofar78.prisonescape.kits.TeamSelectorKit;
import net.tiagofar78.prisonescape.managers.ConfigManager;
import net.tiagofar78.prisonescape.managers.GameManager;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;
import net.tiagofar78.prisonescape.menus.ClickReturnAction;
import net.tiagofar78.prisonescape.menus.Clickable;
import net.tiagofar78.prisonescape.menus.Shop;

import org.bukkit.block.Block;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

public class PrisonEscapeGame {

    private static final int TICKS_PER_SECOND = 20;
    private static final String POLICE_TEAM_NAME = "Police";
    private static final String PRISIONERS_TEAM_NAME = "Prisioners";

    private Settings _settings;

    private int _currentDay;
    private DayPeriod _dayPeriod;
    private PrisonBuilding _prison;

    private List<PrisonEscapePlayer> _playersOnLobby;
    private PrisonEscapeTeam<Guard> _policeTeam;
    private PrisonEscapeTeam<Prisioner> _prisionersTeam;

    private Hashtable<String, Clickable> _playerOpenMenu;

    private Phase _phase;

    private boolean _hasDoorCode;

    public PrisonEscapeGame(String mapName, PrisonEscapeLocation referenceBlock) {
        _settings = new Settings();

        _currentDay = 0;
        _prison = new PrisonBuilding(referenceBlock);

        _playersOnLobby = new ArrayList<>();
        _policeTeam = new PrisonEscapeTeam<Guard>(POLICE_TEAM_NAME);
        _prisionersTeam = new PrisonEscapeTeam<Prisioner>(PRISIONERS_TEAM_NAME);

        _playerOpenMenu = new Hashtable<>();

        _hasDoorCode = false;

        startWaitingPhase();
    }

    public PrisonBuilding getPrison() {
        return _prison;
    }

//	#########################################
//	#                 Lobby                 #
//	#########################################

    /**
     * @return 0 if success<br>
     *         -1 if already on game<br>
     *         -2 if already started<br>
     *         -3 if the lobby is full
     */
    public int playerJoin(String playerName) {
        if (isPlayerOnGame(playerName)) {
            return -1;
        }

        if (_phase.hasGameStarted()) {
            return -2;
        }

        ConfigManager config = ConfigManager.getInstance();
        if (_playersOnLobby.size() >= config.getMaxPlayers()) {
            return -3;
        }

        PrisonEscapePlayer player = new WaitingPlayer(playerName);
        _playersOnLobby.add(player);

        BukkitTeleporter.teleport(player, _prison.getWaitingLobbyLocation());
        player.setKit(new TeamSelectorKit());

        int maxPlayers = config.getMaxPlayers();
        int playerNumber = _playersOnLobby.size();
        for (PrisonEscapePlayer playerOnLobby : _playersOnLobby) {
            MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(playerOnLobby.getName());
            BukkitMessageSender.sendChatMessage(
                    playerOnLobby,
                    messages.getSuccessfullyJoinedGameMessage(playerName, playerNumber, maxPlayers)
            );
        }

        return 0;
    }

    /**
     * @return 0 if success<br>
     *         -1 if game has not started <br>
     *         -2 if already on game<br>
     *         -3 if player never on game<br>
     */
    public int playerRejoin(String playerName) {
        if (!_phase.hasGameStarted()) {
            return -1;
        }

        if (isPlayerOnGame(playerName)) {
            return -2;
        }

        PrisonEscapePlayer player = getPlayerOnPoliceTeam(playerName);
        if (player != null) {
            teleportPoliceToSpawnPoint(player);
        } else {
            player = getPlayerOnPrisionersTeam(playerName);
            if (player == null) {
                return -3;
            }
            teleportPrisionerToSpawnPoint(player);
        }

        _playersOnLobby.add(player);

        ConfigManager config = ConfigManager.getInstance();
        int maxPlayers = config.getMaxPlayers();
        int playerNumber = _playersOnLobby.size();
        for (PrisonEscapePlayer playerOnLobby : _playersOnLobby) {
            MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(playerOnLobby.getName());
            BukkitMessageSender.sendChatMessage(
                    playerOnLobby,
                    messages.getSuccessfullyRejoinedGameMessage(playerName, playerNumber, maxPlayers)
            );
        }

        return 0;
    }

    /**
     * @return 0 if success<br>
     *         -1 if not on game
     */
    public int playerLeft(String playerName) {
        if (!isPlayerOnGame(playerName)) {
            return -1;
        }

        PrisonEscapePlayer player = null;
        for (int i = 0; i < _playersOnLobby.size(); i++) {
            if (_playersOnLobby.get(i).getName().equals(playerName)) {
                player = _playersOnLobby.get(i);
                _playersOnLobby.remove(i);
                break;
            }
        }

        teleportToLeavingLocation(player);

        ConfigManager config = ConfigManager.getInstance();
        int maxPlayers = config.getMaxPlayers();
        int playerNumber = _playersOnLobby.size();
        for (PrisonEscapePlayer playerOnLobby : _playersOnLobby) {
            MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(playerOnLobby.getName());
            BukkitMessageSender.sendChatMessage(
                    playerOnLobby,
                    messages.getSuccessfullyLeftGameMessage(playerName, playerNumber, maxPlayers)
            );
        }

        return 0;
    }

    private boolean isPlayerOnGame(String playerName) {
        for (int i = 0; i < _playersOnLobby.size(); i++) {
            if (_playersOnLobby.get(i).getName().equals(playerName)) {
                return true;
            }
        }

        return false;
    }

    private PrisonEscapePlayer getPlayerOnPoliceTeam(String playerName) {
        for (int i = 0; i < _policeTeam.getSize(); i++) {
            if (_policeTeam.getMember(i).getName().equals(playerName)) {
                return _policeTeam.getMember(i);
            }
        }

        return null;
    }

    private PrisonEscapePlayer getPlayerOnPrisionersTeam(String playerName) {
        for (int i = 0; i < _prisionersTeam.getSize(); i++) {
            if (_prisionersTeam.getMember(i).getName().equals(playerName)) {
                return _prisionersTeam.getMember(i);
            }
        }

        return null;
    }

    public boolean isGuard(PrisonEscapePlayer player) {
        return player.isGuard();
    }

    public boolean isPrisioner(PrisonEscapePlayer player) {
        return player.isPrisioner();
    }

    public PrisonEscapeTeam<Guard> getGuardsTeam() {
        return _policeTeam;
    }

//	########################################
//	#              Admin zone              #
//	########################################

    /**
     * @return 0 if successful<br>
     *         -1 if already started ongoing phase
     */
    public int forceStart() {
        if (_phase.hasGameStarted()) {
            return -1;
        }

        startOngoingPhase();
        return 0;
    }

    public void forceStop() {
        _phase = new Finished();
        disableGame();
    }

    /**
     * @return 0 if successful<br>
     *         -1 if not in finished phase
     */
    public int stop() {
        if (!_phase.hasGameEnded()) {
            return -1;
        }

        disableGame();
        return 0;
    }

//	########################################
//	#                Phases                #
//	########################################

    private void startWaitingPhase() {
        _phase = new Waiting();

        _prison.raiseWall();

        ConfigManager config = ConfigManager.getInstance();

        runWaitingPhaseScheduler(config.getWaitingPhaseDuration(), true);
    }

    private void runWaitingPhaseScheduler(int remainingSeconds, boolean isFirst) {
        ConfigManager config = ConfigManager.getInstance();

        BukkitScheduler.runSchedulerLater(new Runnable() {

            @Override
            public void run() {
                if (_phase.hasGameStarted()) {
                    return;
                }

                if (remainingSeconds == 0) {
                    if (_playersOnLobby.size() >= config.getMinimumPlayers()) {
                        startOngoingPhase();
                        return;
                    }

                    for (PrisonEscapePlayer player : _playersOnLobby) {
                        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(player.getName());
                        BukkitMessageSender.sendChatMessage(player, messages.getGameCancelledFewPlayersMessage());
                    }

                    disableGame();
                    return;
                }

                if (remainingSeconds % config.getDelayBetweenAnnouncements() == 0) {
                    List<String> playersNames = BukkitMessageSender.getOnlinePlayersNames();
                    for (String playerName : playersNames) {
                        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(playerName);

                        List<String> announcement = messages.getGameStartingAnnouncementMessage(
                                remainingSeconds,
                                _playersOnLobby.size()
                        );
                        BukkitMessageSender.sendChatMessage(playerName, announcement);
                    }
                }

                int fullLobbyWaitDuration = config.getFullLobbyWaitDuration();
                if (_playersOnLobby.size() == config.getMaxPlayers() && remainingSeconds > fullLobbyWaitDuration) {
                    runWaitingPhaseScheduler(fullLobbyWaitDuration, false);
                }

                runWaitingPhaseScheduler(remainingSeconds - 1, false);
            }
        }, isFirst ? 0 : 1 * TICKS_PER_SECOND);
    }

    private void startOngoingPhase() {
        distributePlayersPerTeam();

        _phase = _phase.next();

        for (PrisonEscapePlayer player : _prisionersTeam.getMembers()) {
            MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(player.getName());
            BukkitMessageSender.sendChatMessage(player, messages.getPrisionerGameStartedMessage());
            player.setKit(new PrisionerKit());
            teleportPrisionerToSpawnPoint(player);
        }

        for (PrisonEscapePlayer player : _policeTeam.getMembers()) {
            MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(player.getName());
            BukkitMessageSender.sendChatMessage(player, messages.getPoliceGameStartedMessage());
            player.setKit(new PoliceKit());
            teleportPoliceToSpawnPoint(player);
        }

        _prison.addVaults(_prisionersTeam.getMembers());
        _prison.putRandomCracks();

        startDay();
    }

    private void startFinishedPhase(PrisonEscapeTeam<? extends PrisonEscapePlayer> winnerTeam) {
        _phase = _phase.next();

        boolean prisionersWon = winnerTeam.getName().equals(_prisionersTeam.getName());
        int playersInPrison = (int) _prisionersTeam.getMembers().stream().filter(p -> p.isImprisioned()).count();

        for (PrisonEscapePlayer player : _playersOnLobby) {
            MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(player.getName());

            String title;
            String subtitle;
            if (prisionersWon) {
                title = messages.getPrisionersWonTitle();
                subtitle = messages.getPrisionersWonSubtitle();
            } else {
                title = messages.getPoliceWonTitle();
                subtitle = messages.getPoliceWonSubtitle(playersInPrison);
            }

            List<String> resultMessage = messages.getGameResultMessage(winnerTeam.isOnTeam(player));

            BukkitMessageSender.sendTitleMessage(player.getName(), title, subtitle);
            BukkitMessageSender.sendChatMessage(player, resultMessage);
        }

        int finishedPhaseDuration = ConfigManager.getInstance().getFinishedPhaseDuration();
        BukkitScheduler.runSchedulerLater(new Runnable() {

            @Override
            public void run() {
                disableGame();
            }
        }, finishedPhaseDuration * TICKS_PER_SECOND);
    }

    private void disableGame() {
        for (PrisonEscapePlayer player : _playersOnLobby) {
            teleportToLeavingLocation(player);
            player.removeScoreboard();
        }

        _prison.deleteVaults();
        _prison.deleteCameras();
        _prison.deleteSoundDetectors();

        GameManager.removeGame();
    }

//	########################################
//	#                 Time                 #
//	########################################

    private void startDay() {
        if (_phase.isClockStopped()) {
            return;
        }

        _dayPeriod = DayPeriod.DAY;
        _currentDay++;
        BukkitWorldEditor.changeTimeToDay();

        _prison.reloadChests();

        for (PrisonEscapePlayer player : _playersOnLobby) {
            MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(player.getName());
            String title = messages.getNewDayTitleMessage(_currentDay);
            String subtitle = messages.getNewDaySubtitleMessage();
            BukkitMessageSender.sendTitleMessage(player.getName(), title, subtitle);
        }

        BukkitScheduler.runSchedulerLater(new Runnable() {

            @Override
            public void run() {
                startNight();
            }
        }, _settings.getDayDuration() * TICKS_PER_SECOND);
    }

    private void startNight() {
        if (_phase.isClockStopped()) {
            return;
        }

        _dayPeriod = DayPeriod.NIGHT;
        BukkitWorldEditor.changeTimeToNight();

        for (PrisonEscapePlayer player : _playersOnLobby) {
            MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(player.getName());
            String title = messages.getNightTitleMessage();
            String subtitle = messages.getNightSubtitleMessage();
            BukkitMessageSender.sendTitleMessage(player.getName(), title, subtitle);
        }

        BukkitScheduler.runSchedulerLater(new Runnable() {

            @Override
            public void run() {
                if (_currentDay == _settings.getDays()) {
                    startFinishedPhase(_policeTeam);
                } else {
                    startDay();
                }
            }
        }, _settings.getNightDuration() * TICKS_PER_SECOND);
    }

//	########################################
//	#                Events                #
//	########################################

    public void playerMove(String playerName, PrisonEscapeLocation loc) {
        PrisonEscapePlayer player = getPrisonEscapePlayer(playerName);
        if (player == null) {
            return;
        }

        if (!_phase.hasGameStarted() || _phase.hasGameEnded()) {
            return;
        }

        for (SoundDetector soundDetector : _prison.getSoundDetectors()) {
            soundDetector.playerMoved(player, loc);
        }

        if (!isPrisioner(player)) {
            return;
        }

        Prisioner prisioner = (Prisioner) player;

        if (prisioner.hasEscaped()) {
            return;
        }

        if (_prison.isOutsidePrison(loc)) {
            playerEscaped(prisioner);
        }

        if (_prison.isInRestrictedArea(loc)) {
            prisioner.enteredRestrictedArea();
        } else if (prisioner.isInRestrictedArea()) {
            prisioner.leftRestrictedArea();
        }

        if (_prison.checkIfMetalDetectorTriggered(loc, player)) {
            // TODO: Do beep
        }
    }

    public int playerInteract(
            String playerName,
            PrisonEscapeLocation blockLocation,
            int itemSlot,
            PlayerInteractEvent e
    ) {
        PrisonEscapePlayer player = getPrisonEscapePlayer(playerName);
        if (player == null) {
            return -1;
        }

        Item item = player.getItemAt(itemSlot);

        if (blockLocation != null) {
            int vaultIndex = _prison.getVaultIndex(blockLocation);
            if (vaultIndex != -1) {
                playerOpenVault(player, vaultIndex, item);
                return 0;
            }

            Chest chest = _prison.getChest(blockLocation);
            if (chest != null) {
                playerOpenChest(player, chest);
                return 0;
            }

            WallCrack crack = _prison.getWallCrack(blockLocation);
            if (crack != null) {
                int returnCode = playerFixWallCrack(player, crack);
                if (returnCode == 0) {
                    return 0;
                }
            }

            Helicopter helicopter = _prison.getHelicopter(blockLocation);
            if (helicopter != null) {
                helicopter.click(player, _prison.getHelicopterExitLocation(), _prison.getHelicopterJoinLocation());
                return 0;
            }

            PrisonEscapeLocation destination = _prison.getSecretPassageDestinationLocation(
                    blockLocation,
                    isPrisioner(player)
            );
            if (destination != null) {
                BukkitTeleporter.teleport(player, destination);
                return 0;
            }

            Door door = _prison.getDoor(blockLocation);
            if (door != null) {
                int index = BukkitMenu.convertToIndexPlayerInventory(itemSlot);
                playerInteractWithDoor(player, index, item, door, blockLocation);
                return 0;
            }

            Obstacle obstacle = _prison.getObstacle(blockLocation);
            if (obstacle != null) {
                if (obstacleTookDamage(player, obstacle, item) == 0) {
                    return 0;
                }
            }
        }

        if (item.isFunctional()) {
            ((FunctionalItem) item).use(e);
        }

        return 0;
    }

    public void playerCloseMenu(String playerName) {
        if (getPrisonEscapePlayer(playerName) == null) {
            return;
        }

        if (_playerOpenMenu.containsKey(playerName)) {
            _playerOpenMenu.get(playerName).close();
            _playerOpenMenu.remove(playerName);
        }
    }

    public ClickReturnAction playerClickMenu(String playerName, int slot, Item itemHeld, boolean clickedPlayerInv) {
        PrisonEscapePlayer player = getPrisonEscapePlayer(playerName);
        if (player == null) {
            return ClickReturnAction.IGNORE;
        }

        if (!_playerOpenMenu.containsKey(playerName)) {
            return ClickReturnAction.IGNORE;
        }

        Clickable clicakble = _playerOpenMenu.get(player.getName());
        return clicakble.click(player, slot, itemHeld, clickedPlayerInv);
    }

    public void playerSneak(String playerName) {
        PrisonEscapePlayer player = getPrisonEscapePlayer(playerName);
        if (player == null) {
            return;
        }

        if (!player.isGuard()) {
            return;
        }

        Guard guard = (Guard) player;
        if (guard.isWatchingCamera()) {
            guard.stoppedWatchingCamera();
        }
    }

    public void sendTeamOnlyMessage(String senderName, String message) {
        PrisonEscapePlayer player = getPrisonEscapePlayer(senderName);
        if (player == null) {
            return;
        }

        if (isPrisioner(player)) {
            sendMessageToPrisionersTeam(senderName, message);
        } else if (isGuard(player)) {
            sendMessageToPoliceTeam(senderName, message);
        }
    }

    public void sendGeneralMessage(String senderName, String message) {
        for (PrisonEscapePlayer player : _playersOnLobby) {
            MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(player.getName());
            BukkitMessageSender.sendChatMessage(player, messages.getGeneralMessage(senderName, message));
        }
    }

    public void explosion(List<Block> explodedBlocks) {
        _prison.removeExplodedBlocks(explodedBlocks);
    }

//	########################################
//	#            Events Results            #
//	########################################

    public void playerEscaped(Prisioner player) {
        player.escaped();

        for (PrisonEscapePlayer playerOnLobby : _playersOnLobby) {
            MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(playerOnLobby.getName());
            BukkitMessageSender.sendChatMessage(playerOnLobby, messages.getPlayerEscapedMessage(player.getName()));
        }

        if (_prisionersTeam.getMembers().stream().filter(p -> p.isImprisioned()).count() == 0) {
            startFinishedPhase(_prisionersTeam);
        }
    }

    private void arrestPlayer(Prisioner arrested, Guard arrester) {
        teleportToSolitary(arrested);

        for (PrisonEscapePlayer player : _playersOnLobby) {
            MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(player.getName());
            String announcement = messages.getPrisionerArrested(arrested.getName());
            BukkitMessageSender.sendChatMessage(player.getName(), announcement);
        }

        BukkitScheduler.runSchedulerLater(new Runnable() {

            @Override
            public void run() {
                if (_phase.isClockStopped()) {
                    return;
                }

                arrested.removeWanted();

                MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(arrested.getName());
                BukkitMessageSender.sendChatMessage(arrested.getName(), messages.getPrisionerFreedOfSolitary());

                if (_dayPeriod == DayPeriod.DAY) {
                    teleportToSolitaryExit(arrested);
                } else if (_dayPeriod == DayPeriod.NIGHT) {
                    teleportPrisionerToSpawnPoint(arrested);
                }
            }
        }, TICKS_PER_SECOND * _settings.getSecondsInSolitary());
    }

    public void playerSelectPrisionersTeam(String playerName) {
        PrisonEscapePlayer player = getPrisonEscapePlayer(playerName);
        if (player == null) {
            return;
        }

        WaitingPlayer waitingPlayer = (WaitingPlayer) player;

        waitingPlayer.setPreference(TeamPreference.PRISIONERS);

        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(playerName);
        BukkitMessageSender.sendChatMessage(player, messages.getSelectedPrisionersTeamMessage());
    }

    public void playerSelectPoliceTeam(String playerName) {
        PrisonEscapePlayer player = getPrisonEscapePlayer(playerName);
        if (player == null) {
            return;
        }

        WaitingPlayer waitingPlayer = (WaitingPlayer) player;

        waitingPlayer.setPreference(TeamPreference.POLICE);

        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(player.getName());
        BukkitMessageSender.sendChatMessage(player, messages.getSelectedPoliceTeamMessage());
    }

    public void playerRemovedTeamPreference(String playerName) {
        PrisonEscapePlayer player = getPrisonEscapePlayer(playerName);
        if (player == null) {
            return;
        }

        WaitingPlayer waitingPlayer = (WaitingPlayer) player;

        waitingPlayer.setPreference(TeamPreference.RANDOM);

        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(player.getName());
        BukkitMessageSender.sendChatMessage(player, messages.getRemovedTeamPreferenceMessage());
    }

    private void playerOpenVault(PrisonEscapePlayer player, int vaultIndex, Item item) {
        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(player.getName());

        Vault vault = _prison.getVault(vaultIndex);

        if (isGuard(player)) {
            if (!(item instanceof SearchItem)) {
                BukkitMessageSender.sendChatMessage(player, messages.getPoliceOpenVaultMessage());
                return;
            }

            policeSearchVault((Guard) player, vault, messages);
            return;
        }

        if (_prisionersTeam.getPlayerIndex(player) != vaultIndex) {
            BukkitMessageSender.sendChatMessage(player, messages.getPrisionerOtherVaultMessage());
            return;
        }

        _playerOpenMenu.put(player.getName(), vault);
        vault.open(player);
    }

    /**
     * @return 0 if success<br>
     *         -1 if cannot drop that item
     */
    public int playerDropItem(String playerName, int slot) {
        PrisonEscapePlayer player = getPrisonEscapePlayer(playerName);

        int return_code = player.removeItem(slot);
        if (return_code == -1) {
            MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(playerName);
            BukkitMessageSender.sendChatMessage(player, messages.getCannotDropThatItemMessage());
        }

        return return_code;
    }

    private void policeSearchVault(Guard guard, Vault vault, MessageLanguageManager messagesPolice) {
        Prisioner vaultOwner = vault.getOwner();
        MessageLanguageManager messagesPrisioner = MessageLanguageManager.getInstanceByPlayer(vaultOwner.getName());

        int returnCode = vault.search();
        if (returnCode == 1) {
            vaultOwner.setWanted();

            BukkitMessageSender.sendChatMessage(
                    guard,
                    messagesPolice.getPoliceFoundIllegalItemsMessage(vaultOwner.getName())
            );
            BukkitMessageSender.sendChatMessage(vaultOwner, messagesPrisioner.getPrisionerFoundIllegalItemsMessage());
        } else if (returnCode == 0) {
            BukkitMessageSender.sendChatMessage(guard, messagesPolice.getPoliceNoIllegalItemsFoundMessage());
            BukkitMessageSender.sendChatMessage(vaultOwner, messagesPrisioner.getPrisionerNoIllegalItemsFoundMessage());
        }

        return;
    }

    private void playerOpenChest(PrisonEscapePlayer player, Chest chest) {
        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(player.getName());

        if (isGuard(player)) {
            BukkitMessageSender.sendChatMessage(player, messages.getPoliceCanNotOpenChestMessage());
            return;
        }

        if (chest.isOpened()) {
            BukkitMessageSender.sendChatMessage(player, messages.getChestAlreadyOpenedMessage());
            return;
        }

        chest.open(player);
        _playerOpenMenu.put(player.getName(), chest);
    }

    public void playerDrankEnergyDrink(String playerName, int eneryDrinkIndex) {
        PrisonEscapePlayer player = getPrisonEscapePlayer(playerName);
        if (player == null) {
            return;
        }

        ConfigManager config = ConfigManager.getInstance();

        BukkitEffectGiver.giveSpeedEffect(playerName, config.getSpeedLevel(), config.getSpeedDuration());

        int contentIndex = BukkitMenu.convertToIndexPlayerInventory(eneryDrinkIndex);
        player.removeItem(contentIndex);
    }

    public void playerCalledHelicopter(String playerName, PrisonEscapeLocation location, int itemSlot) {
        PrisonEscapePlayer player = getPrisonEscapePlayer(playerName);

        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(playerName);

        if (!_prison.hasCellPhoneCoverage(location)) {
            BukkitMessageSender.sendChatMessage(player, messages.getNoCellPhoneCoverageMessage());
            return;
        }

        int helicopterSpawnDelay = ConfigManager.getInstance().getHelicopterSpawnDelay();
        BukkitMessageSender.sendChatMessage(player, messages.getHelicopterOnTheWayMessage(helicopterSpawnDelay));
        player.removeItem(itemSlot);
        player.updateInventory();

        _prison.callHelicopter();
    }

    public void policeOpenShop(String playerName) {
        PrisonEscapePlayer player = getPlayerOnPoliceTeam(playerName);
        if (player == null) {
            return;
        }

        Shop shop = new Shop();
        _playerOpenMenu.put(player.getName(), shop);
        shop.open(player);
    }

    public void policeHandcuffedPrisioner(String policeName, String prisionerName) {
        PrisonEscapePlayer playerGuard = getPrisonEscapePlayer(policeName);
        PrisonEscapePlayer playerPrisioner = getPrisonEscapePlayer(prisionerName);
        if (playerGuard == null || playerPrisioner == null) {
            return;
        }

        if (_phase.isClockStopped()) {
            return;
        }

        if (!isPrisioner(playerPrisioner) || !isGuard(playerGuard)) {
            return;
        }

        Prisioner prisioner = (Prisioner) playerPrisioner;
        Guard guard = (Guard) playerGuard;

        if (prisioner.canBeArrested()) {
            arrestPlayer(prisioner, guard);
        } else {
            MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(policeName);
            BukkitMessageSender.sendChatMessage(prisionerName, messages.getNotWantedPlayerMessage());
        }
    }

    public void policeInspectedPrisioner(String policeName, String prisionerName) {
        PrisonEscapePlayer playerGuard = getPrisonEscapePlayer(policeName);
        PrisonEscapePlayer playerPrisioner = getPrisonEscapePlayer(prisionerName);
        if (playerGuard == null || playerPrisioner == null) {
            return;
        }

        if (_phase.isClockStopped()) {
            return;
        }

        if (!isPrisioner(playerPrisioner) || !isGuard(playerGuard)) {
            return;
        }

        Prisioner prisioner = (Prisioner) playerPrisioner;

        if (prisioner.hasIllegalItems()) {
            prisioner.setWanted();
        } else {
            MessageLanguageManager prisionerMessages = MessageLanguageManager.getInstanceByPlayer(prisionerName);
            BukkitMessageSender.sendChatMessage(prisionerName, prisionerMessages.getPrisionerInspectedMessage());

            MessageLanguageManager policeMessages = MessageLanguageManager.getInstanceByPlayer(policeName);
            BukkitMessageSender.sendChatMessage(policeName, policeMessages.getPoliceInspectedMessage(prisionerName));
        }
    }

    public void playerInteractWithDoor(
            PrisonEscapePlayer player,
            int inventoryIndex,
            Item itemHeld,
            Door door,
            PrisonEscapeLocation doorLocation
    ) {
        ClickDoorReturnAction returnAction = door.click(player, itemHeld);

        if (returnAction == ClickDoorReturnAction.CLOSE_DOOR) {
            door.close();
            BukkitWorldEditor.closeDoor(doorLocation);
            return;
        }

        if (returnAction == ClickDoorReturnAction.OPEN_DOOR) {
            door.open();
            BukkitWorldEditor.openDoor(doorLocation);
            player.removeItem(inventoryIndex);
            player.updateInventory();
        }
    }

    public void placeBomb(PrisonEscapeLocation location) {
        _prison.placeBomb(location);
    }

    public int playerFixWallCrack(PrisonEscapePlayer player, WallCrack crack) {
        if (!isGuard(player)) {
            return -1;
        }

        int returnCode = crack.fixCrack();
        if (returnCode == -1) {
            MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(player.getName());
            BukkitMessageSender.sendChatMessage(player, messages.getCanOnlyFixHolesMessage());
        }

        return 0;
    }

    public int obstacleTookDamage(PrisonEscapePlayer player, Obstacle obstacle, Item item) {
        if (isGuard(player)) {
            if (obstacle instanceof Regenerable) {
                ((Regenerable) obstacle).regenerate();
                return 0;
            }

            return -1;
        }

        if (!item.isTool()) {
            return 0;
        }

        double returnCode = obstacle.takeDamage((ToolItem) item);
        if (returnCode == 0) {
            obstacle.removeFromWorld();
        }

        player.updateInventory();

        return 0;
    }

//	########################################
//	#                 Util                 #
//	########################################

    public PrisonEscapePlayer getPrisonEscapePlayer(String playerName) {
        for (PrisonEscapePlayer player : _playersOnLobby) {
            if (player.getName().equals(playerName)) {
                return player;
            }
        }

        return null;
    }

    private void sendMessageToPoliceTeam(String senderName, String message) {
        for (PrisonEscapePlayer player : _policeTeam.getMembers()) {
            MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(player.getName());
            BukkitMessageSender.sendChatMessage(player, messages.getPoliceTeamMessage(senderName, message));
        }
    }

    private void sendMessageToPrisionersTeam(String senderName, String message) {
        for (PrisonEscapePlayer player : _prisionersTeam.getMembers()) {
            MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(player.getName());
            BukkitMessageSender.sendChatMessage(player, messages.getPrisionerTeamMessage(senderName, message));
        }
    }

    private void distributePlayersPerTeam() {
        int numberOfPlayers = _playersOnLobby.size();
        int requiredPrisioners = (int) Math.round(
                numberOfPlayers * ConfigManager.getInstance().getPrisionerRatio()
        );
        int requiredOfficers = (int) Math.round(
                numberOfPlayers * ConfigManager.getInstance().getOfficerRatio()
        );

        Collections.shuffle(_playersOnLobby);
        List<PrisonEscapePlayer> remainingPlayers = new ArrayList<>();

        List<PrisonEscapePlayer> newLobbyPlayers = new ArrayList<>();

        for (PrisonEscapePlayer player : _playersOnLobby) {
            WaitingPlayer waitingPlayer = (WaitingPlayer) player;

            TeamPreference preference = waitingPlayer.getPreference();

            if (preference == TeamPreference.POLICE && requiredOfficers != 0) {
                Guard guard = new Guard(player.getName());
                _policeTeam.addMember(guard);
                newLobbyPlayers.add(guard);
                requiredOfficers--;
            } else if (preference == TeamPreference.PRISIONERS && requiredPrisioners != 0) {
                Prisioner prisioner = new Prisioner(player.getName());
                _prisionersTeam.addMember(prisioner);
                newLobbyPlayers.add(prisioner);
                requiredPrisioners--;
            } else {
                remainingPlayers.add(player);
            }
        }

        for (PrisonEscapePlayer player : remainingPlayers) {
            if (requiredPrisioners != 0) {
                Prisioner prisioner = new Prisioner(player.getName());
                _prisionersTeam.addMember(prisioner);
                newLobbyPlayers.add(prisioner);
                requiredPrisioners--;
            } else {
                Guard guard = new Guard(player.getName());
                _policeTeam.addMember(guard);
                newLobbyPlayers.add(guard);
                requiredOfficers--;
            }
        }

        _playersOnLobby = newLobbyPlayers;
    }

//	#########################################
//	#               Locations               #
//	#########################################

    private void teleportPoliceToSpawnPoint(PrisonEscapePlayer player) {
        int playerIndex = _policeTeam.getPlayerIndex(player);
        BukkitTeleporter.teleport(player, _prison.getPoliceSpawnLocation(playerIndex));
    }

    private void teleportPrisionerToSpawnPoint(PrisonEscapePlayer player) {
        int playerIndex = _prisionersTeam.getPlayerIndex(player);
        BukkitTeleporter.teleport(player, _prison.getPlayerCellLocation(playerIndex));
    }

    private void teleportToSolitary(PrisonEscapePlayer player) {
        BukkitTeleporter.teleport(player, _prison.getSolitaryLocation());
    }

    private void teleportToSolitaryExit(PrisonEscapePlayer player) {
        BukkitTeleporter.teleport(player, _prison.getSolitaryExitLocation());
    }

    private void teleportToLeavingLocation(PrisonEscapePlayer player) {
        BukkitTeleporter.teleport(player, ConfigManager.getInstance().getLeavingLocation());
    }

//	#########################################
//	#                DoorCode               #
//	#########################################

    public boolean playersHaveDoorCode() {
        return _hasDoorCode;
    }

    public void findDoorCode() {
        _hasDoorCode = true;
    }

}
