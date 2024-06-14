package net.tiagofar78.prisonescape.game.phases;

public abstract class Phase {

    public abstract Phase next();

    public abstract boolean isClockStopped();

    public abstract boolean hasGameStarted();

    public abstract boolean hasGameEnded();

    public abstract boolean isGameDisabled();

}
