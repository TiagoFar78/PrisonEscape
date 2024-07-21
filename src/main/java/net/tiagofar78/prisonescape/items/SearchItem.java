package net.tiagofar78.prisonescape.items;

import net.tiagofar78.prisonescape.bukkit.BukkitMessageSender;
import net.tiagofar78.prisonescape.game.Guard;
import net.tiagofar78.prisonescape.game.PEGame;
import net.tiagofar78.prisonescape.game.PEPlayer;
import net.tiagofar78.prisonescape.game.Prisoner;
import net.tiagofar78.prisonescape.managers.ConfigManager;
import net.tiagofar78.prisonescape.managers.GameManager;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
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

    private void use(String guardName, String prisonerName, int heldItemSlot) {
        PEGame game = GameManager.getGame();
        if (game.getCurrentPhase().isClockStopped()) {
            return;
        }

        PEPlayer playerPrisoner = game.getPEPlayer(prisonerName);
        if (!playerPrisoner.isPrisoner()) {
            return;
        }

        Guard guard = (Guard) game.getPEPlayer(guardName);
        MessageLanguageManager guardMessages = MessageLanguageManager.getInstanceByPlayer(guardName);

        Prisoner prisoner = (Prisoner) playerPrisoner;
        if (prisoner.hasEscaped()) {
            return;
        }

        if (prisoner.isWanted()) {
            BukkitMessageSender.sendChatMessage(guardName, guardMessages.getAlreadyWantedPlayerMessage());
        } else if (prisoner.hasIllegalItems()) {
            game.setWanted(prisoner, guard);
        } else {
            MessageLanguageManager prisonerMessages = MessageLanguageManager.getInstanceByPlayer(prisonerName);
            BukkitMessageSender.sendChatMessage(prisonerName, prisonerMessages.getPrisonerInspectedMessage());

            BukkitMessageSender.sendChatMessage(guardName, guardMessages.getPoliceInspectedMessage(prisonerName));

            guard.usedSearch();
            guard.removeItem(heldItemSlot);
        }

    }

    @Override
    public void use(PlayerInteractEntityEvent e) {
        Player player = e.getPlayer();
        use(player.getName(), e.getRightClicked().getName(), player.getInventory().getHeldItemSlot());
    }

    @Override
    public void use(EntityDamageByEntityEvent e) {
        Player player = (Player) e.getDamager();
        use(player.getName(), e.getEntity().getName(), player.getInventory().getHeldItemSlot());
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
