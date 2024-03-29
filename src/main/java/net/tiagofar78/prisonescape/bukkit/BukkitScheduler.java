package net.tiagofar78.prisonescape.bukkit;

import net.tiagofar78.prisonescape.PrisonEscape;

import org.bukkit.Bukkit;

public class BukkitScheduler {

    public static void runSchedulerLater(Runnable runnable, long delay) {
        Bukkit.getScheduler().runTaskLater(PrisonEscape.getPrisonEscape(), runnable, delay);
    }

}
