package net.tiagofar78.prisonescape.kits;

import net.tiagofar78.prisonescape.game.PrisonEscapeGame;
import net.tiagofar78.prisonescape.game.PrisonEscapeItem;
import net.tiagofar78.prisonescape.managers.GameManager;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
        inv.setItem(SELECT_PRISIONER_ITEM_INDEX, createSelectPrisionersTeamItem(messages));
        inv.setItem(SELECT_POLICE_ITEM_INDEX, createSelectPoliceTeamItem(messages));
        inv.setItem(SELECT_NONE_ITEM_INDEX, createSelectNoneTeamItem(messages));
    }

    private static ItemStack createSelectPrisionersTeamItem(MessageLanguageManager messages) {
        return buildItem(Material.ORANGE_WOOL, messages.getSelectPrisionerTeamItemName());
    }

    private static ItemStack createSelectPoliceTeamItem(MessageLanguageManager messages) {
        return buildItem(Material.BLUE_WOOL, messages.getSelectPoliceTeamItemName());
    }

    private static ItemStack createSelectNoneTeamItem(MessageLanguageManager messages) {
        return buildItem(Material.GRAY_WOOL, messages.getSelectNoneTeamItemName());
    }

    private static ItemStack buildItem(Material type, String name) {
        ItemStack item = new ItemStack(type);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);

        item.setItemMeta(meta);
        return item;
    }

    @EventHandler
    public void playerInteract(PlayerInteractEvent e) {
        PrisonEscapeGame game = GameManager.getGame();
        if (game == null) {
            return;
        }

        String playerName = e.getPlayer().getName();
        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(playerName);

        @SuppressWarnings("deprecation")
        ItemStack item = e.getPlayer().getItemInHand();
        if (item == null || item.getType() == Material.AIR) {
            return;
        }

        PrisonEscapeItem prisonEscapeItem = null;
        if (item.isSimilar(createSelectPrisionersTeamItem(messages))) {
            prisonEscapeItem = PrisonEscapeItem.SELECT_PRISIONER_TEAM;
        } else if (item.isSimilar(createSelectPoliceTeamItem(messages))) {
            prisonEscapeItem = PrisonEscapeItem.SELECT_POLICE_TEAM;
        } else if (item.isSimilar(createSelectNoneTeamItem(messages))) {
            prisonEscapeItem = PrisonEscapeItem.SELECT_NONE_TEAM;
        }

        game.playerInteract(playerName, prisonEscapeItem);
    }

}
