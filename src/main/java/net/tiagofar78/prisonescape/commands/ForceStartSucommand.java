package net.tiagofar78.prisonescape.commands;

import org.bukkit.command.CommandSender;

import net.tiagofar78.prisonescape.PrisonEscape;
import net.tiagofar78.prisonescape.game.PrisonEscapeGame;
import net.tiagofar78.prisonescape.managers.GameManager;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

public class ForceStartSucommand implements PrisonEscapeSubcommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, String label, String[] args) {
		MessageLanguageManager messages = MessageLanguageManager.getInstance(sender.getName());
		
		if (!sender.hasPermission(PrisonEscape.ADMIN_PERMISSION)) {	
			sender.sendMessage(messages.getNotAllowedMessage());
			return true;
		}
		
		if (args.length != 0) {
			sender.sendMessage(messages.getForceStartCommandUsage());
			return false;
		}
		
		PrisonEscapeGame game = GameManager.getGame();
		if (game == null) {
			sender.sendMessage(messages.getGameNotStartedYetMessage());
			return true;
		}
		
		int returnCode = game.forceStart();
		if (returnCode == -1) {
			sender.sendMessage(messages.getGameAlreadyOngoingMessage());
			return true;
		}
		
		sender.sendMessage(messages.getSuccessfullyForceStartedGameMessage());
		return true;
	}

}
