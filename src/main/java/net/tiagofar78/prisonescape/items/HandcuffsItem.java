package net.tiagofar78.prisonescape.items;

import net.tiagofar78.prisonescape.game.PrisonEscapeGame;
import net.tiagofar78.prisonescape.managers.GameManager;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

public class HandcuffsItem extends Item implements Listener {

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

    @EventHandler(ignoreCancelled = false)
    public void onInteract(PlayerInteractEntityEvent e) {
        onInteract(e.getPlayer(), e.getRightClicked());
    }

    @EventHandler(ignoreCancelled = false)
    public void onInteract(EntityDamageByEntityEvent e) {
        onInteract(e.getDamager(), e.getEntity());
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

}
