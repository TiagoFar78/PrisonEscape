package net.tiagofar78.prisonescape.items;

import net.tiagofar78.prisonescape.managers.ConfigManager;

import org.bukkit.Material;

public class TrapItem extends Item implements Buyable {

    @Override
    public int getPrice() {
        return ConfigManager.getInstance().getTrapPrice();
    }

    @Override
    public int getLimit() {
        return ConfigManager.getInstance().getTrapLimit();
    }

    @Override
    public boolean isMetalic() {
        return false;
    }

    @Override
    public boolean isIllegal() {
        return false;
    }

    @Override
    public Material getMaterial() {
        return Material.COBWEB;
    }

    @Override
    public boolean isBuyable() {
        return true;
    }

}
