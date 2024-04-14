package net.tiagofar78.prisonescape.items;

import org.bukkit.Material;

public class PlasticShovelItem extends ToolItem {

    @Override
    protected int usesAmount() {
        return 10;
    }

    @Override
    public int damageToBlock() {
        return 50;
    }

    @Override
    public boolean isMetalic() {
        return false;
    }

    @Override
    public boolean isIllegal() {
        return true;
    }

    @Override
    public Material getMaterial() {
        return Material.STONE_SHOVEL;
    }

}
