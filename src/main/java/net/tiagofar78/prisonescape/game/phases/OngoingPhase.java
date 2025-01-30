package net.tiagofar78.prisonescape.game.phases;

import net.tiagofar78.prisonescape.bukkit.BukkitMessageSender;
import net.tiagofar78.prisonescape.game.DayPeriod;
import net.tiagofar78.prisonescape.game.Guard;
import net.tiagofar78.prisonescape.game.PEGame;
import net.tiagofar78.prisonescape.game.PEPlayer;
import net.tiagofar78.prisonescape.game.PETeam;
import net.tiagofar78.prisonescape.game.Prisoner;
import net.tiagofar78.prisonescape.game.TeamPreference;
import net.tiagofar78.prisonescape.game.WaitingPlayer;
import net.tiagofar78.prisonescape.game.prisonbuilding.PrisonBuilding;
import net.tiagofar78.prisonescape.managers.ConfigManager;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class OngoingPhase extends Phase {

    @Override
    public Phase next() {
        return new FinishedPhase();
    }

    @Override
    public boolean isClockStopped() {
        return false;
    }

    @Override
    public boolean hasGameStarted() {
        return true;
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
        distributePlayersPerTeam(game);

        PETeam<Prisoner> prisonersTeam = game.getPrisonerTeam();
        for (Prisoner player : prisonersTeam.getMembers()) {
            MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(player.getName());
            BukkitMessageSender.sendChatMessage(player, messages.getPrisonerGameStartedMessage());

            game.addPrisonerToStartedGame(player, DayPeriod.DAY);
        }

        for (Guard player : game.getGuardsTeam().getMembers()) {
            MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(player.getName());
            BukkitMessageSender.sendChatMessage(player, messages.getPoliceGameStartedMessage());

            game.addGuardToStartedGame(player, DayPeriod.DAY);
        }

        PrisonBuilding prison = game.getPrison();
        prison.addVaults(prisonersTeam.getMembers());
        prison.putRandomCracks();

        game.startDay();
    }

    public void distributePlayersPerTeam(PEGame game) {
        List<PEPlayer> playersOnLobby = game.getPlayersOnLobby();

        int numberOfPlayers = playersOnLobby.size();
        int requiredPrisoners = (int) Math.round(numberOfPlayers * ConfigManager.getInstance().getPrisonerRatio());
        int requiredOfficers = (int) Math.round(numberOfPlayers * ConfigManager.getInstance().getOfficerRatio());

        Collections.shuffle(playersOnLobby);
        List<PEPlayer> remainingPlayers = new ArrayList<>();
        List<PEPlayer> newLobbyPlayers = new ArrayList<>();

        PETeam<Prisoner> prisonersTeam = game.getPrisonerTeam();
        PETeam<Guard> guardsTeam = game.getGuardsTeam();

        for (PEPlayer player : playersOnLobby) {
            WaitingPlayer waitingPlayer = (WaitingPlayer) player;

            TeamPreference preference = waitingPlayer.getPreference();

            if (preference == TeamPreference.POLICE && requiredOfficers != 0) {
                Guard guard = new Guard(game, player.getName());
                guardsTeam.addMember(guard);
                newLobbyPlayers.add(guard);
                requiredOfficers--;
            } else if (preference == TeamPreference.PRISIONERS && requiredPrisoners != 0) {
                Prisoner prisoner = new Prisoner(game, player.getName());
                prisonersTeam.addMember(prisoner);
                newLobbyPlayers.add(prisoner);
                requiredPrisoners--;
            } else {
                remainingPlayers.add(player);
            }
        }

        for (PEPlayer player : remainingPlayers) {
            if (requiredPrisoners != 0) {
                Prisoner prisoner = new Prisoner(game, player.getName());
                prisonersTeam.addMember(prisoner);
                newLobbyPlayers.add(prisoner);
                requiredPrisoners--;
            } else {
                Guard guard = new Guard(game, player.getName());
                guardsTeam.addMember(guard);
                newLobbyPlayers.add(guard);
                requiredOfficers--;
            }
        }

        game.setPlayersOnLobby(newLobbyPlayers);
    }
}
