package net.tiagofar78.prisonescape.game.prisonbuilding;

import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
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
import com.sk89q.worldedit.regions.CuboidRegion;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.block.BlockState;

import net.tiagofar78.prisonescape.PEResources;
import net.tiagofar78.prisonescape.PrisonEscape;
import net.tiagofar78.prisonescape.bukkit.BukkitScheduler;
import net.tiagofar78.prisonescape.bukkit.BukkitTeleporter;
import net.tiagofar78.prisonescape.game.PEGame;
import net.tiagofar78.prisonescape.game.PEPlayer;
import net.tiagofar78.prisonescape.game.Prisoner;
import net.tiagofar78.prisonescape.managers.ConfigManager;
import net.tiagofar78.prisonescape.managers.GameManager;

import org.bukkit.Location;
import org.bukkit.Material;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Helicopter {

    private static final String HELICOPTER_SCHEM_NAME = "helicopter.schem";
    private static final int TICKS_PER_SECOND = 20;

    private Location _upperLocation;
    private Location _lowerLocation;
    private List<Prisoner> _players = new ArrayList<>();
    private boolean _isOnGround = false;

    protected Helicopter(Location upperLocation, Location lowerLocation) {
        _upperLocation = upperLocation;
        _lowerLocation = lowerLocation;
    }

    public boolean contains(Location location) {
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        return _lowerLocation.getX() <= x && x <= _upperLocation.getX() && _lowerLocation.getY() <= y &&
                y <= _upperLocation.getY() && _lowerLocation.getZ() <= z && z <= _upperLocation.getZ();
    }

    public boolean isOnGround() {
        return _isOnGround;
    }

    private void buildHelicopter() {
        _isOnGround = true;
        placeOnWorld();
    }

    private void destroyHelicopter() {
        _isOnGround = false;
        removeFromWorld();
    }

    public void call() {
        int helicopterLandDelay = ConfigManager.getInstance().getHelicopterSpawnDelay();
        BukkitScheduler.runSchedulerLater(new Runnable() {

            @Override
            public void run() {
                land();
            }
        }, helicopterLandDelay * TICKS_PER_SECOND);
    }

    private void land() {
        buildHelicopter();

        int helicopterDepartureDelay = ConfigManager.getInstance().getHelicopterDepartureDelay();
        BukkitScheduler.runSchedulerLater(new Runnable() {

            @Override
            public void run() {
                if (isOnGround()) {
                    departed();
                }
            }

        }, helicopterDepartureDelay * TICKS_PER_SECOND);
    }

    public void departed() {
        destroyHelicopter();

        PEGame game = GameManager.getGame();
        for (Prisoner player : _players) {
            game.playerEscaped(player);
        }

        _players.clear();
    }

    public void click(PEPlayer player, Location exitLocation, Location joinLocation) {
        if (!isOnGround()) {
            return;
        }

        if (player.isPrisoner()) {
            prisionerClicked((Prisoner) player, joinLocation);
            return;
        }

        policeClicked(exitLocation);
    }

    private void prisionerClicked(Prisoner player, Location joinLocation) {
        if (_players.contains(player)) {
            return;
        }

        _players.add(player);
        BukkitTeleporter.teleport(player, joinLocation);
    }

    private void policeClicked(Location exitLocation) {
        destroyHelicopter();

        for (PEPlayer player : _players) {
            BukkitTeleporter.teleport(player, exitLocation);
        }

        _players.clear();
    }

    private void placeOnWorld() {
        File file = new File(PrisonEscape.getPrisonEscape().getDataFolder(), HELICOPTER_SCHEM_NAME);
        ClipboardFormat format = ClipboardFormats.findByFile(file);
        try {

            ClipboardReader reader = format.getReader(new FileInputStream(file));
            Clipboard clipboard = reader.read();

            World world = BukkitAdapter.adapt(PEResources.getWorld());
            EditSession editSession = WorldEdit.getInstance().newEditSessionBuilder().world(world).build();
            Operation operation = new ClipboardHolder(clipboard).createPaste(editSession).to(
                    BlockVector3.at(_lowerLocation.getX(), _lowerLocation.getY(), _lowerLocation.getZ())
            ).ignoreAirBlocks(false).build();
            Operations.complete(operation);
            editSession.commit();
            editSession.close();
        } catch (IOException | WorldEditException e) {
            e.printStackTrace();
        }
    }

    private void removeFromWorld() {
        World world = BukkitAdapter.adapt(PEResources.getWorld());
        BlockVector3 min = BlockVector3.at(_lowerLocation.getX(), _lowerLocation.getY(), _lowerLocation.getZ());
        BlockVector3 max = BlockVector3.at(_upperLocation.getX(), _upperLocation.getY(), _upperLocation.getZ());
        CuboidRegion selection = new CuboidRegion(world, min, max);

        BlockState air = BukkitAdapter.adapt(Material.AIR.createBlockData());
        EditSession editSession = WorldEdit.getInstance().newEditSessionBuilder().world(world).build();
        try {
            editSession.setBlocks(selection, air);
            editSession.commit();
            editSession.close();
        } catch (MaxChangedBlocksException e) {
            e.printStackTrace();
        }
    }

}
