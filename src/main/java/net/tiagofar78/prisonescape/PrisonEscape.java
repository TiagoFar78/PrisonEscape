package net.tiagofar78.prisonescape;

import net.tiagofar78.prisonescape.commands.PrisonEscapeCommand;
import net.tiagofar78.prisonescape.listener.PlayerListener;
import net.tiagofar78.prisonescape.listener.WorldListener;
import net.tiagofar78.prisonescape.managers.ConfigManager;
import net.tiagofar78.prisonescape.managers.MapManager;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PrisonEscape extends JavaPlugin {

    public static final String PLAY_PERMISSION = "TF_PrisonEscape.Play";
    public static final String ADMIN_PERMISSION = "TF_PrisonEscape.Admin";

    private static final String COMMAND_LABEL = "PrisonEscape";
    private static final List<String> COMMAND_ALIASES = getAliases();

    private static final List<String> getAliases() {
        List<String> aliases = new ArrayList<>();
        aliases.add("PE");
        return aliases;
    }

    @Override
    public void onEnable() {
        if (!new File(getDataFolder(), "config.yml").exists()) {
            saveDefaultConfig();
        }

        getCommand(COMMAND_LABEL).setAliases(COMMAND_ALIASES);
        getCommand(COMMAND_LABEL).setExecutor(new PrisonEscapeCommand());

        getServer().getPluginManager().registerEvents(new PlayerListener(), this);
        getServer().getPluginManager().registerEvents(new WorldListener(), this);

        loadResourcesAndManagers();
    }

    public static PrisonEscape getPrisonEscape() {
        return (PrisonEscape) Bukkit.getServer().getPluginManager().getPlugin("TF_PrisonEscape");
    }

    private void loadResourcesAndManagers() {
        if (!ConfigManager.load()) {
            Bukkit.getLogger().severe("[TF_PrisonEscape] Could not load config");
            Bukkit.getPluginManager().disablePlugin(this);
        }

        if (!MapManager.load()) {
            Bukkit.getLogger().severe("[TF_PrisonEscape] Could not load maps");
            Bukkit.getPluginManager().disablePlugin(this);
        }

        if (!MessageLanguageManager.load()) {
            Bukkit.getLogger().severe("[TF_PrisonEscape] Could not load messages");
            Bukkit.getPluginManager().disablePlugin(this);
        }

        if (!PEResources.initializeWorld()) {
            Bukkit.getLogger().severe("[TF_PrisonEscape] Could not load resources");
            Bukkit.getPluginManager().disablePlugin(this);
        }
    }

}
