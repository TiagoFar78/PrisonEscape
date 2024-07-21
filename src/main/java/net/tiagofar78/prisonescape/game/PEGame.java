package net.tiagofar78.prisonescape.game;

import net.tiagofar78.prisonescape.bukkit.BukkitMessageSender;
import net.tiagofar78.prisonescape.bukkit.BukkitScheduler;
import net.tiagofar78.prisonescape.bukkit.BukkitTeleporter;
import net.tiagofar78.prisonescape.bukkit.BukkitWorldEditor;
import net.tiagofar78.prisonescape.dataobjects.ItemProbability;
import net.tiagofar78.prisonescape.game.phases.DisabledPhase;
import net.tiagofar78.prisonescape.game.phases.FinishedPhase;
import net.tiagofar78.prisonescape.game.phases.OngoingPhase;
import net.tiagofar78.prisonescape.game.phases.Phase;
import net.tiagofar78.prisonescape.game.phases.WaitingPhase;
import net.tiagofar78.prisonescape.game.prisonbuilding.PrisonBuilding;
import net.tiagofar78.prisonescape.items.ItemFactory;
import net.tiagofar78.prisonescape.kits.Kit;
import net.tiagofar78.prisonescape.kits.PoliceKit;
import net.tiagofar78.prisonescape.kits.PrisonerKit;
import net.tiagofar78.prisonescape.kits.TeamSelectorKit;
import net.tiagofar78.prisonescape.managers.ConfigManager;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;

import java.util.ArrayList;
import java.util.List;

public class PEGame {

    private static final int TICKS_PER_SECOND = 20;
    public static final String GUARDS_TEAM_NAME = "Guards";
    public static final String PRISONERS_TEAM_NAME = "Prisoners";
    public static final String WAITING_TEAM_NAME = "Waiting";
    public static final String CELLS_REGION_NAME = "Cells";

    private Settings _settings;

    private int _currentDay;
    private DayPeriod _dayPeriod;
    private PrisonBuilding _prison;

    private List<PEPlayer> _playersOnLobby;
    private PETeam<Guard> _policeTeam;
    private PETeam<Prisoner> _prisonersTeam;

    private Phase _phase;

    private boolean _hasDoorCode;

    private BossBar _bossBar;

    public PEGame(String mapName, Location referenceBlock) {
        _settings = new Settings();

        _currentDay = 0;
        _prison = new PrisonBuilding(referenceBlock);

        _playersOnLobby = new ArrayList<>();
        _policeTeam = new PETeam<Guard>(GUARDS_TEAM_NAME);
        _prisonersTeam = new PETeam<Prisoner>(PRISONERS_TEAM_NAME);

        _hasDoorCode = false;

        _bossBar = Bukkit.createBossBar(mapName, BarColor.YELLOW, BarStyle.SOLID);

        startNextPhase(new WaitingPhase());
    }

    public Phase getCurrentPhase() {
        return _phase;
    }

    public PrisonBuilding getPrison() {
        return _prison;
    }

    public DayPeriod getPeriod() {
        return _dayPeriod;
    }

