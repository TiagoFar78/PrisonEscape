package net.tiagofar78.prisonescape.items;

import net.tiagofar78.prisonescape.game.PEGame;
import net.tiagofar78.prisonescape.managers.ConfigManager;
import net.tiagofar78.prisonescape.managers.GameManager;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class SearchItem extends FunctionalItem implements Buyable {

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
        return Material.SPYGLASS;
    }

    @Override
    public void use(PlayerInteractEntityEvent e) {
        PEGame game = GameManager.getGame();
        if (game == null) {
            return;
        }

        Entity prisioner = e.getRightClicked();
        if (!(prisioner instanceof Player)) {
            return;
        }

        game.policeInspectedPrisoner(e.getPlayer().getName(), prisioner.getName());
    }

    @Override
    public boolean isBuyable() {
        return true;
    }

    @Override
    public int getPrice() {
        return ConfigManager.getInstance().getSearchPrice();
    }

    @Override
    public int getLimit() {
        return ConfigManager.getInstance().getSearchLimit();
    }

}
