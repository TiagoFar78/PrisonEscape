package net.tiagofar78.prisonescape.game.prisonbuilding.regions;

import org.bukkit.Location;

public abstract class Region implements Locatable {

    private String _name;
    private boolean _isRestricted;
    private boolean _isHelicopterCallable;

    public Region(String name, boolean isRestricted) {
        _name = name;
        _isRestricted = isRestricted;
        _isHelicopterCallable = true;
    }

    public Region(String name, boolean isRestricted, boolean isHelicopterCallable) {
        this(name, isRestricted);
        _isHelicopterCallable = isHelicopterCallable;
    }

    public String getName() {
        return _name;
    }

    public boolean isRestricted() {
        return _isRestricted;
    }

    public boolean canCallHelicopter() {
        return _isHelicopterCallable;
    }

    public abstract void add(Location location);

    @Override
    public abstract boolean contains(Location loc);

}
