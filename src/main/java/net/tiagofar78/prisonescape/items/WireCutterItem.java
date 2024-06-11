package net.tiagofar78.prisonescape.items;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.List;

public class WireCutterItem extends ToolItem implements Craftable {

    public WireCutterItem() {

    }

    public WireCutterItem(boolean useRandomDurability) {
        super(useRandomDurability);
    }

    @Override
    protected int usesAmount() {
        return 5;
    }

    @Override
    public int damageToBlock() {
        return 20;
    }

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
        return Material.SHEARS;
    }

    @Override
    public List<Item> getCratingItems() {
        List<Item> items = new ArrayList<>();

        items.add(new BoltsItem());
        items.add(new MatchesItem());
        items.add(new DuctTapeItem());

        return items;
    }

}
