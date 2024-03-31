package net.tiagofar78.prisonescape.game.prisonbuilding;

import net.tiagofar78.prisonescape.bukkit.BukkitMenu;
import net.tiagofar78.prisonescape.dataobjects.ItemProbability;
import net.tiagofar78.prisonescape.game.PrisonEscapeItem;
import net.tiagofar78.prisonescape.game.PrisonEscapePlayer;
import net.tiagofar78.prisonescape.managers.ConfigManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Chest implements Clickable {

    private static final int CONTENTS_SIZE = 5;

    private List<PrisonEscapeItem> _contents;
    private List<ItemProbability> _itemsProbability;
    private boolean _isOpened;

    protected Chest() {
        this._contents = createContentsList();
        this._itemsProbability = createItemsProbabilityList();
        this._isOpened = false;
    }

    private List<PrisonEscapeItem> createContentsList() {
        List<PrisonEscapeItem> list = new ArrayList<>();

        for (int i = 0; i < CONTENTS_SIZE; i++) {
            list.add(null);
        }

        return list;
    }

    private List<ItemProbability> createItemsProbabilityList() {
        List<ItemProbability> list = new ArrayList<>();

        ConfigManager config = ConfigManager.getInstance();
        double commonProbability = config.getCommonItemsProbability();
        double rareProbability = config.getRareItemsProbability();

        for (PrisonEscapeItem item : PrisonEscapeItem.values()) {
            double probability = item.isRare() ? rareProbability : commonProbability;
            list.add(new ItemProbability(item, probability));
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

    private PrisonEscapeItem getRandomItem() {
        double totalWeight = _itemsProbability.stream().mapToDouble(ItemProbability::getProbability).sum();
        double randomValue = new Random().nextDouble(totalWeight);

        double cumulativeWeight = 0;
        for (ItemProbability itemProbability : _itemsProbability) {
            cumulativeWeight += itemProbability.getProbability();
            if (randomValue < cumulativeWeight) {
                return itemProbability.getItem();
            }
        }

        return null;
    }

    @Override
    public void open(PrisonEscapePlayer player) {
        BukkitMenu.openChest(player.getName(), _contents);
        _isOpened = true;
    }

    @Override
    public int click(PrisonEscapePlayer player, int slot, PrisonEscapeItem itemHeld) {
        int index = BukkitMenu.convertToIndexChest(slot);
        if (index == -1) {
            return -1;
        }

        PrisonEscapeItem item = _contents.get(index);
        if (item == null) {
            return -1;
        }

        int returnCode = player.giveItem(item);
        if (returnCode == -1) {
            // TODO send full inventory message
            return -1;
        }

        _contents.set(index, null);
        // TODO send delete item return code

        return -1;
    }

}
