package net.tiagofar78.prisonescape.items;

import net.tiagofar78.prisonescape.bukkit.BukkitItems;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public abstract class Item {

    public abstract boolean isMetalic();

    public abstract boolean isIllegal();

    public abstract Material getMaterial();

    public String getConfigName() {
        String subClassName = this.getClass().getName();
        int itemWordLength = "Item".length();

        return subClassName.substring(0, subClassName.length() - itemWordLength);
    }

    public String getDisplayName(MessageLanguageManager messages) {
        return messages.getItemName(getConfigName());
    }

    public List<String> getLore(MessageLanguageManager messages) {
        return messages.getItemLore(getConfigName());
    }

    public ItemStack toItemStack(MessageLanguageManager messages) {
        ItemStack item = BukkitItems.createItemStack(Material.ORANGE_WOOL);
        BukkitItems.setName(item, getDisplayName(messages));

        List<String> lore = getLore(messages);
        if (lore != null) {
            BukkitItems.setLore(item, lore);
        }

        return item;
    }

    public boolean matches(Material material) {
        return material == getMaterial();
    }

    public boolean matches(ItemStack item) {
        return matches(item.getType());
    }

}
