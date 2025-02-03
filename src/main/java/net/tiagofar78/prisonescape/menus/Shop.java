package net.tiagofar78.prisonescape.menus;

import net.tiagofar78.prisonescape.bukkit.BukkitMessageSender;
import net.tiagofar78.prisonescape.game.Guard;
import net.tiagofar78.prisonescape.game.PEGame;
import net.tiagofar78.prisonescape.game.PEPlayer;
import net.tiagofar78.prisonescape.items.Buyable;
import net.tiagofar78.prisonescape.items.CameraItem;
import net.tiagofar78.prisonescape.items.EnergyDrinkItem;
import net.tiagofar78.prisonescape.items.Item;
import net.tiagofar78.prisonescape.items.RadarItem;
import net.tiagofar78.prisonescape.items.SearchItem;
import net.tiagofar78.prisonescape.items.SoundDetectorItem;
import net.tiagofar78.prisonescape.items.TrapItem;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

import org.bukkit.Bukkit;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Shop implements Clickable {

    private static final int NUM_OF_ITEMS_FOR_SALE = 6;

    private List<Item> _contents;

    public Shop() {
        _contents = createContentsList();
    }

    private List<Item> createContentsList() {
        List<Item> list = new ArrayList<>();
        list.add(new EnergyDrinkItem());
        list.add(new TrapItem());
        list.add(new SoundDetectorItem());
        list.add(new CameraItem());
        list.add(new RadarItem());
        list.add(new SearchItem());
        return list;
    }

    @Override
    public ClickReturnAction click(PEPlayer player, int slot, boolean isPlayerInv, ClickType type) {
        if (isPlayerInv) {
            return ClickReturnAction.NOTHING;
        }

        Guard guard = (Guard) player;

        int index = convertToIndex(slot);
        if (index == -1) {
            return ClickReturnAction.NOTHING;
        }

        Item item = _contents.get(index);
        if (!item.isBuyable()) {
            return ClickReturnAction.NOTHING;
        }

        Buyable buyableItem = (Buyable) item;
        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(player.getName());

        int returnCode = guard.buyItem(item, buyableItem.getPrice());
        if (returnCode == -1) {
            BukkitMessageSender.sendChatMessage(player, messages.getReachedItemLimitMessage());
            return ClickReturnAction.NOTHING;
        } else if (returnCode == -2) {
            BukkitMessageSender.sendChatMessage(player, messages.getNotEnoughMoneyMessage());
            return ClickReturnAction.NOTHING;
        } else if (returnCode == -3) {
            BukkitMessageSender.sendChatMessage(player, messages.getFullInventoryMessage());
            return ClickReturnAction.NOTHING;
        }

        BukkitMessageSender.sendChatMessage(player, messages.getSuccessfullyBoughtItemMessage(guard.getBalance()));
        return ClickReturnAction.NOTHING;
    }

    @Override
    public Inventory toInventory(PEGame game, PEPlayer player) {
        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(player.getName());
        Inventory shopMenu = Bukkit.createInventory(null, 9, messages.getShopMenuTitle());

        shopMenu.setItem(0, createShopItem(new EnergyDrinkItem(), player, messages));
        shopMenu.setItem(1, createShopItem(new TrapItem(), player, messages));
        shopMenu.setItem(2, createShopItem(new SoundDetectorItem(), player, messages));
        shopMenu.setItem(3, createShopItem(new CameraItem(), player, messages));
        shopMenu.setItem(4, createShopItem(new RadarItem(), player, messages));
        shopMenu.setItem(5, createShopItem(new SearchItem(), player, messages));

        return shopMenu;
    }

    private <T extends Item & Buyable> ItemStack createShopItem(
            T item,
            PEPlayer player,
            MessageLanguageManager messages
    ) {
        ItemStack shopItem = item.toItemStack(player.getGame(), player);
        ItemMeta meta = shopItem.getItemMeta();
        List<String> lore = meta.getLore();
        if (lore == null) {
            lore = new ArrayList<>();
        }

        lore.add("");
        lore.add(messages.getShopItemsPriceLine(item.getPrice()));
        if (item.getLimit() != -1) {
            lore.add(messages.getShopItemsLimitLine(item.getLimit()));
        }
        meta.setLore(lore);
        shopItem.setItemMeta(meta);

        return shopItem;
    }

    private int convertToIndex(int slot) {
        if (slot >= NUM_OF_ITEMS_FOR_SALE) {
            return -1;
        }

        return slot;
    }

}
