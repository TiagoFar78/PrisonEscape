package net.tiagofar78.prisonescape.items;

import net.tiagofar78.prisonescape.managers.ConfigManager;

import org.bukkit.Material;

public class SensorItem extends Item implements Buyable {

    @Override
    public int getPrice() {
        return ConfigManager.getInstance().getSensorPrice();
    }

    @Override
    public int getLimit() {
        return ConfigManager.getInstance().getSensorLimit();
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
        return Material.LIGHTNING_ROD;
    }

    @Override
    public boolean isBuyable() {
        return true;
    }

}
