package net.tiagofar78.prisonescape.items;

import net.tiagofar78.prisonescape.managers.GameManager;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;

public class BombItem extends FunctionalItem implements Craftable {

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
        GameManager.getGame().placeBomb(blockLoc);
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

    @Override
    public List<Item> getCratingItems() {
        List<Item> items = new ArrayList<>();

        items.add(new CircuitBoardItem());
        items.add(new OilItem());
        items.add(new DuctTapeItem());
        items.add(new BoltsItem());

        return items;
    }

}
