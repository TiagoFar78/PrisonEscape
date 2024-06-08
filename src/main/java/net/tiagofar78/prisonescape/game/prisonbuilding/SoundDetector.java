package net.tiagofar78.prisonescape.game.prisonbuilding;

import net.tiagofar78.prisonescape.PrisonEscape;
import net.tiagofar78.prisonescape.game.Guard;
import net.tiagofar78.prisonescape.game.PEGame;
import net.tiagofar78.prisonescape.game.PEPlayer;
import net.tiagofar78.prisonescape.managers.ConfigManager;
import net.tiagofar78.prisonescape.managers.GameManager;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public class SoundDetector {

    private static final int MAX_VALUE = 10;
    private static final int MIN_VALUE = 1;
    private static final int CIRCLE_SEGMENTS = 32;

    private int _index;
    private boolean _isWorking;
    private Location _location;
    private List<PEPlayer> _playersInRange;

    public SoundDetector(int index, Location location) {
        _index = index;
        _isWorking = true;
        _location = location;
        _playersInRange = new ArrayList<>();

        createOnWorld();
        createPerimeterParticles();

        PEGame game = GameManager.getGame();
        List<Guard> guards = game.getGuardsTeam().getMembers();
        for (Guard guard : guards) {
            guard.addSoundDetectorLine(MIN_VALUE);
        }
    }

    public boolean isDetectingSound() {
        return _playersInRange.size() != 0;
    }

    public void playerMoved(PEPlayer player, Location playerLocation) {
        if (isInRange(playerLocation) && !_playersInRange.contains(player)) {
            _playersInRange.add(player);
            updateValue();
        } else if (!isInRange(playerLocation) && _playersInRange.contains(player)) {
            _playersInRange.remove(player);
            updateValue();
        }
    }

    private boolean isInRange(Location location) {
        double range = ConfigManager.getInstance().getSoundDetectorRange();
        return _location.distance(location) <= range;
    }

    public void delete() {
        _isWorking = false;
        deleteFromWorld();
    }

    private void createOnWorld() {
        _location.getBlock().setType(Material.LIGHTNING_ROD);
    }

    private void createPerimeterParticles() {
        double centerX = _location.getX() + 0.5;
        double centerY = _location.getY();
        double centerZ = _location.getZ() + 0.5;
        double radius = ConfigManager.getInstance().getSoundDetectorRange();

        loopCreateParticles(_location.getWorld(), centerX, centerY, centerZ, radius);
    }

    private void loopCreateParticles(World world, double centerX, double centerY, double centerZ, double radius) {
        if (!_isWorking) {
            return;
        }

        for (int i = 0; i < CIRCLE_SEGMENTS; i++) {
            double angle = i * Math.PI * 2 / CIRCLE_SEGMENTS;

            double x = centerX + Math.sin(angle) * radius;
            double z = centerZ + Math.cos(angle) * radius;
            world.spawnParticle(Particle.REDSTONE, x, centerY, z, 1, new Particle.DustOptions(Color.YELLOW, 1));
        }

        Bukkit.getScheduler().runTaskLater(PrisonEscape.getPrisonEscape(), new Runnable() {

            @Override
            public void run() {
                loopCreateParticles(world, centerX, centerY, centerZ, radius);
            }

        }, 10);
    }

    private void deleteFromWorld() {
        _location.getBlock().setType(Material.AIR);
    }

    private void updateValue() {
        int value = isDetectingSound() ? MAX_VALUE : MIN_VALUE;

        List<Guard> guards = GameManager.getGame().getGuardsTeam().getMembers();
        for (Guard guard : guards) {
            guard.updateSoundDetectorValue(_index, value);
        }
    }

}
