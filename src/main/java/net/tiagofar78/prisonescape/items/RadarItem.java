package net.tiagofar78.prisonescape.items;

import net.tiagofar78.prisonescape.managers.ConfigManager;

import org.bukkit.Material;

public class RadarItem extends Item implements Buyable {

    @Override
    public int getPrice() {
        return ConfigManager.getInstance().getRadarPrice();
    }

    @Override
    public int getLimit() {
        return ConfigManager.getInstance().getRadarLimit();
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
        return Material.COMPASS;
    }

    @Override
    public boolean isBuyable() {
        return true;
    }

}
