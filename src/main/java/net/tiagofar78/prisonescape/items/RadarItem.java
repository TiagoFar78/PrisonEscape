package net.tiagofar78.prisonescape.items;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffectType;

import net.tiagofar78.prisonescape.game.PEGame;
import net.tiagofar78.prisonescape.game.PEPlayer;
import net.tiagofar78.prisonescape.game.Prisoner;
import net.tiagofar78.prisonescape.managers.ConfigManager;

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
    public void use(PEGame game, PEPlayer player, PlayerInteractEvent e) {
        player.removeItem(e.getPlayer().getInventory().getHeldItemSlot());

        ConfigManager config = ConfigManager.getInstance();
        int effectDuration = config.getRadarDuration();

        List<Prisoner> prisioners = game.getPrisonerTeam().getMembers();
        for (Prisoner prisioner : prisioners) {
            if (!prisioner.hasEscaped()) {
                prisioner.setEffect(PotionEffectType.GLOWING, effectDuration, 1);
            }
        }
    }

}
