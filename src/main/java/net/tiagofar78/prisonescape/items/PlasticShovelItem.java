package net.tiagofar78.prisonescape.items;

import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class PlasticShovelItem extends ToolItem implements Craftable {

    public PlasticShovelItem() {

    }

    public PlasticShovelItem(boolean useRandomDurability) {
        super(useRandomDurability);
    }

    @Override
    protected int usesAmount() {
        return 10;
    }

    @Override
    public int damageToBlock() {
        return 50;
    }

    @Override
    public boolean isMetalic() {
        return false;
    }

    @Override
    public boolean isIllegal() {
        return true;
    }

    @Override
    public ItemStack toItemStack(MessageLanguageManager messages) {
        ItemStack item = super.toItemStack(messages);

        removeAttributes(item);

        return item;
    }

    @Override
    public Material getMaterial() {
        return Material.STONE_SHOVEL;
    }

    @Override
    public List<Item> getCratingItems() {
        List<Item> items = new ArrayList<>();

        items.add(new StickItem());
        items.add(new DuctTapeItem());
        items.add(new PlasticPlateItem());

        return items;
    }

}
