package net.tiagofar78.prisonescape.game.prisonbuilding;

import net.tiagofar78.prisonescape.bukkit.BukkitWorldEditor;
import net.tiagofar78.prisonescape.items.ToolItem;
import net.tiagofar78.prisonescape.items.WrenchItem;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.data.Bisected.Half;
import org.bukkit.block.data.type.TrapDoor;

public class Vent extends Obstacle {

    private Location _location;

    public Vent(Location location) {
        _location = location;
    }

    @Override
    public boolean isEffectiveTool(ToolItem tool) {
        return tool instanceof WrenchItem;
    }

    @Override
    public boolean contains(Location location) {
        return location.equals(_location);
    }

    @Override
    public void removeFromWorld() {
        World world = BukkitWorldEditor.getWorld();
        world.getBlockAt(_location).setType(Material.AIR);
    }

    public void generate() {
        World world = BukkitWorldEditor.getWorld();
        Block block = world.getBlockAt(_location);
        block.setType(Material.IRON_TRAPDOOR);
        TrapDoor trapdoor = (TrapDoor) block.getBlockData();
        trapdoor.setHalf(Half.TOP);

        block.setBlockData(trapdoor);
    }

}
