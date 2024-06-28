package net.tiagofar78.prisonescape.game.prisonbuilding;

import net.tiagofar78.prisonescape.PEResources;
import net.tiagofar78.prisonescape.PrisonEscape;
import net.tiagofar78.prisonescape.game.PEPlayer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.World;

public class MetalDetector {

    private final static int RED_BLINKS_COUNT = 6;
    private final static int GREEN_BLINKS_COUNT = 2;
    private final static int RED_BLINK_DELAY_TICKS = 5;
    private final static int GREEN_BLINK_DELAY_TICKS = 40;
    private final static int WARNING_SOUND_COUNT = 4;
    private final static int WARNING_SOUND_DELAY = 3;
    private final static int SAFE_SOUND_COUNT = 2;
    private final static int SAFE_SOUND_DELAY = 3;

    private int _currentAnimationId;
    private Location _lowerCorner;
    private Location _upperCorner;
    private boolean _isOnXAxis;

    public MetalDetector(Location upperCorner, Location lowerCorner) {
        _lowerCorner = lowerCorner;
        _upperCorner = upperCorner;
        _isOnXAxis = lowerCorner.getBlockZ() == upperCorner.getBlockZ();
    }

    public boolean checkIfDetectedAndTrigger(PEPlayer player, Location locTo, Location locFrom) {
        if (!isInside(locTo) || isInside(locFrom)) {
            return false;
        }

        trigger(player);
        return true;
    }

    private boolean isInside(Location loc) {
        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();

        return _lowerCorner.getBlockX() <= x && x <= _upperCorner.getBlockX() &&
                _lowerCorner.getBlockY() <= y && y <= _upperCorner.getBlockY() &&
                _lowerCorner.getBlockZ() <= z && z <= _upperCorner.getBlockZ();
    }

    private void trigger(PEPlayer player) {
        if (player.hasMetalItems()) {
            runMetalDetectedProcess(_currentAnimationId, player);
        } else {
            runMetalNotDetectedProcess(_currentAnimationId, player);
        }
    }

    private void runMetalDetectedProcess(int animationId, PEPlayer player) {
        executeLightChange(animationId, RED_BLINK_DELAY_TICKS, RED_BLINKS_COUNT, Material.RED_STAINED_GLASS);
        executeSound(animationId, WARNING_SOUND_COUNT, WARNING_SOUND_DELAY, player, Sound.BLOCK_NOTE_BLOCK_PLING);
    }

    private void runMetalNotDetectedProcess(int animationId, PEPlayer player) {
        executeLightChange(animationId, GREEN_BLINK_DELAY_TICKS, GREEN_BLINKS_COUNT, Material.GREEN_STAINED_GLASS);
        executeSound(animationId, SAFE_SOUND_COUNT, SAFE_SOUND_DELAY, player, Sound.BLOCK_NOTE_BLOCK_BIT);
    }

    private void executeLightChange(int animationId, int delay, int count, Material material1) {
        executeLightChange(animationId, delay, count, material1, Material.BLACK_STAINED_GLASS);
    }

    private void executeLightChange(int animationId, int delay, int count, Material material1, Material material2) {
        if (count == 0 || animationId != _currentAnimationId) {
            return;
        }

        setGlassesTo(material1);

        Bukkit.getScheduler().runTaskLater(PrisonEscape.getPrisonEscape(), new Runnable() {

            @Override
            public void run() {
                executeLightChange(animationId, delay, count - 1, material2, material1);
            }

        }, delay);
    }

    private void executeSound(int animationId, int count, int delay, PEPlayer player, Sound sound) {
        if (count == 0 || animationId != _currentAnimationId) {
            return;
        }

        player.playSound(sound, 50);

        Bukkit.getScheduler().runTaskLater(PrisonEscape.getPrisonEscape(), new Runnable() {

            @Override
            public void run() {
                executeSound(animationId, count - 1, delay, player, sound);
            }

        }, delay);
    }

    private void setGlassesTo(Material material) {
        World world = PEResources.getWorld();

        int widthMin = _lowerCorner.getBlockX();
        int widthMax = _upperCorner.getBlockX();
        int xMultiplier = 1;
        int zMultiplier = 0;
        if (!_isOnXAxis) {
            widthMin = _lowerCorner.getBlockZ();
            widthMax = _upperCorner.getBlockZ();
            xMultiplier = 0;
            zMultiplier = 1;
        }

        for (int width = 0; width <= widthMax - widthMin; width++) {
            int x = _lowerCorner.getBlockX() + width * xMultiplier;
            int z = _lowerCorner.getBlockZ() + width * zMultiplier;
            world.setType(x, _lowerCorner.getBlockY(), z, material);
        }

        for (int y = _lowerCorner.getBlockY(); y <= _upperCorner.getBlockY(); y++) {
            world.setType(_upperCorner.getBlockX(), y, _upperCorner.getBlockZ(), material);
        }

        for (int width = 0; width <= widthMax - widthMin; width++) {
            int x = _lowerCorner.getBlockX() + width * xMultiplier;
            int z = _lowerCorner.getBlockZ() + width * zMultiplier;
            world.setType(x, _upperCorner.getBlockY(), z, material);
        }

        for (int y = _lowerCorner.getBlockY(); y <= _upperCorner.getBlockY(); y++) {
            world.setType(_lowerCorner.getBlockX(), y, _lowerCorner.getBlockZ(), material);
        }
    }

}
