package net.tiagofar78.prisonescape.bukkit;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;

import net.tiagofar78.prisonescape.game.prisonbuilding.PrisonEscapeLocation;
import net.tiagofar78.prisonescape.managers.ConfigManager;

public class BukkitWorldEditor {
	
	private static final int SIGN_INDEX = 1;	
	private static final World WORLD = Bukkit.getWorld(ConfigManager.getInstance().getWorldName());
	
	public static void addSignAboveVault(PrisonEscapeLocation location, String text) {
		Location bukkitLocation = new Location(WORLD, location.getX(), location.getY() + 1, location.getZ());
		bukkitLocation.getBlock().setType(Material.OAK_WALL_SIGN);
		Sign sign = (Sign) bukkitLocation.getBlock().getState();
		sign.getSide(Side.FRONT).setLine(SIGN_INDEX, text);
		sign.update();
	}
	
	public static void addVault(PrisonEscapeLocation location) {
		Location bukkitLocation = new Location(WORLD, location.getX(), location.getY(), location.getZ());
		
		Block block = bukkitLocation.getBlock();
		block.setType(Material.CHEST);
	}
	
	public static void deleteVaultAndRespectiveSign(PrisonEscapeLocation location) {
		Location bukkitLocation = new Location(WORLD, location.getX(), location.getY(), location.getZ());
		
		bukkitLocation.getBlock().setType(Material.AIR);;
	}

}
