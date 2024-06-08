package net.tiagofar78.prisonescape.game.prisonbuilding.regions;

import org.bukkit.Location;

import java.util.List;

public class ComplexRegion extends Region {

    public ComplexRegion(String name, boolean isRestricted, List<Location> locations) {
        super(name, isRestricted);
    }

    @Override
    public boolean contains(Location loc) {
        // TODO
        return false;
    }

    @Override
    public void add(Location location) {
        // TODO
    }

}
