package net.tiagofar78.prisonescape.commands;

import org.bukkit.command.CommandSender;

import net.tiagofar78.prisonescape.PrisonEscape;
import net.tiagofar78.prisonescape.game.PrisonEscapeGame;
import net.tiagofar78.prisonescape.managers.GameManager;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

public class ForceStopSubcommand implements PrisonEscapeSubcommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, String label, String[] args) {
        MessageLanguageManager messages = MessageLanguageManager.getInstance(sender.getName());

        if (!sender.hasPermission(PrisonEscape.ADMIN_PERMISSION)) {
            sender.sendMessage(messages.getNotAllowedMessage());
            return true;
        }

        if (args.length != 0) {
            sender.sendMessage(messages.getForceStopCommandUsage());
            return false;
        }

        PrisonEscapeGame game = GameManager.getGame();
        if (game == null) {
            sender.sendMessage(messages.getGameNotStartedYetMessage());
            return true;
        }

        game.forceStop();

        sender.sendMessage(messages.getSuccessfullyForceStoppedGameMessage());
        return true;
    }

}