package net.tiagofar78.prisonescape.items;

import net.tiagofar78.prisonescape.game.PEGame;
import net.tiagofar78.prisonescape.game.PEPlayer;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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

    protected ItemStack toItemStack(MessageLanguageManager messages) {
        ItemStack item = createItemStack(getMaterial());
        setName(item, getDisplayName(messages));
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
            setLore(item, lore);
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

//  #########################################
//  #               ItemStack               #
//  #########################################

    public ItemStack createItemStack(Material type) {
        return new ItemStack(type);
    }

    public void setName(ItemStack item, String name) {
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);

        item.setItemMeta(meta);
    }

    public void setLore(ItemStack item, List<String> lore) {
        ItemMeta meta = item.getItemMeta();
        meta.setLore(lore);

        item.setItemMeta(meta);
    }

    public void removeAttributes(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);

        item.setItemMeta(meta);
    }

}
