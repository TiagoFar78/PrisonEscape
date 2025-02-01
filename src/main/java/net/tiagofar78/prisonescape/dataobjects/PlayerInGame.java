package net.tiagofar78.prisonescape.dataobjects;

import net.tiagofar78.prisonescape.game.PEGame;
import net.tiagofar78.prisonescape.game.PEPlayer;

public class PlayerInGame {

    private PEGame _game;
    private PEPlayer _player;

    public PlayerInGame(PEGame game, PEPlayer player) {
        _game = game;
        _player = player;
    }

    public PEGame getGame() {
        return _game;
    }

    public PEPlayer getPlayer() {
        return _player;
    }

}
