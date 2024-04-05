package net.tiagofar78.prisonescape.game.prisonbuilding.regions;

import net.tiagofar78.prisonescape.game.prisonbuilding.PrisonEscapeLocation;

public class SquaredRegion extends Region {

    private PrisonEscapeLocation _upperCorner;
    private PrisonEscapeLocation _lowerCorner;

    public SquaredRegion(
            String name,
            boolean isRestricted,
            PrisonEscapeLocation upperCorner,
            PrisonEscapeLocation lowerCorner
    ) {
        super(name, isRestricted);
        _upperCorner = upperCorner;
        _lowerCorner = lowerCorner;
    }

    @Override
    public Region add(PrisonEscapeLocation location) {
        return new SquaredRegion(getName(), isRestricted(), _upperCorner.add(location), _lowerCorner).add(location);
    }

    @Override
    public boolean isInside(PrisonEscapeLocation loc) {
        boolean isXBetweenBoundaries = _upperCorner.getX() >= loc.getX() && _lowerCorner.getX() <= loc.getX();
        boolean isYBetweenBoundaries = _upperCorner.getY() >= loc.getY() && _lowerCorner.getY() <= loc.getY();
        boolean isZBetweenBoundaries = _upperCorner.getZ() >= loc.getZ() && _lowerCorner.getZ() <= loc.getZ();

        return isXBetweenBoundaries && isYBetweenBoundaries && isZBetweenBoundaries;
    }

}
