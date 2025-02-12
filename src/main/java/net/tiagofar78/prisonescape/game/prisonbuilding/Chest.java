package net.tiagofar78.prisonescape.game.prisonbuilding;

import net.tiagofar78.prisonescape.dataobjects.ItemProbability;
import net.tiagofar78.prisonescape.game.PEGame;
import net.tiagofar78.prisonescape.game.PEPlayer;
import net.tiagofar78.prisonescape.items.Item;
import net.tiagofar78.prisonescape.items.ItemFactory;
import net.tiagofar78.prisonescape.items.NullItem;
import net.tiagofar78.prisonescape.managers.ConfigManager;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;
import net.tiagofar78.prisonescape.menus.ClickReturnAction;
import net.tiagofar78.prisonescape.menus.Clickable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Chest implements Clickable {

    private static final int CONTENTS_SIZE = 5;
    private static final int[] CHEST_CONTENT_INDEXES = {
            1 * 9 + 2,
            1 * 9 + 3,
            1 * 9 + 4,
            1 * 9 + 5,
            1 * 9 + 6};


    private List<Location> _locations;
    private List<Item> _contents;
    private List<ItemProbability> _itemsProbability;
    private boolean _isOpened;

    public Chest(List<Location> locations, String regionName) {
        ConfigManager config = ConfigManager.getInstance();

        _locations = locations;
        _contents = createContentsList();
        _itemsProbability = config.getChestContents(regionName);
        _isOpened = false;
    }

    private List<Item> createContentsList() {
        List<Item> list = new ArrayList<>();

        for (int i = 0; i < CONTENTS_SIZE; i++) {
            list.add(new NullItem());
        }

        return list;
    }

    public boolean isIn(Location location) {
        return _locations.contains(location);
    }

    public boolean isOpened() {
        return _isOpened;
    }

    public void close() {
        _isOpened = false;
    }

    public void reload() {
        for (int i = 0; i < CONTENTS_SIZE; i++) {
            _contents.set(i, ItemFactory.getRandomItem(_itemsProbability));
        }
    }

    @Override
    public ClickReturnAction click(PEPlayer player, int slot, boolean isPlayerInv, ClickType type) {
        if (isPlayerInv) {
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

        int returnCode = player.giveItem(item);
        if (returnCode == -1) {
            MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(player.getName());

            player.sendChatMessage(messages.getFullInventoryMessage());
            return ClickReturnAction.NOTHING;
        }

        _contents.set(index, new NullItem());
        return ClickReturnAction.DELETE_HOLD_AND_SELECTED;
    }

    @Override
    public Inventory toInventory(PEGame game, PEPlayer player) {
        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(player.getName());
        int lines = 3;
        String title = messages.getContainerName();
        Inventory inv = Bukkit.createInventory(null, lines * 9, title);

        ItemStack glassItem = createGlassItem();

        for (int i = 0; i < lines * 9; i++) {
            inv.setItem(i, glassItem);
        }

        for (int i = 0; i < _contents.size(); i++) {
            ItemStack item = _contents.get(i).toItemStack(game, player);
            inv.setItem(CHEST_CONTENT_INDEXES[i], item);
        }

        return inv;
    }

    private static ItemStack createGlassItem() {
        ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(" ");
        item.setItemMeta(itemMeta);

        return item;
    }

    private int convertToIndex(int slot) {
        for (int i = 0; i < CHEST_CONTENT_INDEXES.length; i++) {
            if (CHEST_CONTENT_INDEXES[i] == slot) {
                return i;
            }
        }

        return -1;
    }

}
