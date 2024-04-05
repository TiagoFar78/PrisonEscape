package net.tiagofar78.prisonescape.game.prisonbuilding.regions;

import net.tiagofar78.prisonescape.game.prisonbuilding.PrisonEscapeLocation;

import java.util.List;

public class ComplexRegion extends Region {

    public ComplexRegion(String name, boolean isRestricted, List<PrisonEscapeLocation> locations) {
        super(name, isRestricted);
    }

    @Override
    public boolean isInside(PrisonEscapeLocation loc) {
        // TODO
        return false;
    }

    @Override
    public Region add(PrisonEscapeLocation location) {
        // TODO
        return null;
    }

}
