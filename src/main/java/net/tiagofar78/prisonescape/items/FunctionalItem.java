package net.tiagofar78.prisonescape.items;

import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public abstract class FunctionalItem extends Item {

    @Override
    public boolean isFunctional() {
        return true;
    }

    public void use(PlayerInteractEvent e) {
        // Do nothing, is is only useful so children classes can Override the method they want
    }

    public void use(PlayerInteractEntityEvent e) {
        // Do nothing, is is only useful so children classes can Override the method they want
    }

    public void use(EntityDamageByEntityEvent e) {
        // Do nothing, is is only useful so children classes can Override the method they want
    }

    public void use(BlockPlaceEvent e) {
        // Do nothing, is is only useful so children classes can Override the method they want
    }
}
