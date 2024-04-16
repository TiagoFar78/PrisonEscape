package net.tiagofar78.prisonescape.items;

import org.bukkit.Material;

public class GlassItem extends Item {

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
        return Material.BLACK_STAINED_GLASS_PANE;
    }

}
