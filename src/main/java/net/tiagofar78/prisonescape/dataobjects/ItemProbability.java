package net.tiagofar78.prisonescape.dataobjects;

import net.tiagofar78.prisonescape.game.PrisonEscapeItem;

public class ItemProbability {

    private PrisonEscapeItem _item;
    private double _probability;

    public ItemProbability(PrisonEscapeItem item, double probability) {
        _item = item;
        _probability = probability;
    }

    public PrisonEscapeItem getItem() {
        return _item;
    }

    public double getProbability() {
        return _probability;
    }

}
