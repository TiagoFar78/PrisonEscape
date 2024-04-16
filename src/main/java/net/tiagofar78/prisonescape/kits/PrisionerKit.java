package net.tiagofar78.prisonescape.kits;

import net.tiagofar78.prisonescape.items.GlassItem;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PrisionerKit {

    private static final int FIRST_GLASS_ITEM_INDEX = 4;
    private static final int LAST_GLASS_ITEM_INDEX = 35;

    public static void giveKitToPlayer(String playerName) {
        Player bukkitPlayer = Bukkit.getPlayer(playerName);
        if (bukkitPlayer == null || !bukkitPlayer.isOnline()) {
            return;
        }

        Inventory inv = bukkitPlayer.getInventory();
        inv.clear();

        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(playerName);

        ItemStack glassItem = new GlassItem().toItemStack(messages);
        for (int i = FIRST_GLASS_ITEM_INDEX; i <= LAST_GLASS_ITEM_INDEX; i++) {
            inv.setItem(i, glassItem);
        }
    }
}
