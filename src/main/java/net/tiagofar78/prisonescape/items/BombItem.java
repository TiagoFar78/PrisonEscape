package net.tiagofar78.prisonescape.items;

import net.tiagofar78.prisonescape.game.prisonbuilding.PrisonEscapeLocation;
import net.tiagofar78.prisonescape.managers.GameManager;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.player.PlayerInteractEvent;

public class BombItem extends FunctionalItem {

    @Override
    public boolean isMetalic() {
        return true;
    }

    @Override
    public boolean isIllegal() {
        return true;
    }

    @Override
    public Material getMaterial() {
        return Material.TNT;
    }

    @Override
    public void use(PlayerInteractEvent e) {
        Block block = e.getClickedBlock();
        if (block == null) {
            return;
        }

        Location blockLoc = getPlacedBlockLocation(block.getLocation(), e.getBlockFace());
        PrisonEscapeLocation location = new PrisonEscapeLocation(
                blockLoc.getBlockX(),
                blockLoc.getBlockY(),
                blockLoc.getBlockZ()
        );
        GameManager.getGame().placeBomb(location);
    }

    private Location getPlacedBlockLocation(Location blockLocation, BlockFace face) {
        switch (face) {
            case UP:
                return blockLocation.add(0, 1, 0);
            case DOWN:
                return blockLocation.add(0, -1, 0);
            case NORTH:
                return blockLocation.add(0, 0, -1);
            case SOUTH:
                return blockLocation.add(0, 0, 1);
            case EAST:
                return blockLocation.add(1, 0, 0);
            case WEST:
                return blockLocation.add(-1, 0, 0);
            default:
                return null;
        }
    }

}
