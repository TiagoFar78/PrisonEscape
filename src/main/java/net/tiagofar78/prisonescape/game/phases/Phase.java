package net.tiagofar78.prisonescape.game.phases;

import net.tiagofar78.prisonescape.game.PEGame;

public abstract class Phase {

    public abstract Phase next();

    public abstract boolean isClockStopped();

    public abstract boolean hasGameStarted();

    public abstract boolean hasGameEnded();

    public abstract boolean isGameDisabled();

    public abstract void start(PEGame game);

}
