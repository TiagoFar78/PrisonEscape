package net.tiagofar78.prisonescape.items;

import net.tiagofar78.prisonescape.game.prisonbuilding.PrisonEscapeLocation;
import net.tiagofar78.prisonescape.managers.GameManager;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.block.BlockPlaceEvent;

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
    public void use(BlockPlaceEvent e) {
        Block block = e.getBlock();
        PrisonEscapeLocation location = new PrisonEscapeLocation(block.getX(), block.getY(), block.getZ());
        GameManager.getGame().placeBomb(location);
    }

}
