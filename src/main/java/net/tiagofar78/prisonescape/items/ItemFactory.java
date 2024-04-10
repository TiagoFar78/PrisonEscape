package net.tiagofar78.prisonescape.items;

import org.bukkit.inventory.ItemStack;

public class ItemFactory {

    private static Item[] items = { /* TODO add all items */ };

    public static Item createItem(ItemStack bukkitItem) {
        for (Item item : items) {
            if (item.matches(bukkitItem)) {
                return item;
            }
        }

        return new NullItem();
    }

}
