package net.tiagofar78.prisonescape.missions;

import net.tiagofar78.prisonescape.game.Guard;
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

public class SortMission extends Mission implements Clickable {

    private static final Material[] ITEMS = {
            Material.DANDELION,
            Material.POPPY,
            Material.BLUE_ORCHID,
            Material.ALLIUM,
            Material.AZURE_BLUET,
            Material.WHITE_TULIP,
            Material.ORANGE_TULIP};
    private static final int TEMP_SLOT = 9 * 1 + 4;
    private static final int ITEMS_FIRST_SLOT = 9 * 3 + 4 - ITEMS.length / 2;

    private Guard _guard;
    private int _missionIndex;

    private SortGame _game;
    private int _tempItemIndex = -1;
    private int _itemToSwapIndex = -1;
    private SwapFunction _swapFunction;

    public SortMission(String regionName) {
        super(regionName);
    }

    @Override
    public void start(Guard guard, int missionIndex) {
        _guard = guard;
        _missionIndex = missionIndex;

        _game = new SortGame(ITEMS.length);

        _guard.openMenu(this);
    }

    @Override
    public Inventory toInventory(MessageLanguageManager messages) {
        String title = messages.getSortTitle("");
        int lines = 5;
        Inventory inv = Bukkit.createInventory(null, lines * 9, title);

        buildBaseInventory(inv, lines);

        return inv;
    }

    private void buildBaseInventory(Inventory inv, int lines) {
        ItemStack glass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        for (int i = 0; i < lines * 9; i++) {
            inv.setItem(i, glass);
        }

        inv.setItem(TEMP_SLOT, null);
        for (int i = 0; i < ITEMS.length; i++) {
            ItemStack item = new ItemStack(ITEMS[i]);
            inv.setItem(ITEMS_FIRST_SLOT + i, item);
        }
    }

    @Override
    public ClickReturnAction click(PEPlayer player, int slot, boolean isPlayerInv, ClickType type) {
        if (isPlayerInv) {
            return ClickReturnAction.NOTHING;
        }

        if (slot == TEMP_SLOT) {
            if (_tempItemIndex == -1) {
                return ClickReturnAction.NOTHING;
            }

            _swapFunction = (inv) -> swapWithTemp(inv);
            player.updateView();
            _tempItemIndex = -1;
            return ClickReturnAction.NOTHING;
        }

        int index = slot - ITEMS_FIRST_SLOT;
        if (index < 0 || index >= ITEMS.length) {
            return ClickReturnAction.NOTHING;
        }

        if (_tempItemIndex == -1) {
            _tempItemIndex = index;
            _swapFunction = (inv) -> swapWithTemp(inv);
            player.updateView();
            return ClickReturnAction.NOTHING;
        }

        _itemToSwapIndex = index;
        _game.swap(_tempItemIndex, _itemToSwapIndex);

        if (_game.isComplete()) {
            _guard.closeMenu(true);
            complete(_guard, _missionIndex);
            return ClickReturnAction.NOTHING;
        }

        int correctPositions = _game.countCorrect();
        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(player.getName());

        player.updateViewTitle(messages.getSortTitle(Integer.toString(correctPositions)));
        _swapFunction = (inv) -> swapBetweenSlots(inv);
        player.updateView();
        _tempItemIndex = -1;
        _itemToSwapIndex = -1;

        return ClickReturnAction.NOTHING;
    }

    public void updateInventory(Inventory inv, PEPlayer player) {
        _swapFunction.swap(inv);
    }

    private void swapWithTemp(Inventory inv) {
        ItemStack tempItem = inv.getItem(TEMP_SLOT);
        ItemStack item1 = inv.getItem(ITEMS_FIRST_SLOT + _tempItemIndex);

        inv.setItem(ITEMS_FIRST_SLOT + _tempItemIndex, tempItem);
        inv.setItem(TEMP_SLOT, item1);
    }

    private void swapBetweenSlots(Inventory inv) {
        ItemStack item1 = inv.getItem(TEMP_SLOT);
        ItemStack item2 = inv.getItem(ITEMS_FIRST_SLOT + _itemToSwapIndex);

        inv.setItem(TEMP_SLOT, null);
        inv.setItem(ITEMS_FIRST_SLOT + _tempItemIndex, item2);
        inv.setItem(ITEMS_FIRST_SLOT + _itemToSwapIndex, item1);
    }

    private class SortGame {

        private int[] _currentOrder;

        private SortGame(int amount) {
            _currentOrder = generateInitialOrder(amount);

            while (isComplete()) {
                _currentOrder = generateInitialOrder(amount);
            }
        }

        private int[] generateInitialOrder(int amount) {
            int[] initialOrder = new int[amount];

            Random random = new Random();
            List<Integer> indexes = new ArrayList<>();
            for (int i = 0; i < amount; i++) {
                indexes.add(i);
            }

            for (int i = 0; i < amount; i++) {
                int randomIndex = random.nextInt(indexes.size());
                initialOrder[i] = indexes.get(randomIndex);

                indexes.remove(randomIndex);
            }

            return initialOrder;
        }

        private void swap(int slot1, int slot2) {
            int temp = _currentOrder[slot1];
            _currentOrder[slot1] = _currentOrder[slot2];
            _currentOrder[slot2] = temp;
        }

        private int countCorrect() {
            int correct = 0;
            for (int i = 0; i < _currentOrder.length; i++) {
                if (i == _currentOrder[i]) {
                    correct++;
                }
            }

            return correct;
        }

        private boolean isComplete() {
            for (int i = 0; i < _currentOrder.length; i++) {
                if (i != _currentOrder[i]) {
                    return false;
                }
            }

            return true;
        }

    }

    private interface SwapFunction {

        void swap(Inventory inv);

    }

}
