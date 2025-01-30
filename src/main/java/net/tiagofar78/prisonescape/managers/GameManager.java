package net.tiagofar78.prisonescape.managers;

import java.util.ArrayList;
import java.util.List;

import net.tiagofar78.prisonescape.dataobjects.PlayerInGame;
import net.tiagofar78.prisonescape.game.PEGame;
import net.tiagofar78.prisonescape.game.PEPlayer;

public class GameManager {

    private static int currentId = 0;
    private static List<PEGame> games = new ArrayList<>();
    
    public static PEGame getGame(int id) {
        for (PEGame game : games) {
            if (game.getId() == id) {
                return game;
            }
        }
        
        return null;
    }

    public static List<Integer> getGamesIds() {
        List<Integer> ids = new ArrayList<>();
        
        for (PEGame game : games) {
            ids.add(game.getId());
        }
        
        return ids; 
    }
    
    public static PEGame getJoinableGame() {        
        return getJoinableGame(1);
    }

    public static PEGame getJoinableGame(int partySize) {
        int maxPlayers = ConfigManager.getInstance().getMaxPlayers();
        
        for (PEGame game : games) {
            if (!game.getCurrentPhase().hasGameStarted() && game.getPlayersOnLobby().size() + partySize <= maxPlayers) {
                return game;
            }
        }
        
        return null;
    }
    
    public static PlayerInGame getPlayerInGame(String playerName) {
        for (PEGame game : games) {
            PEPlayer player = game.getPEPlayer(playerName);
            if (player != null) {
                return new PlayerInGame(game, player);
            }
        }
        
        return null;
    }
    
    public static PEGame getGamePlayerWas(String playerName) {
        for (PEGame game : games) {
            if (game.getPrisonerTeam().getMember(playerName) != null || game.getGuardsTeam().getMember(playerName) != null) {
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
    @SuppressWarnings("deprecation")
    public static PEGame startNewGame() {
        ConfigManager config = ConfigManager.getInstance();
        int maxGames = config.getMaxGames();
        if (games.size() >= maxGames) {
            return null;
        }

        PEGame game = new PEGame(currentId, null, config.getReferenceBlock());
        games.add(game);
        currentId++;

        return game;
    }

    public static void removeGame(int id) {
        for (int i = 0; i < games.size(); i++) {
            if (games.get(i).getId() == id) {
                games.remove(i);
            }
        }
    }

}
