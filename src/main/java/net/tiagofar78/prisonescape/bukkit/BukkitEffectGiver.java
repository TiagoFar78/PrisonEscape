package net.tiagofar78.prisonescape.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BukkitEffectGiver {

    private static final int TICKS_PER_SECOND = 20;

    public static void giveSpeedEffect(String playerName, int level, int duration) {
        Player player = Bukkit.getPlayer(playerName);
        if (player == null || !player.isOnline()) {
            return;
        }

        int ticksDuration = duration * TICKS_PER_SECOND;

        player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, ticksDuration, level));
    }

}
