package net.tiagofar78.prisonescape.game.phases;

public class Finished extends Phase {

    @Override
    public Phase next() {
        return null;
    }

    @Override
    public boolean isClockStopped() {
        return false;
    }

    @Override
    public boolean hasGameStarted() {
        return true;
    }

    @Override
    public boolean hasGameEnded() {
        return true;
    }
}
