package net.tiagofar78.prisonescape.game.prisonbuilding.doors;

import net.tiagofar78.prisonescape.game.PrisonEscapeGame;
import net.tiagofar78.prisonescape.game.PrisonEscapePlayer;
import net.tiagofar78.prisonescape.items.GoldenKeyItem;
import net.tiagofar78.prisonescape.items.Item;
import net.tiagofar78.prisonescape.managers.GameManager;

public class GoldenDoor {

    private boolean _isOpen;

    public GoldenDoor() {
        _isOpen = false;
    }

    public boolean isOpened() {
        return _isOpen;
    }

    public void open(PrisonEscapePlayer player) {
        _isOpen = true;
    }

    public void close() {
        _isOpen = false;
    }

    public ClickDoorReturnAction click(PrisonEscapePlayer player, Item itemHeld) {
        PrisonEscapeGame game = GameManager.getGame();

        if (game.isPolice(player))
            return _isOpen ? ClickDoorReturnAction.CLOSE_DOOR : ClickDoorReturnAction.NOTHING;

        if (game.isPrisioner(player))
            return !_isOpen && itemHeld instanceof GoldenKeyItem
                    ? ClickDoorReturnAction.OPEN_DOOR
                    : ClickDoorReturnAction.NOTHING;

        return ClickDoorReturnAction.IGNORE;
    }
}
