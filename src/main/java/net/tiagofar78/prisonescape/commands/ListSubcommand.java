package net.tiagofar78.prisonescape.commands;

import org.bukkit.command.CommandSender;

import net.tiagofar78.prisonescape.PrisonEscape;
import net.tiagofar78.prisonescape.managers.GameManager;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

public class ListSubcommand implements PrisonEscapeSubcommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, String label, String[] args) {
        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(sender.getName());

        if (!sender.hasPermission(PrisonEscape.ADMIN_PERMISSION)) {
            sender.sendMessage(messages.getNotAllowedMessage());
            return true;
        }
        
        if (args.length != 0) {
            sender.sendMessage(messages.getListCommandUsage());
            return false;
        }
        
        sender.sendMessage(messages.getActiveGamesMessage(GameManager.getGamesIds()));
        return true;
    }

}
