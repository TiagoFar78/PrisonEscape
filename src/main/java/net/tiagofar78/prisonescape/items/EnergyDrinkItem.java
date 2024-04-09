package net.tiagofar78.prisonescape.items;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import net.tiagofar78.prisonescape.bukkit.BukkitItems;
import net.tiagofar78.prisonescape.managers.ConfigManager;
import net.tiagofar78.prisonescape.managers.GameManager;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

public class EnergyDrinkItem extends Item implements Listener {

    private final static int TICKS_PER_SECOND = 20;

    @Override
    public boolean isMetalic() {
        return false;
    }

    @Override
    public boolean isIllegal() {
        return false;
    }

    @Override
    public Material getMaterial() {
        return Material.POTION;
    }

    @Override
    public ItemStack toItemStack(MessageLanguageManager messages) {
        ItemStack item = super.toItemStack(messages);

        ConfigManager config = ConfigManager.getInstance();

        int speedDuration = config.getSpeedDuration() * TICKS_PER_SECOND;
        int speedLevel = config.getSpeedLevel();
        PotionEffect speedEffect = new PotionEffect(PotionEffectType.SPEED, speedDuration, speedLevel);

        PotionMeta meta = (PotionMeta) item.getItemMeta();
        meta.setColor(PotionEffectType.SPEED.getColor());
        meta.addCustomEffect(speedEffect, true);

        item.setItemMeta(meta);

        return item;
    }
    
    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        if (matches(BukkitItems.getEventItem(e))) {
            Player player = e.getPlayer();
            int heldItemSlot = player.getInventory().getHeldItemSlot();
            GameManager.getGame().playerDrankEnergyDrink(player.getName(), heldItemSlot);
        }
    }

}
