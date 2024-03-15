package net.tiagofar78.prisonescape.game.phases;

public class Waiting extends Phase {

    @Override
    public Phase next() {
        return new Ongoing();
    }

    @Override
    public boolean isClockStopped() {
        return false;
    }

    @Override
    public boolean hasGameStarted() {
        return false;
    }
}
