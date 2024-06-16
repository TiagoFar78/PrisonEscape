package net.tiagofar78.prisonescape.game.phases;

public class Ongoing extends Phase {

    @Override
    public Phase next() {
        return new Finished();
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
        return false;
    }

    @Override
    public boolean isGameDisabled() {
        return false;
    }
}
