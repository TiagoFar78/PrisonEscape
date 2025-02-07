package net.tiagofar78.prisonescape.kits;

import net.tiagofar78.prisonescape.game.PEGame;
import net.tiagofar78.prisonescape.game.PEPlayer;
import net.tiagofar78.prisonescape.items.Item;
import net.tiagofar78.prisonescape.items.SelectNoneTeamItem;
import net.tiagofar78.prisonescape.items.SelectPoliceTeamItem;
import net.tiagofar78.prisonescape.items.SelectPrisonerTeamItem;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Hashtable;

public class TeamSelectorKit extends Kit {

    private static final int SELECT_PRISIONER_ITEM_INDEX = 1;
    private static final int SELECT_GUARD_ITEM_INDEX = 7;
    private static final int SELECT_NONE_ITEM_INDEX = 4;

    @Override
    protected Hashtable<Integer, Item> getContents() {
        Hashtable<Integer, Item> items = new Hashtable<>();

        items.put(SELECT_PRISIONER_ITEM_INDEX, new SelectPrisonerTeamItem());
        items.put(SELECT_GUARD_ITEM_INDEX, new SelectPoliceTeamItem());
        items.put(SELECT_NONE_ITEM_INDEX, new SelectNoneTeamItem());

        return items;
    }

    @Override
    public void update(PEGame game, PEPlayer player) {
        Player bukkitPlayer = Bukkit.getPlayer(player.getName());
        if (bukkitPlayer == null || !bukkitPlayer.isOnline()) {
            return;
        }

        ItemStack selectPrsionerItem = getItemAt(SELECT_PRISIONER_ITEM_INDEX).toItemStack(game, player);
        ItemStack selectGuardItem = getItemAt(SELECT_GUARD_ITEM_INDEX).toItemStack(game, player);
        ItemStack selectNoneItem = getItemAt(SELECT_NONE_ITEM_INDEX).toItemStack(game, player);

        Inventory inv = bukkitPlayer.getInventory();
        inv.setItem(SELECT_PRISIONER_ITEM_INDEX, selectPrsionerItem);
        inv.setItem(SELECT_GUARD_ITEM_INDEX, selectGuardItem);
        inv.setItem(SELECT_NONE_ITEM_INDEX, selectNoneItem);
    }

}
