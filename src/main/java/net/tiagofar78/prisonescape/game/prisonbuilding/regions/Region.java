package net.tiagofar78.prisonescape.game.prisonbuilding.regions;

import net.tiagofar78.prisonescape.game.prisonbuilding.PrisonEscapeLocation;

public abstract class Region implements Locatable {

    private String _name;
    private boolean _isRestricted;

    public Region(String name, boolean isRestricted) {
        _name = name;
        _isRestricted = isRestricted;
    }

    public String getName() {
        return _name;
    }

    public boolean isRestricted() {
        return _isRestricted;
    }

    public abstract void add(PrisonEscapeLocation location);

    @Override
    public abstract boolean isInside(PrisonEscapeLocation loc);

}
