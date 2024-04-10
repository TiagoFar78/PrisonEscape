package net.tiagofar78.prisonescape.items;

import net.tiagofar78.prisonescape.game.PrisonEscapeGame;
import net.tiagofar78.prisonescape.managers.GameManager;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

public class HandcuffsItem extends FunctionalItem {

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
        return Material.IRON_BARS;
    }

    private void onInteract(Entity police, Entity prisioner) {
        PrisonEscapeGame game = GameManager.getGame();
        if (game == null) {
            return;
        }

        if (!(prisioner instanceof Player) || !(police instanceof Player)) {
            return;
        }

        @SuppressWarnings("deprecation")
        ItemStack item = ((Player) police).getItemInHand();
        if (!matches(item)) {
            return;
        }

        game.policeHandcuffedPrisioner(police.getName(), prisioner.getName());
    }

    @Override
    public void use(Event event) {
        Entity police;
        Entity prisioner;
        if (event instanceof PlayerInteractEntityEvent) {
            PlayerInteractEntityEvent e = (PlayerInteractEntityEvent) event;
            police = e.getPlayer();
            prisioner = e.getRightClicked();
        } else if (event instanceof EntityDamageByEntityEvent) {
            EntityDamageByEntityEvent e = (EntityDamageByEntityEvent) event;
            police = e.getDamager();
            prisioner = e.getEntity();
        } else {
            return;
        }

        onInteract(police, prisioner);
    }

}
