package net.tiagofar78.prisonescape.items;

public abstract class ToolItem extends Item {

    private double _durability = 100;

    @Override
    public boolean isTool() {
        return true;
    }

    /**
     * @return true if durability is now 0 or less<br>
     *         false otherwise
     */
    public boolean decreaseDurability() {
        _durability -= (double) 100 / (double) usesAmount();

        return _durability <= 0;
    }

    protected abstract int usesAmount();

    public abstract int damageToBlock();

}
