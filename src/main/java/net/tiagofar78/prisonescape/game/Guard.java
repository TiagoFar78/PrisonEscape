package net.tiagofar78.prisonescape.game;

import net.tiagofar78.prisonescape.bukkit.BukkitTeleporter;
import net.tiagofar78.prisonescape.items.CameraItem;
import net.tiagofar78.prisonescape.items.Item;
import net.tiagofar78.prisonescape.items.SoundDetectorItem;
import net.tiagofar78.prisonescape.items.TrapItem;
import net.tiagofar78.prisonescape.managers.ConfigManager;
import net.tiagofar78.prisonescape.managers.GameManager;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

import org.bukkit.GameMode;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class Guard extends PEPlayer {

    private static final int BALANCE_LINE_INDEX = 2;
    private static final int SOUND_DETECTORS_FIRST_LINE_INDEX = 3;
    private static final int INITIAL_SEARCHES = 1;

    private int _balance;

    private int _numOfCamerasBought = 0;
    private int _numOfSensorsBought = 0;
    private int _numOfTrapsBought = 0;
    private int _searchesAmount = INITIAL_SEARCHES;

    private Location _locationBeforeWatchingCameras = null;

    public Guard(String name) {
        super(name);

        increaseBalance(ConfigManager.getInstance().getStartingBalance());
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

    public int buyItem(Item item, int price, boolean isGivableItem) {
        if (!canBuyItem(item)) {
            return -1;
        }
        if (price > _balance) {
            return -2;
        }

        if (isGivableItem && giveItem(item) == -1) {
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

    public void increaseTrapLimit() {
        _numOfTrapsBought--;
    }

//  #########################################
//  #                Searches               #
//  #########################################

    public int countSearches() {
        return _searchesAmount;
    }

    public void usedSearch() {
        _searchesAmount--;
        getKit().update(getName());
    }

    public void boughtSearch() {
        _searchesAmount++;
        getKit().update(getName());
    }

//  ########################################
//  #                Camera                #
//  ########################################

    public boolean isWatchingCamera() {
        return _locationBeforeWatchingCameras != null;
    }

    public void startedWatchingCamera(Location location) {
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

    @Override
    public ScoreboardData createScoreboardData() {
        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(getName());

        ScoreboardData sbData = super.createScoreboardData();

        List<Integer> linesIndexes = new ArrayList<>();
        List<String> linesContents = new ArrayList<>();

        String balanceLine = messages.getGuardSideBarBalanceLine(getBalance());
        linesIndexes.add(BALANCE_LINE_INDEX);
        linesContents.add(balanceLine);

        int emptyLines = 2;
        List<String> baseSideBar = buildBaseSideBar(emptyLines, linesIndexes, linesContents);
        sbData.createSideBar(messages.getScoreboardDisplayName(), baseSideBar);

        return sbData;
    }

    private void updateBalanceLine() {
        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(getName());
        String balanceLine = messages.getGuardSideBarBalanceLine(getBalance());
        getScoreboardData().updateLine(BALANCE_LINE_INDEX, balanceLine);
    }

    public void addSoundDetectorLine(int value) {
        int soundDetectorsAmount = GameManager.getGame().getPrison().countSoundDetectors();
        if (soundDetectorsAmount == 0) {
            String emptyLine = "§a";
            getScoreboardData().addLine(SOUND_DETECTORS_FIRST_LINE_INDEX, emptyLine);

            MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(getName());
            String line = messages.getGuardSideBarSoundDetectorLine();
            getScoreboardData().addLine(SOUND_DETECTORS_FIRST_LINE_INDEX, line);
        }

        String soundDetectorValueLine = createSoundDetectorValueLine(soundDetectorsAmount + 1, value);
        getScoreboardData().addLine(
                SOUND_DETECTORS_FIRST_LINE_INDEX + 1 + soundDetectorsAmount,
                soundDetectorValueLine
        );
    }

    public void updateSoundDetectorValue(int index, double d) {
        String soundDetectorValueLine = createSoundDetectorValueLine(index + 1, d);
        getScoreboardData().updateLine(SOUND_DETECTORS_FIRST_LINE_INDEX + 1 + index, soundDetectorValueLine);
    }

    private String createSoundDetectorValueLine(int index, double value) {
        value = Math.max(Math.min(value, 1), 0);

        int barsAmount = 30;
        String valueBars = "|".repeat(barsAmount);

        String[] colors = {"§c", "§e", "§a", "§8"};
        double[] colorPercentage = {0.15, 0.75, 0.10, 1};
        int coloredBars = (int) (30 * value / 1);

        int currentColoredBars = 0;
        for (int i = 0; i < colors.length; i++) {
            int colorIndex = currentColoredBars + i * 2;
            currentColoredBars += coloredBars * colorPercentage[i];
            valueBars = valueBars.substring(0, colorIndex) + colors[i] + valueBars.substring(colorIndex);
        }

        return "§f" + index + ": " + valueBars;
    }

}
