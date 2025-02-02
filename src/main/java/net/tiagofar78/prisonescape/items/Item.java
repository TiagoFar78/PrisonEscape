package net.tiagofar78.prisonescape.items;

import net.tiagofar78.prisonescape.bukkit.BukkitItems;
import net.tiagofar78.prisonescape.game.PEGame;
import net.tiagofar78.prisonescape.game.PEPlayer;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class Item {

    public abstract boolean isMetalic();

    public abstract boolean isIllegal();

    public boolean isBuyable() {
        return false;
    }

    public boolean isFunctional() {
        return false;
    }

    public boolean isTool() {
        return false;
    }

    public abstract Material getMaterial();

    public String getConfigName() {
        String subClassName = this.getClass().getSimpleName();
        int itemWordLength = "Item".length();

        return subClassName.substring(0, subClassName.length() - itemWordLength);
    }

    public String getDisplayName(MessageLanguageManager messages) {
        return messages.getItemName(getConfigName());
    }

    public List<String> getLore(MessageLanguageManager messages) {
        return messages.getItemLore(getConfigName());
    }

    public ItemStack toItemStack(PEGame game, PEPlayer player) {
        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(player.getName());
        return toItemStack(messages);
    }

    public ItemStack toItemStack(MessageLanguageManager messages) {
        ItemStack item = BukkitItems.createItemStack(getMaterial());
        BukkitItems.setName(item, getDisplayName(messages));
        setLore(item, messages);

        return item;
    }

    private void setLore(ItemStack item, MessageLanguageManager messages) {
        List<String> lore = getLore(messages);
        if (lore == null) {
            lore = new ArrayList<>();
        }

        List<String> itemPropertiesLore = messages.getItemPropertiesLore(isMetalic(), isIllegal());
        if (itemPropertiesLore.size() > 0) {
            lore.add("");
            lore.addAll(itemPropertiesLore);
            lore.add("");
        }

        if (lore.size() > 0) {
            BukkitItems.setLore(item, lore);
        }
    }

    public boolean matches(Material material) {
        return material == getMaterial();
    }

    public boolean matches(ItemStack item) {
        if (item == null) {
            return false;
        }

        return matches(item.getType());
    }

    @Override
    public boolean equals(Object o) {
        return o.getClass().getSimpleName().equals(this.getClass().getSimpleName());
    }

}
