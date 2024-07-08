package net.tiagofar78.prisonescape.missions;

import net.tiagofar78.prisonescape.bukkit.BukkitMessageSender;
import net.tiagofar78.prisonescape.game.Guard;
import net.tiagofar78.prisonescape.game.prisonbuilding.regions.Region;
import net.tiagofar78.prisonescape.managers.ConfigManager;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

public abstract class Mission {

    public static Mission getRandomMission(Region region) {
        return null; // TODO add missions
    }

    private Region _region;

    public Mission(Region region) {
        _region = region;
    }

    public Region getRegion() {
        return _region;
    }

    public abstract void start(Guard guard, int missionIndex);

    public void complete(Guard guard, int missionIndex) {
        ConfigManager config = ConfigManager.getInstance();
        int reward = config.getMissionsMoneyReward();

        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(guard.getName());
        BukkitMessageSender.sendChatMessage(guard, messages.getCompletedMissionMessage(reward));

        guard.increaseBalance(reward);
        guard.removeMission(missionIndex);
    }

}
