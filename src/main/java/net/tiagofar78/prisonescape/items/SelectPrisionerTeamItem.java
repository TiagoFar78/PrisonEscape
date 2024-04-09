package net.tiagofar78.prisonescape.items;

import net.tiagofar78.prisonescape.bukkit.BukkitItems;
import net.tiagofar78.prisonescape.managers.GameManager;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class SelectPrisionerTeamItem extends Item implements Listener {

    @Override
    public boolean isMetalic() {
        return false;
    }

    @Override
    public boolean isIllegal() {
        return false;
    }

    @Override
    public Material getMaterial() {
        return Material.ORANGE_WOOL;
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (matches(BukkitItems.getEventItem(e))) {
            GameManager.getGame().playerSelectPrisionersTeam(e.getPlayer().getName());
        }
    }

}
