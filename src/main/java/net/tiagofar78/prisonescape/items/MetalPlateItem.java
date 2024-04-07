package net.tiagofar78.prisonescape.items;

import org.bukkit.Material;

public class MetalPlateItem extends Item {

    @Override
    public boolean isMetalic() {
        return true;
    }

    @Override
    public boolean isIllegal() {
        return true;
    }

    @Override
    public Material getMaterial() {
        return Material.HEAVY_WEIGHTED_PRESSURE_PLATE;
    }

}
