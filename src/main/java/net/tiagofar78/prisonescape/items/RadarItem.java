package net.tiagofar78.prisonescape.items;

import net.tiagofar78.prisonescape.game.Prisioner;
import net.tiagofar78.prisonescape.game.PrisonEscapeGame;
import net.tiagofar78.prisonescape.managers.ConfigManager;
import net.tiagofar78.prisonescape.managers.GameManager;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class RadarItem extends FunctionalItem implements Buyable {

    @Override
    public int getPrice() {
        return ConfigManager.getInstance().getRadarPrice();
    }

    @Override
    public int getLimit() {
        return ConfigManager.getInstance().getRadarLimit();
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
    public Material getMaterial() {
        return Material.COMPASS;
    }

    @Override
    public boolean isBuyable() {
        return true;
    }

    @Override
    public void use(PlayerInteractEvent e) {
        PrisonEscapeGame game = GameManager.getGame();

        String playerName = e.getPlayer().getName();
        game.getPrisonEscapePlayer(playerName).removeItem(e.getPlayer().getInventory().getHeldItemSlot());

        ConfigManager config = ConfigManager.getInstance();
        int effectDuration = config.getRadarDuration();

        List<Prisioner> prisioners = game.getPrisionerTeam().getMembers();
        for (Prisioner prisioner : prisioners) {
            prisioner.setEffect(PotionEffectType.GLOWING, effectDuration, 1);
        }
    }

}
