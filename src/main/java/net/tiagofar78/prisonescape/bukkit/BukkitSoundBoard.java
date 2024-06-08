package net.tiagofar78.prisonescape.bukkit;

import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.World;

public class BukkitSoundBoard {

    private static final World WORLD = BukkitWorldEditor.getWorld();

    private static final float DEFAULT_VOLUME = 1.0f;
    private static final float DEFAULT_PITCH = 1.0f;

    public static void playMetalDetectorSound(Location location) {
        WORLD.playSound(location, Sound.BLOCK_NOTE_BLOCK_HARP, DEFAULT_VOLUME, DEFAULT_PITCH);
    }

}
