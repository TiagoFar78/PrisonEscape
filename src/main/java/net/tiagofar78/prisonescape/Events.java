package net.tiagofar78.prisonescape;

import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import net.tiagofar78.prisonescape.game.PrisonEscapeGame;
import net.tiagofar78.prisonescape.game.prisonbuilding.PrisonEscapeLocation;
import net.tiagofar78.prisonescape.managers.GameManager;

public class Events implements Listener {

	@EventHandler
	public void playerMove(PlayerMoveEvent e) {
		PrisonEscapeGame game = GameManager.getGame();
		if (game == null) {
			return;
		}
		
		Location bukkitLocFrom = e.getFrom();
		int xFrom = bukkitLocFrom.getBlockX();
		int yFrom = bukkitLocFrom.getBlockY();
		int zFrom = bukkitLocFrom.getBlockZ();
		
		Location bukkitLoc = e.getTo();
		int x = bukkitLoc.getBlockX();
		int y = bukkitLoc.getBlockY();
		int z = bukkitLoc.getBlockZ();
		
		if (xFrom == x && yFrom == y && zFrom == z) {
			return;
		}
		
		PrisonEscapeLocation location = new PrisonEscapeLocation(x, y, z);
		
		game.playerMove(e.getPlayer().getName(), location);
	}
	
}
