package net.tiagofar78.prisonescape.items;

import net.tiagofar78.prisonescape.game.PrisonEscapeGame;
import net.tiagofar78.prisonescape.managers.GameManager;

import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerInteractEvent;

public class SelectPoliceTeamItem extends FunctionalItem {

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
        return Material.BLUE_WOOL;
    }

    @Override
    public void use(Event event) {
        if (!(event instanceof PlayerInteractEvent)) {
            return;
        }

        PlayerInteractEvent e = (PlayerInteractEvent) event;

        PrisonEscapeGame game = GameManager.getGame();
        if (game == null) {
            return;
        }

        game.playerSelectPoliceTeam(e.getPlayer().getName());
    }

}
