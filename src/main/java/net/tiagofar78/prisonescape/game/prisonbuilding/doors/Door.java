package net.tiagofar78.prisonescape.game.prisonbuilding.doors;

import net.tiagofar78.prisonescape.bukkit.BukkitWorldEditor;
import net.tiagofar78.prisonescape.game.PrisonEscapePlayer;
import net.tiagofar78.prisonescape.items.Item;

import org.bukkit.Location;
import org.bukkit.block.Block;

public abstract class Door {

    private boolean _isOpen;

    public Door() {
        _isOpen = false;
    }

    public boolean isOpened() {
        return _isOpen;
    }

    public void open(Location location) {
        _isOpen = true;
        updateDoor(location, true);
    }

    public void close(Location location) {
        _isOpen = false;
        updateDoor(location, false);
    }

    public abstract ClickDoorReturnAction click(PrisonEscapePlayer player, Item itemHeld);

    private static void updateDoor(Location blockLocation, boolean isOpen) {
        Block block = BukkitWorldEditor.getWorld().getBlockAt(blockLocation);

        if (block == null || !(block.getBlockData() instanceof org.bukkit.block.data.type.Door)) {
            throw new IllegalArgumentException("This location should contain a door");
        }

        org.bukkit.block.data.type.Door door = (org.bukkit.block.data.type.Door) block.getBlockData();
        door.setOpen(isOpen);
        block.setBlockData(door);
    }

}
