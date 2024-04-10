package net.tiagofar78.prisonescape.items;

import net.tiagofar78.prisonescape.managers.GameManager;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

public class SelectNoneTeamItem extends FunctionalItem {

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
        return Material.GRAY_WOOL;
    }

    @Override
    public void use(PlayerInteractEvent e) {
        GameManager.getGame().playerRemovedTeamPreference(e.getPlayer().getName());
    }

}
