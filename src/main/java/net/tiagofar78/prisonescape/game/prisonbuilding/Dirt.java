package net.tiagofar78.prisonescape.game.prisonbuilding;

import net.tiagofar78.prisonescape.bukkit.BukkitWorldEditor;
import net.tiagofar78.prisonescape.items.MetalShovelItem;
import net.tiagofar78.prisonescape.items.MetalSpoonItem;
import net.tiagofar78.prisonescape.items.PlasticShovelItem;
import net.tiagofar78.prisonescape.items.PlasticSpoonItem;
import net.tiagofar78.prisonescape.items.ToolItem;

public class Dirt extends Obstacle {
    
    private PrisonEscapeLocation _upperCornerLocation;
    private PrisonEscapeLocation _lowerCornerLocation;
    
    public Dirt(PrisonEscapeLocation upperCornerLocation, PrisonEscapeLocation lowerCornerLocation) {
        _upperCornerLocation = upperCornerLocation;
        _lowerCornerLocation = lowerCornerLocation;
    }

    @Override
    public boolean isEffectiveTool(ToolItem tool) {
        return tool instanceof PlasticSpoonItem || tool instanceof MetalSpoonItem
                || tool instanceof PlasticShovelItem || tool instanceof MetalShovelItem;
    }

    @Override
    public boolean contains(PrisonEscapeLocation location) {
        int x = location.getX();
        int y = location.getY();
        int z = location.getZ();
        
        return isBetweenCorners(x, y, z) && BukkitWorldEditor.isDirtBlock(x, y, z);
    }
    
    private boolean isBetweenCorners(int x, int y, int z) {
        return _lowerCornerLocation.getX() <= x && x <= _upperCornerLocation.getX() &&
                _lowerCornerLocation.getY() <= y && y <= _upperCornerLocation.getY() &&
                _lowerCornerLocation.getZ() <= z && z <= _upperCornerLocation.getZ();
    }

    @Override
    public void removeFromWorld() {
        BukkitWorldEditor.clearDirtFromMazePart(_upperCornerLocation, _lowerCornerLocation);
    }

}
