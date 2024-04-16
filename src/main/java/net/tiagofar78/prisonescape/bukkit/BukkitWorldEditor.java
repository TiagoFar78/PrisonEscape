package net.tiagofar78.prisonescape.bukkit;

import net.tiagofar78.prisonescape.game.prisonbuilding.PrisonEscapeLocation;
import net.tiagofar78.prisonescape.managers.ConfigManager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.sign.Side;

public class BukkitWorldEditor {

    private static final World WORLD = Bukkit.getWorld(ConfigManager.getInstance().getWorldName());

//  #########################################
//  #                 Vault                 #
//  #########################################

    private static final int SIGN_INDEX = 1;
    
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
        Location vaultLocation = new Location(WORLD, location.getX(), location.getY(), location.getZ());
        Location signLocation = new Location(WORLD, location.getX(), location.getY() + 1, location.getZ());

        vaultLocation.getBlock().setType(Material.AIR);
        signLocation.getBlock().setType(Material.AIR);
    }

//  ########################################
//  #                 Time                 #
//  ########################################
    
    private static final int DAY_START_TIME = 0;
    private static final int NIGHT_START_TIME = 13000;

    public static void changeTimeToDay() {
        WORLD.setTime(DAY_START_TIME);
    }

    public static void changeTimeToNight() {
        WORLD.setTime(NIGHT_START_TIME);
    }

//  ########################################
//  #                 Wall                 #
//  ########################################
    
    private static final Material DEFAULT_BLOCK = Material.STONE_BRICKS;
    private static final Material CRACKED_BLOCK = Material.CRACKED_STONE_BRICKS;
    
    public static void buildWall(PrisonEscapeLocation loc1, PrisonEscapeLocation loc2) {
        int lowerX;
        int higherX;
        if (loc1.getX() >= loc2.getX()) {
            higherX = loc1.getX();
            lowerX = loc2.getX();
        }
        else {
            higherX = loc2.getX();
            lowerX = loc1.getX();
        }
        
        int lowerZ;
        int higherZ;
        if (loc1.getZ() >= loc2.getZ()) {
            higherZ = loc1.getZ();
            lowerZ = loc2.getZ();
        }
        else {
            higherZ = loc2.getZ();
            lowerZ = loc1.getZ();
        }
        
        int lowerY;
        int higherY;
        if (loc1.getY() >= loc2.getY()) {
            higherY = loc1.getY();
            lowerY = loc2.getY();
        }
        else {
            higherY = loc2.getY();
            lowerY = loc1.getY();
        }
        
        for (int x = lowerX; x <= higherX; x++) {
            for (int z = lowerZ; z <= higherZ; z++) {
                for (int y = lowerY; y <= higherY; y++) {
                    WORLD.getBlockAt(x, y, z).setType(DEFAULT_BLOCK);
                }
            }
        }
    }
    
    public static void putCrackOnWall(PrisonEscapeLocation loc) {
        WORLD.getBlockAt(loc.getX(), loc.getY(), loc.getZ()).setType(CRACKED_BLOCK);
    }

}
