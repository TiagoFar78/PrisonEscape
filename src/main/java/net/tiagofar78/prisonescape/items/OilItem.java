package net.tiagofar78.prisonescape.items;

import org.bukkit.Material;

public class OilItem extends Item {

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
        return Material.FLINT_AND_STEEL;
    }

}
