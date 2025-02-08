package net.tiagofar78.prisonescape.game.phases;

import net.tiagofar78.prisonescape.bukkit.BukkitScheduler;
import net.tiagofar78.prisonescape.game.PEGame;
import net.tiagofar78.prisonescape.game.PEPlayer;
import net.tiagofar78.prisonescape.managers.ConfigManager;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

import java.util.List;

public class WaitingPhase extends Phase {

    private static final int TICKS_PER_SECOND = 20;

    @Override
    public Phase next() {
        return new OngoingPhase();
    }

    @Override
    public boolean isClockStopped() {
        return true;
    }

    @Override
    public boolean hasGameStarted() {
        return false;
    }

    @Override
    public boolean hasGameEnded() {
        return false;
    }

    @Override
    public boolean isGameDisabled() {
        return false;
    }

    @Override
    public void start(PEGame game) {
        game.getPrison().raiseWall();

        ConfigManager config = ConfigManager.getInstance();
        runScheduler(game, config.getWaitingPhaseDuration(), true);
    }

    private void runScheduler(PEGame game, int remainingSeconds, boolean isFirst) {
        ConfigManager config = ConfigManager.getInstance();

        List<PEPlayer> playersOnLobby = game.getPlayersOnLobby();

        BukkitScheduler.runSchedulerLater(new Runnable() {

            @Override
            public void run() {
                if (game.getCurrentPhase().hasGameStarted()) {
                    return;
                }

                if (remainingSeconds == 0) {
                    if (playersOnLobby.size() >= config.getMinimumPlayers()) {
                        game.startNextPhase();
                        return;
                    }

                    for (PEPlayer player : playersOnLobby) {
                        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(player.getName());
                        player.sendChatMessage(messages.getGameCancelledFewPlayersMessage());
                    }

                    game.startNextPhase(new DisabledPhase());
                    return;
                }

                if (remainingSeconds % config.getDelayBetweenAnnouncements() == 0) {
                    for (PEPlayer player : playersOnLobby) {
                        String playerName = player.getName();
                        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(playerName);
                        player.sendChatMessage(messages.getGameStartingAnnouncementMessage(remainingSeconds));
                    }
                }

                int fullLobbyWaitDuration = config.getFullLobbyWaitDuration();
                if (playersOnLobby.size() == config.getMaxPlayers() && remainingSeconds > fullLobbyWaitDuration) {
                    runScheduler(game, fullLobbyWaitDuration, false);
                }

                runScheduler(game, remainingSeconds - 1, false);
            }
        }, isFirst ? 0 : 1 * TICKS_PER_SECOND);
    }
}
