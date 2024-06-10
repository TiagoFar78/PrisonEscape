package net.tiagofar78.prisonescape.items;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class CircuitBoardItem extends Item implements Craftable {

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
        return Material.REPEATER;
    }

    @Override
    public List<Item> getCratingItems() {
        List<Item> items = new ArrayList<>();

        items.add(new PlasticPlateItem());
        items.add(new CopperItem());
        items.add(new MatchesItem());

        return items;
    }

}
