package net.tiagofar78.prisonescape.menus;

import net.tiagofar78.prisonescape.game.PrisonEscapePlayer;
import net.tiagofar78.prisonescape.items.Item;

public interface Clickable {

    public void open(PrisonEscapePlayer player);

    public void close();

    public boolean isOpened();

    public ClickReturnAction click(PrisonEscapePlayer player, int slot, Item itemHeld, boolean clickedPlayerInv);

}
