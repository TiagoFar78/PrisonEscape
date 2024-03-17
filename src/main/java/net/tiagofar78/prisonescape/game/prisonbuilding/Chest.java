package net.tiagofar78.prisonescape.game.prisonbuilding;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.tiagofar78.prisonescape.bukkit.BukkitInventory;
import net.tiagofar78.prisonescape.dataobjects.ItemProbability;
import net.tiagofar78.prisonescape.game.PrisonEscapeItem;
import net.tiagofar78.prisonescape.game.PrisonEscapePlayer;
import net.tiagofar78.prisonescape.managers.ConfigManager;

public class Chest {
	
	private static final int SLOTS_PER_LINE = 9;
	private static final ItemStack GRAY_PANEL = getGrayPanel();
	
	private List<Integer> _contentsIndexes;
	private Hashtable<Integer, PrisonEscapeItem> _contents;
	private List<ItemProbability> _itemsProbability;
	private BukkitInventory _inventoryManager;
	private Inventory _inventory;
	
	protected Chest(int size, List<ItemProbability> itemsProbability) {
		this._contentsIndexes = getIndexesForContents(size);
		this._contents = new Hashtable<>();
		this._itemsProbability = itemsProbability;
		
		// Create inventory
		int lines = 3;
		ConfigManager config = ConfigManager.getInstance();
		this._inventory = Bukkit.createInventory(null, lines * SLOTS_PER_LINE, config.getContainerName());
		
		this._inventoryManager = new BukkitInventory(_inventory);

		reload();
	}
	
	public void reload() {
		_contents.clear();
		_inventoryManager.clear();
		
		for (int index : _contentsIndexes) {
			_contents.put(index, getRandomItem());
			buildInventory();
		}
	}
	
	private PrisonEscapeItem getRandomItem() {
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
	
	public void buildInventory() {
		int lines = 3;
		for (int i = 0; i < lines * SLOTS_PER_LINE; i++) {
			_inventoryManager.addItem(i, GRAY_PANEL);
		}
		
		for (int slot : _contentsIndexes) {
			_inventoryManager.addItem(slot, _contents.get(slot));
		}
	}
	
	public void playerClickEvent(InventoryClickEvent e, PrisonEscapePlayer player) {		
		int slot = e.getRawSlot();
		
		if (!_contents.containsKey(slot)) {
			e.setCancelled(true);
			return;
		}
		
		PrisonEscapeItem item = _contents.get(slot);
		if (item == null) {
			return;
		}
		
		int returnCode = player.giveItem(item);
		if (returnCode == -1) {
			// TODO send message of full inventory to player
		}
		else if (returnCode == 0) {
			_contents.put(slot, null);
			_inventoryManager.deleteItem(slot);
		}
		
	}
	
	private List<Integer> getIndexesForContents(int size) {
		// This method should be remade so that it will allow different sizes besides _size = 5
		List<Integer> positions = new ArrayList<>();
		
		for (int i = 2; i <= 2 + size; i++) {
			positions.add(i + 9);
		}
		
		return positions;
	}
	
	private static ItemStack getGrayPanel() {
		ItemStack item = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName("");
		item.setItemMeta(meta);
		return item;
	}

}
