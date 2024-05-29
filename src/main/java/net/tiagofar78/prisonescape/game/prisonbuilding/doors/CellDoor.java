package net.tiagofar78.prisonescape.game.prisonbuilding.doors;

import net.tiagofar78.prisonescape.game.PrisonEscapePlayer;
import net.tiagofar78.prisonescape.game.prisonbuilding.PrisonEscapeLocation;
import net.tiagofar78.prisonescape.items.Item;

public class CellDoor extends Door {

    private PrisonEscapeLocation _location;

    public CellDoor(PrisonEscapeLocation location) {
        _location = location;
    }

    public void open() {
        super.open(_location);
    }

    public void close() {
        super.close(_location);
    }

    @Override
    public ClickDoorReturnAction click(PrisonEscapePlayer player, Item itemHeld) {
        // Nothing
        return ClickDoorReturnAction.NOTHING;
    }

}
