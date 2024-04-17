package net.tiagofar78.prisonescape.items;

import org.bukkit.Material;

public class CameraItem extends Item implements Buyable {

    @Override
    public int getPrice() {
        // TODO: Use config manager to get these values
        return 10;
    }

    @Override
    public int getLimit() {
        return 1;
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

}
