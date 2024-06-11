package net.tiagofar78.prisonescape.game.prisonbuilding.placeables;

import net.tiagofar78.prisonescape.game.PEPlayer;

import org.bukkit.Location;
import org.bukkit.Material;

public class Trap {

    private Location _location;
    private boolean _caughtAPrisoner;

    public Trap(Location location) {
        _location = location;
        _caughtAPrisoner = false;
        create();
    }

    public void create() {
        _location.getBlock().setType(Material.COBWEB);
    }

    public void delete() {
        _location.getBlock().setType(Material.AIR);
    }

    public void triggerTrap(PEPlayer player) {
        if (_caughtAPrisoner) {
            return;
        }
        _caughtAPrisoner = true;
        // TODO --- restrict player movement

        delete();
    }


}
