package net.tiagofar78.prisonescape.game.prisonbuilding.doors;

import net.tiagofar78.prisonescape.game.PEGame;
import net.tiagofar78.prisonescape.game.PrisonEscapePlayer;
import net.tiagofar78.prisonescape.items.GrayKeyItem;
import net.tiagofar78.prisonescape.items.Item;
import net.tiagofar78.prisonescape.managers.GameManager;

public class GrayDoor extends Door {

    public ClickDoorReturnAction click(PrisonEscapePlayer player, Item itemHeld) {
        PEGame game = GameManager.getGame();
        boolean isOpened = isOpened();

        if (game.isGuard(player))
            return isOpened ? ClickDoorReturnAction.CLOSE_DOOR : ClickDoorReturnAction.NOTHING;

        if (game.isPrisioner(player))
            return !isOpened && itemHeld instanceof GrayKeyItem
                    ? ClickDoorReturnAction.OPEN_DOOR
                    : ClickDoorReturnAction.NOTHING;

        return ClickDoorReturnAction.IGNORE;
    }
}
