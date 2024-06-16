package net.tiagofar78.prisonescape.game.phases;

public class Finished extends Phase {

    @Override
    public Phase next() {
        return new Disabled();
    }

    @Override
    public boolean isClockStopped() {
        return true;
    }

    @Override
    public boolean hasGameStarted() {
        return true;
    }

    @Override
    public boolean hasGameEnded() {
        return true;
    }

    @Override
    public boolean isGameDisabled() {
        return false;
    }
}
