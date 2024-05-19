package net.tiagofar78.prisonescape.menus;

import net.tiagofar78.prisonescape.bukkit.BukkitMenu;
import net.tiagofar78.prisonescape.bukkit.BukkitMessageSender;
import net.tiagofar78.prisonescape.game.Guard;
import net.tiagofar78.prisonescape.game.PrisonEscapePlayer;
import net.tiagofar78.prisonescape.items.Buyable;
import net.tiagofar78.prisonescape.items.CameraItem;
import net.tiagofar78.prisonescape.items.EnergyDrinkItem;
import net.tiagofar78.prisonescape.items.Item;
import net.tiagofar78.prisonescape.items.NullItem;
import net.tiagofar78.prisonescape.items.RadarItem;
import net.tiagofar78.prisonescape.items.SoundDetectorItem;
import net.tiagofar78.prisonescape.items.TrapItem;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class Shop extends Menu {

    private boolean _isOpen = false;
    private List<Item> _contents;

    public Shop() {
        _contents = createContentsList();
    }

    @Override
    public void open(PrisonEscapePlayer player) {
        BukkitMenu.openShop(player.getName());
        _isOpen = true;
    }

    @Override
    public void close() {
        _isOpen = false;
    }

    @Override
    public boolean isOpened() {
        return _isOpen;
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
    public ClickReturnAction click(PrisonEscapePlayer player, int slot, Item itemHeld, boolean clickedPlayerInv) {
        Guard guard = (Guard) player;

        if (clickedPlayerInv) {
            return ClickReturnAction.NOTHING;
        }
        int index = BukkitMenu.convertToIndexShop(slot);
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
        // TODO Auto-generated method stub
        return null;
    }

}
