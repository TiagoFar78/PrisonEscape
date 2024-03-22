package net.tiagofar78.prisonescape;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class PrisonEscape extends JavaPlugin {
	
	public static final String PLAY_PERMISSION = "TF_PrisonEscape.Play";
	public static final String ADMIN_PERMISSION = "TF_PrionEscape.PERMISSION";
	
	@Override
	public void onEnable() {		
		if (!new File(getDataFolder(), "config.yml").exists()) {
			saveDefaultConfig();
		}
	}
	
	public static PrisonEscape getPrisonEscape() {
		return (PrisonEscape)Bukkit.getServer().getPluginManager().getPlugin("TF_PrisonEscape");
	}
	
}