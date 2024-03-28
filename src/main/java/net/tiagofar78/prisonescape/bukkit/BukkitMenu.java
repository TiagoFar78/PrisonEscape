package net.tiagofar78.prisonescape.bukkit;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import net.tiagofar78.prisonescape.game.PrisonEscapeItem;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

public class BukkitMenu {
	
	private static ItemStack createGlassItem() {
		ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName("");
		item.setItemMeta(itemMeta);
		
		return item;
	}
	
	public static void openVault(String playerName, List<PrisonEscapeItem> contents,  List<PrisonEscapeItem> hiddenContents) {
		Player bukkitPlayer = Bukkit.getPlayer(playerName);
		if (bukkitPlayer == null || !bukkitPlayer.isOnline()) {
			return;
		}
		
		MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(playerName);
		
		String title = messages.getVaultTitle();
		int lines = 6;
		Inventory inv = Bukkit.createInventory(null, lines*9, title);
		
		ItemStack glassItem = createGlassItem();
		for (int i = 0; i < lines * 9; i++) {
			inv.setItem(i, glassItem);
		}
		
		for (int i = 0; i < contents.size(); i++) {
			ItemStack item = BukkitItems.convertToItemStack(contents.get(i));
			inv.setItem(9 + 2, item);
		}
		
		ItemStack hiddenIndicatorItem = createHiddenIndicatorItem(messages); 
		for (int line = 3; line < 6; line++) {
			for (int column = 4; column < 7; column++) {
				int index = (line - 1) + (column - 1);
				inv.setItem(index, hiddenIndicatorItem);
			}
		}
		
		ItemStack hiddenItem = BukkitItems.convertToItemStack(hiddenContents.get(0));
		inv.setItem(9 * 4 + 4, hiddenItem);
	}
	
	private static ItemStack createHiddenIndicatorItem(MessageLanguageManager messages) {
		ItemStack item = new ItemStack(Material.RED_STAINED_GLASS_PANE);
		ItemMeta itemMeta = item.getItemMeta();
		itemMeta.setDisplayName(messages.getVaultHiddenGlassName());
		item.setItemMeta(itemMeta);
		
		return item;
	}

}
