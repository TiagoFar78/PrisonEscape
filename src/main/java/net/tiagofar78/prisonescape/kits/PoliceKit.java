package net.tiagofar78.prisonescape.kits;

import net.tiagofar78.prisonescape.items.GlassItem;
import net.tiagofar78.prisonescape.items.HandcuffsItem;
import net.tiagofar78.prisonescape.items.Item;
import net.tiagofar78.prisonescape.items.MissionsItem;
import net.tiagofar78.prisonescape.items.OpenCamerasItem;
import net.tiagofar78.prisonescape.items.SearchItem;
import net.tiagofar78.prisonescape.items.ShopItem;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.Hashtable;

public class PoliceKit extends Kit {

    private static final int FIRST_GLASS_ITEM_INDEX = 9;
    private static final int LAST_GLASS_ITEM_INDEX = 35;

    private static final int SEARCH_ITEM_INDEX = 4;
    private static final int HANDCUFF_ITEM_INDEX = 5;
    private static final int CAMERA_ITEM_INDEX = 6;
    private static final int MISSIONS_ITEM_INDEX = 7;
    private static final int SHOP_ITEM_INDEX = 8;

    private static final int CHESTPLATE_INDEX = 38;
    private static final int LEGGINGS_INDEX = 37;
    private static final int BOOTS_INDEX = 36;

    @Override
    protected Hashtable<Integer, Item> getContents() {
        Hashtable<Integer, Item> items = new Hashtable<>();

        Item glass = new GlassItem();
        for (int i = FIRST_GLASS_ITEM_INDEX; i <= LAST_GLASS_ITEM_INDEX; i++) {
            items.put(i, glass);
        }

        items.put(SEARCH_ITEM_INDEX, new SearchItem());
        items.put(HANDCUFF_ITEM_INDEX, new HandcuffsItem());
        items.put(CAMERA_ITEM_INDEX, new OpenCamerasItem());
        items.put(MISSIONS_ITEM_INDEX, new MissionsItem());
        items.put(SHOP_ITEM_INDEX, new ShopItem());

        return items;
    }

    @Override
    public Hashtable<Integer, ItemStack> getVisualContents(MessageLanguageManager messages) {
        Hashtable<Integer, ItemStack> items = new Hashtable<>();

        String uniformName = messages.getPrisonerUniformName();

        items.put(CHESTPLATE_INDEX, createColoredArmor(Material.LEATHER_CHESTPLATE, uniformName));
        items.put(LEGGINGS_INDEX, createColoredArmor(Material.LEATHER_LEGGINGS, uniformName));
        items.put(BOOTS_INDEX, createColoredArmor(Material.LEATHER_BOOTS, uniformName));

        return items;
    }

    private ItemStack createColoredArmor(Material type, String name) {
        ItemStack item = new ItemStack(type);
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setColor(Color.BLUE);
        meta.setDisplayName(name);
        item.setItemMeta(meta);

        return item;
    }

    @Override
    public void update(String playerName) {
        Player bukkitPlayer = Bukkit.getPlayer(playerName);
        if (bukkitPlayer == null || !bukkitPlayer.isOnline()) {
            return;
        }

        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(playerName);

        Inventory inv = bukkitPlayer.getInventory();
        inv.setItem(CAMERA_ITEM_INDEX, getItemAt(CAMERA_ITEM_INDEX).toItemStack(messages));
    }

}
