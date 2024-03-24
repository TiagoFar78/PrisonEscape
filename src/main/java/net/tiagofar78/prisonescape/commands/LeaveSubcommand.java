package net.tiagofar78.prisonescape.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.tiagofar78.prisonescape.PrisonEscape;
import net.tiagofar78.prisonescape.game.PrisonEscapeGame;
import net.tiagofar78.prisonescape.managers.GameManager;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

public class LeaveSubcommand implements PrisonEscapeSubcommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, String label, String[] args) {
        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(sender.getName());

        if (!(sender instanceof Player)) {
            sender.sendMessage(messages.getOnlyPlayersCanUseThisCommandMessage());
            return true;
        }

        if (!sender.hasPermission(PrisonEscape.PLAY_PERMISSION)) {
            sender.sendMessage(messages.getNotAllowedMessage());
            return true;
        }

        if (args.length != 0) {
            sender.sendMessage(messages.getLeaveCommandUsage());
            return false;
        }

        PrisonEscapeGame game = GameManager.getGame();
        if (game == null) {
            sender.sendMessage(messages.getGameNotStartedYetMessage());
            return true;
        }
        
        int returnCode = game.playerLeft(sender.getName());
        if (returnCode == -1) {
            sender.sendMessage(messages.getPlayerNotOnLobbyMessage());
            return true;
        }
        
        sender.sendMessage(messages.getSuccessfullyLeftGameMessage());
        return true;
    }

}
