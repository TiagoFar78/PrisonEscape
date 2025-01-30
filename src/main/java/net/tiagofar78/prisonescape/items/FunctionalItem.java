package net.tiagofar78.prisonescape.items;

import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import net.tiagofar78.prisonescape.game.PEGame;
import net.tiagofar78.prisonescape.game.PEPlayer;

public abstract class FunctionalItem extends Item {

    @Override
    public boolean isFunctional() {
        return true;
    }

    public void use(PEGame game, PEPlayer player, PlayerInteractEvent e) {
        // Do nothing, is is only useful so children classes can Override the method they want
    }

    public void use(PEGame game, PEPlayer player, PlayerInteractEntityEvent e) {
        // Do nothing, is is only useful so children classes can Override the method they want
    }

    public void use(PEGame game, PEPlayer player, EntityDamageByEntityEvent e) {
        // Do nothing, is is only useful so children classes can Override the method they want
    }
}
