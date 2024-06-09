package net.tiagofar78.prisonescape.items;

import net.tiagofar78.prisonescape.managers.GameManager;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

public class CellPhoneItem extends FunctionalItem {

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
        return Material.GOAT_HORN;
    }

    @Override
    public void use(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        Location loc = player.getLocation();

        GameManager.getGame().playerCalledHelicopter(player.getName(), loc, player.getInventory().getHeldItemSlot());
    }

}
