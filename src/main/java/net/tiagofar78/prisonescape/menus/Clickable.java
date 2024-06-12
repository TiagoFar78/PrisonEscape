package net.tiagofar78.prisonescape.menus;

import net.tiagofar78.prisonescape.game.PEPlayer;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

import org.bukkit.inventory.Inventory;

public interface Clickable {

    public default void close(PEPlayer player) {
        // Nothing
    }

    public abstract Inventory toInventory(MessageLanguageManager messages);

    public default void updateInventory(Inventory inv, PEPlayer player) {
        // Nothing
    }

    public abstract ClickReturnAction click(PEPlayer player, int slot, boolean clickedPlayerInv);

}
