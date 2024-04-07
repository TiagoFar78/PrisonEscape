package net.tiagofar78.prisonescape.items;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public interface Clickable extends Listener {

    @EventHandler
    public void click(PlayerInteractEvent e);

}
