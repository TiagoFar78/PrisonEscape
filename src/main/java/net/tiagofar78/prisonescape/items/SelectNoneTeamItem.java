package net.tiagofar78.prisonescape.items;

import net.tiagofar78.prisonescape.game.PrisonEscapeGame;
import net.tiagofar78.prisonescape.managers.GameManager;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class SelectNoneTeamItem extends Item implements Clickable {

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
        return Material.GRAY_WOOL;
    }

    @Override
    @EventHandler
    public void click(PlayerInteractEvent e) {
        PrisonEscapeGame game = GameManager.getGame();
        if (game == null) {
            return;
        }

        @SuppressWarnings("deprecation")
        ItemStack item = e.getPlayer().getItemInHand();
        if (item == null || item.getType() == Material.AIR) {
            return;
        }

        if (matches(item)) {
            game.playerRemovedTeamPreference(e.getPlayer().getName());
        }
    }

}
