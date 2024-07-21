package net.tiagofar78.prisonescape.game;

import net.tiagofar78.prisonescape.bukkit.BukkitTeleporter;
import net.tiagofar78.prisonescape.items.Buyable;
import net.tiagofar78.prisonescape.items.Item;
import net.tiagofar78.prisonescape.items.SearchItem;
import net.tiagofar78.prisonescape.items.TrapItem;
import net.tiagofar78.prisonescape.managers.ConfigManager;
import net.tiagofar78.prisonescape.managers.GameManager;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;
import net.tiagofar78.prisonescape.missions.Mission;

import org.bukkit.GameMode;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

public class Guard extends PEPlayer {

    private static final int BALANCE_LINE_INDEX = 2;
    private static final int SOUND_DETECTORS_FIRST_LINE_INDEX = 4;

    private int _balance;
    private List<Mission> _missions;

    private Hashtable<String, Integer> _itemsBought;

    private Location _locationBeforeWatchingCameras = null;

    public Guard(String name) {
        super(name);

        _missions = new ArrayList<>();

        _itemsBought = new Hashtable<>();

        SearchItem search = new SearchItem();
        giveItem(search);
        updateItemCount(search);

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

    public int buyItem(Item item, int price) {
        if (!canBuyItem((Buyable) item)) {
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

    private boolean canBuyItem(Buyable item) {
        int limit = item.getLimit();
        if (limit < 0) {
            return true;
        }

        Integer bought = _itemsBought.get(item.getClass().getSimpleName());
        if (bought == null) {
            bought = 0;
        }

        return bought < item.getLimit();
    }

    private void updateItemCount(String itemName, int amount) {
        Integer currentCount = _itemsBought.get(itemName);
        if (currentCount == null) {
            currentCount = 0;
        }

        _itemsBought.put(itemName, currentCount + amount);
    }

    private void updateItemCount(Item item) {
        updateItemCount(item.getClass().getSimpleName(), 1);
    }

    public void increaseTrapLimit() {
        updateItemCount(TrapItem.class.getSimpleName(), -1);
    }

    public void usedSearch() {
        updateItemCount(SearchItem.class.getSimpleName(), -1);
    }

//  ########################################
//  #                Mission               #
//  ########################################

    public List<Mission> getMissions() {
        return _missions;
    }

    public void resetMissions() {
        _missions.clear();

        ConfigManager config = ConfigManager.getInstance();
        int missions = config.getMissionsPerDay();
        List<String> missionsRegionsNames = config.getMissionsRegions();
        Random random = new Random();

        for (int i = 0; i < missions; i++) {
            int randomMissionRegion = random.nextInt(missionsRegionsNames.size());
            _missions.add(Mission.getRandomMission(missionsRegionsNames.get(randomMissionRegion)));
            missionsRegionsNames.remove(randomMissionRegion);
        }

        getKit().update(getName());
    }

    public void removeMission(int index) {
        _missions.remove(index);
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
