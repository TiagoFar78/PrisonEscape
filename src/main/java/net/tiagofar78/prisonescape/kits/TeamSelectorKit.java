package net.tiagofar78.prisonescape.kits;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;

import net.tiagofar78.prisonescape.items.SelectNoneTeamItem;
import net.tiagofar78.prisonescape.items.SelectPoliceTeamItem;
import net.tiagofar78.prisonescape.items.SelectPrisionerTeamItem;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

public class TeamSelectorKit implements Listener {

    private static final int SELECT_PRISIONER_ITEM_INDEX = 1;
    private static final int SELECT_POLICE_ITEM_INDEX = 7;
    private static final int SELECT_NONE_ITEM_INDEX = 4;

    public static void giveToPlayer(String playerName) {
        Player bukkitPlayer = Bukkit.getPlayer(playerName);
        if (bukkitPlayer == null || !bukkitPlayer.isOnline()) {
            return;
        }

        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(playerName);

        Inventory inv = bukkitPlayer.getInventory();
        inv.clear();
        inv.setItem(SELECT_PRISIONER_ITEM_INDEX, new SelectPrisionerTeamItem().toItemStack(messages));
        inv.setItem(SELECT_POLICE_ITEM_INDEX, new SelectPoliceTeamItem().toItemStack(messages));
        inv.setItem(SELECT_NONE_ITEM_INDEX, new SelectNoneTeamItem().toItemStack(messages));
    }

}
