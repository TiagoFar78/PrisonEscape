package net.tiagofar78.prisonescape.game.prisonbuilding;

import net.tiagofar78.prisonescape.bukkit.BukkitScheduler;
import net.tiagofar78.prisonescape.bukkit.BukkitTeleporter;
import net.tiagofar78.prisonescape.game.PrisonEscapeGame;
import net.tiagofar78.prisonescape.game.PrisonEscapePlayer;
import net.tiagofar78.prisonescape.managers.ConfigManager;
import net.tiagofar78.prisonescape.managers.GameManager;

import java.util.ArrayList;
import java.util.List;

public class Helicopter {

    private static final int TICKS_PER_SECOND = 20;

    private PrisonEscapeLocation _upperLocation;
    private PrisonEscapeLocation _lowerLocation;
    private List<PrisonEscapePlayer> _players = new ArrayList<>();
    private boolean _isOnGround = false;

    protected Helicopter(PrisonEscapeLocation upperLocation, PrisonEscapeLocation lowerLocation) {
        _upperLocation = upperLocation;
        _lowerLocation = lowerLocation;
    }

    public boolean contains(PrisonEscapeLocation location) {
        int x = location.getX();
        int y = location.getY();
        int z = location.getZ();

        return _lowerLocation.getX() <= x && x <= _upperLocation.getX() && _lowerLocation
                .getY() <= y && y <= _upperLocation.getY() && _lowerLocation.getZ() <= z && z <= _upperLocation.getZ();
    }

    public boolean isOnGround() {
        return _isOnGround;
    }

    private void buildHelicopter() {
        _isOnGround = true;
        // TODO
    }

    private void destroyHelicopter() {
        _isOnGround = false;
        // TODO
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

        PrisonEscapeGame game = GameManager.getGame();
        for (PrisonEscapePlayer player : _players) {
            game.playerEscaped(player);
        }

        _players.clear();
    }

    public void click(
            PrisonEscapePlayer player,
            boolean isPrisioner,
            PrisonEscapeLocation exitLocation,
            PrisonEscapeLocation joinLocation
    ) {
        if (!isOnGround()) {
            return;
        }

        if (isPrisioner) {
            prisionerClicked(player, joinLocation);
            return;
        }

        policeClicked(exitLocation);
    }

    private void prisionerClicked(PrisonEscapePlayer player, PrisonEscapeLocation joinLocation) {
        if (_players.contains(player)) {
            return;
        }

        _players.add(player);
        BukkitTeleporter.teleport(player, joinLocation);
    }

    private void policeClicked(PrisonEscapeLocation exitLocation) {
        destroyHelicopter();

        for (PrisonEscapePlayer player : _players) {
            BukkitTeleporter.teleport(player, exitLocation);
        }

        _players.clear();
    }

}
