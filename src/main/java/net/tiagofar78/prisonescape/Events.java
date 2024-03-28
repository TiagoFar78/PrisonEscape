package net.tiagofar78.prisonescape;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

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
	
	@EventHandler
	public void playerInteractWithPrison(PlayerInteractEvent e) {
		Block block = e.getClickedBlock();
		if (block == null) {
			return;
		}

		PrisonEscapeGame game = GameManager.getGame();
		if (game == null) {
			return;
		}
		
		PrisonEscapeLocation location = new PrisonEscapeLocation(block.getX(), block.getY(), block.getZ());
		
		if (block.getType() == Material.CHEST) {			
			game.playerInteractWithPrison(e.getPlayer().getName(), location, null);
			e.setCancelled(true);
			return;
		}
	}
	
	@EventHandler
	public void playerLeave(PlayerQuitEvent e) {
		PrisonEscapeGame game = GameManager.getGame();
		if (game == null) {
			return;
		}
		
		game.playerCloseMenu(e.getPlayer().getName());
	}
	
	@EventHandler
	public void playerCloseInventory(InventoryCloseEvent e) {
		PrisonEscapeGame game = GameManager.getGame();
		if (game == null) {
			return;
		}
		
		game.playerCloseMenu(e.getPlayer().getName());
	}
	
}
