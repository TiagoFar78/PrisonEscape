package net.tiagofar78.prisonescape.kits;

import java.util.Hashtable;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import net.tiagofar78.prisonescape.items.CraftingMenuItem;
import net.tiagofar78.prisonescape.items.Item;
import net.tiagofar78.prisonescape.items.MapItem;
import net.tiagofar78.prisonescape.items.TradeItem;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

public class PrisonerKit extends Kit {

    private static final int FIRST_GLASS_ITEM_INDEX = 4;
    private static final int LAST_GLASS_ITEM_INDEX = 35;
    private static final int TRADE_ITEM_INDEX = 6;
    private static final int MAP_ITEM_INDEX = 7;
    private static final int CRAFTING_MENU_INDEX = 8;

    private static final int CHESTPLATE_INDEX = 38;
    private static final int LEGGINGS_INDEX = 37;
    private static final int BOOTS_INDEX = 36;

    @Override
    public Hashtable<Integer, Item> getContents() {
        Hashtable<Integer, Item> items = new Hashtable<>();

        items.put(CRAFTING_MENU_INDEX, new CraftingMenuItem());
        items.put(TRADE_ITEM_INDEX, new TradeItem());
        items.put(MAP_ITEM_INDEX, new MapItem());

        return items;
    }

    @Override
    public Hashtable<Integer, ItemStack> getVisualContents(MessageLanguageManager messages) {
        Hashtable<Integer, ItemStack> items = new Hashtable<>();

        ItemStack glass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        for (int i = FIRST_GLASS_ITEM_INDEX; i <= LAST_GLASS_ITEM_INDEX; i++) {
            items.put(i, glass);
        }

        String uniformName = messages.getPrisonerUniformName();

        items.put(CHESTPLATE_INDEX, createColoredArmor(Material.LEATHER_CHESTPLATE, uniformName));
        items.put(LEGGINGS_INDEX, createColoredArmor(Material.LEATHER_LEGGINGS, uniformName));
        items.put(BOOTS_INDEX, createColoredArmor(Material.LEATHER_BOOTS, uniformName));

        return items;
    }

    private ItemStack createColoredArmor(Material type, String name) {
        ItemStack item = new ItemStack(type);
        LeatherArmorMeta meta = (LeatherArmorMeta) item.getItemMeta();
        meta.setColor(Color.ORANGE);
        meta.setDisplayName(name);
        item.setItemMeta(meta);

        return item;
    }
}
