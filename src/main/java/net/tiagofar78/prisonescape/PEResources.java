package net.tiagofar78.prisonescape;

import net.tiagofar78.prisonescape.managers.ConfigManager;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class PEResources {

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
        return new File(
                PrisonEscape.getPrisonEscape().getDataFolder() + File.separator + "languages",
                language + ".yml"
        );
    }

//  #########################################
//  #                 World                 #
//  #########################################

    private static World world;

    public static boolean initializeWorld() {
        world = Bukkit.getWorld(ConfigManager.getInstance().getWorldName());
        return world != null;
    }

    public static World getWorld() {
        return world;
    }

}
