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

import net.tiagofar78.prisonescape.dataobjects.ItemProbability;
import net.tiagofar78.prisonescape.game.PrisonEscapeItem;
import net.tiagofar78.prisonescape.game.PrisonEscapePlayer;
import net.tiagofar78.prisonescape.managers.ConfigManager;
import net.tiagofar78.prisonescape.managers.InventoryManager;

public class Chest {
	
	private static final int SLOTS_PER_LINE = 9;
	private static final ItemStack GRAY_PANEL = getGrayPanel();
	
	private List<Integer> _contentsIndexes;
	private Hashtable<Integer, PrisonEscapeItem> _contents;
	private List<ItemProbability> _itemsProbability;
	private InventoryManager _inventoryManager;
	
	protected Chest(int size, List<ItemProbability> itemsProbability) {
		this._contentsIndexes = getIndexesForContents(size);
		this._contents = new Hashtable<>();
		this._itemsProbability = itemsProbability;
		this._inventoryManager = new InventoryManager();
		
		reload();
	}
	
	public void reload() {
		_contents.clear();
		
		for (int index : _contentsIndexes) {
			_contents.put(index, getRandomItem());
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
	
	public Inventory buildInventory() {
		ConfigManager config = ConfigManager.getInstance();
		
		int lines = 3;
		Inventory inv = Bukkit.createInventory(null, lines * SLOTS_PER_LINE, config.getContainerName());
		
		for (int i = 0; i < lines * SLOTS_PER_LINE; i++) {
			inv.setItem(i, GRAY_PANEL);
		}
		
		for (int slot : _contentsIndexes) {
			ItemStack itemStack = _inventoryManager.convertToItemStack(_contents.get(slot));
			inv.setItem(slot, itemStack);
		}
		
		return inv;
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
