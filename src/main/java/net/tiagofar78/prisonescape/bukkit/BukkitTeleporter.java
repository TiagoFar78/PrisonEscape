package net.tiagofar78.prisonescape.bukkit;

import net.tiagofar78.prisonescape.game.PEPlayer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class BukkitTeleporter {

    private static final double CENTER_OF_BLOCK = 0.5;

    public static void teleport(PEPlayer player, Location loc) {
        Player bukkitPlayer = Bukkit.getPlayer(player.getName());
        if (bukkitPlayer == null || !bukkitPlayer.isOnline()) {
            return;
        }

        bukkitPlayer.teleport(loc.clone().add(CENTER_OF_BLOCK, 0, CENTER_OF_BLOCK));
    }

}
