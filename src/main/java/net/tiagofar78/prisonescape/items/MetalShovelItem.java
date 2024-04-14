package net.tiagofar78.prisonescape.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.tiagofar78.prisonescape.bukkit.BukkitItems;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

public class MetalShovelItem extends ToolItem {

    @Override
    protected int usesAmount() {
        return 11;
    }

    @Override
    public int damageToBlock() {
        return 100;
    }

    @Override
    public boolean isMetalic() {
        return true;
    }

    @Override
    public boolean isIllegal() {
        return true;
    }
    
    @Override
    public ItemStack toItemStack(MessageLanguageManager messages) {
        ItemStack item = super.toItemStack(messages);
        
        BukkitItems.removeAttributes(item);
        
        return item;
    }

    @Override
    public Material getMaterial() {
        return Material.DIAMOND_SHOVEL;
    }

}
