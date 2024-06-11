package net.tiagofar78.prisonescape.game.prisonbuilding.placeables;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import net.tiagofar78.prisonescape.bukkit.BukkitWorldEditor;
import net.tiagofar78.prisonescape.game.PrisonEscapePlayer;
import net.tiagofar78.prisonescape.game.prisonbuilding.PrisonEscapeLocation;

public class Trap {

    private PrisonEscapeLocation _location;
    private boolean _caughtAPrisoner;

    public Trap(PrisonEscapeLocation location) {
        _location = location;
        _caughtAPrisoner = false;
        create();
    }

    public void create() {
        World world = BukkitWorldEditor.getWorld();
        Location location = new Location(world, _location.getX(), _location.getY(), _location.getZ());

        location.getBlock().setType(Material.COBWEB);
    }
    
    public void delete() {
        World world = BukkitWorldEditor.getWorld();
        Location location = new Location(world, _location.getX(), _location.getY(), _location.getZ());
        location.getBlock().setType(Material.AIR);
    }

    public void triggerTrap(PrisonEscapePlayer player) {
        if (_caughtAPrisoner) {
            return;
        }
        _caughtAPrisoner = true;
        // TODO --- restrict player movement

        delete();
    }


}
