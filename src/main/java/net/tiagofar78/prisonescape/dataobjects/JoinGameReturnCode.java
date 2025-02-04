package net.tiagofar78.prisonescape.dataobjects;

import net.tiagofar78.prisonescape.game.PEGame;

public class JoinGameReturnCode {

    private int _code;
    private PEGame _game;

    public JoinGameReturnCode(int code, PEGame game) {
        _code = code;
        _game = game;
    }

    public int getCode() {
        return _code;
    }

    public PEGame getGame() {
        return _game;
    }
}
