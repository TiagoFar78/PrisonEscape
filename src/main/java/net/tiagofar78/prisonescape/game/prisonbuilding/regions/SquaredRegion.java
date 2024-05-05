package net.tiagofar78.prisonescape.game.prisonbuilding.regions;

import net.tiagofar78.prisonescape.game.prisonbuilding.PrisonEscapeLocation;

public class SquaredRegion extends Region {

    private PrisonEscapeLocation _upperCornerLocation;
    private PrisonEscapeLocation _lowerCornerLocation;

    public SquaredRegion(SquaredRegion region) {
        this(
                region.getName(),
                region.isRestricted(),
                region.canCallHelicopter(),
                new PrisonEscapeLocation(region._upperCornerLocation),
                new PrisonEscapeLocation(region._lowerCornerLocation)
        );
    }

    public SquaredRegion(
            String name,
            boolean isRestricted,
            boolean isHelicopterCallable,
            PrisonEscapeLocation upperCorner,
            PrisonEscapeLocation lowerCorner
    ) {
        super(name, isRestricted, isHelicopterCallable);
        _upperCornerLocation = upperCorner;
        _lowerCornerLocation = lowerCorner;
    }

    @Override
    public void add(PrisonEscapeLocation location) {
        _upperCornerLocation.add(location);
        _lowerCornerLocation.add(location);
    }

    @Override
    public boolean contains(PrisonEscapeLocation loc) {
        int x = loc.getX();
        int y = loc.getY();
        int z = loc.getZ();

        boolean isXBetweenBoundaries = _upperCornerLocation.getX() >= x && _lowerCornerLocation.getX() <= x;
        boolean isYBetweenBoundaries = _upperCornerLocation.getY() >= y && _lowerCornerLocation.getY() <= y;
        boolean isZBetweenBoundaries = _upperCornerLocation.getZ() >= z && _lowerCornerLocation.getZ() <= z;

        return isXBetweenBoundaries && isYBetweenBoundaries && isZBetweenBoundaries;
    }

}
