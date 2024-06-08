package net.tiagofar78.prisonescape.game.prisonbuilding.regions;

import org.bukkit.Location;

public class SquaredRegion extends Region {

    private Location _upperCornerLocation;
    private Location _lowerCornerLocation;

    public SquaredRegion(SquaredRegion region) {
        this(
                region.getName(),
                region.isRestricted(),
                region.canCallHelicopter(),
                region._upperCornerLocation.clone(),
                region._lowerCornerLocation.clone()
        );
    }

    public SquaredRegion(
            String name,
            boolean isRestricted,
            boolean isHelicopterCallable,
            Location upperCorner,
            Location lowerCorner
    ) {
        super(name, isRestricted, isHelicopterCallable);
        _upperCornerLocation = upperCorner;
        _lowerCornerLocation = lowerCorner;
    }

    @Override
    public void add(Location location) {
        _upperCornerLocation.clone().add(location);
        _lowerCornerLocation.clone().add(location);
    }

    @Override
    public boolean contains(Location loc) {
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();

        boolean isXBetweenBoundaries = _upperCornerLocation.getX() >= x && _lowerCornerLocation.getX() <= x;
        boolean isYBetweenBoundaries = _upperCornerLocation.getY() >= y && _lowerCornerLocation.getY() <= y;
        boolean isZBetweenBoundaries = _upperCornerLocation.getZ() >= z && _lowerCornerLocation.getZ() <= z;

        return isXBetweenBoundaries && isYBetweenBoundaries && isZBetweenBoundaries;
    }

}
