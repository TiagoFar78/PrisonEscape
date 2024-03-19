package net.tiagofar78.prisonescape.commands;

import org.bukkit.command.CommandSender;

public interface PrisonEscapeSubcommandExecutor {
	
	public boolean onCommand(CommandSender sender, String label, String[] args);

}
