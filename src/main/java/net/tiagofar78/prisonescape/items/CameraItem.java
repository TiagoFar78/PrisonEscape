package net.tiagofar78.prisonescape.items;

import net.tiagofar78.prisonescape.managers.ConfigManager;

import org.bukkit.Material;

public class CameraItem extends Item implements Buyable {

    @Override
    public int getPrice() {
        return ConfigManager.getInstance().getCameraPrice();
    }

    @Override
    public int getLimit() {
        return ConfigManager.getInstance().getCameraLimit();
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
        return Material.OBSERVER;
    }

    @Override
    public boolean isBuyable() {
        return true;
    }

}
