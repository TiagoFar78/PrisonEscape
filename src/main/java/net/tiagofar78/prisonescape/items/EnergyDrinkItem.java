package net.tiagofar78.prisonescape.items;

import net.tiagofar78.prisonescape.game.PEGame;
import net.tiagofar78.prisonescape.game.PEPlayer;
import net.tiagofar78.prisonescape.managers.ConfigManager;
import net.tiagofar78.prisonescape.managers.GameManager;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class EnergyDrinkItem extends FunctionalItem implements Buyable {

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

    @Override
    public void use(PlayerInteractEvent e) {
        Player bukkitPlayer = e.getPlayer();
        int heldItemSlot = bukkitPlayer.getInventory().getHeldItemSlot();

        PEGame game = GameManager.getGame();
        PEPlayer player = game.getPEPlayer(bukkitPlayer.getName());

        ConfigManager config = ConfigManager.getInstance();
        player.giveEnergyDrinkEffect(config.getSpeedDuration(), config.getSpeedLevel());

        player.removeItem(heldItemSlot);

    }

    @Override
    public int getPrice() {
        return ConfigManager.getInstance().getEnergyDrinkPrice();
    }

    @Override
    public int getLimit() {
        return ConfigManager.getInstance().getEnergyDrinkLimit();
    }

    @Override
    public boolean isBuyable() {
        return true;
    }

}
