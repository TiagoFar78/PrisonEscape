package net.tiagofar78.prisonescape.items;

import net.tiagofar78.prisonescape.game.PEGame;
import net.tiagofar78.prisonescape.game.TeamPreference;
import net.tiagofar78.prisonescape.managers.GameManager;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

import org.bukkit.Material;
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
    public void use(PlayerInteractEvent e) {
        PEGame game = GameManager.getGame();
        String playerName = e.getPlayer().getName();

        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(playerName);
        String message = messages.getSelectedPoliceTeamMessage();

        game.updateTeamPreference(playerName, message, TeamPreference.POLICE, PEGame.GUARDS_TEAM_NAME);
    }

}
