package net.tiagofar78.prisonescape.items;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.tiagofar78.prisonescape.bukkit.BukkitItems;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

public class PlasticShovelItem extends ToolItem {

    @Override
    protected int usesAmount() {
        return 10;
    }

    @Override
    public int damageToBlock() {
        return 50;
    }

    @Override
    public boolean isMetalic() {
        return false;
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
        return Material.STONE_SHOVEL;
    }

}
