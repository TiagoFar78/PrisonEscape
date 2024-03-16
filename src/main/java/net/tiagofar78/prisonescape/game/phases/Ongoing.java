package net.tiagofar78.prisonescape.game.phases;

public class Ongoing extends Phase {

    @Override
    public Phase next() {
        return new Finished();
    }

    @Override
    public boolean isClockStopped() {
        return true;
    }

    @Override
    public boolean hasGameStarted() {
        return true;
    }
}
