package net.tiagofar78.prisonescape;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import net.tiagofar78.prisonescape.commands.PrisonEscapeCommand;
import net.tiagofar78.prisonescape.items.SelectNoneTeamItem;
import net.tiagofar78.prisonescape.items.SelectPoliceTeamItem;
import net.tiagofar78.prisonescape.items.SelectPrisionerTeamItem;

public class PrisonEscape extends JavaPlugin {

    public static final String PLAY_PERMISSION = "TF_PrisonEscape.Play";
    public static final String ADMIN_PERMISSION = "TF_PrisonEscape.PERMISSION";

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

        PluginManager pluginManager = getServer().getPluginManager();
        pluginManager.registerEvents(new Events(), this);
        for (Listener item : getListenerItems()) {
            pluginManager.registerEvents(item, getPrisonEscape());
        }
    }

    public static PrisonEscape getPrisonEscape() {
        return (PrisonEscape) Bukkit.getServer().getPluginManager().getPlugin("TF_PrisonEscape");
    }
    
    private static List<Listener> getListenerItems() {
        List<Listener> list = new ArrayList<>();
        
        list.add(new SelectPrisionerTeamItem());
        list.add(new SelectPoliceTeamItem());
        list.add(new SelectNoneTeamItem());
        
        return list;
    }

}
