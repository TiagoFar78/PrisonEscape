package net.tiagofar78.prisonescape.bukkit;

import net.tiagofar78.prisonescape.PEResources;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.MultipleFacing;

public class BukkitWorldEditor {

//  ########################################
//  #                 Time                 #
//  ########################################

    private static final int DAY_START_TIME = 0;
    private static final int NIGHT_START_TIME = 13000;

    public static void changeTimeToDay() {
        PEResources.getWorld().setTime(DAY_START_TIME);
    }

    public static void changeTimeToNight() {
        PEResources.getWorld().setTime(NIGHT_START_TIME);
    }

//  ########################################
//  #                 Wall                 #
//  ########################################

    private static final Material DEFAULT_BLOCK = Material.STONE_BRICKS;
    public static final Material CRACKED_BLOCK = Material.CRACKED_STONE_BRICKS;

    public static void buildWall(Location loc1, Location loc2) {
        int lowerX;
        int higherX;
        if (loc1.getBlockX() >= loc2.getBlockX()) {
            higherX = loc1.getBlockX();
            lowerX = loc2.getBlockX();
        } else {
            higherX = loc2.getBlockX();
            lowerX = loc1.getBlockX();
        }

        int lowerZ;
        int higherZ;
        if (loc1.getBlockZ() >= loc2.getBlockZ()) {
            higherZ = loc1.getBlockZ();
            lowerZ = loc2.getBlockZ();
        } else {
            higherZ = loc2.getBlockZ();
            lowerZ = loc1.getBlockZ();
        }

        int lowerY;
        int higherY;
        if (loc1.getBlockY() >= loc2.getBlockY()) {
            higherY = loc1.getBlockY();
            lowerY = loc2.getBlockY();
        } else {
            higherY = loc2.getBlockY();
            lowerY = loc1.getBlockY();
        }

        for (int x = lowerX; x <= higherX; x++) {
            for (int z = lowerZ; z <= higherZ; z++) {
                for (int y = lowerY; y <= higherY; y++) {
                    PEResources.getWorld().getBlockAt(x, y, z).setType(DEFAULT_BLOCK);
                }
            }
        }
    }

    public static void putCrackOnWall(Location loc) {
        putCrackOnWall(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    public static void putCrackOnWall(int x, int y, int z) {
        PEResources.getWorld().getBlockAt(x, y, z).setType(CRACKED_BLOCK);
    }

    public static void fixCrack(Location location) {
        PEResources.getWorld().getBlockAt(location).setType(DEFAULT_BLOCK);
    }

    public static void removeWallBlock(Location location) {
        PEResources.getWorld().getBlockAt(location).setType(Material.AIR);
    }

//  ########################################
//  #                 Maze                 #
//  ########################################

    public static void fillMazeWithDirt(Location upperCorner, Location lowerCorner) {
        fill(upperCorner, lowerCorner, Material.DIRT);
    }

    public static void clearMazePart(Location upperCorner, Location lowerCorner) {
        fill(upperCorner, lowerCorner, Material.AIR);
    }

    public static void raiseMazeWall(Location upperCorner, Location lowerCorner) {
        fill(upperCorner, lowerCorner, Material.COBBLESTONE);
    }

    public static boolean isDirtBlock(int x, int y, int z) {
        return PEResources.getWorld().getBlockAt(x, y, z).getType() == Material.DIRT;
    }

    public static void clearDirtFromMazePart(Location upperCorner, Location lowerCorner) {
        for (int x = lowerCorner.getBlockX(); x <= upperCorner.getBlockX(); x++) {
            for (int y = lowerCorner.getBlockY(); y <= upperCorner.getBlockY(); y++) {
                for (int z = lowerCorner.getBlockZ(); z <= upperCorner.getBlockZ(); z++) {
                    Block block = PEResources.getWorld().getBlockAt(x, y, z);
                    if (block.getType() == Material.DIRT) {
                        block.setType(Material.AIR);
                    }
                }
            }
        }
    }

//  ########################################
//  #               Obstacle               #
//  ########################################

    public static void fillWithBars(Location upperCorner, Location lowerCorner) {
        fill(upperCorner, lowerCorner, Material.IRON_BARS);
        connectBars(upperCorner, lowerCorner);
    }

    private static void connectBars(Location upperCorner, Location lowerCorner) {
        for (int x = lowerCorner.getBlockX(); x <= upperCorner.getBlockX(); x++) {
            for (int y = lowerCorner.getBlockY(); y <= upperCorner.getBlockY(); y++) {
                for (int z = lowerCorner.getBlockZ(); z <= upperCorner.getBlockZ(); z++) {
                    Block block = PEResources.getWorld().getBlockAt(x, y, z);
                    BlockData bd = Bukkit.createBlockData("minecraft:iron_bars");
                    MultipleFacing ironBars = (MultipleFacing) bd;

                    if (!isAir(block.getRelative(BlockFace.NORTH))) {
                        ironBars.setFace(BlockFace.NORTH, true);
                    }
                    if (!isAir(block.getRelative(BlockFace.SOUTH))) {
                        ironBars.setFace(BlockFace.SOUTH, true);
                    }
                    if (!isAir(block.getRelative(BlockFace.EAST))) {
                        ironBars.setFace(BlockFace.EAST, true);
                    }
                    if (!isAir(block.getRelative(BlockFace.WEST))) {
                        ironBars.setFace(BlockFace.WEST, true);
                    }

                    block.setBlockData(bd);
                }
            }
        }
    }

//  ########################################
//  #                 Util                 #
//  ########################################

    public static void clear(Location upperCorner, Location lowerCorner) {
        fill(upperCorner, lowerCorner, Material.AIR);
    }

    private static void fill(Location upperCorner, Location lowerCorner, Material type) {
        for (int x = lowerCorner.getBlockX(); x <= upperCorner.getBlockX(); x++) {
            for (int y = lowerCorner.getBlockY(); y <= upperCorner.getBlockY(); y++) {
                for (int z = lowerCorner.getBlockZ(); z <= upperCorner.getBlockZ(); z++) {
                    PEResources.getWorld().getBlockAt(x, y, z).setType(type);
                }
            }
        }
    }

    private static boolean isAir(Block block) {
        return block.getType() == Material.AIR;
    }
}
