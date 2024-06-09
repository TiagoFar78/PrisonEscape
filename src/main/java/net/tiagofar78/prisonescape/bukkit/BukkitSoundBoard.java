package net.tiagofar78.prisonescape.bukkit;

import net.tiagofar78.prisonescape.PEResources;

import org.bukkit.Location;
import org.bukkit.Sound;

public class BukkitSoundBoard {

    private static final float DEFAULT_VOLUME = 1.0f;
    private static final float DEFAULT_PITCH = 1.0f;

    public static void playMetalDetectorSound(Location location) {
        PEResources.getWorld().playSound(location, Sound.BLOCK_NOTE_BLOCK_HARP, DEFAULT_VOLUME, DEFAULT_PITCH);
    }

}
