package net.tiagofar78.prisonescape.game.prisonbuilding;

import net.tiagofar78.prisonescape.bukkit.BukkitMenu;
import net.tiagofar78.prisonescape.bukkit.BukkitMessageSender;
import net.tiagofar78.prisonescape.dataobjects.ItemProbability;
import net.tiagofar78.prisonescape.game.PrisonEscapePlayer;
import net.tiagofar78.prisonescape.items.Item;
import net.tiagofar78.prisonescape.items.ItemFactory;
import net.tiagofar78.prisonescape.items.NullItem;
import net.tiagofar78.prisonescape.managers.ConfigManager;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;
import net.tiagofar78.prisonescape.menus.ClickReturnAction;
import net.tiagofar78.prisonescape.menus.Menu;

import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Chest extends Menu {

    private static final int CONTENTS_SIZE = 5;

    private List<Item> _contents;
    private List<ItemProbability> _itemsProbability;
    private boolean _isOpened;

    public Chest(String regionName) {
        ConfigManager config = ConfigManager.getInstance();

        this._contents = createContentsList();
        this._itemsProbability = config.getChestContents(regionName);
        this._isOpened = false;
    }

    private List<Item> createContentsList() {
        List<Item> list = new ArrayList<>();

        for (int i = 0; i < CONTENTS_SIZE; i++) {
            list.add(new NullItem());
        }

        return list;
    }

    public boolean isOpened() {
        return _isOpened;
    }

    public void close() {
        _isOpened = false;
    }

    public void reload() {
        for (int i = 0; i < CONTENTS_SIZE; i++) {
            _contents.set(i, getRandomItem());
        }
    }

    private Item getRandomItem() {
        double randomValue = new Random().nextDouble();

        double cumulativeWeight = 0;
        for (ItemProbability itemProbability : _itemsProbability) {
            cumulativeWeight += itemProbability.getProbability();
            if (randomValue < cumulativeWeight) {
                return ItemFactory.createItem(itemProbability.getItemName());
            }
        }

        return new NullItem();
    }

    @Override
    public void open(PrisonEscapePlayer player) {
        BukkitMenu.openChest(player.getName(), _contents);
        _isOpened = true;
    }

    @Override
    public ClickReturnAction click(PrisonEscapePlayer player, int slot, Item itemHeld, boolean clickedPlayerInv) {
        if (clickedPlayerInv) {
            return ClickReturnAction.NOTHING;
        }

        int index = BukkitMenu.convertToIndexChest(slot);
        if (index == -1) {
            return ClickReturnAction.NOTHING;
        }

        Item item = _contents.get(index);
        if (item instanceof NullItem) {
            return ClickReturnAction.NOTHING;
        }

        int returnCode = player.giveItem(item);
        if (returnCode == -1) {
            MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(player.getName());

            BukkitMessageSender.sendChatMessage(player, messages.getFullInventoryMessage());
            return ClickReturnAction.NOTHING;
        }

        _contents.set(index, new NullItem());
        return ClickReturnAction.DELETE_HOLD_AND_SELECTED;
    }

    @Override
    public Inventory toInventory(MessageLanguageManager messages) {
        // TODO Auto-generated method stub
        return null;
    }

}
