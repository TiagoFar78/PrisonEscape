package net.tiagofar78.prisonescape.items;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class SearchItem extends FunctionalItem {

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
        return Material.SPYGLASS;
    }

    @Override
    public void use(PlayerInteractEntityEvent e) {
        // TODO add search interaction to player
    }

}
