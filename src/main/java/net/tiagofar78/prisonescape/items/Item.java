package net.tiagofar78.prisonescape.items;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.tiagofar78.prisonescape.bukkit.BukkitItems;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

public abstract class Item {
    
    public abstract boolean isMetalic();
    
    public abstract boolean isIllegal();
    
    public abstract Material getMaterial();
    
    public abstract String getDisplayName(MessageLanguageManager messages);
    
    public abstract List<String> getLore(MessageLanguageManager messages);
    
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
