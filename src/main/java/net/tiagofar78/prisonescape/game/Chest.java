package net.tiagofar78.prisonescape.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import net.tiagofar78.prisonescape.dataobjects.ItemProbability;
import net.tiagofar78.prisonescape.managers.ConfigManager;

public class Chest {
	
	private static final int SLOTS_PER_LINE = 9;
	
	private int _size;
	private List<ItemStack> _contents;
	private List<ItemProbability> _itemsProbability;
	private String _playerOpener;
	
	protected Chest(int size, List<ItemProbability> itemsProbability) {
		this._size = size;
		this._contents = new ArrayList<>();
		this._itemsProbability = itemsProbability;
		
		initializeContents();
		
		reload();
	}
	
	private void initializeContents() {
		for (int i = 0; i < _size; i++) {
			_contents.add(null);
		}
	}
	
	public void reload() {
		_contents.clear();
		
		for (int i = 0; i < _size; i++) {
			_contents.set(i, getRandomItem());
		}
	}
	
	private ItemStack getRandomItem() {
		double totalWeight = _itemsProbability.stream().mapToDouble(ItemProbability::getProbability).sum();
        double randomValue = new Random().nextDouble() * totalWeight;

        double cumulativeWeight = 0;
        for (ItemProbability itemProbability : _itemsProbability) {
            cumulativeWeight += itemProbability.getProbability();
            if (randomValue < cumulativeWeight) {
                return itemProbability.getItem();
            }
        }
        
        return null;
	}
	
	public void removeItem(int inventoryIndex) {
		
	}
	
	public Inventory buildInventory() {
		ConfigManager config = ConfigManager.getInstance();
		
		int lines = 3;
		Inventory inv = Bukkit.createInventory(null, lines * SLOTS_PER_LINE, config.getContainerName());
		
		for (int i = 0; i < lines * SLOTS_PER_LINE; i++) {
			
		}
		
		return inv;
	}

}
