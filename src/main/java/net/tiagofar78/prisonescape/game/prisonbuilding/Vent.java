package net.tiagofar78.prisonescape.game.prisonbuilding;

import net.tiagofar78.prisonescape.bukkit.BukkitWorldEditor;
import net.tiagofar78.prisonescape.items.ToolItem;
import net.tiagofar78.prisonescape.items.WrenchItem;

public class Vent extends Obstacle {

    private PrisonEscapeLocation _location;

    public Vent(PrisonEscapeLocation location) {
        _location = location;
    }

    @Override
    public boolean isEffectiveTool(ToolItem tool) {
        return tool instanceof WrenchItem;
    }

    @Override
    public boolean contains(PrisonEscapeLocation location) {
        return location.equals(_location);
    }

    @Override
    public void removeFromWorld() {
        BukkitWorldEditor.clear(_location, _location);
    }

}
