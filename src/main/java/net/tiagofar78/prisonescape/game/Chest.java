package net.tiagofar78.prisonescape.game;

import java.util.List;

import org.bukkit.inventory.ItemStack;

public class Chest {
	
	private int _size;
	private List<ItemStack> _contents;
	private List<ItemProbability> _itemsProbability;
	
	public Chest(int size, List<ItemProbability> itemsProbability) {
		this._size = size;
		this._itemsProbability = itemsProbability;
		
		reload();
	}
	
	public void reload() {
		_contents.clear();
		
		
	}
	
	
	public void removeItem() {
		
	}

}
