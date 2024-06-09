package net.tiagofar78.prisonescape.managers;

import net.tiagofar78.prisonescape.game.PEGame;

public class GameManager {

    private static PEGame game = null;

    public static PEGame getGame() {
        return game;
    }

    /**
     *
     * @return 0 if successful
     *         -1 if there is already a game at the moment
     */
    @SuppressWarnings("deprecation")
    public static int startNewGame() {
        if (game != null) {
            return -1;
        }

        ConfigManager config = ConfigManager.getInstance();
        game = new PEGame(null, config.getReferenceBlock());

        return 0;
    }

    public static void removeGame() {
        game = null;
    }

}
