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
        double x = loc.getX();
        double y = loc.getY();
        double z = loc.getZ();

        boolean isXBetweenBoundaries = _upperCornerLocation.getX() + 1.5 >= x && _lowerCornerLocation.getX() - 0.5 <= x;
        boolean isYBetweenBoundaries = _upperCornerLocation.getY() + 1.5 >= y && _lowerCornerLocation.getY() - 0.5 <= y;
        boolean isZBetweenBoundaries = _upperCornerLocation.getZ() + 1.5 >= z && _lowerCornerLocation.getZ() - 0.5 <= z;

        return isXBetweenBoundaries && isYBetweenBoundaries && isZBetweenBoundaries;
    }

}
