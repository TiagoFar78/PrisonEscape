package net.tiagofar78.prisonescape.items;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class AntenaItem extends Item implements Craftable {

    @Override
    public boolean isMetalic() {
        return false;
    }

    @Override
    public boolean isIllegal() {
        return true;
    }

    @Override
    public Material getMaterial() {
        return Material.END_ROD;
    }

    @Override
    public List<Item> getCratingItems() {
        List<Item> items = new ArrayList<>();

        items.add(new PlasticSpoonItem());
        items.add(new MatchesItem());

        return items;
    }

}
