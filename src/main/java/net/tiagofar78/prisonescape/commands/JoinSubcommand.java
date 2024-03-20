package net.tiagofar78.prisonescape.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.tiagofar78.prisonescape.PrisonEscape;
import net.tiagofar78.prisonescape.game.PrisonEscapeGame;
import net.tiagofar78.prisonescape.managers.GameManager;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

public class JoinSubcommand implements PrisonEscapeSubcommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, String label, String[] args) {
        MessageLanguageManager messages = MessageLanguageManager.getInstance(sender.getName());

        if (!sender.hasPermission(PrisonEscape.PLAY_PERMISSION)) {
            sender.sendMessage(messages.getNotAllowedMessage());
            return true;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage(messages.getOnlyPlayersCanUseThisCommandMessage());
            return true;
        }

        if (args.length != 0) {
            sender.sendMessage(messages.getJoinCommandUsage());
            return false;
        }

        PrisonEscapeGame game = GameManager.getGame();
        if (game == null) {
            sender.sendMessage(messages.getGameNotStartedYetMessage());
            return true;
        }

        Player player = (Player) sender;
        int returnCode = game.playerJoin(player);
        if (returnCode == -1) {
            sender.sendMessage(messages.getPlayerAlreadyJoinedMessage());
            return true;
        }
        else if (returnCode == -2) {
            sender.sendMessage(messages.getGameAlreadyOngoingMessage());
            return true;
        }

        return true;
    }

}
