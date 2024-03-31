package net.tiagofar78.prisonescape.game.prisonbuilding;

import net.tiagofar78.prisonescape.game.PrisonEscapeItem;
import net.tiagofar78.prisonescape.game.PrisonEscapePlayer;

public interface Clickable {

    public void open(PrisonEscapePlayer player);

    public void close();

    public boolean isOpened();

    public int click(PrisonEscapePlayer player, int slot, PrisonEscapeItem itemHeld);

}
