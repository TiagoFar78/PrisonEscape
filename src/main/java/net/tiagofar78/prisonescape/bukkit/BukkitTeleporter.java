package net.tiagofar78.prisonescape.bukkit;

import net.tiagofar78.prisonescape.game.PrisonEscapePlayer;
import net.tiagofar78.prisonescape.game.prisonbuilding.PrisonEscapeLocation;
import net.tiagofar78.prisonescape.managers.ConfigManager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class BukkitTeleporter {

    private static final double CENTER_OF_BLOCK = 0.5;

    private static final World WORLD = Bukkit.getWorld(ConfigManager.getInstance().getWorldName());

    public static void teleport(PrisonEscapePlayer player, PrisonEscapeLocation loc) {
        Player bukkitPlayer = Bukkit.getPlayer(player.getName());
        if (bukkitPlayer == null || !bukkitPlayer.isOnline()) {
            return;
        }

        Location bukkitLocation = new Location(
                WORLD,
                loc.getX() + CENTER_OF_BLOCK,
                loc.getY(),
                loc.getZ() + CENTER_OF_BLOCK
        );

        bukkitPlayer.teleport(bukkitLocation);
    }

}
