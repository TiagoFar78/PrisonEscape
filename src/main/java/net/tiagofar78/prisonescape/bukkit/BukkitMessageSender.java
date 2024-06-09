package net.tiagofar78.prisonescape.bukkit;

import net.tiagofar78.prisonescape.game.PEPlayer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

public class BukkitMessageSender {

    private static final int TICKS_PER_SECOND = 20;
    private static final int FADE = (int) (1 * TICKS_PER_SECOND);
    private static final int STAY = (int) (3.5 * TICKS_PER_SECOND);

    public static List<String> getOnlinePlayersNames() {
        return Bukkit.getOnlinePlayers().stream().map(p -> p.getName()).toList();
    }

    public static void sendChatMessage(PEPlayer player, String message) {
        sendChatMessage(player.getName(), message);
    }

    public static void sendChatMessage(String playerName, String message) {
        Player bukkitPlayer = Bukkit.getPlayer(playerName);
        if (bukkitPlayer == null || !bukkitPlayer.isOnline()) {
            return;
        }

        bukkitPlayer.sendMessage(message);
    }

    public static void sendChatMessage(PEPlayer player, String[] message) {
        sendChatMessage(player.getName(), message);
    }

    public static void sendChatMessage(String playerName, String[] message) {
        Player bukkitPlayer = Bukkit.getPlayer(playerName);
        if (bukkitPlayer == null || !bukkitPlayer.isOnline()) {
            return;
        }

        bukkitPlayer.sendMessage(message);
    }

    public static void sendChatMessage(PEPlayer player, List<String> message) {
        sendChatMessage(player.getName(), message);
    }

    public static void sendChatMessage(String playerName, List<String> message) {
        String[] messageArray = message.toArray(new String[0]);

        sendChatMessage(playerName, messageArray);
    }

    public static void sendTitleMessage(String playerName, String titleMessage, String subtitleMessage) {
        Player bukkitPlayer = Bukkit.getPlayer(playerName);
        if (bukkitPlayer == null || !bukkitPlayer.isOnline()) {
            return;
        }

        bukkitPlayer.sendTitle(titleMessage, subtitleMessage, FADE, STAY, FADE);
    }

}
