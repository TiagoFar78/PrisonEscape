package net.tiagofar78.prisonescape.items;

import org.bukkit.Material;

public class RadarItem extends Item implements Buyable {

    @Override
    public int getPrice() {
        // TODO: Use config to get these values
        return 10;
    }

    @Override
    public int getLimit() {
        return -1;
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
        // TODO Pick material
        throw new UnsupportedOperationException("Unimplemented method 'getMaterial'");
    }

}
