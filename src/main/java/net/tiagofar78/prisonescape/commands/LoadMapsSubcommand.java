package net.tiagofar78.prisonescape.commands;

import net.tiagofar78.prisonescape.PrisonEscape;
import net.tiagofar78.prisonescape.managers.GameManager;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

import org.bukkit.command.CommandSender;

public class LoadMapsSubcommand implements PrisonEscapeSubcommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, String label, String[] args) {
        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(sender.getName());

        if (!sender.hasPermission(PrisonEscape.ADMIN_PERMISSION)) {
            sender.sendMessage(messages.getNotAllowedMessage());
            return true;
        }

        if (args.length != 0) {
            sender.sendMessage(messages.getLoadMapsCommandUsage());
            return false;
        }

        GameManager.generateMaps();
        sender.sendMessage(messages.getSuccessfullyLoadedMapsMessage());
        return true;
    }

}
