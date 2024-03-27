package net.tiagofar78.prisonescape.bukkit;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.tiagofar78.prisonescape.game.PrisonEscapeItem;

public class BukkitItems {
	
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
