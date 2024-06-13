package net.tiagofar78.prisonescape.kits;

import net.tiagofar78.prisonescape.items.CraftingMenuItem;
import net.tiagofar78.prisonescape.items.GlassItem;
import net.tiagofar78.prisonescape.items.Item;

import java.util.Hashtable;

public class PrisonerKit extends Kit {

    private static final int FIRST_GLASS_ITEM_INDEX = 4;
    private static final int LAST_GLASS_ITEM_INDEX = 35;
    private static final int CRAFTING_MENU_INDEX = 8;

    @Override
    public Hashtable<Integer, Item> getContents() {
        Hashtable<Integer, Item> items = new Hashtable<>();

        Item glass = new GlassItem();
        for (int i = FIRST_GLASS_ITEM_INDEX; i <= LAST_GLASS_ITEM_INDEX; i++) {
            items.put(i, glass);
        }

        items.put(CRAFTING_MENU_INDEX, new CraftingMenuItem());

        return items;
    }
}
