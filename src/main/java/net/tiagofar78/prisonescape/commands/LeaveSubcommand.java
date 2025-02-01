package net.tiagofar78.prisonescape.commands;

import net.tiagofar78.prisonescape.PrisonEscape;
import net.tiagofar78.prisonescape.dataobjects.PlayerInGame;
import net.tiagofar78.prisonescape.managers.GameManager;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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

        String playerName = sender.getName();
        PlayerInGame playerInGame = GameManager.getPlayerInGame(playerName);
        if (playerInGame == null) {
            sender.sendMessage(messages.getPlayerNotOnLobbyMessage());
            return true;
        }

        playerInGame.getGame().playerLeft(playerName);
        return true;
    }

}
