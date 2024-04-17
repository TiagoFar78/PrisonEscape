package net.tiagofar78.prisonescape.kits;

import net.tiagofar78.prisonescape.items.CameraItem;
import net.tiagofar78.prisonescape.items.GlassItem;
import net.tiagofar78.prisonescape.items.HandcuffsItem;
import net.tiagofar78.prisonescape.items.MissionsItem;
import net.tiagofar78.prisonescape.items.SearchItem;
import net.tiagofar78.prisonescape.items.ShopItem;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class PoliceKit {

    private static final int FIRST_GLASS_ITEM_INDEX = 5;
    private static final int LAST_GLASS_ITEM_INDEX = 35;

    private static final int SEARCH_ITEM_INDEX = 0;
    private static final int HANDCUFF_ITEM_INDEX = 1;
    private static final int CAMERA_ITEM_INDEX = 2;
    private static final int MISSIONS_ITEM_INDEX = 3;
    private static final int SHOP_ITEM_INDEX = 4;

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

        inv.setItem(SEARCH_ITEM_INDEX, new SearchItem().toItemStack(messages));
        inv.setItem(HANDCUFF_ITEM_INDEX, new HandcuffsItem().toItemStack(messages));
        inv.setItem(CAMERA_ITEM_INDEX, new CameraItem().toItemStack(messages));
        inv.setItem(MISSIONS_ITEM_INDEX, new MissionsItem().toItemStack(messages));
        inv.setItem(SHOP_ITEM_INDEX, new ShopItem().toItemStack(messages));
    }

}
