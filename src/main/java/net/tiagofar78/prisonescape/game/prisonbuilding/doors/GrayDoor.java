package net.tiagofar78.prisonescape.game.prisonbuilding.doors;

import net.tiagofar78.prisonescape.game.PEGame;
import net.tiagofar78.prisonescape.game.PEPlayer;
import net.tiagofar78.prisonescape.items.GrayKeyItem;
import net.tiagofar78.prisonescape.items.Item;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

import org.bukkit.Location;

public class GrayDoor extends Door {

    public GrayDoor(Location location) {
        super(location);
    }

    public ClickDoorReturnAction click(PEPlayer player, Item itemHeld) {
        PEGame game = player.getGame();
        boolean isOpened = isOpened();

        if (game.isGuard(player))
            return isOpened ? ClickDoorReturnAction.CLOSE_DOOR : ClickDoorReturnAction.NOTHING;

        if (game.isPrisoner(player)) {
            if (isOpened) {
                return ClickDoorReturnAction.NOTHING;
            }

            if (itemHeld instanceof GrayKeyItem) {
                return ClickDoorReturnAction.OPEN_DOOR;
            }

            MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(player.getName());
            player.sendChatMessage(messages.getGrayDoorRequirementsMessage());
            return ClickDoorReturnAction.NOTHING;
        }


        return ClickDoorReturnAction.IGNORE;
    }
}
