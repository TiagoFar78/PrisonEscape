package net.tiagofar78.prisonescape.kits;

import net.tiagofar78.prisonescape.items.GlassItem;
import net.tiagofar78.prisonescape.items.HandcuffsItem;
import net.tiagofar78.prisonescape.items.Item;
import net.tiagofar78.prisonescape.items.MissionsItem;
import net.tiagofar78.prisonescape.items.OpenCamerasItem;
import net.tiagofar78.prisonescape.items.SearchItem;
import net.tiagofar78.prisonescape.items.ShopItem;

import java.util.Hashtable;

public class PoliceKit extends Kit {

    private static final int FIRST_GLASS_ITEM_INDEX = 9;
    private static final int LAST_GLASS_ITEM_INDEX = 35;

    private static final int SEARCH_ITEM_INDEX = 4;
    private static final int HANDCUFF_ITEM_INDEX = 5;
    private static final int CAMERA_ITEM_INDEX = 6;
    private static final int MISSIONS_ITEM_INDEX = 7;
    private static final int SHOP_ITEM_INDEX = 8;

    @Override
    protected Hashtable<Integer, Item> getContents() {
        Hashtable<Integer, Item> items = new Hashtable<>();

        Item glass = new GlassItem();
        for (int i = FIRST_GLASS_ITEM_INDEX; i <= LAST_GLASS_ITEM_INDEX; i++) {
            items.put(i, glass);
        }

        items.put(SEARCH_ITEM_INDEX, new SearchItem());
        items.put(HANDCUFF_ITEM_INDEX, new HandcuffsItem());
        items.put(CAMERA_ITEM_INDEX, new OpenCamerasItem());
        items.put(MISSIONS_ITEM_INDEX, new MissionsItem());
        items.put(SHOP_ITEM_INDEX, new ShopItem());

        return items;
    }

}
