package net.tiagofar78.prisonescape;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
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
	
	public static YamlConfiguration getYamlConfiguration() {
		return YamlConfiguration.loadConfiguration(configFile());
	}
	
	private static File configFile() {
		return new File(getPrisonEscape().getDataFolder(), "config.yml");
	}
	
	public static PrisonEscape getPrisonEscape() {
		return (PrisonEscape)Bukkit.getServer().getPluginManager().getPlugin("TF_PrisonEscape");
	}
	
	public static void saveConfiguration(YamlConfiguration config) {
		File configFile = configFile();
		
		try {
			config.save(configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
}