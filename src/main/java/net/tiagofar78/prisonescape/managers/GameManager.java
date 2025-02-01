package net.tiagofar78.prisonescape.managers;

import net.tiagofar78.prisonescape.dataobjects.PlayerInGame;
import net.tiagofar78.prisonescape.game.PEGame;
import net.tiagofar78.prisonescape.game.PEPlayer;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class GameManager {

    private static final int MAPS_DISTANCE = 1000;
    private static final int MAPS_Y_CORD = 0;

    private static int currentId = 0;
    private static PEGame[] games = new PEGame[ConfigManager.getInstance().getMaxGames()];

    public static PEGame getGame(int id) {
        for (PEGame game : games) {
            if (game != null && game.getId() == id) {
                return game;
            }
        }

        return null;
    }

    public static List<Integer> getGamesIds() {
        List<Integer> ids = new ArrayList<>();

        for (PEGame game : games) {
            if (game != null) {
                ids.add(game.getId());
            }
        }

        return ids;
    }

    public static PEGame getJoinableGame() {
        return getJoinableGame(1);
    }

    public static PEGame getJoinableGame(int partySize) {
        int maxPlayers = ConfigManager.getInstance().getMaxPlayers();

        for (PEGame game : games) {
            if (game != null && !game.getCurrentPhase().hasGameStarted() &&
                    game.getPlayersOnLobby().size() + partySize <= maxPlayers) {
                return game;
            }
        }

        return startNewGame();
    }

    public static PlayerInGame getPlayerInGame(String playerName) {
        for (PEGame game : games) {
            if (game != null) {
                PEPlayer player = game.getPEPlayer(playerName);
                if (player != null) {
                    return new PlayerInGame(game, player);
                }
            }
        }

        return null;
    }

    public static PEGame getGamePlayerWas(String playerName) {
        for (PEGame game : games) {
            if (game != null && game.getPrisonerTeam().getMember(playerName) != null || game.getGuardsTeam().getMember(
                    playerName
            ) != null) {
                return game;
            }
        }

        return null;
    }

    /**
     *
     * @return a PEGame instance if successful<br>
     *         null if the max number of simultaneous games was reached
     */
    private static PEGame startNewGame() {
        int freeSlot = getFreeSlot();
        if (freeSlot == -1) {
            return null;
        }

        int mapIndex = 0;
        PEGame game = new PEGame(currentId, null, getReferenceBlock(freeSlot, mapIndex));
        games[freeSlot] = game;
        currentId++;

        return game;
    }

    private static int getFreeSlot() {
        for (int i = 0; i < games.length; i++) {
            if (games[i] == null) {
                return i;
            }
        }

        return -1;
    }

    private static Location getReferenceBlock(int gameIndex, int mapIndex) {
        String worldName = ConfigManager.getInstance().getWorldName();
        return new Location(
                Bukkit.getWorld(worldName),
                mapIndex * MAPS_DISTANCE,
                MAPS_Y_CORD,
                gameIndex * MAPS_DISTANCE
        );
    }

    public static void removeGame(int id) {
        for (int i = 0; i < games.length; i++) {
            if (games[i] != null && games[i].getId() == id) {
                games[i] = null;
            }
        }
    }

}
