package net.tiagofar78.prisonescape.kits;

import net.tiagofar78.prisonescape.game.PEGame;
import net.tiagofar78.prisonescape.game.PEPlayer;
import net.tiagofar78.prisonescape.items.Item;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

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

    public void give(PEGame game, PEPlayer player) {
        Player bukkitPlayer = player.getBukkitPlayer();
        if (bukkitPlayer == null || !bukkitPlayer.isOnline()) {
            return;
        }

        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(player.getName());

        Inventory inv = bukkitPlayer.getInventory();
        inv.clear();

        for (Entry<Integer, ItemStack> entry : getVisualContents(messages).entrySet()) {
            inv.setItem(entry.getKey(), entry.getValue());
        }

        for (Entry<Integer, Item> entry : getContents().entrySet()) {
            inv.setItem(entry.getKey(), entry.getValue().toItemStack(game, player));
        }
    }

    public void update(PEGame game, PEPlayer player) {
        // Nothing
    }

    public Item getItemAt(int slot) {
        return _items.get(slot);
    }

}
