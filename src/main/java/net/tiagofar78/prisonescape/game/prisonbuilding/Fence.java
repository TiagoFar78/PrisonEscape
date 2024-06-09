package net.tiagofar78.prisonescape.game.prisonbuilding;

import net.tiagofar78.prisonescape.bukkit.BukkitWorldEditor;
import net.tiagofar78.prisonescape.items.ToolItem;
import net.tiagofar78.prisonescape.items.WireCutterItem;

import org.bukkit.Location;

public class Fence extends Obstacle implements Regenerable {

    private Location _upperCornerLocation;
    private Location _lowerCornerLocation;
    private boolean _isDestroyed;

    public Fence(Location upperCornerLocation, Location lowerCornerLocation) {
        _upperCornerLocation = upperCornerLocation;
        _lowerCornerLocation = lowerCornerLocation;
        _isDestroyed = false;
        generate();
    }

    @Override
    public boolean isEffectiveTool(ToolItem tool) {
        return tool instanceof WireCutterItem;
    }

    @Override
    public boolean contains(Location location) {
        return isBetweenCorners(location.getBlockX(), location.getBlockY(), location.getBlockZ());
    }

    private boolean isBetweenCorners(int x, int y, int z) {
        return _lowerCornerLocation.getX() <= x && x <= _upperCornerLocation.getX() &&
                _lowerCornerLocation.getY() <= y && y <= _upperCornerLocation.getY() &&
                _lowerCornerLocation.getZ() <= z && z <= _upperCornerLocation.getZ();
    }

    @Override
    public void removeFromWorld() {
        BukkitWorldEditor.clear(_upperCornerLocation, _lowerCornerLocation);
        BukkitWorldEditor.fillWithBars(_upperCornerLocation, _upperCornerLocation);
        BukkitWorldEditor.fillWithBars(_lowerCornerLocation, _lowerCornerLocation);
        _isDestroyed = true;
    }

    public void generate() {
        BukkitWorldEditor.fillWithBars(_upperCornerLocation, _lowerCornerLocation);
    }

    @Override
    public void regenerate() {
        if (!_isDestroyed) {
            return;
        }

        generate();
        _isDestroyed = false;
    }

    @Override
    public boolean isDestroyed() {
        return _isDestroyed;
    }

}
