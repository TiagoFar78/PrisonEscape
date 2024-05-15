package net.tiagofar78.prisonescape.game;

import net.tiagofar78.prisonescape.bukkit.BukkitTeleporter;
import net.tiagofar78.prisonescape.game.prisonbuilding.PrisonEscapeLocation;
import net.tiagofar78.prisonescape.items.CameraItem;
import net.tiagofar78.prisonescape.items.Item;
import net.tiagofar78.prisonescape.items.SoundDetectorItem;
import net.tiagofar78.prisonescape.items.TrapItem;
import net.tiagofar78.prisonescape.managers.ConfigManager;
import net.tiagofar78.prisonescape.managers.GameManager;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.scoreboard.Team;

import java.util.ArrayList;
import java.util.List;

public class Guard extends PrisonEscapePlayer {

    private static final int BALANCE_LINE_INDEX = 1;
    private static final int SOUND_DETECTORS_FIRST_LINE_INDEX = 3;

    private int _balance;

    private int _numOfCamerasBought = 0;
    private int _numOfSensorsBought = 0;
    private int _numOfTrapsBought = 0;

    private PrisonEscapeLocation _locationBeforeWatchingCameras = null;

    private ScoreboardData _scoreboardData;

    public Guard(String name) {
        super(name);

        _balance = ConfigManager.getInstance().getStartingBalance();

        _scoreboardData = createScoreboardData();
        setScoreboard(_scoreboardData.getScoreboard());
    }

    @Override
    public boolean isGuard() {
        return true;
    }

//  ########################################
//  #                Balance               #
//  ########################################

    public int getBalance() {
        return _balance;
    }

    public void increaseBalance(int amount) {
        _balance += amount;
        updateBalanceLine();
    }

    public void decreaseBalance(int amount) {
        _balance -= amount;
        updateBalanceLine();
    }

    public int buyItem(Item item, int price) {
        if (!canBuyItem(item)) {
            return -1;
        }
        if (price > _balance) {
            return -2;
        }

        if (giveItem(item) == -1) {
            return -3;
        }

        decreaseBalance(price);
        updateItemCount(item);
        return 0;
    }

    private boolean canBuyItem(Item item) {
        if (item instanceof TrapItem && _numOfTrapsBought >= ((TrapItem) item).getLimit()) {
            return false;
        } else if (item instanceof CameraItem && _numOfCamerasBought >= ((CameraItem) item).getLimit()) {
            return false;
        } else if (item instanceof SoundDetectorItem && _numOfSensorsBought >= ((SoundDetectorItem) item).getLimit()) {
            return false;
        }
        return true;
    }

    private void updateItemCount(Item item) {
        if (item instanceof TrapItem) {
            _numOfTrapsBought++;
        } else if (item instanceof CameraItem) {
            _numOfCamerasBought++;
        } else if (item instanceof SoundDetectorItem) {
            _numOfSensorsBought++;
        }
    }

//  ########################################
//  #                Camera                #
//  ########################################

    public boolean isWatchingCamera() {
        return _locationBeforeWatchingCameras != null;
    }

    public void startedWatchingCamera(PrisonEscapeLocation location) {
        _locationBeforeWatchingCameras = location;
    }

    public void stoppedWatchingCamera() {
        BukkitTeleporter.teleport(this, _locationBeforeWatchingCameras);
        setGameMode(GameMode.SURVIVAL);
        _locationBeforeWatchingCameras = null;
    }

//  ########################################
//  #              Scoreboard              #
//  ########################################

    public ScoreboardData createScoreboardData() {
        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(getName());

        ScoreboardData sbData = new ScoreboardData();

        String balanceLine = messages.getGuardSideBarBalanceLine(getBalance());
        String lastLine = messages.getSideBarLastLine();
        List<String> baseSideBar = buildBaseSideBar(balanceLine, lastLine);
        sbData.createSideBar(messages.getScoreboardDisplayName(), baseSideBar);

        String guardsTeamName = GameManager.getGame().getGuardsTeam().getName();
        registerTeam(sbData, guardsTeamName, ChatColor.BLUE);

        String prisionersTeamName = GameManager.getGame().getPrisionerTeam().getName();
        registerTeam(sbData, prisionersTeamName, ChatColor.GOLD);

        return sbData;
    }

    private void registerTeam(ScoreboardData sbData, String teamName, ChatColor color) {
        Team sbTeam = sbData.registerTeam(teamName);
        sbTeam.setColor(color);
    }

    public void updateScoreaboardTeams() {
        PrisonEscapeTeam<Guard> guardsTeam = GameManager.getGame().getGuardsTeam();
        addScoreboardTeamMembers(guardsTeam);

        PrisonEscapeTeam<Prisioner> prisionersTeam = GameManager.getGame().getPrisionerTeam();
        addScoreboardTeamMembers(prisionersTeam);
    }

    private void addScoreboardTeamMembers(PrisonEscapeTeam<? extends PrisonEscapePlayer> team) {
        Team sbTeam = _scoreboardData.getScoreboard().getTeam(team.getName());
        for (PrisonEscapePlayer player : team.getMembers()) {
            sbTeam.addEntry(player.getName());
        }
    }

    private List<String> buildBaseSideBar(String balanceLine, String lastLine) {
        List<String> baseSideBar = new ArrayList<>();

        int emptyLines = 2;
        for (int i = 0; i < emptyLines; i++) {
            baseSideBar.add("§" + i);
        }

        baseSideBar.add(BALANCE_LINE_INDEX, balanceLine);

        baseSideBar.add(lastLine);

        return baseSideBar;
    }

    private void updateBalanceLine() {
        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(getName());
        String balanceLine = messages.getGuardSideBarBalanceLine(getBalance());
        _scoreboardData.updateLine(BALANCE_LINE_INDEX, balanceLine);
    }

    public void addSoundDetectorLine(int value) {
        int soundDetectorsAmount = GameManager.getGame().getPrison().countSoundDetectors();
        if (soundDetectorsAmount == 0) {
            String emptyLine = "§a";
            _scoreboardData.addLine(SOUND_DETECTORS_FIRST_LINE_INDEX, emptyLine);

            MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(getName());
            String line = messages.getGuardSideBarSoundDetectorLine();
            _scoreboardData.addLine(SOUND_DETECTORS_FIRST_LINE_INDEX, line);
        }

        String soundDetectorValueLine = createSoundDetectorValueLine(soundDetectorsAmount + 1, value);
        _scoreboardData.addLine(SOUND_DETECTORS_FIRST_LINE_INDEX + 1 + soundDetectorsAmount, soundDetectorValueLine);
    }

    public void updateSoundDetectorValue(int index, int value) {
        String soundDetectorValueLine = createSoundDetectorValueLine(index + 1, value);
        _scoreboardData.updateLine(SOUND_DETECTORS_FIRST_LINE_INDEX + 1 + index, soundDetectorValueLine);
    }

    private String createSoundDetectorValueLine(int index, int value) {
        // TODO improve how this looks
        String valueBars = value == 10 ? "§c|||§e||||§a|||" : "§8||||||||||";
        return "§f" + index + ": " + valueBars;
    }

}
