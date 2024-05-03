package net.tiagofar78.prisonescape.game.prisonbuilding.doors;

import net.tiagofar78.prisonescape.game.PrisonEscapePlayer;
import net.tiagofar78.prisonescape.items.Item;

public abstract class Door {

    private boolean _isOpen;

    public Door() {
        _isOpen = false;
    }

    public boolean isOpened() {
        return _isOpen;
    }

    public void open() {
        _isOpen = true;
    }

    public void close() {
        _isOpen = false;
    }

    public abstract ClickDoorReturnAction click(PrisonEscapePlayer player, Item itemHeld);
}
