package net.tiagofar78.prisonescape.items;

import org.bukkit.Material;

public class PlasticSpoonItem extends ToolItem {

    @Override
    protected double durabilityLostOnUse() {
        return 0;
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
        return Material.WOODEN_SHOVEL;
    }

}
