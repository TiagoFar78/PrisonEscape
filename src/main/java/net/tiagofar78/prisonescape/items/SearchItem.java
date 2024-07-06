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

    @Override
    public void use(PlayerInteractEntityEvent e) {
        PEGame game = GameManager.getGame();
        if (game.getCurrentPhase().isClockStopped()) {
            return;
        }

        String guardName = e.getPlayer().getName();
        String prisonerName = e.getRightClicked().getName();

        PEPlayer playerPrisoner = game.getPEPlayer(prisonerName);
        if (!playerPrisoner.isPrisoner()) {
            return;
        }

        Guard guard = (Guard) game.getPEPlayer(guardName);
        MessageLanguageManager guardMessages = MessageLanguageManager.getInstanceByPlayer(guardName);
        if (guard.countSearches() == 0) {
            BukkitMessageSender.sendChatMessage(guardName, guardMessages.getNoSearchesMessage());
            return;
        }

        Prisoner prisoner = (Prisoner) playerPrisoner;
        if (prisoner.isWanted()) {
            BukkitMessageSender.sendChatMessage(guardName, guardMessages.getAlreadyWantedPlayerMessage());
        } else if (prisoner.hasIllegalItems()) {
            game.setWanted(prisoner, guard);
        } else {
            MessageLanguageManager prisonerMessages = MessageLanguageManager.getInstanceByPlayer(prisonerName);
            BukkitMessageSender.sendChatMessage(prisonerName, prisonerMessages.getPrisonerInspectedMessage());

            BukkitMessageSender.sendChatMessage(guardName, guardMessages.getPoliceInspectedMessage(prisonerName));

            guard.usedSearch();
        }
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
