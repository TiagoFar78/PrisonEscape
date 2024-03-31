package net.tiagofar78.prisonescape.game.prisonbuilding;

import net.tiagofar78.prisonescape.bukkit.BukkitMenu;
import net.tiagofar78.prisonescape.dataobjects.ItemProbability;
import net.tiagofar78.prisonescape.game.PrisonEscapeItem;
import net.tiagofar78.prisonescape.managers.ConfigManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Chest {

    private static final int CONTENTS_SIZE = 5;

    private List<PrisonEscapeItem> _contents;
    private List<ItemProbability> _itemsProbability;

    protected Chest() {
        this._contents = createContentsList();
        this._itemsProbability = createItemsProbabilityList();
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

    public void open(String playerName) {
        BukkitMenu.openChest(playerName, _contents);
    }

}
