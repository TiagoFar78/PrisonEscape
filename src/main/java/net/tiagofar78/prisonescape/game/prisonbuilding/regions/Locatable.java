package net.tiagofar78.prisonescape.game.prisonbuilding.regions;

import net.tiagofar78.prisonescape.game.prisonbuilding.PrisonEscapeLocation;

public interface Locatable {

    public boolean isInside(PrisonEscapeLocation loc);

}
