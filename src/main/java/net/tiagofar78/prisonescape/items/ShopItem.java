package net.tiagofar78.prisonescape.items;

import net.tiagofar78.prisonescape.game.PEGame;
import net.tiagofar78.prisonescape.game.PEPlayer;
import net.tiagofar78.prisonescape.managers.GameManager;
import net.tiagofar78.prisonescape.menus.Shop;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

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
    public void use(PlayerInteractEvent e) {
        PEGame game = GameManager.getGame();
        PEPlayer player = game.getPEPlayer(e.getPlayer().getName());
        player.openMenu(new Shop());
    }
}
