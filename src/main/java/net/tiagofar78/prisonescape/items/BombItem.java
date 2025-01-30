package net.tiagofar78.prisonescape.items;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.player.PlayerInteractEvent;

import net.tiagofar78.prisonescape.PEResources;
import net.tiagofar78.prisonescape.game.PEGame;
import net.tiagofar78.prisonescape.game.PEPlayer;

public class BombItem extends FunctionalItem implements Craftable {

    private static final int EXPLOSION_TICKS = 20 * 4;

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
    public void use(PEGame game, PEPlayer peplayer, PlayerInteractEvent e) {
        Block block = e.getClickedBlock();
        if (block == null) {
            return;
        }

        Location blockLoc = getPlacedBlockLocation(block.getLocation(), e.getBlockFace());

        TNTPrimed bomb = (TNTPrimed) PEResources.getWorld().spawn(blockLoc, TNTPrimed.class);
        bomb.setFuseTicks(EXPLOSION_TICKS);

        Player player = e.getPlayer();
        bomb.setSource(player);
        
        game.getPEPlayer(player.getName()).removeItem(player.getInventory().getHeldItemSlot());
    }

    private Location getPlacedBlockLocation(Location blockLocation, BlockFace face) {
        if (face.isCartesian() ) {
            return blockLocation.add(face.getModX(), face.getModY(), face.getModZ());
        }

        return null;
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
