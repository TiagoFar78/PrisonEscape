package net.tiagofar78.prisonescape.bukkit;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import net.tiagofar78.prisonescape.game.PrisonEscapePlayer;

public class BukkitMessageSender {
	
	public static void sendChatMessage(PrisonEscapePlayer player, String message) {
		Player bukkitPlayer = Bukkit.getPlayer(player.getName());
		if (bukkitPlayer == null || !bukkitPlayer.isOnline()) {
			return;
		}
		
		bukkitPlayer.sendMessage(message);
	}
	
	public static void sendChatMessage(PrisonEscapePlayer player, String[] message) {
		Player bukkitPlayer = Bukkit.getPlayer(player.getName());
		if (bukkitPlayer == null || !bukkitPlayer.isOnline()) {
			return;
		}
		
		bukkitPlayer.sendMessage(message);
	}
	
	public static void sendChatMessage(PrisonEscapePlayer player, List<String> message) {
		String[] messageArray = message.toArray(new String[0]);
		
		sendChatMessage(player, messageArray);
	}

}
