package net.tiagofar78.prisonescape.menus;

import net.tiagofar78.prisonescape.bukkit.BukkitMenu;
import net.tiagofar78.prisonescape.game.PrisonEscapePlayer;
import net.tiagofar78.prisonescape.items.Buyable;
import net.tiagofar78.prisonescape.items.Item;
import net.tiagofar78.prisonescape.items.NullItem;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

import java.util.List;

public class Shop implements Clickable {

    private boolean _isOpen = false;
    private List<Item> _contents;

    @Override
    public void open(PrisonEscapePlayer player) {
        BukkitMenu.openShop(player.getName());
        _isOpen = true;
    }

    @Override
    public void close() {
        _isOpen = false;
    }

    @Override
    public boolean isOpened() {
        return _isOpen;
    }

    @Override
    public ClickReturnAction click(PrisonEscapePlayer player, int slot, Item itemHeld, boolean clickedPlayerInv) {
        if (clickedPlayerInv) {
            return ClickReturnAction.NOTHING;
        }
        int index = BukkitMenu.convertToIndexChest(slot);
        if (index == -1) {
            return ClickReturnAction.NOTHING;
        }

        Item item = _contents.get(index);
        if (item instanceof NullItem) {
            return ClickReturnAction.NOTHING;
        }

        if (!(item instanceof Buyable)) {
            return ClickReturnAction.NOTHING;
        }
        Buyable buyableItem = (Buyable) item;
        int returnCode = player.buyItem(buyableItem.getPrice());
        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(player.getName());
        if (returnCode == -1) {

            // Send message of not enough money to buy

            return ClickReturnAction.NOTHING;
        } else if (returnCode == -2) {
            // Send message of not limit of object reached
            return ClickReturnAction.NOTHING;
        }

        // Send message successfully bought, current balance is x

        return ClickReturnAction.CHANGE_HOLD_AND_SELECTED;
    }

}
