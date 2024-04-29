package net.tiagofar78.prisonescape.dataobjects;

public class ItemProbability {

    private String _itemName;
    private double _probability;

    public ItemProbability(String itemName, double probability) {
        _itemName = itemName;
        _probability = probability;
    }

    public String getItemName() {
        return _itemName;
    }

    public double getProbability() {
        return _probability;
    }

}
