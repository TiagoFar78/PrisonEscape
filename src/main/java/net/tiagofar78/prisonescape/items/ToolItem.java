package net.tiagofar78.prisonescape.items;

import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Random;

public abstract class ToolItem extends Item {

    private static final int[] COLORS_MIN_DURABILITY = {80, 50, 20, 0};
    private static final String[] COLORS_CODES = {"§a", "§e", "§c", "§4"};
    private static final int MAX_DURABILITY = 100;

    private double _durability = durabilityPerUse() * randomUsesAmount();

    public ToolItem() {
        this(true);
    }

    public ToolItem(boolean useRandomDurability) {
        _durability = (useRandomDurability ? randomUsesAmount() : usesAmount()) * durabilityPerUse();
    }

    @Override
    public boolean isTool() {
        return true;
    }

    public boolean isBroken() {
        return _durability <= 0;
    }

    /**
     * @return true if durability is now 0 or less<br>
     *         false otherwise
     */
    public boolean decreaseDurability() {
        _durability -= durabilityPerUse();

        return _durability <= 0;
    }

    private double durabilityPerUse() {
        return (double) MAX_DURABILITY / (double) usesAmount();
    }

    private int randomUsesAmount() {
        return new Random().nextInt(0, usesAmount()) + 1;
    }

    protected abstract int usesAmount();

    public abstract int damageToBlock();

    @Override
    public ItemStack toItemStack(MessageLanguageManager messages) {
        ItemStack item = super.toItemStack(messages);

        ItemMeta meta = item.getItemMeta();
        if (meta instanceof Damageable) {
            Damageable damageable = (Damageable) meta;
            damageable.setDamage(getItemStackDamage());
            item.setItemMeta(damageable);
        }

        double roundedDurability = (double) Math.round(_durability * 100) / (double) 100;
        String newName = meta.getDisplayName() + " - " + getDurabilityColorCode() + roundedDurability + "%";
        setName(item, newName);

        return item;
    }

    private int getItemStackDamage() {
        return getMaterial().getMaxDurability() - getItemStackDurability();
    }

    private int getItemStackDurability() {
        return (int) (getMaterial().getMaxDurability() * _durability / MAX_DURABILITY);
    }

    private String getDurabilityColorCode() {
        for (int i = 0; i < COLORS_MIN_DURABILITY.length; i++) {
            if (_durability >= COLORS_MIN_DURABILITY[i]) {
                return COLORS_CODES[i];
            }
        }

        return null;
    }

}
