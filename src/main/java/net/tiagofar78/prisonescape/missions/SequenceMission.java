package net.tiagofar78.prisonescape.missions;

import net.tiagofar78.prisonescape.PrisonEscape;
import net.tiagofar78.prisonescape.game.Guard;
import net.tiagofar78.prisonescape.game.PEGame;
import net.tiagofar78.prisonescape.game.PEPlayer;
import net.tiagofar78.prisonescape.managers.ConfigManager;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;
import net.tiagofar78.prisonescape.menus.ClickReturnAction;
import net.tiagofar78.prisonescape.menus.Clickable;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SequenceMission extends Mission implements Clickable {

    private final static int[] BUTTONS_SLOTS = {
            9 * 1 + 3,
            9 * 1 + 4,
            9 * 1 + 5,
            9 * 2 + 3,
            9 * 2 + 4,
            9 * 2 + 5,
            9 * 3 + 3,
            9 * 3 + 4,
            9 * 3 + 5};
    private final static Material BUTTON_MATERIAL = Material.GRAY_STAINED_GLASS_PANE;
    private final static Material HIGHLIGHT_MATERIAL = Material.BLUE_STAINED_GLASS_PANE;
    private final static Material FAIL_MATERIAL = Material.RED_STAINED_GLASS_PANE;
    private final static int ANIMATION_TICKS = 20;

    private Guard _guard;
    private int _missionIndex;

    private SequenceGame _game;
    private int _currentSequenceIndex;
    private int _reachedSequenceIndex;
    private boolean _isClickingEnabled;
    private AnimationFunction _animation;

    public SequenceMission(String regionName) {
        super(regionName);
    }

    @Override
    public void start(Guard guard, int missionIndex) {
        _guard = guard;
        _missionIndex = missionIndex;

        int sequenceSize = ConfigManager.getInstance().getSequenceSize();
        _game = new SequenceGame(sequenceSize);
        _currentSequenceIndex = 0;
        _reachedSequenceIndex = 0;
        _isClickingEnabled = false;

        _guard.openMenu(this);

        _guard.updateInventory();
    }

    @Override
    public Inventory toInventory(PEGame game, PEPlayer player) {
        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(player.getName());
        String title = messages.getSequenceTitle();
        int lines = 5;
        Inventory inv = Bukkit.createInventory(null, lines * 9, title);

        buildBaseInventory(inv, lines);

        runShowFullSequeceAnimation(inv);

        return inv;
    }

    private void buildBaseInventory(Inventory inv, int lines) {
        ItemStack item = createNamelessItem(Material.BLACK_STAINED_GLASS_PANE);
        for (int i = 0; i < lines * 9; i++) {
            inv.setItem(i, item);
        }

        item = createNamelessItem(BUTTON_MATERIAL);
        for (int i = 0; i < BUTTONS_SLOTS.length; i++) {
            inv.setItem(BUTTONS_SLOTS[i], item);
        }
    }

    private ItemStack createNamelessItem(Material material) {
        ItemStack item = new ItemStack(material);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(" ");
        item.setItemMeta(meta);

        return item;
    }

    @Override
    public ClickReturnAction click(PEPlayer player, int slot, boolean isPlayerInv, ClickType type) {
        if (!_isClickingEnabled || isPlayerInv) {
            return ClickReturnAction.NOTHING;
        }

        int value = getValue(slot);
        if (value == -1) {
            return ClickReturnAction.NOTHING;
        }

        if (_game.match(_currentSequenceIndex, value)) {
            if (_currentSequenceIndex == _game.getSequenceSize() - 1) {
                player.closeMenu(true);
                complete(_guard, _missionIndex);
                return ClickReturnAction.NOTHING;
            }

            _animation = (inv) -> executeSuccessClick(inv);
        } else {
            _animation = (inv) -> executeFailedClick(inv);
        }

        player.updateView();
        return ClickReturnAction.NOTHING;
    }

    public void updateInventory(Inventory inv, PEPlayer player) {
        _animation.animate(inv);
    }

    private void executeSuccessClick(Inventory inv) {
        if (_currentSequenceIndex == _reachedSequenceIndex) {
            runSuccessAnimation(inv, _currentSequenceIndex, true);
            _reachedSequenceIndex++;
            _currentSequenceIndex = 0;
            return;
        }

        runSuccessAnimation(inv, _currentSequenceIndex, false);
        _currentSequenceIndex++;
    }

    private void executeFailedClick(Inventory inv) {
        runFailAnimation(inv);
        _currentSequenceIndex = 0;
        _reachedSequenceIndex = 0;
    }

    private int getValue(int slot) {
        for (int i = 0; i < BUTTONS_SLOTS.length; i++) {
            if (slot == BUTTONS_SLOTS[i]) {
                return i;
            }
        }

        return -1;
    }

    private void runShowFullSequeceAnimation(Inventory inv) {
        Bukkit.getScheduler().runTaskLater(PrisonEscape.getPrisonEscape(), new Runnable() {

            @Override
            public void run() {
                _isClickingEnabled = false;
                runShowSequenceAnimationAux(inv, 0, _reachedSequenceIndex);
            }

        }, ANIMATION_TICKS / 2);
    }

    private void runShowSequenceAnimationAux(Inventory inv, int currentIndex, int reachedIndex) {
        int slot = BUTTONS_SLOTS[_game.getValue(currentIndex)];
        inv.setItem(slot, createNamelessItem(HIGHLIGHT_MATERIAL));

        Bukkit.getScheduler().runTaskLater(PrisonEscape.getPrisonEscape(), new Runnable() {

            @Override
            public void run() {
                clearHighlight(inv, slot);

                if (currentIndex == reachedIndex) {
                    _isClickingEnabled = true;
                    return;
                }

                Bukkit.getScheduler().runTaskLater(PrisonEscape.getPrisonEscape(), new Runnable() {

                    @Override
                    public void run() {
                        runShowSequenceAnimationAux(inv, currentIndex + 1, reachedIndex);
                    }

                }, ANIMATION_TICKS / 2);
            }

        }, ANIMATION_TICKS);
    }

    private void runSuccessAnimation(Inventory inv, int index, boolean showFullSequenceAfter) {
        _isClickingEnabled = false;
        int slot = BUTTONS_SLOTS[_game.getValue(index)];
        inv.setItem(slot, createNamelessItem(HIGHLIGHT_MATERIAL));

        Bukkit.getScheduler().runTaskLater(PrisonEscape.getPrisonEscape(), new Runnable() {

            @Override
            public void run() {
                clearHighlight(inv, slot);

                if (showFullSequenceAfter) {
                    runShowFullSequeceAnimation(inv);
                } else {
                    _isClickingEnabled = true;
                }
            }

        }, ANIMATION_TICKS);
    }

    private void runFailAnimation(Inventory inv) {
        _isClickingEnabled = false;
        ItemStack item = createNamelessItem(FAIL_MATERIAL);
        int[] slots = new int[BUTTONS_SLOTS.length];
        for (int i = 0; i < BUTTONS_SLOTS.length; i++) {
            int slot = BUTTONS_SLOTS[i];
            inv.setItem(slot, item);
            slots[i] = slot;
        }

        Bukkit.getScheduler().runTaskLater(PrisonEscape.getPrisonEscape(), new Runnable() {

            @Override
            public void run() {
                _isClickingEnabled = true;
                clearHighlight(inv, slots);

                runShowFullSequeceAnimation(inv);
            }

        }, ANIMATION_TICKS);
    }

    private void clearHighlight(Inventory inv, int... slotsToClear) {
        for (int i : slotsToClear) {
            inv.setItem(i, createNamelessItem(BUTTON_MATERIAL));
        }
    }

    private class SequenceGame {

        private int[] _order;

        private SequenceGame(int sequenceSize) {
            List<Integer> elements = new ArrayList<>();
            for (int i = 0; i < BUTTONS_SLOTS.length; i++) {
                elements.add(i);
            }

            _order = new int[sequenceSize];

            Random random = new Random();
            for (int i = 0; i < sequenceSize; i++) {
                int randomElement = random.nextInt(elements.size());
                _order[i] = elements.get(randomElement);

                elements.remove(randomElement);
            }
        }

        private int getSequenceSize() {
            return _order.length;
        }

        private int getValue(int index) {
            return _order[index];
        }

        private boolean match(int index, int value) {
            return _order[index] == value;
        }

    }

    private interface AnimationFunction {

        void animate(Inventory inv);

    }

}
