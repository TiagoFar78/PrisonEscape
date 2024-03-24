package net.tiagofar78.prisonescape;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.YamlConfiguration;

public class PrisonEscapeResources {
	
//	########################################
//	#                Config                #
//	########################################
	
	public static YamlConfiguration getYamlConfiguration() {
		return YamlConfiguration.loadConfiguration(configFile());
	}
	
	private static File configFile() {
		return new File(PrisonEscape.getPrisonEscape().getDataFolder(), "config.yml");
	}
	
	public static void saveConfig(YamlConfiguration config) {
		File configFile = configFile();
		
		try {
			config.save(configFile);
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
//	########################################
//	#               Language               #
//	########################################
	
	public static YamlConfiguration getYamlLanguage(String language) {
		return YamlConfiguration.loadConfiguration(languageFile(language));
	}
	
	private static File languageFile(String language) {
		return new File(PrisonEscape.getPrisonEscape().getDataFolder() + File.separator + "languages", language + ".yml");
	}

}
