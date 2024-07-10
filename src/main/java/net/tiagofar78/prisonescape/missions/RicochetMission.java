package net.tiagofar78.prisonescape.missions;

import java.util.Arrays;
import java.util.Random;
import java.util.function.Predicate;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.tiagofar78.prisonescape.game.Guard;
import net.tiagofar78.prisonescape.game.PEPlayer;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;
import net.tiagofar78.prisonescape.menus.ClickReturnAction;
import net.tiagofar78.prisonescape.menus.Clickable;

public class RicochetMission extends Mission implements Clickable {

    private static final int LEFT_SLOT = 9 * 2;
    private static final int RIGTH_SLOT = 9 * 2 + 8;
    private static final int UP_SLOT = 4;
    private static final int DOWN_SLOT = 9 * 5 + 4;

    private static final int[][] INPUTS = {
            {14, 13, 4, 17, 8},
            {21, 4, 3, 6, 8, 18, 23, 24, 27},
            {22, 27, 2, 11, 14, 16, 20, 23, 25},
            {21, 20, 6, 7, 10, 11, 16, 24}};

    private Guard _guard;
    private int _missionIndex;

    private RicochetGame _game;
    private int _lastPlayerSlot;

    public RicochetMission(String regionName) {
        super(regionName);
    }

    @Override
    public void start(Guard guard, int missionIndex) {
        _guard = guard;
        _missionIndex = missionIndex;

        _game = new RicochetGame(getRandomGame());
        _lastPlayerSlot = toInventorySlot(_game._playerSlot);

        _guard.openMenu(this);
    }

    private int[] getRandomGame() {
        Random random = new Random();
        return INPUTS[random.nextInt(INPUTS.length)];
    }

    @Override
    public Inventory toInventory(MessageLanguageManager messages) {
        String title = messages.getRicochetTitle();
        int lines = 6;
        Inventory inv = Bukkit.createInventory(null, lines * 9, title);

        buildBaseInventory(inv, messages);

        ItemStack obstacle = buildObstacleItem();
        for (int wallSlot : _game._wallsSlots) {
            wallSlot = toInventorySlot(wallSlot);
            inv.setItem(wallSlot, obstacle);
        }

        inv.setItem(toInventorySlot(_game._playerSlot), buildPlayerHead(_guard.getName()));
        inv.setItem(toInventorySlot(_game._flagSlot), buildFlagItem(messages.getRicochetFlagItemName()));

        return inv;
    }

    private void buildBaseInventory(Inventory inv, MessageLanguageManager messages) {
        ItemStack obstacle = buildObstacleItem();
        for (int i = 0; i < 9; i++) {
            inv.setItem(i, obstacle);
            inv.setItem(9 * 5 + i, obstacle);
        }

        for (int i = 0; i < 4; i++) {
            inv.setItem((i + 1) * 9, obstacle);
            inv.setItem((i + 1) * 9 + 8, obstacle);
        }

        inv.setItem(LEFT_SLOT, buildMovementItem(messages.getRicochetLeftItemName()));
        inv.setItem(RIGTH_SLOT, buildMovementItem(messages.getRicochetRightItemName()));
        inv.setItem(UP_SLOT, buildMovementItem(messages.getRicochetUpItemName()));
        inv.setItem(DOWN_SLOT, buildMovementItem(messages.getRicochetDownItemName()));
    }

    @Override
    public ClickReturnAction click(PEPlayer player, int slot, boolean isPlayerInv, ClickType type) {
        if (isPlayerInv) {
            return ClickReturnAction.NOTHING;
        }

        _lastPlayerSlot = toInventorySlot(_game._playerSlot);

        switch (slot) {
            case LEFT_SLOT:
                _game.moveLeft();
                break;
            case RIGTH_SLOT:
                _game.moveRight();
                break;
            case UP_SLOT:
                _game.moveUp();
                break;
            case DOWN_SLOT:
                _game.moveDown();
                break;
        }

        if (_game.playerReachedFlag()) {
            player.closeMenu(true);
            complete(_guard, _missionIndex);
        } else {
            player.updateView();
        }

        return ClickReturnAction.NOTHING;
    }

    public void updateInventory(Inventory inv, PEPlayer player) {
        inv.setItem(_lastPlayerSlot, null);
        inv.setItem(toInventorySlot(_game._playerSlot), buildPlayerHead(player.getName()));
    }

    private ItemStack buildPlayerHead(String playerName) {
        return buildItem(Material.PLAYER_HEAD, "§f" + playerName);
    }

    private ItemStack buildObstacleItem() {
        return buildItem(Material.BLACK_STAINED_GLASS_PANE, " ");
    }

    private ItemStack buildFlagItem(String name) {
        return buildItem(Material.RED_BANNER, name);
    }

    private ItemStack buildMovementItem(String name) {
        return buildItem(Material.ARROW, name);
    }

    private ItemStack buildItem(Material material, String name) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        item.setItemMeta(meta);

        return item;
    }

    private int toInventorySlot(int gameSlot) {
        return 8 + (gameSlot / RicochetGame.COLUMNS + 1) * 2 + gameSlot;
    }

    private class RicochetGame {

        private final static int LINES = 4;
        private final static int COLUMNS = 7;

        private int _playerSlot;
        private int _flagSlot;
        private int[] _wallsSlots;

        private RicochetGame(int[] input) {
            _playerSlot = input[0];
            _flagSlot = input[1];
            _wallsSlots = Arrays.copyOfRange(input, 2, input.length);
        }

        private void moveUp() {
            Predicate<Integer> predicate = x -> x >= 0;
            move(-COLUMNS, predicate);
        }

        private void moveDown() {
            Predicate<Integer> predicate = x -> x < LINES * COLUMNS;
            move(COLUMNS, predicate);
        }

        private void moveRight() {
            Predicate<Integer> predicate = x -> x / COLUMNS == (x - 1) / COLUMNS && x < LINES * COLUMNS;
            move(1, predicate);
        }

        private void moveLeft() {
            Predicate<Integer> predicate = x -> x / COLUMNS == (x + 1) / COLUMNS && x >= 0;
            move(-1, predicate);
        }

        private void move(int amount, Predicate<Integer> predicate) {
            int nextSlot = _playerSlot + amount;
            while (predicate.test(nextSlot) && !isWallSlot(nextSlot)) {
                _playerSlot = nextSlot;
                nextSlot = _playerSlot + amount;
            }
        }

        private boolean isWallSlot(int slot) {
            for (int wallSlot : _wallsSlots) {
                if (wallSlot == slot) {
                    return true;
                }
            }

            return false;
        }

        private boolean playerReachedFlag() {
            return _playerSlot == _flagSlot;
        }

    }

}
