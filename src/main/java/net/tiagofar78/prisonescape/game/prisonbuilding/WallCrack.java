package net.tiagofar78.prisonescape.game.prisonbuilding;

import net.tiagofar78.prisonescape.bukkit.BukkitWorldEditor;

import java.util.ArrayList;
import java.util.List;

public class WallCrack {

    private static final char CRACK_CHAR = '#';

    private List<PrisonEscapeLocation> _locations;
    private boolean _isExploded;

    public WallCrack(PrisonEscapeLocation location, List<String> format, int xDirection, int zDirection) {
        _locations = new ArrayList<>();
        _isExploded = false;

        int crackLength = format.get(0).length();
        int crackHeight = format.size();

        for (int l = 0; l < crackLength; l++) {
            for (int h = 0; h < crackHeight; h++) {
                if (format.get(crackHeight - 1 - h).charAt(l) == CRACK_CHAR) {
                    int x = location.getX() + xDirection * l;
                    int y = location.getY() + h;
                    int z = location.getZ() + zDirection * l;

                    _locations.add(new PrisonEscapeLocation(x, y, z));
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

        for (PrisonEscapeLocation crackLocation : _locations) {
            BukkitWorldEditor.putCrackOnWall(crackLocation);
        }
    }

    public void fixCrack() {
        for (PrisonEscapeLocation crackLocation : _locations) {
            BukkitWorldEditor.fixCrack(crackLocation);
        }
    }

    public boolean contains(List<PrisonEscapeLocation> locations) {
        for (PrisonEscapeLocation location : locations) {
            if (contains(location)) {
                return true;
            }
        }

        return false;
    }

    public boolean contains(PrisonEscapeLocation location) {
        return _locations.contains(location);
    }

}
