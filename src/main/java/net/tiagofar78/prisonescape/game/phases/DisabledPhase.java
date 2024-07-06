package net.tiagofar78.prisonescape.game.phases;

import net.tiagofar78.prisonescape.game.PEGame;
import net.tiagofar78.prisonescape.game.PEPlayer;
import net.tiagofar78.prisonescape.game.prisonbuilding.PrisonBuilding;
import net.tiagofar78.prisonescape.managers.GameManager;

public class DisabledPhase extends Phase {

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

    @Override
    public void start(PEGame game) {
        for (PEPlayer player : game.getPlayersOnLobby()) {
            game.removePlayerFromGame(player);
        }

        PrisonBuilding prison = game.getPrison();
        prison.deleteVaults();
        prison.deletePlaceables();

        game.getBossBar().removeAll();

        GameManager.removeGame();
    }

}
