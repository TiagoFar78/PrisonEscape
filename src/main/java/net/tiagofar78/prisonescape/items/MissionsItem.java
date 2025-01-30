package net.tiagofar78.prisonescape.items;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

import net.tiagofar78.prisonescape.bukkit.BukkitMessageSender;
import net.tiagofar78.prisonescape.game.Guard;
import net.tiagofar78.prisonescape.game.PEGame;
import net.tiagofar78.prisonescape.game.PEPlayer;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;
import net.tiagofar78.prisonescape.missions.Mission;

public class MissionsItem extends FunctionalItem {

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
        return Material.BOOK;
    }

    @Override
    public void use(PEGame game, PEPlayer player, PlayerInteractEvent e) {
        Guard guard = (Guard) player;

        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(guard.getName());

        List<Mission> missions = guard.getMissions();
        if (missions.size() == 0) {
            BukkitMessageSender.sendChatMessage(guard, messages.getAllMissionsCompletedMessage());
            return;
        }

        Location loc = guard.getLocation();
        for (int i = 0; i < missions.size(); i++) {
            Mission mission = missions.get(i);
            if (mission.getRegionName().equals(game.getPrison().getRegionName(loc))) {
                mission.start(guard, i);
                return;
            }
        }

        BukkitMessageSender.sendChatMessage(guard, messages.getGoToMissionLocationsMessage());
        for (Mission mission : missions) {
            String regionName = mission.getRegionName();
            BukkitMessageSender.sendChatMessage(guard, messages.getMissionLocationLineMessage(regionName));
        }
    }

}
