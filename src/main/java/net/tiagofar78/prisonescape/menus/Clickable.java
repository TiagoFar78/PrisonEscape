package net.tiagofar78.prisonescape.menus;

import net.tiagofar78.prisonescape.game.PEGame;
import net.tiagofar78.prisonescape.game.PEPlayer;

import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;

public interface Clickable {

    public default void close(PEPlayer player) {
        // Nothing
    }

    public abstract Inventory toInventory(PEGame game, PEPlayer player);

    public default void updateInventory(Inventory inv, PEPlayer player) {
        // Nothing
    }

    public abstract ClickReturnAction click(PEPlayer player, int slot, boolean isPlayerInv, ClickType type);

}
