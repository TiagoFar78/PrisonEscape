package net.tiagofar78.prisonescape.menus;

import net.tiagofar78.prisonescape.game.PrisonEscapePlayer;
import net.tiagofar78.prisonescape.items.Item;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

import org.bukkit.inventory.Inventory;

public interface Clickable {

    public default void close(PrisonEscapePlayer player) {
        // Nothing
    }

    public abstract Inventory toInventory(MessageLanguageManager messages);

    public default void updateInventory(Inventory inv, PrisonEscapePlayer player) {
        // Nothing
    }

    public abstract ClickReturnAction click(
            PrisonEscapePlayer player,
            int slot,
            Item itemHeld,
            boolean clickedPlayerInv
    );

}
