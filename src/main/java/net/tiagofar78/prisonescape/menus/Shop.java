package net.tiagofar78.prisonescape.menus;

import net.tiagofar78.prisonescape.bukkit.BukkitMessageSender;
import net.tiagofar78.prisonescape.game.Guard;
import net.tiagofar78.prisonescape.game.PEPlayer;
import net.tiagofar78.prisonescape.items.Buyable;
import net.tiagofar78.prisonescape.items.CameraItem;
import net.tiagofar78.prisonescape.items.EnergyDrinkItem;
import net.tiagofar78.prisonescape.items.Item;
import net.tiagofar78.prisonescape.items.NullItem;
import net.tiagofar78.prisonescape.items.RadarItem;
import net.tiagofar78.prisonescape.items.SoundDetectorItem;
import net.tiagofar78.prisonescape.items.TrapItem;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class Shop implements Clickable {

    private static final int NUM_OF_ITEMS_FOR_SALE = 5;

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
        return list;
    }

    @Override
    public ClickReturnAction click(PEPlayer player, int slot, boolean clickedPlayerInv) {
        Guard guard = (Guard) player;

        if (clickedPlayerInv) {
            return ClickReturnAction.NOTHING;
        }
        int index = convertToIndex(slot);
        if (index == -1) {
            return ClickReturnAction.NOTHING;
        }
        Item item = _contents.get(index);
        if (item instanceof NullItem) {
            return ClickReturnAction.NOTHING;
        }
        if (!(item.isBuyable())) {
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
    public Inventory toInventory(MessageLanguageManager messages) {
        Inventory shopMenu = Bukkit.createInventory(null, 9, "Buy Menu");

        shopMenu.setItem(0, new EnergyDrinkItem().toItemStack(messages));
        shopMenu.setItem(1, new TrapItem().toItemStack(messages));
        shopMenu.setItem(2, new SoundDetectorItem().toItemStack(messages));
        shopMenu.setItem(3, new CameraItem().toItemStack(messages));
        shopMenu.setItem(4, new RadarItem().toItemStack(messages));

        return shopMenu;
    }

    private int convertToIndex(int slot) {
        if (slot >= NUM_OF_ITEMS_FOR_SALE) {
            return -1;
        }

        return slot;
    }

}
