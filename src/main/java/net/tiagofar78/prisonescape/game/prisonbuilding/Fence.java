package net.tiagofar78.prisonescape.game.prisonbuilding;

import net.tiagofar78.prisonescape.bukkit.BukkitWorldEditor;
import net.tiagofar78.prisonescape.items.ToolItem;
import net.tiagofar78.prisonescape.items.WireCutterItem;

public class Fence extends Obstacle {

    private PrisonEscapeLocation _upperCornerLocation;
    private PrisonEscapeLocation _lowerCornerLocation;

    public Fence(PrisonEscapeLocation upperCornerLocation, PrisonEscapeLocation lowerCornerLocation) {
        _upperCornerLocation = upperCornerLocation;
        _lowerCornerLocation = lowerCornerLocation;
    }

    @Override
    public boolean isEffectiveTool(ToolItem tool) {
        return tool instanceof WireCutterItem;
    }

    @Override
    public boolean contains(PrisonEscapeLocation location) {
        return isBetweenCorners(location.getX(), location.getY(), location.getZ());
    }

    private boolean isBetweenCorners(int x, int y, int z) {
        return _lowerCornerLocation.getX() <= x && x <= _upperCornerLocation.getX() && _lowerCornerLocation
                .getY() <= y && y <= _upperCornerLocation.getY() && _lowerCornerLocation
                        .getZ() <= z && z <= _upperCornerLocation.getZ();
    }

    @Override
    public void removeFromWorld() {
        BukkitWorldEditor.clear(_upperCornerLocation, _lowerCornerLocation);
    }

}
