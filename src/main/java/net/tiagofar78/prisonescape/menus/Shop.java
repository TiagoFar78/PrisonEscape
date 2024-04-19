package net.tiagofar78.prisonescape.menus;

import net.tiagofar78.prisonescape.bukkit.BukkitMenu;
import net.tiagofar78.prisonescape.bukkit.BukkitMessageSender;
import net.tiagofar78.prisonescape.game.PrisonEscapePlayer;
import net.tiagofar78.prisonescape.items.Buyable;
import net.tiagofar78.prisonescape.items.CameraItem;
import net.tiagofar78.prisonescape.items.EnergyDrinkItem;
import net.tiagofar78.prisonescape.items.Item;
import net.tiagofar78.prisonescape.items.NullItem;
import net.tiagofar78.prisonescape.items.RadarItem;
import net.tiagofar78.prisonescape.items.SensorItem;
import net.tiagofar78.prisonescape.items.TrapItem;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

import java.util.ArrayList;
import java.util.List;

public class Shop implements Clickable {

    private boolean _isOpen = false;
    private List<Item> _contents;

    @Override
    public void open(PrisonEscapePlayer player) {
        BukkitMenu.openShop(player.getName());
        _isOpen = true;
        _contents = createContentsList();
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
        list.add(new SensorItem());
        list.add(new CameraItem());
        list.add(new RadarItem());
        return list;
    }

    @Override
    public ClickReturnAction click(PrisonEscapePlayer player, int slot, Item itemHeld, boolean clickedPlayerInv) {
        System.out.println("clicou");
        if (clickedPlayerInv) {
            return ClickReturnAction.NOTHING;
        }
        System.out.println("1");
        int index = BukkitMenu.convertToIndexShop(slot);
        if (index == -1) {
            return ClickReturnAction.NOTHING;
        }
        System.out.println("2");
        Item item = _contents.get(index);
        if (item instanceof NullItem) {
            return ClickReturnAction.NOTHING;
        }
        System.out.println("3");
        System.out.println(item.isBuyable());
        if (!(item.isBuyable())) {
            return ClickReturnAction.NOTHING;
        }
        System.out.println("4");
        Buyable buyableItem = (Buyable) item;
        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(player.getName());

        int returnCode = player.buyItem(item, buyableItem.getPrice());
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

        BukkitMessageSender.sendChatMessage(player, messages.getSuccessfullyBoughtItemMessage(player.getBalance()));
        return ClickReturnAction.DELETE_HOLD_AND_SELECTED;
    }

}
