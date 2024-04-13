package net.tiagofar78.prisonescape.items;

import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class NullItem extends Item {

    @Override
    public boolean isMetalic() {
        return false;
    }

    @Override
    public boolean isIllegal() {
        return false;
    }

    @Override
    public ItemStack toItemStack(MessageLanguageManager messages) {
        return null;
    }

    @Override
    public Material getMaterial() {
        return Material.AIR;
    }

}
