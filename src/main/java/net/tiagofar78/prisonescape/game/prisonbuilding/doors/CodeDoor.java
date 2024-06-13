package net.tiagofar78.prisonescape.game.prisonbuilding.doors;

import net.tiagofar78.prisonescape.game.PEGame;
import net.tiagofar78.prisonescape.game.PEPlayer;
import net.tiagofar78.prisonescape.items.Item;
import net.tiagofar78.prisonescape.managers.GameManager;

public class CodeDoor extends Door {

    public CodeDoor() {
        super();
    }

    public ClickDoorReturnAction click(PEPlayer player, Item itemHeld) {
        PEGame game = GameManager.getGame();
        boolean isOpened = isOpened();

        if (game.isGuard(player)) {
            if (isOpened) {
                game.changeDoorCode();
                return ClickDoorReturnAction.CLOSE_DOOR;
            }

            return ClickDoorReturnAction.NOTHING;
        }


        if (game.isPrisoner(player))
            return !isOpened && game.playersHaveDoorCode()
                    ? ClickDoorReturnAction.OPEN_DOOR
                    : ClickDoorReturnAction.NOTHING;

        return ClickDoorReturnAction.IGNORE;
    }
}
