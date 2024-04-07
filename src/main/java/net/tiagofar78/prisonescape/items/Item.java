package net.tiagofar78.prisonescape.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public abstract class Item {
    
    public abstract boolean isMetalic();
    
    public abstract boolean isIllegal();
    
    public abstract ItemStack toItemStack();
    
    public abstract boolean matches(Material material);
    
    public boolean matches(ItemStack item) {
        return matches(item.getType());
    }

}
