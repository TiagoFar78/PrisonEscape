package net.tiagofar78.prisonescape.dataobjects;

import net.tiagofar78.prisonescape.items.Item;

public class ItemProbability {

    private Item _item;
    private double _probability;

    public ItemProbability(Item item, double probability) {
        _item = item;
        _probability = probability;
    }

    public Item getItem() {
        return _item;
    }

    public double getProbability() {
        return _probability;
    }

}
