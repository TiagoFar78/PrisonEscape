package net.tiagofar78.prisonescape.managers;

import net.tiagofar78.prisonescape.game.PrisonEscapeItem;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.Inventory;

public class InventoryManager {

    Inventory _inventory;

    public InventoryManager(Inventory inventory) {
        this._inventory = inventory;
    }

    private ItemStack convertToItemStack(PrisonEscapeItem item) {
        ItemStack itemStack;

        Material material;
        switch (item) {
            case SEARCH:
                material = Material.MAP;
                break;
            case HANDCUFS:
                material = Material.IRON_BARS;
                break;
            case SPOON:
                material = Material.WOODEN_SHOVEL;
                break;
            case SHOVEL:
                material = Material.IRON_SHOVEL;
                break;
            case MATCHES:
                material = Material.MAGMA_CREAM;
                break;
            case BOLTS:
                material = Material.IRON_NUGGET;
                break;
            case DUCTTAPE:
                material = Material.PAPER;
                break;
            case WIRE:
                material = Material.STRING;
                break;
            case PLASTIC_PLATE:
                material = Material.WHITE_CONCRETE;
                break;
            case OIL:
                material = Material.LAVA_BUCKET;
                break;
            case STICK:
                material = Material.STICK;
                break;
            case COFFEE:
                material = Material.COCOA_BEANS;
                break;
            case ENERGY_DRINK:
                material = Material.SUGAR;
                break;
            default:
                material = Material.STONE;
                break;
        }

        itemStack = new ItemStack(material);

        ItemMeta meta = itemStack.getItemMeta();
        if (meta != null) {
            meta.setDisplayName(item.toString());
            itemStack.setItemMeta(meta);
        }
        
        return itemStack;
    }

    public void addItemToInventory(int slot, PrisonEscapeItem item) {
        _inventory.setItem(slot, this.convertToItemStack(item));
    }

    public void addItemToInventory(int slot, ItemStack item) {
        _inventory.setItem(slot, item);
    }

    public void deleteItemFromInventory(int slot) {
        _inventory.clear(slot);
    }

    public void clearInventory() {
        _inventory.clear();
    }
}
