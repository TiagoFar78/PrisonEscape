package net.tiagofar78.prisonescape.items;

import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class PlasticSpoonItem extends ToolItem {

    public PlasticSpoonItem() {

    }

    public PlasticSpoonItem(boolean useRandomDurability) {
        super(useRandomDurability);
    }

    @Override
    protected int usesAmount() {
        return 6;
    }

    @Override
    public int damageToBlock() {
        return 25;
    }

    @Override
    public boolean isMetalic() {
        return false;
    }

    @Override
    public boolean isIllegal() {
        return false;
    }

    @Override
    public ItemStack toItemStack(MessageLanguageManager messages) {
        ItemStack item = super.toItemStack(messages);

        removeAttributes(item);

        return item;
    }

    @Override
    public Material getMaterial() {
        return Material.WOODEN_SHOVEL;
    }

}
