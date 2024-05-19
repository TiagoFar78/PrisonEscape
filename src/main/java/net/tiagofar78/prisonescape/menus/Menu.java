package net.tiagofar78.prisonescape.menus;

import net.tiagofar78.prisonescape.game.PrisonEscapePlayer;
import net.tiagofar78.prisonescape.items.Item;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

import org.bukkit.inventory.Inventory;

public abstract class Menu {

    public abstract Inventory toInventory(MessageLanguageManager messages);

    public abstract void open(PrisonEscapePlayer player);

    public abstract void close();

    public abstract boolean isOpened();

    public abstract ClickReturnAction click(
            PrisonEscapePlayer player,
            int slot,
            Item itemHeld,
            boolean clickedPlayerInv
    );

}
