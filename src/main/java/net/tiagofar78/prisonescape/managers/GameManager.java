package net.tiagofar78.prisonescape.managers;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardReader;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;

import net.tiagofar78.prisonescape.PEResources;
import net.tiagofar78.prisonescape.PrisonEscape;
import net.tiagofar78.prisonescape.dataobjects.JoinGameReturnCode;
import net.tiagofar78.prisonescape.dataobjects.PlayerInGame;
import net.tiagofar78.prisonescape.game.PEGame;
import net.tiagofar78.prisonescape.game.PEPlayer;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

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

    /**
     *
     * @return code 0 and a game that already exists<br>
     *         code 1 and a newly created game<br>
     *         code 2 and a null game if the max number of simultaneous games was reached<br>
     *         code 3 and a null game if the map name is invalid
     */
    public static JoinGameReturnCode getJoinableGame(String mapName) {
        return getJoinableGame(1, mapName);
    }

    /**
     *
     * @return code 0 and a game that already exists<br>
     *         code 1 and a newly created game<br>
     *         code 2 and a null game if the max number of simultaneous games was reached<br>
     *         code 3 and a null game if the map name is invalid
     */
    public static JoinGameReturnCode getJoinableGame(int partySize, String mapName) {
        ConfigManager config = ConfigManager.getInstance();
        int maxPlayers = config.getMaxPlayers();

        if (mapName != null && !config.getAvailableMaps().contains(mapName)) {
            return new JoinGameReturnCode(3, null);
        }

        for (PEGame game : games) {
            if (game != null && (mapName == null || game.getMapName().equals(mapName)) &&
                    !game.getCurrentPhase().hasGameStarted() &&
                    game.getPlayersOnLobby().size() + partySize <= maxPlayers) {
                return new JoinGameReturnCode(0, game);
            }
        }

        PEGame game = startNewGame(mapName);
        return new JoinGameReturnCode(game != null ? 1 : 2, game);
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
    private static PEGame startNewGame(String mapName) {
        int freeSlot = getFreeSlot();
        if (freeSlot == -1) {
            return null;
        }

        int mapIndex = 0;
        List<String> availableMaps = ConfigManager.getInstance().getAvailableMaps();
        if (mapName == null) {
            mapIndex = new Random().nextInt(availableMaps.size());
            mapName = availableMaps.get(mapIndex);
        } else {
            while (mapIndex < availableMaps.size() && !availableMaps.get(mapIndex).equals(mapName)) {
                mapIndex++;
            }

            if (mapIndex == availableMaps.size()) {
                return null;
            }
        }

        PEGame game = new PEGame(currentId, mapName, getReferenceBlock(freeSlot, mapIndex));
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

    public static void generateMaps() {
        ConfigManager config = ConfigManager.getInstance();
        int maxGames = config.getMaxGames();
        List<String> maps = config.getAvailableMaps();

        for (int x = 0; x < maps.size(); x++) {
            for (int z = 0; z < maxGames; z++) {
                generateMap(x * MAPS_DISTANCE, z * MAPS_DISTANCE, maps.get(x));
            }
        }
    }

    private static void generateMap(int x, int z, String mapName) {
        File file = new File(
                PrisonEscape.getPrisonEscape().getDataFolder() + File.separator + "maps",
                mapName + ".schem"
        );
        if (!file.exists()) {
            throw new IllegalArgumentException("There is no map named " + mapName);
        }

        ClipboardFormat format = ClipboardFormats.findByFile(file);
        try {

            ClipboardReader reader = format.getReader(new FileInputStream(file));
            Clipboard clipboard = reader.read();

            World world = BukkitAdapter.adapt(PEResources.getWorld());
            EditSession editSession = WorldEdit.getInstance().newEditSessionBuilder().world(world).build();
            Operation operation = new ClipboardHolder(clipboard).createPaste(editSession).to(
                    BlockVector3.at(x, MAPS_Y_CORD, z)
            ).ignoreAirBlocks(true).copyBiomes(true).build();
            Operations.complete(operation);
            editSession.commit();
            editSession.close();
        } catch (IOException | WorldEditException e) {
            e.printStackTrace();
        }
    }

}
