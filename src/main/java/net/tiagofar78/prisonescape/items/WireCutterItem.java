package net.tiagofar78.prisonescape.items;

import org.bukkit.Material;

public class WireCutterItem extends ToolItem {

    @Override
    protected int usesAmount() {
        return 5;
    }

    @Override
    public int damageToBlock() {
        return 20;
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
        return Material.SHEARS;
    }

}
