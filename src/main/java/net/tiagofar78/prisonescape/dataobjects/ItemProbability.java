package net.tiagofar78.prisonescape.dataobjects;

import org.bukkit.inventory.ItemStack;

public class ItemProbability {
	
	private ItemStack _item;
	private double _probability;
	
	public ItemProbability(ItemStack item, double probability) {
		_item = item;
		_probability = probability;
	}
	
	public ItemStack getItem() {
		return _item;
	}
	
	public double getProbability() {
		return _probability;
	}

}
