package net.tiagofar78.prisonescape.game.prisonbuilding.doors;

import net.tiagofar78.prisonescape.bukkit.BukkitWorldEditor;
import net.tiagofar78.prisonescape.game.PrisonEscapePlayer;
import net.tiagofar78.prisonescape.game.prisonbuilding.PrisonEscapeLocation;
import net.tiagofar78.prisonescape.items.Item;

import org.bukkit.block.Block;

public abstract class Door {

    private boolean _isOpen;

    public Door() {
        _isOpen = false;
    }

    public boolean isOpened() {
        return _isOpen;
    }

    public void open(PrisonEscapeLocation location) {
        _isOpen = true;
        updateDoor(location, true);
    }

    public void close(PrisonEscapeLocation location) {
        _isOpen = false;
        updateDoor(location, false);
    }

    public abstract ClickDoorReturnAction click(PrisonEscapePlayer player, Item itemHeld);

    private static void updateDoor(PrisonEscapeLocation blockLocation, boolean isOpen) {
        int x = blockLocation.getX();
        int y = blockLocation.getY();
        int z = blockLocation.getZ();
        Block block = BukkitWorldEditor.getWorld().getBlockAt(x, y, z);

        if (block == null || !(block.getBlockData() instanceof Door)) {
            throw new IllegalArgumentException("This location should contain a door");
        }

        org.bukkit.block.data.type.Door door = (org.bukkit.block.data.type.Door) block.getBlockData();
        door.setOpen(isOpen);
        block.setBlockData(door);
    }

}
