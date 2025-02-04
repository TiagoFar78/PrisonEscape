package net.tiagofar78.prisonescape.commands;

import net.tiagofar78.prisonescape.PrisonEscape;
import net.tiagofar78.prisonescape.dataobjects.JoinGameReturnCode;
import net.tiagofar78.prisonescape.game.PEGame;
import net.tiagofar78.prisonescape.managers.GameManager;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class JoinSubcommand implements PrisonEscapeSubcommandExecutor {

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

        if (args.length > 1) {
            sender.sendMessage(messages.getJoinCommandUsage());
            return false;
        }

        String playerName = sender.getName();
        if (GameManager.getPlayerInGame(playerName) != null) {
            sender.sendMessage(messages.getPlayerAlreadyJoinedMessage());
            return true;
        }

        String mapName = args.length == 1 ? args[0] : null;
        JoinGameReturnCode returnCode = GameManager.getJoinableGame(mapName);
        PEGame game = returnCode.getGame();
        if (game == null) {
            if (returnCode.getCode() == 2) {
                sender.sendMessage(messages.getMaxGamesReachedMessage());
                return true;
            } else if (returnCode.getCode() == 3) {
                sender.sendMessage(messages.getInvalidMapNameMessage(args[0]));
                return true;
            }
        }

        game.playerJoin(playerName);
        return true;
    }

}
