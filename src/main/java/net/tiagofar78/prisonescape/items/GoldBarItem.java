package net.tiagofar78.prisonescape.items;

import org.bukkit.Material;

public class GoldBarItem extends Item {

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
        return Material.GOLD_INGOT;
    }

}
