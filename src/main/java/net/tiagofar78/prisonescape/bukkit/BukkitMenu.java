package net.tiagofar78.prisonescape.bukkit;

import net.tiagofar78.prisonescape.items.CameraItem;
import net.tiagofar78.prisonescape.items.EnergyDrinkItem;
import net.tiagofar78.prisonescape.items.Item;
import net.tiagofar78.prisonescape.items.RadarItem;
import net.tiagofar78.prisonescape.items.SensorItem;
import net.tiagofar78.prisonescape.items.TrapItem;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class BukkitMenu {

    private static final int SLOTS_PER_LINE = 9;
    private static final ItemStack GLASS_ITEM = createGlassItem();

    private static ItemStack createGlassItem() {
        ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(" ");
        item.setItemMeta(itemMeta);

        return item;
    }

//  #########################################
//  #                 Vault                 #
//  #########################################

    private static final int[] NON_HIDDEN_ITEMS_INDEXES = {9 + 2, 9 + 3, 9 + 5, 9 + 6};
    private static final int HIDDEN_ITEM_INDEX = 9 * 4 + 4;

    public static void openVault(String playerName, List<Item> contents, List<Item> hiddenContents) {
        Player bukkitPlayer = Bukkit.getPlayer(playerName);
        if (bukkitPlayer == null || !bukkitPlayer.isOnline()) {
            return;
        }

        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(playerName);

        String title = messages.getVaultTitle();
        int lines = 6;
        Inventory inv = Bukkit.createInventory(null, lines * 9, title);

        for (int i = 0; i < lines * 9; i++) {
            inv.setItem(i, GLASS_ITEM);
        }

        for (int i = 0; i < contents.size(); i++) {
            ItemStack item = contents.get(i).toItemStack(messages);
            inv.setItem(NON_HIDDEN_ITEMS_INDEXES[i], item);
        }

        ItemStack hiddenIndicatorItem = createHiddenIndicatorItem(messages);
        for (int line = 4; line < 7; line++) {
            for (int column = 4; column < 7; column++) {
                int index = (line - 1) * 9 + (column - 1);
                inv.setItem(index, hiddenIndicatorItem);
            }
        }

        ItemStack hiddenItem = hiddenContents.get(0).toItemStack(messages);
        inv.setItem(HIDDEN_ITEM_INDEX, hiddenItem);

        bukkitPlayer.openInventory(inv);
    }

    public static int convertToIndexVault(int slot) {
        for (int i = 0; i < NON_HIDDEN_ITEMS_INDEXES.length; i++) {
            if (NON_HIDDEN_ITEMS_INDEXES[i] == slot) {
                return i;
            }
        }

        if (HIDDEN_ITEM_INDEX == slot) {
            return 0;
        }

        return -1;
    }

    public static boolean isHiddenIndexVault(int slot) {
        return HIDDEN_ITEM_INDEX == slot;
    }

    private static ItemStack createHiddenIndicatorItem(MessageLanguageManager messages) {
        ItemStack item = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(messages.getVaultHiddenGlassName());
        item.setItemMeta(itemMeta);

        return item;
    }

//  #########################################
//  #                 Chest                 #
//  #########################################

    private static final int[] CHEST_CONTENT_INDEXES =
            {SLOTS_PER_LINE * 1 + 2, SLOTS_PER_LINE * 1 + 3, SLOTS_PER_LINE * 1 + 4, SLOTS_PER_LINE * 1 + 5, SLOTS_PER_LINE * 1 + 6};

    public static void openChest(String playerName, List<Item> contents) {
        Player bukkitPlayer = Bukkit.getPlayer(playerName);
        if (bukkitPlayer == null || !bukkitPlayer.isOnline()) {
            return;
        }

        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(playerName);

        int lines = 3;
        String title = messages.getContainerName();
        Inventory inv = Bukkit.createInventory(bukkitPlayer, lines * SLOTS_PER_LINE, title);

        for (int i = 0; i < lines * SLOTS_PER_LINE; i++) {
            inv.setItem(i, GLASS_ITEM);
        }

        for (int i = 0; i < contents.size(); i++) {
            ItemStack item = contents.get(i).toItemStack(messages);
            inv.setItem(CHEST_CONTENT_INDEXES[i], item);
        }

        bukkitPlayer.openInventory(inv);
    }

    public static int convertToIndexChest(int slot) {
        for (int i = 0; i < CHEST_CONTENT_INDEXES.length; i++) {
            if (CHEST_CONTENT_INDEXES[i] == slot) {
                return i;
            }
        }

        return -1;
    }

//  ########################################
//  #           Player Inventory           #
//  ########################################

    private static final int[] UNCOVERED_INDEXES = {0, 1, 2, 3};

    public static void setItem(String playerName, int index, Item item) {
        Player bukkitPlayer = Bukkit.getPlayer(playerName);
        if (bukkitPlayer == null || !bukkitPlayer.isOnline()) {
            return;
        }

        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(playerName);
        ItemStack bukkitItem = item.toItemStack(messages);
        bukkitPlayer.getInventory().setItem(UNCOVERED_INDEXES[index], bukkitItem);
    }

    public static int convertToIndexPlayerInventory(int slot) {
        for (int i = 0; i < UNCOVERED_INDEXES.length; i++) {
            if (slot == UNCOVERED_INDEXES[i]) {
                return i;
            }
        }

        return -1;
    }

//  ########################################
//  #                 Shop                 #
//  ########################################

    private static final int NUM_OF_ITEMS_FOR_SALE = 5;

    public static void openShop(String playerName) {
        Player bukkitPlayer = Bukkit.getPlayer(playerName);
        if (bukkitPlayer == null || !bukkitPlayer.isOnline()) {
            return;
        }

        Inventory shopMenu = Bukkit.createInventory(null, 9, "Buy Menu");
        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(playerName);

        shopMenu.setItem(0, new EnergyDrinkItem().toItemStack(messages));
        shopMenu.setItem(1, new TrapItem().toItemStack(messages));
        shopMenu.setItem(2, new SensorItem().toItemStack(messages));
        shopMenu.setItem(3, new CameraItem().toItemStack(messages));
        shopMenu.setItem(4, new RadarItem().toItemStack(messages));

        bukkitPlayer.openInventory(shopMenu);
    }

    public static int convertToIndexShop(int slot) {
        if (slot >= NUM_OF_ITEMS_FOR_SALE) {
            return -1;
        }

        return slot;
    }
}
