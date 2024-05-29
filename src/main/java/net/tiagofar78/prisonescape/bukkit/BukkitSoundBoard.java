package net.tiagofar78.prisonescape.bukkit;

import net.tiagofar78.prisonescape.game.prisonbuilding.PrisonEscapeLocation;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;

public class BukkitSoundBoard {

    private static final World WORLD = BukkitWorldEditor.getWorld();

    private static final float DEFAULT_VOLUME = 1.0f;
    private static final float DEFAULT_PITCH = 1.0f;

    public static void playMetalDetectorSound(PrisonEscapeLocation location) {
        Location bukkitLocation = new Location(WORLD, location.getX(), location.getY(), location.getZ());
        WORLD.playSound(bukkitLocation, Sound.BLOCK_NOTE_BLOCK_HARP, DEFAULT_VOLUME, DEFAULT_PITCH);
    }

}
