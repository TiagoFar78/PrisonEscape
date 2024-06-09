package net.tiagofar78.prisonescape.kits;

import net.tiagofar78.prisonescape.items.Item;
import net.tiagofar78.prisonescape.items.SelectNoneTeamItem;
import net.tiagofar78.prisonescape.items.SelectPoliceTeamItem;
import net.tiagofar78.prisonescape.items.SelectPrisonerTeamItem;

import java.util.Hashtable;

public class TeamSelectorKit extends Kit {

    private static final int SELECT_PRISIONER_ITEM_INDEX = 1;
    private static final int SELECT_POLICE_ITEM_INDEX = 7;
    private static final int SELECT_NONE_ITEM_INDEX = 4;

    @Override
    protected Hashtable<Integer, Item> getContents() {
        Hashtable<Integer, Item> items = new Hashtable<>();

        items.put(SELECT_PRISIONER_ITEM_INDEX, new SelectPrisonerTeamItem());
        items.put(SELECT_POLICE_ITEM_INDEX, new SelectPoliceTeamItem());
        items.put(SELECT_NONE_ITEM_INDEX, new SelectNoneTeamItem());

        return items;
    }

}
