package net.tiagofar78.prisonescape.kits;

import net.tiagofar78.prisonescape.items.CraftingMenuItem;
import net.tiagofar78.prisonescape.items.GlassItem;
import net.tiagofar78.prisonescape.items.Item;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.Hashtable;

public class PrisonerKit extends Kit {

    private static final int FIRST_GLASS_ITEM_INDEX = 4;
    private static final int LAST_GLASS_ITEM_INDEX = 35;
    private static final int CRAFTING_MENU_INDEX = 8;

    private static final int CHESTPLATE_INDEX = 38;
    private static final int LEGGINGS_INDEX = 37;
    private static final int BOOTS_INDEX = 36;

    @Override
    public Hashtable<Integer, Item> getContents() {
        Hashtable<Integer, Item> items = new Hashtable<>();

        Item glass = new GlassItem();
        for (int i = FIRST_GLASS_ITEM_INDEX; i <= LAST_GLASS_ITEM_INDEX; i++) {
            items.put(i, glass);
        }

        items.put(CRAFTING_MENU_INDEX, new CraftingMenuItem());

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
        meta.setColor(Color.ORANGE);
        meta.setDisplayName(name);
        item.setItemMeta(meta);

        return item;
    }
}
