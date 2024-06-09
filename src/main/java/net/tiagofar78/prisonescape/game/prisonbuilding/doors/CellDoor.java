package net.tiagofar78.prisonescape.game.prisonbuilding.doors;

import net.tiagofar78.prisonescape.game.PEPlayer;
import net.tiagofar78.prisonescape.items.Item;

import org.bukkit.Location;

public class CellDoor extends Door {

    private Location _location;

    public CellDoor(Location location) {
        _location = location;
    }

    public void open() {
        super.open(_location);
    }

    public void close() {
        super.close(_location);
    }

    @Override
    public ClickDoorReturnAction click(PEPlayer player, Item itemHeld) {
        return ClickDoorReturnAction.NOTHING;
    }

}
