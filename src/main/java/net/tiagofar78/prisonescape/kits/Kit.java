package net.tiagofar78.prisonescape.kits;

import net.tiagofar78.prisonescape.items.Item;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Hashtable;
import java.util.Map.Entry;

public abstract class Kit {

    private Hashtable<Integer, Item> _items = getContents();

    protected abstract Hashtable<Integer, Item> getContents();

    protected Hashtable<Integer, ItemStack> getVisualContents(MessageLanguageManager messages) {
        return new Hashtable<Integer, ItemStack>();
    }

    public void give(String playerName) {
        Player bukkitPlayer = Bukkit.getPlayer(playerName);
        if (bukkitPlayer == null || !bukkitPlayer.isOnline()) {
            return;
        }

        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(playerName);

        Inventory inv = bukkitPlayer.getInventory();
        inv.clear();

        for (Entry<Integer, Item> entry : getContents().entrySet()) {
            inv.setItem(entry.getKey(), entry.getValue().toItemStack(messages));
        }

        for (Entry<Integer, ItemStack> entry : getVisualContents(messages).entrySet()) {
            inv.setItem(entry.getKey(), entry.getValue());
        }
    }

    public void update(String playerName) {
        // Nothing
    }

    public Item getItemAt(int slot) {
        return _items.get(slot);
    }

}
