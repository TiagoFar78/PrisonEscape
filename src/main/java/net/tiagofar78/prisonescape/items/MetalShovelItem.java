package net.tiagofar78.prisonescape.items;

import org.bukkit.Material;

public class MetalShovelItem extends ToolItem {

    @Override
    protected double durabilityLostOnUse() {
        return 0;
    }

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
        return Material.DIAMOND_SHOVEL;
    }

}
