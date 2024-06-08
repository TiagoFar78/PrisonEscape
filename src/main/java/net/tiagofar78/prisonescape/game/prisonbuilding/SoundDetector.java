package net.tiagofar78.prisonescape.game.prisonbuilding;

import net.tiagofar78.prisonescape.PrisonEscape;
import net.tiagofar78.prisonescape.bukkit.BukkitWorldEditor;
import net.tiagofar78.prisonescape.game.Guard;
import net.tiagofar78.prisonescape.game.PrisonEscapeGame;
import net.tiagofar78.prisonescape.game.PrisonEscapePlayer;
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
import java.util.Random;

public class SoundDetector {

    private static final int CIRCLE_SEGMENTS = 32;
    private static final int UPDATE_TICKS_DELAY = 3;

    private int _index;
    private boolean _isWorking;
    private PrisonEscapeLocation _location;
    private List<PrisonEscapePlayer> _playersInRange;

    private Random _random;
    private int _updateId;

    public SoundDetector(int index, PrisonEscapeLocation location) {
        _index = index;
        _isWorking = true;
        _location = location;
        _playersInRange = new ArrayList<>();

        _random = new Random();
        _updateId = 0;

        createOnWorld();
        createPerimeterParticles();

        PrisonEscapeGame game = GameManager.getGame();
        List<Guard> guards = game.getGuardsTeam().getMembers();
        for (Guard guard : guards) {
            guard.addSoundDetectorLine(calculateValue());
        }
    }

    public boolean isDetectingSound() {
        return _playersInRange.size() != 0;
    }

    public void playerMoved(PrisonEscapePlayer player, PrisonEscapeLocation playerLocation) {
        if (isInRange(playerLocation) && !_playersInRange.contains(player)) {
            _playersInRange.add(player);
            updateValue();
        } else if (!isInRange(playerLocation) && _playersInRange.contains(player)) {
            _playersInRange.remove(player);
            updateValue();
        }
    }

    private boolean isInRange(PrisonEscapeLocation location) {
        double range = ConfigManager.getInstance().getSoundDetectorRange();
        return distance(_location, location) <= range;
    }

    private double distance(PrisonEscapeLocation loc1, PrisonEscapeLocation loc2) {
        double xComponent = Math.pow(loc1.getX() - loc2.getX(), 2);
        double yComponent = Math.pow(loc1.getY() - loc2.getY(), 2);
        double zComponent = Math.pow(loc1.getZ() - loc2.getZ(), 2);

        return Math.sqrt(xComponent + yComponent + zComponent);
    }

    public void delete() {
        _isWorking = false;
        deleteFromWorld();
    }

    private void createOnWorld() {
        World world = BukkitWorldEditor.getWorld();
        Location location = new Location(world, _location.getX(), _location.getY(), _location.getZ());

        location.getBlock().setType(Material.LIGHTNING_ROD);
    }

    private void createPerimeterParticles() {
        World world = BukkitWorldEditor.getWorld();

        double centerX = _location.getX() + 0.5;
        double centerY = _location.getY();
        double centerZ = _location.getZ() + 0.5;
        double radius = ConfigManager.getInstance().getSoundDetectorRange();

        loopCreateParticles(world, centerX, centerY, centerZ, radius);
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
        World world = BukkitWorldEditor.getWorld();
        Location location = new Location(world, _location.getX(), _location.getY(), _location.getZ());
        location.getBlock().setType(Material.AIR);
    }

    private int calculateValue() {
        return isDetectingSound() ? 1 : 0;
    }

    private void updateValue() {
        updateScoreboards(calculateValue());
    }

    private double addRandomNoise(int value) {
        double intensity = 0.1;
        return value + (_random.nextDouble() * 2 - 1) * intensity;
    }

    private void updateScoreboards(int value) {
        _updateId++;
        updateAudioLevelMeters(value, _updateId);
    }

    private void updateAudioLevelMeters(int value, int updateId) {
        PrisonEscapeGame game = GameManager.getGame();
        if (game == null) {
            return;
        }

        List<Guard> guards = game.getGuardsTeam().getMembers();
        for (Guard guard : guards) {
            guard.updateSoundDetectorValue(_index, addRandomNoise(value));
        }

        Bukkit.getScheduler().runTaskLater(PrisonEscape.getPrisonEscape(), new Runnable() {

            @Override
            public void run() {
                if (updateId == _updateId) {
                    updateAudioLevelMeters(value, updateId);
                }
            }

        }, UPDATE_TICKS_DELAY);

    }

}
