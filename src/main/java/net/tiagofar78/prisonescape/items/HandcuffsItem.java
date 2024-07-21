package net.tiagofar78.prisonescape.items;

import net.tiagofar78.prisonescape.bukkit.BukkitMessageSender;
import net.tiagofar78.prisonescape.game.Guard;
import net.tiagofar78.prisonescape.game.PEGame;
import net.tiagofar78.prisonescape.game.PEPlayer;
import net.tiagofar78.prisonescape.game.Prisoner;
import net.tiagofar78.prisonescape.managers.GameManager;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

import org.bukkit.Material;
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

    private void onInteract(String guardName, String prisonerName) {
        PEGame game = GameManager.getGame();
        if (game.getCurrentPhase().isClockStopped()) {
            return;
        }

        PEPlayer playerPrisoner = game.getPEPlayer(prisonerName);
        if (!playerPrisoner.isPrisoner()) {
            return;
        }

        Prisoner prisoner = (Prisoner) playerPrisoner;
        if (prisoner.hasEscaped()) {
            return;
        }
        
        Guard guard = (Guard) game.getPEPlayer(guardName);

        if (prisoner.canBeArrested()) {
            game.arrestPlayer(prisoner, guard);
        } else {
            MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(guardName);
            BukkitMessageSender.sendChatMessage(guardName, messages.getNotWantedPlayerMessage());
        }
    }

    @Override
    public void use(PlayerInteractEntityEvent e) {
        onInteract(e.getPlayer().getName(), e.getRightClicked().getName());
    }

    @Override
    public void use(EntityDamageByEntityEvent e) {
        onInteract(e.getDamager().getName(), e.getEntity().getName());
    }

}
