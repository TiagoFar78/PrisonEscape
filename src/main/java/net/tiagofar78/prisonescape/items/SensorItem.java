package net.tiagofar78.prisonescape.items;

import net.tiagofar78.prisonescape.items.util.Buyable;

import org.bukkit.Material;

public class SensorItem extends Item implements Buyable {

    @Override
    public int getPrice() {
        // TODO: Use config to get these values;
        return 10;
    }

    @Override
    public int getLimit() {
        return 2;
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
        // TODO Pick Material
        throw new UnsupportedOperationException("Unimplemented method 'getMaterial'");
    }

}
