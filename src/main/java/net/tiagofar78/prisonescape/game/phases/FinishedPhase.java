package net.tiagofar78.prisonescape.game.phases;

import net.tiagofar78.prisonescape.bukkit.BukkitMessageSender;
import net.tiagofar78.prisonescape.bukkit.BukkitScheduler;
import net.tiagofar78.prisonescape.game.PEGame;
import net.tiagofar78.prisonescape.game.PEPlayer;
import net.tiagofar78.prisonescape.game.PETeam;
import net.tiagofar78.prisonescape.game.Prisoner;
import net.tiagofar78.prisonescape.managers.ConfigManager;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

import java.util.List;

public class FinishedPhase extends Phase {

    private static final int TICKS_PER_SECOND = 20;

    private PETeam<? extends PEPlayer> _winnerTeam;

    public FinishedPhase() {
        _winnerTeam = null;
    }

    public FinishedPhase(PETeam<? extends PEPlayer> winnerTeam) {
        _winnerTeam = winnerTeam;
    }

    @Override
    public Phase next() {
        return new DisabledPhase();
    }

    @Override
    public boolean isClockStopped() {
        return true;
    }

    @Override
    public boolean hasGameStarted() {
        return true;
    }

    @Override
    public boolean hasGameEnded() {
        return true;
    }

    @Override
    public boolean isGameDisabled() {
        return false;
    }

    @Override
    public void start(PEGame game) {
        if (_winnerTeam == null) {
            _winnerTeam = game.getGuardsTeam();
        }

        PETeam<Prisoner> prisonersTeam = game.getPrisonerTeam();

        boolean prisonersWon = _winnerTeam.getName().equals(prisonersTeam.getName());
        int playersInPrison = (int) prisonersTeam.getMembers().stream().filter(p -> p.isImprisioned()).count();

        for (PEPlayer player : game.getPlayersOnLobby()) {
            MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(player.getName());

            String title;
            String subtitle;
            if (prisonersWon) {
                title = messages.getPrisonersWonTitle();
                subtitle = messages.getPrisonersWonSubtitle();
            } else {
                title = messages.getPoliceWonTitle();
                subtitle = messages.getPoliceWonSubtitle(playersInPrison);
            }

            List<String> resultMessage = messages.getGameResultMessage(_winnerTeam.isOnTeam(player));

            BukkitMessageSender.sendTitleMessage(player.getName(), title, subtitle);
            BukkitMessageSender.sendChatMessage(player, resultMessage);
        }

        int finishedPhaseDuration = ConfigManager.getInstance().getFinishedPhaseDuration();
        BukkitScheduler.runSchedulerLater(new Runnable() {

            @Override
            public void run() {
                if (!game.getCurrentPhase().isGameDisabled()) {
                    game.startNextPhase();
                }
            }
        }, finishedPhaseDuration * TICKS_PER_SECOND);
    }
}
