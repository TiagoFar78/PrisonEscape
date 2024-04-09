package net.tiagofar78.prisonescape.bukkit;

import net.tiagofar78.prisonescape.game.PrisonEscapeGame;
import net.tiagofar78.prisonescape.game.PrisonEscapeItem;
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

    public static PrisonEscapeItem convertToPrisonEscapeItem(ItemStack item) {
        if (item == null) {
            return null;
        }

        switch (item.getType()) {
            case SPYGLASS:
                return PrisonEscapeItem.SEARCH;
            case IRON_BARS:
                return PrisonEscapeItem.HANDCUFS;
            case WOODEN_SHOVEL:
                return PrisonEscapeItem.PLASTIC_SPOON;
            case GOLDEN_SHOVEL:
                return PrisonEscapeItem.METAL_SPOON;
            case STONE_SHOVEL:
                return PrisonEscapeItem.PLASTIC_SHOVEL;
            case DIAMOND_SHOVEL:
                return PrisonEscapeItem.METAL_SHOVEL;
            case TORCH:
                return PrisonEscapeItem.MATCHES;
            case IRON_NUGGET:
                return PrisonEscapeItem.BOLTS;
            case SLIME_BALL:
                return PrisonEscapeItem.DUCTTAPE;
            case STRING:
                return PrisonEscapeItem.WIRE;
            case BOWL:
                return PrisonEscapeItem.PLASTIC_PLATE;
            case FLINT:
                return PrisonEscapeItem.OIL;
            case STICK:
                return PrisonEscapeItem.STICK;
            case POTION:
                return PrisonEscapeItem.COFFEE;
            default:
                return null;
        }
    }

    public static ItemStack convertToItemStack(PrisonEscapeItem item) {
        if (item == null) {
            return null;
        }

        Material material;
        switch (item) {
            case SEARCH:
                material = Material.SPYGLASS;
                break;
            case HANDCUFS:
                material = Material.IRON_BARS;
                break;
            case PLASTIC_SPOON:
                material = Material.WOODEN_SHOVEL;
                break;
            case METAL_SPOON:
                material = Material.GOLDEN_SHOVEL;
                break;
            case PLASTIC_SHOVEL:
                material = Material.STONE_SHOVEL;
                break;
            case METAL_SHOVEL:
                material = Material.DIAMOND_SHOVEL;
                break;
            case MATCHES:
                material = Material.TORCH;
                break;
            case BOLTS:
                material = Material.IRON_NUGGET;
                break;
            case DUCTTAPE:
                material = Material.SLIME_BALL;
                break;
            case WIRE:
                material = Material.STRING;
                break;
            case PLASTIC_PLATE:
                material = Material.BOWL;
                break;
            case OIL:
                material = Material.FLINT;
                break;
            case STICK:
                material = Material.STICK;
                break;
            case COFFEE:
                material = Material.POTION;
                break;
            case ENERGY_DRINK:
                material = Material.POTION;
                break;
            default:
                material = Material.STONE;
                break;
        }

        return new ItemStack(material);
    }

}
