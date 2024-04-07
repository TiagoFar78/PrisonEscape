package net.tiagofar78.prisonescape.items;

import org.bukkit.Material;

public class AntenaItem extends Item {

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
        return Material.END_ROD;
    }

}
