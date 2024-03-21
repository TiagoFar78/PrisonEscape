package net.tiagofar78.prisonescape.bukkit;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.tiagofar78.prisonescape.game.PrisonEscapePlayer;

public class BukkitMessageSender {
	
	public static List<String> getOnlinePlayersNames() {
		return Bukkit.getOnlinePlayers().stream().map(p -> p.getName()).toList();
	}
	
	public static void sendChatMessage(PrisonEscapePlayer player, String message) {
		sendChatMessage(player.getName(), message);
	}
	
	public static void sendChatMessage(String playerName, String message) {
		Player bukkitPlayer = Bukkit.getPlayer(playerName);
		if (bukkitPlayer == null || !bukkitPlayer.isOnline()) {
			return;
		}
		
		bukkitPlayer.sendMessage(message);
	}
	
	public static void sendChatMessage(PrisonEscapePlayer player, String[] message) {
		sendChatMessage(player.getName(), message);
	}
	
	public static void sendChatMessage(String playerName, String[] message) {
		Player bukkitPlayer = Bukkit.getPlayer(playerName);
		if (bukkitPlayer == null || !bukkitPlayer.isOnline()) {
			return;
		}
		
		bukkitPlayer.sendMessage(message);
	}
	
	public static void sendChatMessage(PrisonEscapePlayer player, List<String> message) {
		sendChatMessage(player.getName(), message);
	}
	
	public static void sendChatMessage(String playerName, List<String> message) {
		String[] messageArray = message.toArray(new String[0]);
		
		sendChatMessage(playerName, messageArray);
	}

}
