package net.tiagofar78.prisonescape.items;

import net.tiagofar78.prisonescape.bukkit.BukkitItems;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public abstract class UsableItem extends Item implements Listener {

    public abstract void use(Player player);

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (matches(BukkitItems.getEventItem(e))) {
            use(e.getPlayer());
        }
    }

}
