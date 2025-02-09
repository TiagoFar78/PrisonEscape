package net.tiagofar78.prisonescape.items;

import net.tiagofar78.prisonescape.game.PEGame;
import net.tiagofar78.prisonescape.game.PEPlayer;
import net.tiagofar78.prisonescape.game.Prisoner;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;

public class DoorCodeItem extends FunctionalItem implements Craftable {

    @Override
    public boolean isMetalic() {
        return false;
    }

    @Override
    public boolean isIllegal() {
        return true;
    }

    @Override
    public Material getMaterial() {
        return Material.FILLED_MAP;
    }

    @Override
    public void use(PEGame game, PEPlayer player, PlayerInteractEvent e) {
        String playerName = e.getPlayer().getName();

        if (game.playersHaveDoorCode()) {
            MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(playerName);
            player.sendChatMessage(messages.getDoorCodeAlreadyKnownMessage());
            return;
        }

        game.findDoorCode();
        player.removeItem(e.getPlayer().getInventory().getHeldItemSlot());

        for (Prisoner prisoner : game.getPrisonerTeam().getMembers()) {
            MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(prisoner.getName());
            prisoner.sendChatMessage(messages.getCodeFoundMessage());
        }
    }

    @Override
    public List<Item> getCratingItems() {
        List<Item> items = new ArrayList<>();

        items.add(new NotePartItem(1));
        items.add(new NotePartItem(2));
        items.add(new NotePartItem(3));
        items.add(new NotePartItem(4));

        return items;
    }

}
