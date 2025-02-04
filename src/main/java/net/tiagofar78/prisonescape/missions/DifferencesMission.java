package net.tiagofar78.prisonescape.missions;

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
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class DifferencesMission extends Mission implements Clickable {

    private static final int SIDES_DIFFERENCE = 5;
    private static final Material[] MATERIALS = {
            Material.STONE,
            Material.STONE_STAIRS,
            Material.STONE_SLAB,
            Material.COBBLESTONE,
            Material.COBBLESTONE_STAIRS,
            Material.COBBLESTONE_SLAB,
            Material.SMOOTH_STONE,
            Material.SMOOTH_STONE_SLAB,
            Material.STONE_BRICKS,
            Material.CRACKED_STONE_BRICKS,
            Material.STONE_BRICK_STAIRS,
            Material.STONE_BRICK_SLAB,
            Material.CHISELED_STONE_BRICKS,
            Material.ANDESITE,
            Material.ANDESITE_STAIRS,
            Material.ANDESITE_SLAB,
            Material.POLISHED_ANDESITE,
            Material.POLISHED_ANDESITE_STAIRS,
            Material.POLISHED_ANDESITE_SLAB,
            Material.TUFF,
            Material.BASALT,
            Material.SMOOTH_BASALT,
            Material.POLISHED_BASALT,
            Material.LIGHT_GRAY_WOOL,
            Material.LIGHT_GRAY_CONCRETE,
            Material.LIGHT_GRAY_CONCRETE_POWDER,
            Material.LIGHT_GRAY_STAINED_GLASS,
            Material.LIGHT_GRAY_SHULKER_BOX,
            Material.GRAVEL,
            Material.DEAD_TUBE_CORAL_BLOCK,
            Material.DEAD_BRAIN_CORAL_BLOCK,
            Material.DEAD_BUBBLE_CORAL_BLOCK,
            Material.DEAD_FIRE_CORAL_BLOCK,
            Material.DEAD_HORN_CORAL_BLOCK,
            Material.BLAST_FURNACE,
            Material.LODESTONE,
            Material.SUSPICIOUS_GRAVEL,
            Material.FURNACE,
            Material.DISPENSER,
            Material.DROPPER};

    private Guard _guard;
    private int _missionIndex;

    private DifferencesGame _game;
    private int _lastFoundSlot;

    public DifferencesMission(String regionName) {
        super(regionName);
    }

    @Override
    public void start(Guard guard, int missionIndex) {
        _guard = guard;
        _missionIndex = missionIndex;

        int differences = ConfigManager.getInstance().getDifferencesAmount();
        _game = new DifferencesGame(differences);

        _guard.openMenu(this);
    }

    @Override
    public Inventory toInventory(PEGame game, PEPlayer player) {
        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(player.getName());
        List<Integer> differentGameSlots = _game.getDifferentSlots();

        String title = createTitle(messages, differentGameSlots.size());
        int lines = 6;
        Inventory inv = Bukkit.createInventory(null, lines * 9, title);

        buildBaseInventory(inv);

        List<Material> materialsLeft = placeRandomItems(inv);
        placeDifferences(inv, differentGameSlots, materialsLeft);

        return inv;
    }

    private String createTitle(MessageLanguageManager messages, int undiscoveredDifferences) {
        int totalDifferences = ConfigManager.getInstance().getDifferencesAmount();
        int differencesFound = totalDifferences - undiscoveredDifferences;
        return messages.getDifferencesTitle(differencesFound, totalDifferences);
    }

    private void buildBaseInventory(Inventory inv) {
        ItemStack separatorGlass = createNamelessItem(Material.BLACK_STAINED_GLASS_PANE);
        for (int i = 0; i < 6; i++) {
            inv.setItem(SIDES_DIFFERENCE - 1 + i * 9, separatorGlass);
        }
    }

    private List<Material> placeRandomItems(Inventory inv) {
        Random random = new Random();
        List<Material> materials = new ArrayList<>(Arrays.asList(MATERIALS));
        for (int line = 0; line < DifferencesGame.LINES; line++) {
            for (int col = 0; col < DifferencesGame.COLUMNS; col++) {
                int randomMaterial = random.nextInt(materials.size());

                int inventorySlot = 9 * line + col;
                ItemStack randomItem = createNamelessItem(materials.get(randomMaterial));
                inv.setItem(inventorySlot, randomItem);
                inv.setItem(inventorySlot + SIDES_DIFFERENCE, randomItem);

                materials.remove(randomMaterial);
            }
        }

        return materials;
    }

    private void placeAlreadyFoundDifferences(Inventory inv) {
        ItemStack item = createNamelessItem(Material.RED_STAINED_GLASS_PANE);

        inv.setItem(_lastFoundSlot, item);
        inv.setItem(_lastFoundSlot + SIDES_DIFFERENCE, item);
    }

    private void placeDifferences(Inventory inv, List<Integer> differentSlots, List<Material> materials) {
        Random random = new Random();

        for (int i = 0; i < differentSlots.size(); i++) {
            int randomMaterial = random.nextInt(materials.size());
            ItemStack item = createNamelessItem(materials.get(randomMaterial));
            inv.setItem(toInventorySlot(differentSlots.get(i)), item);

            materials.remove(randomMaterial);
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
        if (isPlayerInv) {
            return ClickReturnAction.NOTHING;
        }

        int gameSlot = toGameSlot(slot);
        if (gameSlot == -1) {
            return ClickReturnAction.NOTHING;
        }

        boolean isDifferent = _game.click(gameSlot);

        if (_game.isCompleted()) {
            complete(_guard, _missionIndex);
            _guard.closeMenu(true);
            return ClickReturnAction.NOTHING;
        }

        if (isDifferent) {
            if (slot % 9 >= SIDES_DIFFERENCE) {
                slot -= SIDES_DIFFERENCE;
            }
            _lastFoundSlot = slot;
            player.updateView();
        }

        return ClickReturnAction.NOTHING;
    }

    @Override
    public void updateInventory(Inventory inv, PEPlayer player) {
        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(player.getName());
        String title = createTitle(messages, _game.getDifferentSlots().size());

        player.updateViewTitle(title);
        placeAlreadyFoundDifferences(inv);
    }

    private int toGameSlot(int inventorySlot) {
        int line = inventorySlot / 9;
        int col = inventorySlot % 9;
        if (col == 4) {
            return -1;
        } else if (col > 4) {
            col -= SIDES_DIFFERENCE;
        }

        return line * DifferencesGame.COLUMNS + col;
    }

    private int toInventorySlot(int gameSlot) {
        int line = gameSlot / DifferencesGame.COLUMNS;
        int col = gameSlot % DifferencesGame.COLUMNS;
        return line * 9 + col;
    }

    private class DifferencesGame {

        private final static int LINES = 6;
        private final static int COLUMNS = 4;

        private List<Integer> _differentSlots = new ArrayList<>();

        private DifferencesGame(int differents) {
            Random random = new Random();
            int max = LINES * COLUMNS;
            if (differents > max / 2) {
                throw new IllegalArgumentException("Amount of different slots can not be higher than " + max);
            }

            for (int i = 0; i < differents; i++) {
                int next = random.nextInt(max);
                if (_differentSlots.contains(next)) {
                    i--;
                    continue;
                }

                _differentSlots.add(next);
            }
        }

        private List<Integer> getDifferentSlots() {
            return _differentSlots;
        }

        /**
         *
         * @return returns if the slot is different
         */
        private boolean click(int slot) {
            for (int i = 0; i < _differentSlots.size(); i++) {
                if (_differentSlots.get(i) == slot) {
                    _differentSlots.remove(i);
                    return true;
                }
            }

            return false;
        }

        private boolean isCompleted() {
            return _differentSlots.isEmpty();
        }

    }

}
