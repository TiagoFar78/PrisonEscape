package net.tiagofar78.prisonescape.items;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class GrayKeyItem extends Item implements Craftable {

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
        return Material.LEVER;
    }

    @Override
    public List<Item> getCratingItems() {
        List<Item> items = new ArrayList<>();

        items.add(new BoltsItem());
        items.add(new DuctTapeItem());
        items.add(new MatchesItem());

        return items;
    }
}
