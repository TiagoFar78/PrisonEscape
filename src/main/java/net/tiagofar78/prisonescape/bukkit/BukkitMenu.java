package net.tiagofar78.prisonescape.bukkit;

import net.tiagofar78.prisonescape.game.PrisonEscapeItem;
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

    public static void openVault(
            String playerName,
            List<PrisonEscapeItem> contents,
            List<PrisonEscapeItem> hiddenContents
    ) {
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
            ItemStack item = BukkitItems.convertToItemStack(contents.get(i));
            inv.setItem(NON_HIDDEN_ITEMS_INDEXES[i], item);
        }

        ItemStack hiddenIndicatorItem = createHiddenIndicatorItem(messages);
        for (int line = 4; line < 7; line++) {
            for (int column = 4; column < 7; column++) {
                int index = (line - 1) * 9 + (column - 1);
                inv.setItem(index, hiddenIndicatorItem);
            }
        }

        ItemStack hiddenItem = BukkitItems.convertToItemStack(hiddenContents.get(0));
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

    public static void openChest(String playerName, List<PrisonEscapeItem> contents) {
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
            ItemStack item = BukkitItems.convertToItemStack(contents.get(i));
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

    public static void setItem(String playerName, int slot, PrisonEscapeItem item) {
        Player bukkitPlayer = Bukkit.getPlayer(playerName);
        if (bukkitPlayer == null || !bukkitPlayer.isOnline()) {
            return;
        }

        ItemStack bukkitItem = BukkitItems.convertToItemStack(item);
        bukkitPlayer.getInventory().setItem(slot, bukkitItem);
    }

}
