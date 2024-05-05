package net.tiagofar78.prisonescape.game.prisonbuilding;

import net.tiagofar78.prisonescape.game.PrisonEscapePlayer;

import java.util.ArrayList;
import java.util.List;

public class Helicopter {

    private List<PrisonEscapePlayer> _players = new ArrayList<>();
    private boolean _isOnGround = false;

    public void playerEntered(PrisonEscapePlayer player) {
        _players.add(player);
    }

    public List<PrisonEscapePlayer> clear() {
        List<PrisonEscapePlayer> copy = new ArrayList<>(_players);
        _players.clear();
        return copy;
    }

    public boolean isOnGround() {
        return _isOnGround;
    }

    public void departed() {
        _isOnGround = false;
        destroyHelicopter();
    }

    public void landed() {
        _isOnGround = true;
        buildHelicopter();
    }

    private void buildHelicopter() {
        // TODO
    }

    private void destroyHelicopter() {
        // TODO
    }

}
