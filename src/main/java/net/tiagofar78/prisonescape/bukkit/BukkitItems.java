package net.tiagofar78.prisonescape.bukkit;

import net.tiagofar78.prisonescape.game.PrisonEscapeGame;
import net.tiagofar78.prisonescape.managers.GameManager;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class BukkitItems {

    public static ItemStack createItemStack(Material type) {
        return new ItemStack(type);
    }

    public static void setName(ItemStack item, String name) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);

        item.setItemMeta(meta);
    }

    public static void setLore(ItemStack item, List<String> lore) {
        ItemMeta meta = item.getItemMeta();
        meta.setLore(lore);

        item.setItemMeta(meta);
    }

    @SuppressWarnings("deprecation")
    public static ItemStack getEventItem(PlayerInteractEvent e) {
        return getEventItem(e.getPlayer().getItemInHand());
    }

    @SuppressWarnings("deprecation")
    public static ItemStack getEventItem(PlayerInteractEntityEvent e) {
        return getEventItem(e.getPlayer().getItemInHand());
    }

    private static ItemStack getEventItem(ItemStack item) {
        PrisonEscapeGame game = GameManager.getGame();
        if (game == null) {
            return null;
        }

        if (item == null || item.getType() == Material.AIR) {
            return null;
        }

        return item;
    }

}
