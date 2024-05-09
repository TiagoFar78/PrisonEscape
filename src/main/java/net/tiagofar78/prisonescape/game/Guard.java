package net.tiagofar78.prisonescape.game;

import net.tiagofar78.prisonescape.items.CameraItem;
import net.tiagofar78.prisonescape.items.Item;
import net.tiagofar78.prisonescape.items.SensorItem;
import net.tiagofar78.prisonescape.items.TrapItem;
import net.tiagofar78.prisonescape.managers.ConfigManager;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

import java.util.ArrayList;
import java.util.List;

public class Guard extends PrisonEscapePlayer {

    private static final int BALANCE_LINE_INDEX = 1;

    private int _balance;

    private int _numOfCamerasBought = 0;
    private int _numOfSensorsBought = 0;
    private int _numOfTrapsBought = 0;

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
        } else if (item instanceof SensorItem && _numOfSensorsBought >= ((SensorItem) item).getLimit()) {
            return false;
        }
        return true;
    }

    private void updateItemCount(Item item) {
        if (item instanceof TrapItem) {
            _numOfTrapsBought++;
        } else if (item instanceof CameraItem) {
            _numOfCamerasBought++;
        } else if (item instanceof SensorItem) {
            _numOfSensorsBought++;
        }
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

        return sbData;
    }

    private List<String> buildBaseSideBar(String balanceLine, String lastLine) {
        List<String> baseSideBar = new ArrayList<>();

        int emptyLines = 2;
        for (int i = 0; i < emptyLines; i++) {
            baseSideBar.add("&" + i);
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

}
