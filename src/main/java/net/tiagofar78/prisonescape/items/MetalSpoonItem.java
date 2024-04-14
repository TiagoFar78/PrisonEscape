package net.tiagofar78.prisonescape.items;

import org.bukkit.Material;

public class MetalSpoonItem extends ToolItem {

    @Override
    protected int usesAmount() {
        return 8;
    }

    @Override
    public int damageToBlock() {
        return 50;
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
        return Material.GOLDEN_SHOVEL;
    }


}
