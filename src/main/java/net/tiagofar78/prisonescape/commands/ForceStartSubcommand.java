package net.tiagofar78.prisonescape.commands;

import net.tiagofar78.prisonescape.PrisonEscape;
import net.tiagofar78.prisonescape.dataobjects.PlayerInGame;
import net.tiagofar78.prisonescape.game.PEGame;
import net.tiagofar78.prisonescape.managers.GameManager;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

import org.bukkit.command.CommandSender;

public class ForceStartSubcommand implements PrisonEscapeSubcommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, String label, String[] args) {
        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(sender.getName());

        if (!sender.hasPermission(PrisonEscape.ADMIN_PERMISSION)) {
            sender.sendMessage(messages.getNotAllowedMessage());
            return true;
        }

        String playerName = sender.getName();
        PEGame game;

        if (args.length == 0) {
            PlayerInGame playerInGame = GameManager.getPlayerInGame(playerName);
            if (playerInGame == null) {
                sender.sendMessage(messages.getPlayerNotOnLobbyMessage());
                return true;
            }

            game = playerInGame.getGame();
        } else if (args.length == 1) {
            int gameId;
            try {
                gameId = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                sender.sendMessage(messages.getForceStartCommandUsage());
                return true;
            }

            game = GameManager.getGame(gameId);
            if (game == null) {
                sender.sendMessage(messages.getNoGameWithThatIdMessage(gameId));
                sender.sendMessage(messages.getActiveGamesMessage(GameManager.getGamesIds()));
                return true;
            }
        } else {
            sender.sendMessage(messages.getForceStartCommandUsage());
            return false;
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
