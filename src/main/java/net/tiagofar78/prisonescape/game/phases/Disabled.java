package net.tiagofar78.prisonescape.game.phases;

public class Disabled extends Phase {

    @Override
    public Phase next() {
        return null;
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
        return true;
    }

}
