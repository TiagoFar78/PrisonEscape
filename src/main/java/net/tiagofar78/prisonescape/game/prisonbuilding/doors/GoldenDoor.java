package net.tiagofar78.prisonescape.game.prisonbuilding.doors;

import net.tiagofar78.prisonescape.game.PrisonEscapeGame;
import net.tiagofar78.prisonescape.game.PrisonEscapePlayer;
import net.tiagofar78.prisonescape.items.GoldenKeyItem;
import net.tiagofar78.prisonescape.items.Item;
import net.tiagofar78.prisonescape.managers.GameManager;

public class GoldenDoor extends Door {

    public GoldenDoor() {
        super();
    }

    public ClickDoorReturnAction click(PrisonEscapePlayer player, Item itemHeld) {
        PrisonEscapeGame game = GameManager.getGame();
        boolean isOpen = isOpened();

        if (game.isPolice(player))
            return isOpen ? ClickDoorReturnAction.CLOSE_DOOR : ClickDoorReturnAction.NOTHING;

        if (game.isPrisioner(player))
            return !isOpen && itemHeld instanceof GoldenKeyItem
                    ? ClickDoorReturnAction.OPEN_DOOR
                    : ClickDoorReturnAction.NOTHING;

        return ClickDoorReturnAction.IGNORE;
    }
}
