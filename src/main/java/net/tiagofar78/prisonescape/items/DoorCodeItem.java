package net.tiagofar78.prisonescape.items;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class DoorCodeItem extends Item implements Craftable {

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
        return Material.FILLED_MAP;
    }

    @Override
    public List<Item> getCratingItems() {
        List<Item> items = new ArrayList<>();

        items.add(new NotePartItem());
        items.add(new NotePartItem());
        items.add(new NotePartItem());
        items.add(new NotePartItem());

        return items;
    }

}
