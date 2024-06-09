package net.tiagofar78.prisonescape.items;

import net.tiagofar78.prisonescape.managers.GameManager;

import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;

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
        if (!(prisioner instanceof Player)) {
            return;
        }

        GameManager.getGame().policeHandcuffedPrisoner(police.getName(), prisioner.getName());
    }

    @Override
    public void use(PlayerInteractEntityEvent e) {
        onInteract(e.getPlayer(), e.getRightClicked());
    }

    @Override
    public void use(EntityDamageByEntityEvent e) {
        onInteract(e.getDamager(), e.getEntity());
    }

}
