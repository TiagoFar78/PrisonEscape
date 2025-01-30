package net.tiagofar78.prisonescape.items;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

import net.tiagofar78.prisonescape.game.PEGame;
import net.tiagofar78.prisonescape.game.PEPlayer;
import net.tiagofar78.prisonescape.menus.Shop;

public class ShopItem extends FunctionalItem {

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
        return Material.CHEST;
    }

    @Override
    public boolean isFunctional() {
        return true;
    }

    @Override
    public void use(PEGame game, PEPlayer player, PlayerInteractEvent e) {
        player.openMenu(new Shop());
    }
}