    public BossBar getBossBar() {
        return _bossBar;
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

        PEPlayer player = new WaitingPlayer(playerName);
        _playersOnLobby.add(player);

        BukkitTeleporter.teleport(player, _prison.getWaitingLobbyLocation());
        player.setKit(new TeamSelectorKit());
        player.clearEffects();

        int maxPlayers = config.getMaxPlayers();
        int playerNumber = _playersOnLobby.size();
        for (PEPlayer playerOnLobby : _playersOnLobby) {
            updatePreferenceTabListDisplay(playerName, WAITING_TEAM_NAME);

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

        PEPlayer player = _prisonersTeam.getMember(playerName);
        if (player != null) {
            addPrisonerToStartedGame(player, _dayPeriod);
        } else {
            player = _policeTeam.getMember(playerName);
            if (player != null) {
                addGuardToStartedGame(player, _dayPeriod);
            } else {
                return -3;
            }
        }

        _playersOnLobby.add(player);

        ConfigManager config = ConfigManager.getInstance();
        int maxPlayers = config.getMaxPlayers();
        int playerNumber = _playersOnLobby.size();
        for (PEPlayer playerOnLobby : _playersOnLobby) {
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

        PEPlayer player = null;
        for (int i = 0; i < _playersOnLobby.size(); i++) {
            if (_playersOnLobby.get(i).getName().equals(playerName)) {
                player = _playersOnLobby.get(i);
                _playersOnLobby.remove(i);
                break;
            }
        }

        removePlayerFromGame(player);

        ConfigManager config = ConfigManager.getInstance();
        int maxPlayers = config.getMaxPlayers();
        int playerNumber = _playersOnLobby.size();
        for (PEPlayer playerOnLobby : _playersOnLobby) {
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

    public boolean isGuard(PEPlayer player) {
        return player.isGuard();
    }

    public boolean isPrisoner(PEPlayer player) {
        return player.isPrisoner();
    }

    public PETeam<Prisoner> getPrisonerTeam() {
        return _prisonersTeam;
    }

    public PETeam<Guard> getGuardsTeam() {
        return _policeTeam;
    }

    public List<PEPlayer> getPlayersOnLobby() {
        return _playersOnLobby;
    }

    public void setPlayersOnLobby(List<PEPlayer> playersOnLobby) {
        _playersOnLobby = playersOnLobby;
    }

    public void addPrisonerToStartedGame(PEPlayer player, DayPeriod dayPeriod) {
        int playerIndex = _prisonersTeam.getPlayerIndex(player);
        Location loc = _prison.getPlayerCellLocation(playerIndex);
        addPlayerToStartedGame(player, new PrisonerKit(), loc, DayPeriod.DAY);
    }

    public void addGuardToStartedGame(PEPlayer player, DayPeriod dayPeriod) {
        int playerIndex = _policeTeam.getPlayerIndex(player);
        Location loc = _prison.getPoliceSpawnLocation(playerIndex);
        addPlayerToStartedGame(player, new PoliceKit(), loc, dayPeriod);
    }

    private void addPlayerToStartedGame(PEPlayer player, Kit kit, Location location, DayPeriod dayPeriod) {
        player.setKit(kit);
        player.setBossBar(_bossBar);
        player.updateScoreaboardTeams();
        player.setScoreboard(player.getScoreboardData().getScoreboard());
        player.updateRegionLine(_prison, dayPeriod);
        BukkitTeleporter.teleport(player, location);
        player.updateInventory();
    }

    public void removePlayerFromGame(PEPlayer player) {
        player.removeScoreboard();
        player.removeBossBar(_bossBar);
        teleportToLeavingLocation(player);
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

        startNextPhase(new OngoingPhase());
        return 0;
    }

    public void forceStop() {
        startNextPhase(new DisabledPhase());
    }

    /**
     * @return 0 if successful<br>
     *         -1 if not in finished phase
     */
    public int stop() {
        if (!_phase.hasGameEnded()) {
            return -1;
        }

        startNextPhase(new DisabledPhase());
        return 0;
    }

//	########################################
//	#                Phases                #
//	########################################

    public void startNextPhase() {
        startNextPhase(_phase.next());
    }

    public void startNextPhase(Phase phase) {
        _phase = phase;
        _phase.start(this);
    }

//	########################################
//	#                 Time                 #
//	########################################

    public void startDay() {
        if (_phase.isClockStopped()) {
            return;
        }

        _dayPeriod = DayPeriod.DAY;
        _currentDay++;
        BukkitWorldEditor.changeTimeToDay();
        setDayTimeBossBar();

        _prison.reloadChests();
        _prison.openCellDoors();

        for (PEPlayer player : _playersOnLobby) {
            MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(player.getName());
            String title = messages.getNewDayTitleMessage(_currentDay);
            String subtitle = messages.getNewDaySubtitleMessage();
            BukkitMessageSender.sendTitleMessage(player.getName(), title, subtitle);

            player.updateRegionLine(_prison, _dayPeriod);
        }

        for (Prisoner prisoner : _prisonersTeam.getMembers()) {
            if (!_prison.isRestrictedArea(prisoner.getRegion(), _dayPeriod)) {
                prisoner.leftRestrictedArea();
            }
        }

        for (Guard guard : _policeTeam.getMembers()) {
            guard.resetMissions();
        }

        givePackagesToFugitives();

        runDayTimer(_settings.getDayDuration(), _settings.getDayDuration());
    }

    private void runDayTimer(int totalSeconds, int secondsLeft) {
        if (_phase.isClockStopped()) {
            return;
        }

        updateBossBarClock(totalSeconds, secondsLeft);

        BukkitScheduler.runSchedulerLater(new Runnable() {

            @Override
            public void run() {
                if (secondsLeft == 0) {
                    startNight();
                } else {
                    runDayTimer(totalSeconds, secondsLeft - 1);
                }
            }
        }, TICKS_PER_SECOND);
    }

    private void setDayTimeBossBar() {
        _bossBar.setColor(BarColor.YELLOW);

        MessageLanguageManager messages = MessageLanguageManager.getInstance("english");
        _bossBar.setTitle(messages.getBossBarDayTitle(_currentDay));
    }

    private void startNight() {
        if (_phase.isClockStopped()) {
            return;
        }

        _dayPeriod = DayPeriod.NIGHT;
        BukkitWorldEditor.changeTimeToNight();
        setNightTimeBossBar();

        _prison.closeCellDoors();

        for (PEPlayer player : _playersOnLobby) {
            MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(player.getName());
            String title = messages.getNightTitleMessage();
            String subtitle = messages.getNightSubtitleMessage();
            BukkitMessageSender.sendTitleMessage(player.getName(), title, subtitle);

            player.updateRegionLine(_prison, _dayPeriod);
        }

        for (Prisoner prisoner : _prisonersTeam.getMembers()) {
            if (_prison.isRestrictedArea(prisoner.getRegion(), _dayPeriod)) {
                prisoner.enteredRestrictedArea();
            }
        }

        runNightTimer(_settings.getNightDuration(), _settings.getNightDuration());
    }

    private void runNightTimer(int totalSeconds, int secondsLeft) {
        if (_phase.isClockStopped()) {
            return;
        }

        updateBossBarClock(totalSeconds, secondsLeft);

        BukkitScheduler.runSchedulerLater(new Runnable() {

            @Override
            public void run() {
                if (secondsLeft == 0) {
                    if (_currentDay == _settings.getDays()) {
                        startNextPhase(new FinishedPhase(_policeTeam));
                    } else {
                        startDay();
                    }
                } else {
                    runNightTimer(totalSeconds, secondsLeft - 1);
                }
            }
        }, TICKS_PER_SECOND);
    }

    private void setNightTimeBossBar() {
        _bossBar.setColor(BarColor.BLUE);

        MessageLanguageManager messages = MessageLanguageManager.getInstance("english");
        _bossBar.setTitle(messages.getBossBarNightTitle(_currentDay));
    }

    private void updateBossBarClock(int totalSeconds, int secondsLeft) {
        _bossBar.setProgress((double) (totalSeconds - secondsLeft) / (double) totalSeconds);
    }

//	########################################
//	#            Events Results            #
//	########################################

    private void givePackagesToFugitives() {
        List<ItemProbability> itemProbabilities = ConfigManager.getInstance().getPackageItemProbabilities();

        for (Prisoner prisoner : _prisonersTeam.getMembers()) {
            if (prisoner.hasEscaped()) {
                prisoner.giveItem(ItemFactory.getRandomItem(itemProbabilities));
            }
        }
    }

    public void playerEscaped(Prisoner player) {
        player.escaped();
        BukkitTeleporter.teleport(player, _prison.getAfterEscapeLocation());

        for (PEPlayer playerOnLobby : _playersOnLobby) {
            MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(playerOnLobby.getName());
            BukkitMessageSender.sendChatMessage(playerOnLobby, messages.getPlayerEscapedMessage(player.getName()));
        }

        if (_prisonersTeam.getMembers().stream().filter(p -> p.isImprisioned()).count() == 0) {
            startNextPhase(new FinishedPhase(_prisonersTeam));
        }
    }

    public void arrestPlayer(Prisoner arrested, Guard arrester) {
        teleportToSolitary(arrested);
        arrested.clearInventory();

        for (PEPlayer player : _playersOnLobby) {
            MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(player.getName());
            String announcement = messages.getPrisonerArrested(arrested.getName());
            BukkitMessageSender.sendChatMessage(player.getName(), announcement);
        }

        runArrestTimer(_settings.getSecondsInSolitary(), arrested);
    }

    private void runArrestTimer(int secondsLeft, Prisoner arrested) {
        BukkitMessageSender.sendTitleMessage(arrested.getName(), "", ChatColor.WHITE + Integer.toString(secondsLeft));

        BukkitScheduler.runSchedulerLater(new Runnable() {

            @Override
            public void run() {
                if (_phase.isClockStopped()) {
                    return;
                }

                if (secondsLeft - 1 != 0) {
                    runArrestTimer(secondsLeft - 1, arrested);
                    return;
                }

                removeWanted(arrested);

                MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(arrested.getName());
                BukkitMessageSender.sendChatMessage(arrested.getName(), messages.getPrisonerFreedOfSolitary());

                if (_dayPeriod == DayPeriod.DAY) {
                    teleportToSolitaryExit(arrested);
                } else if (_dayPeriod == DayPeriod.NIGHT) {
                    teleportPrisonerToSpawnPoint(arrested);
                }

            }
        }, TICKS_PER_SECOND);
    }

    public void updateTeamPreference(String playerName, String message, TeamPreference teamPref, String teamName) {
        PEPlayer player = getPEPlayer(playerName);

        WaitingPlayer waitingPlayer = (WaitingPlayer) player;
        waitingPlayer.setPreference(teamPref);

        BukkitMessageSender.sendChatMessage(player, message);

        updatePreferenceTabListDisplay(playerName, teamName);
        player.getKit().update(playerName);
    }

    private void updatePreferenceTabListDisplay(String playerName, String teamName) {
        for (PEPlayer playerOnLobby : _playersOnLobby) {
            playerOnLobby.addScoreboardTeamMember(playerName, teamName);
        }
    }

    public void setWanted(Prisoner prisoner, PEPlayer guard) {
        prisoner.setWanted();

        String prisonerName = prisoner.getName();
        for (PEPlayer playerOnLobby : _playersOnLobby) {
            playerOnLobby.addScoreboardWantedTeamMember(PRISONERS_TEAM_NAME, prisonerName);
        }

        prisoner.playSound(Sound.BLOCK_BAMBOO_BREAK, 20);
        guard.playSound(Sound.BLOCK_BAMBOO_BREAK, 20);
    }

    private void removeWanted(Prisoner prisoner) {
        prisoner.removeWanted();

        String prisonerName = prisoner.getName();
        for (PEPlayer playerOnLobby : _playersOnLobby) {
            playerOnLobby.removeScoreboardWantedTeamMember(PRISONERS_TEAM_NAME, prisonerName);
        }
    }

//	########################################
//	#                 Util                 #
//	########################################

    public PEPlayer getPEPlayer(String playerName) {
        for (PEPlayer player : _playersOnLobby) {
            if (player.getName().equals(playerName)) {
                return player;
            }
        }

        return null;
    }

//	#########################################
//	#               Locations               #
//	#########################################

    private void teleportPrisonerToSpawnPoint(PEPlayer player) {
        int playerIndex = _prisonersTeam.getPlayerIndex(player);
        BukkitTeleporter.teleport(player, _prison.getPlayerCellLocation(playerIndex));
    }

    private void teleportToSolitary(PEPlayer player) {
        BukkitTeleporter.teleport(player, _prison.getSolitaryLocation());
    }

    private void teleportToSolitaryExit(PEPlayer player) {
        BukkitTeleporter.teleport(player, _prison.getSolitaryExitLocation());
    }

    private void teleportToLeavingLocation(PEPlayer player) {
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

    public void changeDoorCode() {
        _hasDoorCode = false;
    }

}
