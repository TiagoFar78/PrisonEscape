package net.tiagofar78.prisonescape.items;

import org.bukkit.Material;
import org.bukkit.event.Event;

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
    public void use(Event event) {
        // TODO add search interaction to player
    }

}
