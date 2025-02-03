package net.tiagofar78.prisonescape.missions;

import net.tiagofar78.prisonescape.game.Guard;
import net.tiagofar78.prisonescape.game.PEGame;
import net.tiagofar78.prisonescape.game.PEPlayer;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;
import net.tiagofar78.prisonescape.menus.ClickReturnAction;
import net.tiagofar78.prisonescape.menus.Clickable;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ColorConnectMission extends Mission implements Clickable {

    private final static int LINES = 6;
    private final static int COLUMNS = 9;

    private final static int[][] INPUTS = {
            {0, 41, 1, 42, 18, 46, 8, 51, 20, 29, 11, 31, 23, 40},
            {8, 26, 17, 42, 10, 21, 11, 15, 22, 24, 31, 37, 33, 38},
            {0, 13, 9, 37, 18, 46, 12, 48, 5, 44, 6, 17, 39, 50, 21, 51, 22, 53},
            {0, 46, 11, 31, 28, 41, 37, 51, 29, 40, 7, 42, 8, 25, 26, 52, 44, 53}};

    private final static Material[] COLORS = {
            Material.BLACK_STAINED_GLASS_PANE,
            Material.GREEN_STAINED_GLASS_PANE,
            Material.BROWN_STAINED_GLASS_PANE,
            Material.BLUE_STAINED_GLASS_PANE,
            Material.WHITE_STAINED_GLASS_PANE,
            Material.ORANGE_STAINED_GLASS_PANE,
            Material.YELLOW_STAINED_GLASS_PANE,
            Material.PURPLE_STAINED_GLASS_PANE,
            Material.RED_STAINED_GLASS_PANE,
            Material.PINK_STAINED_GLASS_PANE};

    private Guard _guard;
    private int _missionIndex;
    private ColorConnectGame _game;

    public ColorConnectMission(String regionName) {
        super(regionName);
    }

    @Override
    public void start(Guard guard, int missionIndex) {
        _guard = guard;
        _missionIndex = missionIndex;

        _game = new ColorConnectGame(getRandomGame());

        _guard.openMenu(this);
    }

    private int[] getRandomGame() {
        Random random = new Random();
        return INPUTS[random.nextInt(INPUTS.length)];
    }

    @Override
    public Inventory toInventory(PEGame game, PEPlayer player) {
        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(player.getName());
        String title = messages.getColorConnectTitle();
        int lines = 6;
        Inventory inv = Bukkit.createInventory(null, lines * 9, title);

        updateInventory(inv, null);

        return inv;
    }

    @Override
    public void updateInventory(Inventory inv, PEPlayer player) {
        int[] board = _game.getBoard();

        for (int slot = 0; slot < board.length; slot++) {
            inv.setItem(slot, new ItemStack(COLORS[board[slot]]));
        }
    }

    @Override
    public ClickReturnAction click(PEPlayer player, int slot, boolean isPlayerInv, ClickType type) {
        if (isPlayerInv) {
            return ClickReturnAction.NOTHING;
        }

        if (_game.click(slot)) {
            player.closeMenu(true);
            complete(_guard, _missionIndex);
        }

        player.updateView();
        return ClickReturnAction.NOTHING;
    }

    private class ColorConnectGame {

        private int[] _board = new int[LINES * COLUMNS];
        private int[] _limits;
        private Integer[][] _completed;
        private List<Integer> _forceComplete = new ArrayList<>();
        private int _startedSlot = -1;
        private int _lastSlot;
        private List<Integer> _path = new ArrayList<>();

        private ColorConnectGame(int[] slots) {
            _limits = slots;
            _completed = new Integer[_limits.length / 2][];

            for (int i = 0; i < _limits.length; i += 2) {
                int value = i / 2 + 1;

                set(_limits[i], value);
                set(_limits[i + 1], value);

                if (isAdjacent(_limits[i], _limits[i + 1])) {
                    _completed[value - 1] = new Integer[0];
                    _forceComplete.add(value);
                }
            }
        }

        private int[] getBoard() {
            return _board;
        }

        /**
         *
         * @return if the game is finished
         */
        private boolean click(int slot) {
            int value = get(slot);
            if (_startedSlot == -1) {
                if (value != 0) {
                    if (_completed[value - 1] != null) {
                        clearCompleted(value);
                    } else {
                        _startedSlot = slot;
                        _lastSlot = slot;
                    }
                }

                return false;
            }

            int currentValue = get(_startedSlot);
            if (isAdjacent(slot, _lastSlot) && value == 0) {
                set(slot, currentValue);
                _path.add(slot);
                _lastSlot = slot;

                if (isAdjacent(slot, getValueLimit(_startedSlot))) {
                    _completed[currentValue - 1] = _path.toArray(new Integer[0]);
                    _startedSlot = -1;
                    _path.clear();

                    if (areAllCompleted()) {
                        return true;
                    }
                }

                return false;
            }

            clear();

            if (value != 0) {
                clearCompleted(value);
                _startedSlot = -1;
            }

            return false;
        }

        private int get(int slot) {
            return _board[slot];
        }

        private void set(int slot, int value) {
            _board[slot] = value;
        }

        private void clear() {
            for (int slot : _path) {
                set(slot, 0);
            }

            _path.clear();
        }

        private void clearCompleted(int value) {
            if (_completed[value - 1] == null) {
                return;
            }

            for (int slot : _completed[value - 1]) {
                set(slot, 0);
            }

            _completed[value - 1] = _forceComplete.contains(value) ? new Integer[0] : null;
        }

        private boolean isAdjacent(int slot1, int slot2) {
            return isAbove(slot1, slot2) || isBelow(slot1, slot2) || isRigth(slot1, slot2) || isLeft(slot1, slot2);
        }

        private boolean isAbove(int slot1, int slot2) {
            return slot1 == slot2 + COLUMNS;
        }

        private boolean isBelow(int slot1, int slot2) {
            return slot1 == slot2 - COLUMNS;
        }

        private boolean isRigth(int slot1, int slot2) {
            return slot1 / COLUMNS == slot2 / COLUMNS && slot1 == slot2 + 1;
        }

        private boolean isLeft(int slot1, int slot2) {
            return slot1 / COLUMNS == slot2 / COLUMNS && slot1 == slot2 - 1;
        }

        private int getValueLimit(int initialLimit) {
            for (int i = 0; i < _limits.length; i += 2) {
                if (_limits[i] == initialLimit) {
                    return _limits[i + 1];
                } else if (_limits[i + 1] == initialLimit) {
                    return _limits[i];
                }
            }

            throw new IllegalAccessError("It should be impossible to don't find a limit");
        }

        private boolean areAllCompleted() {
            for (Integer[] completed : _completed) {
                if (completed == null) {
                    return false;
                }
            }

            return true;
        }

    }

}
