package net.tiagofar78.prisonescape.items;

import org.bukkit.Material;

public class PlasticSpoonItem extends ToolItem {

    @Override
    protected int usesAmount() {
        return 6;
    }

    @Override
    public int damageToBlock() {
        return 25;
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
