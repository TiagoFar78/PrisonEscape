package net.tiagofar78.prisonescape.game.prisonbuilding;

import net.tiagofar78.prisonescape.PEResources;
import net.tiagofar78.prisonescape.bukkit.BukkitWorldEditor;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class WallCrack {

    private static final char CRACK_CHAR = '#';

    private List<Location> _locations;
    private boolean _isExploded;

    public WallCrack(Location location, List<String> format, int xDirection, int zDirection) {
        _locations = new ArrayList<>();
        _isExploded = false;

        int crackLength = format.get(0).length();
        int crackHeight = format.size();

        for (int l = 0; l < crackLength; l++) {
            for (int h = 0; h < crackHeight; h++) {
                if (format.get(crackHeight - 1 - h).charAt(l) == CRACK_CHAR) {
                    int x = location.getBlockX() + xDirection * l;
                    int y = location.getBlockY() + h;
                    int z = location.getBlockZ() + zDirection * l;

                    _locations.add(new Location(PEResources.getWorld(), x, y, z));
                }
            }
        }
    }

    public boolean isExploded() {
        return _isExploded;
    }

    public void exploded() {
        _isExploded = true;
    }

    public void putCrackOnWall() {
        _isExploded = false;

        for (Location crackLocation : _locations) {
            BukkitWorldEditor.putCrackOnWall(crackLocation);
        }
    }

    public int fixCrack() {
        if (!isExploded()) {
            return -1;
        }

        for (Location crackLocation : _locations) {
            BukkitWorldEditor.fixCrack(crackLocation);
        }

        return 0;
    }

    public boolean contains(List<Location> locations) {
        for (Location location : locations) {
            if (contains(location)) {
                return true;
            }
        }

        return false;
    }

    public boolean contains(Location location) {
        return _locations.contains(location);
    }

}
