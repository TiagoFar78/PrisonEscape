package net.tiagofar78.prisonescape.bukkit;

import net.tiagofar78.prisonescape.game.prisonbuilding.PrisonEscapeLocation;
import net.tiagofar78.prisonescape.managers.ConfigManager;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.data.type.Door;
import org.bukkit.block.sign.Side;
import org.bukkit.entity.TNTPrimed;

public class BukkitWorldEditor {

    private static final World WORLD = Bukkit.getWorld(ConfigManager.getInstance().getWorldName());

    public static World getWorld() {
        return WORLD;
    }

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
    public static final Material CRACKED_BLOCK = Material.CRACKED_STONE_BRICKS;
    private static final int EXPLOSION_TICKS = 20 * 4;

    public static void buildWall(PrisonEscapeLocation loc1, PrisonEscapeLocation loc2) {
        int lowerX;
        int higherX;
        if (loc1.getX() >= loc2.getX()) {
            higherX = loc1.getX();
            lowerX = loc2.getX();
        } else {
            higherX = loc2.getX();
            lowerX = loc1.getX();
        }

        int lowerZ;
        int higherZ;
        if (loc1.getZ() >= loc2.getZ()) {
            higherZ = loc1.getZ();
            lowerZ = loc2.getZ();
        } else {
            higherZ = loc2.getZ();
            lowerZ = loc1.getZ();
        }

        int lowerY;
        int higherY;
        if (loc1.getY() >= loc2.getY()) {
            higherY = loc1.getY();
            lowerY = loc2.getY();
        } else {
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
        putCrackOnWall(loc.getX(), loc.getY(), loc.getZ());
    }

    public static void putCrackOnWall(int x, int y, int z) {
        WORLD.getBlockAt(x, y, z).setType(CRACKED_BLOCK);
    }

    public static void fixCrack(PrisonEscapeLocation loc) {
        WORLD.getBlockAt(loc.getX(), loc.getY(), loc.getZ()).setType(DEFAULT_BLOCK);
    }

    public static void removeWallBlock(PrisonEscapeLocation location) {
        int x = location.getX();
        int y = location.getY();
        int z = location.getZ();
        WORLD.getBlockAt(x, y, z).setType(Material.AIR);
    }

    public static void placeTNT(PrisonEscapeLocation location) {
        Location bukkitLocation = new Location(WORLD, location.getX(), location.getY(), location.getZ());
        ((TNTPrimed) WORLD.spawn(bukkitLocation, TNTPrimed.class)).setFuseTicks(EXPLOSION_TICKS);
    }

//  ########################################
//  #                 Maze                 #
//  ########################################

    public static void fillMazeWithDirt(PrisonEscapeLocation upperCorner, PrisonEscapeLocation lowerCorner) {
        fill(upperCorner, lowerCorner, Material.DIRT);
    }

    public static void clearMazePart(PrisonEscapeLocation upperCorner, PrisonEscapeLocation lowerCorner) {
        fill(upperCorner, lowerCorner, Material.AIR);
    }

    public static void raiseMazeWall(PrisonEscapeLocation upperCorner, PrisonEscapeLocation lowerCorner) {
        fill(upperCorner, lowerCorner, Material.COBBLESTONE);
    }

    public static boolean isDirtBlock(int x, int y, int z) {
        return WORLD.getBlockAt(x, y, z).getType() == Material.DIRT;
    }

    public static void clearDirtFromMazePart(PrisonEscapeLocation upperCorner, PrisonEscapeLocation lowerCorner) {
        for (int x = lowerCorner.getX(); x <= upperCorner.getX(); x++) {
            for (int y = lowerCorner.getY(); y <= upperCorner.getY(); y++) {
                for (int z = lowerCorner.getZ(); z <= upperCorner.getZ(); z++) {
                    Block block = WORLD.getBlockAt(x, y, z);
                    if (block.getType() == Material.DIRT) {
                        block.setType(Material.AIR);
                    }
                }
            }
        }
    }

//  ########################################
//  #                 Doors                #
//  ########################################

    private static boolean updateDoor(PrisonEscapeLocation blockLocation, boolean open) {
        int x = blockLocation.getX();
        int y = blockLocation.getY();
        int z = blockLocation.getZ();
        Block block = WORLD.getBlockAt(x, y, z);

        if (block == null || !(block.getBlockData() instanceof Door)) {
            return false;
        }

        Door door = (Door) block.getBlockData();
        door.setOpen(open);
        block.setBlockData(door);

        return true;
    }

    public static boolean closeDoor(PrisonEscapeLocation blockLocation) {
        return updateDoor(blockLocation, false);
    }

    public static boolean openDoor(PrisonEscapeLocation blockLocation) {
        return updateDoor(blockLocation, true);
    }


//  ########################################
//  #               Obstacle               #
//  ########################################

    public static void fillWithBars(PrisonEscapeLocation upperCorner, PrisonEscapeLocation lowerCorner) {
        fill(upperCorner, lowerCorner, Material.IRON_BARS);
    }

    public static void putVent(PrisonEscapeLocation location) {
        WORLD.getBlockAt(location.getX(), location.getY(), location.getZ()).setType(Material.IRON_TRAPDOOR);
    }

//  ########################################
//  #                 Util                 #
//  ########################################

    public static void clear(PrisonEscapeLocation upperCorner, PrisonEscapeLocation lowerCorner) {
        fill(upperCorner, lowerCorner, Material.AIR);
    }

    private static void fill(PrisonEscapeLocation upperCorner, PrisonEscapeLocation lowerCorner, Material type) {
        for (int x = lowerCorner.getX(); x <= upperCorner.getX(); x++) {
            for (int y = lowerCorner.getY(); y <= upperCorner.getY(); y++) {
                for (int z = lowerCorner.getZ(); z <= upperCorner.getZ(); z++) {
                    WORLD.getBlockAt(x, y, z).setType(type);
                }
            }
        }
    }

}
