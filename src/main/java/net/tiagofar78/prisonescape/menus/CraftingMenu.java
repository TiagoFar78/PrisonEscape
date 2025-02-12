package net.tiagofar78.prisonescape.menus;

import net.tiagofar78.prisonescape.game.PEGame;
import net.tiagofar78.prisonescape.game.PEPlayer;
import net.tiagofar78.prisonescape.items.AntenaItem;
import net.tiagofar78.prisonescape.items.BombItem;
import net.tiagofar78.prisonescape.items.CellPhoneItem;
import net.tiagofar78.prisonescape.items.CircuitBoardItem;
import net.tiagofar78.prisonescape.items.Craftable;
import net.tiagofar78.prisonescape.items.DoorCodeItem;
import net.tiagofar78.prisonescape.items.GoldenKeyItem;
import net.tiagofar78.prisonescape.items.GrayKeyItem;
import net.tiagofar78.prisonescape.items.Item;
import net.tiagofar78.prisonescape.items.LanternItem;
import net.tiagofar78.prisonescape.items.MetalPlateItem;
import net.tiagofar78.prisonescape.items.MetalShovelItem;
import net.tiagofar78.prisonescape.items.MetalSpoonItem;
import net.tiagofar78.prisonescape.items.PlasticShovelItem;
import net.tiagofar78.prisonescape.items.WireCutterItem;
import net.tiagofar78.prisonescape.items.WrenchItem;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class CraftingMenu implements Clickable {

    private static final int CRAFTING_ITEMS_STARTING_SLOT = 9 * 4 + 1;
    private static final int MAX_CRAFTING_ITEMS = 4;
    private static final int CONFIRM_CRAFTING_ITEM_SLOT = 9 * 4 + 6;
    private static final int[] ITEMS_SLOTS = {
            9 + 1,
            9 + 2,
            9 + 3,
            9 + 4,
            9 + 5,
            9 + 6,
            9 + 7,
            18 + 1,
            18 + 2,
            18 + 3,
            18 + 4,
            18 + 5,
            18 + 6,
            18 + 7};

    private Item _selectedItem;

    @Override
    public Inventory toInventory(PEGame game, PEPlayer player) {
        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(player.getName());

        int lines = 6;
        String title = messages.getCraftingMenuTitle();
        Inventory inv = Bukkit.createInventory(null, lines * 9, title);

        placeGlasses(inv, lines);
        placeItems(inv, game, player);

        return inv;
    }

    private void placeGlasses(Inventory inv, int lines) {
        ItemStack glass = createGlass();

        for (int i = 0; i < lines; i++) {
            for (int j = 0; j < 9; j++) {
                inv.setItem(i * 9 + j, glass);
            }
        }
    }

    private ItemStack createGlass() {
        ItemStack glass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.setDisplayName(" ");
        glass.setItemMeta(glassMeta);

        return glass;
    }

    private void placeItems(Inventory inv, PEGame game, PEPlayer player) {
        Item[] items = (Item[]) getItems();

        for (int i = 0; i < ITEMS_SLOTS.length; i++) {
            inv.setItem(ITEMS_SLOTS[i], items[i].toItemStack(game, player));
        }
    }

    private void placeItemsToCraft(Inventory inv, PEGame game, PEPlayer player) {
        Craftable selectedItem = (Craftable) _selectedItem;
        List<Item> craftingItems = selectedItem == null ? new ArrayList<>() : selectedItem.getCratingItems();

        int i;
        for (i = 0; i < craftingItems.size(); i++) {
            inv.setItem(CRAFTING_ITEMS_STARTING_SLOT + i, craftingItems.get(i).toItemStack(game, player));
        }

        ItemStack glass = createGlass();

        for (; i < MAX_CRAFTING_ITEMS; i++) {
            inv.setItem(CRAFTING_ITEMS_STARTING_SLOT + i, glass);
        }
    }

    private void placeConfirmationItem(Inventory inv, PEPlayer player, MessageLanguageManager messages) {
        ItemStack item;
        String itemName;

        if (_selectedItem == null) {
            item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
            itemName = " ";
        } else if (findCraftingItemsIndexes(player).size() == 0) {
            item = new ItemStack(Material.RED_WOOL);
            itemName = messages.getMissingItemsWoolName();
        } else {
            item = new ItemStack(Material.LIME_WOOL);
            itemName = messages.getConfirmCraftWoolName();
        }

        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(itemName);
        item.setItemMeta(meta);

        inv.setItem(CONFIRM_CRAFTING_ITEM_SLOT, item);
    }

    @Override
    public void updateInventory(Inventory inv, PEPlayer player) {
        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(player.getName());
        placeItemsToCraft(inv, player.getGame(), player);
        placeConfirmationItem(inv, player, messages);
    }

    @Override
    public ClickReturnAction click(PEPlayer player, int slot, boolean isPlayerInv, ClickType type) {
        if (isPlayerInv) {
            return ClickReturnAction.NOTHING;
        }

        if (slot == CONFIRM_CRAFTING_ITEM_SLOT) {
            return clickConfirmation(player);
        }

        int index = slotToIndex(slot);
        if (index == -1) {
            return ClickReturnAction.NOTHING;
        }

        _selectedItem = getItems()[index];
        player.updateView();

        return ClickReturnAction.NOTHING;
    }

    private ClickReturnAction clickConfirmation(PEPlayer player) {
        if (_selectedItem == null) {
            return ClickReturnAction.NOTHING;
        }

        List<Integer> itemsIndexes = findCraftingItemsIndexes(player);

        if (itemsIndexes.size() == 0) {
            MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(player.getName());
            player.sendChatMessage(messages.getCraftingItemsMissingMessage());
        } else {
            processCrafting(player, itemsIndexes);
            _selectedItem = null;
            player.updateView();

        }

        return ClickReturnAction.NOTHING;
    }

    private void processCrafting(PEPlayer player, List<Integer> itemsIndexes) {
        for (int itemIndex : itemsIndexes) {
            player.removeItemIndex(itemIndex);
        }

        player.giveItem(_selectedItem);
    }

    private List<Integer> findCraftingItemsIndexes(PEPlayer player) {
        List<Integer> alreadyFoundItemIndexes = new ArrayList<>();

        List<Item> items = player.getItemsInInventory();

        for (Item craftingItem : ((Craftable) _selectedItem).getCratingItems()) {
            boolean foundMatch = false;
            for (int i = 0; i < items.size(); i++) {
                if (craftingItem.equals(items.get(i)) && !alreadyFoundItemIndexes.contains(i)) {
                    foundMatch = true;
                    alreadyFoundItemIndexes.add(i);
                    break;
                }
            }

            if (!foundMatch) {
                return new ArrayList<>();
            }
        }

        return alreadyFoundItemIndexes;
    }

    private Item[] getItems() {
        Item[] items = {
                new MetalSpoonItem(false),
                new PlasticShovelItem(false),
                new MetalShovelItem(false),
                new MetalPlateItem(),
                new GrayKeyItem(),
                new GoldenKeyItem(),
                new DoorCodeItem(),
                new CircuitBoardItem(),
                new BombItem(),
                new AntenaItem(),
                new CellPhoneItem(),
                new WireCutterItem(false),
                new WrenchItem(false),
                new LanternItem()};

        return items;
    }

    private int slotToIndex(int slot) {
        for (int i = 0; i < ITEMS_SLOTS.length; i++) {
            if (ITEMS_SLOTS[i] == slot) {
                return i;
            }
        }

        return -1;
    }

}
