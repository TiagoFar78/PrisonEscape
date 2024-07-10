package net.tiagofar78.prisonescape.missions;

import net.tiagofar78.prisonescape.bukkit.BukkitMessageSender;
import net.tiagofar78.prisonescape.game.Guard;
import net.tiagofar78.prisonescape.managers.ConfigManager;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

public abstract class Mission {

    public static Mission getRandomMission(String regionName) {
        return new ColorConnectMission(regionName);
    }

    private String _regionName;

    public Mission(String regionName) {
        _regionName = regionName;
    }

    public String getRegionName() {
        return _regionName;
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
