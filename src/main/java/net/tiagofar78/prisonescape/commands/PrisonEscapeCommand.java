package net.tiagofar78.prisonescape.commands;

import java.util.Arrays;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import net.tiagofar78.prisonescape.managers.ConfigManager;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

public class PrisonEscapeCommand implements CommandExecutor {
	
	private static final String START_COMMAND = "start";
	private static final String FORCE_START_COMMAND = "forcestart";
	private static final String JOIN_COMMAND = "join";
	private static final String LEAVE_COMMAND = "leave";
	private static final String REJOIN_COMMAND = "rejoin";
	private static final String STOP_COMMAND = "stop";
	private static final String FORCE_STOP_COMMAND = "forcestop";

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		if (args.length == 0) {
			sendUsage(sender);
			return false;
		}
		
		String subcommand = args[0].toLowerCase();
		String[] newArgs = Arrays.copyOfRange(args, 1, args.length);
		
		PrisonEscapeSubcommandExecutor executor = null;
		
		switch (subcommand) {
		case START_COMMAND:
			executor = new StartSubcommand();
			break;
		case FORCE_START_COMMAND:
			executor = new ForceStartSubcommand();
			break;
		case JOIN_COMMAND:
			executor = new JoinSubcommand();
			break;
		case LEAVE_COMMAND:
			executor = new LeaveSubcommand();
			break;
		case REJOIN_COMMAND:
			executor = new RejoinSubcommand();
			break;
		case STOP_COMMAND:
			executor = new StopSubcommand();
			break;
		case FORCE_STOP_COMMAND:
			executor = new ForceStopSubcommand();
			break;
		}
		
		if (executor == null) {
			sendUsage(sender);
			return false;
		}
		
		executor.onCommand(sender, subcommand, newArgs);
		return true;
	}
	
	private void sendUsage(CommandSender sender) {
		ConfigManager config = ConfigManager.getInstance();
		MessageLanguageManager messages = MessageLanguageManager.getInstance(config.getDefaultLanguage());
		
		sender.sendMessage(messages.getUsage().toArray(new String[0]));
	}

}
