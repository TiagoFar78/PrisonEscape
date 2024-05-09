package net.tiagofar78.prisonescape.game;

import net.tiagofar78.prisonescape.items.CameraItem;
import net.tiagofar78.prisonescape.items.Item;
import net.tiagofar78.prisonescape.items.SensorItem;
import net.tiagofar78.prisonescape.items.TrapItem;
import net.tiagofar78.prisonescape.managers.ConfigManager;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Criteria;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.List;

public class Guard extends PrisonEscapePlayer {

    private static final String OBJECTIVE_NAME = "GuardSideBar";

    private int _balance;

    private int _numOfCamerasBought = 0;
    private int _numOfSensorsBought = 0;
    private int _numOfTrapsBought = 0;

    private Scoreboard _scoreboard;

    public Guard(String name) {
        super(name);

        _balance = ConfigManager.getInstance().getStartingBalance();
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

    public void setBalance(int balance) {
        _balance = balance;
    }

    public void increaseBalance(int amount) {
        _balance += amount;
    }

    public void decreaseBalance(int amount) {
        _balance -= amount;
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

    @Override
    public void applyScoreboard() {
        Scoreboard sb = Bukkit.getScoreboardManager().getNewScoreboard();

        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(getName());

        Objective obj = sb.registerNewObjective(OBJECTIVE_NAME, Criteria.DUMMY, messages.getScoreboardDisplayName());
        obj.setDisplaySlot(DisplaySlot.SIDEBAR);

        List<String> guardSideBar = messages.getGuardSideBar();
        for (int i = 0; i < guardSideBar.size(); i++) {
            String line = guardSideBar.get(i).replace("{BALANCE}", Integer.toString(getBalance()));
            obj.getScore(line).setScore(-i);
        }

        Player player = Bukkit.getPlayer(getName());
        if (player != null && player.isOnline()) {
            player.setScoreboard(sb);
        }

        _scoreboard = sb;
    }

}
