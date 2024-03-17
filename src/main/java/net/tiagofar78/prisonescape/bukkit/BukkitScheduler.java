package net.tiagofar78.prisonescape.bukkit;

import org.bukkit.Bukkit;

import net.tiagofar78.prisonescape.PrisonEscape;

public class BukkitScheduler {
	
	public static void runSchedulerLater(Runnable runnable, long delay) {
		Bukkit.getScheduler().runTaskLater(PrisonEscape.getPrisonEscape(), runnable, delay);
	}

}
