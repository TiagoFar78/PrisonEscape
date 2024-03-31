package net.tiagofar78.prisonescape.kits;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class PrisionerKit {

    private static final int FIRST_GLASS_ITEM_INDEX = 4;
    private static final int LAST_GLASS_ITEM_INDEX = 35;
    private static final ItemStack GLASS_ITEM = createGlassItem();

    private static ItemStack createGlassItem() {
        ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(" ");
        item.setItemMeta(itemMeta);

        return item;
    }

    public static void giveToPlayer(String playerName) {
        Player bukkitPlayer = Bukkit.getPlayer(playerName);
        if (bukkitPlayer == null || !bukkitPlayer.isOnline()) {
            return;
        }

        Inventory inv = bukkitPlayer.getInventory();
        inv.clear();

        for (int i = FIRST_GLASS_ITEM_INDEX; i < LAST_GLASS_ITEM_INDEX + 1; i++) {
            inv.setItem(i, GLASS_ITEM);
        }
    }

}
