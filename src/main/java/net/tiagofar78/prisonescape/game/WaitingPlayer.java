package net.tiagofar78.prisonescape.game;

import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

import java.util.ArrayList;
import java.util.List;

public class WaitingPlayer extends PEPlayer {

    private TeamPreference _preference;

    public WaitingPlayer(PEGame game, String name) {
        super(game, name);
        _preference = TeamPreference.RANDOM;
    }

    @Override
    public boolean isWaiting() {
        return true;
    }

    public TeamPreference getPreference() {
        return _preference;
    }

    public void setPreference(TeamPreference preference) {
        this._preference = preference;
    }

//  ########################################
//  #              Scoreboard              #
//  ########################################

    @Override
    public ScoreboardData createScoreboardData() {
        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(getName());

        ScoreboardData sbData = super.createScoreboardData();

        List<Integer> linesIndexes = new ArrayList<>();
        List<String> linesContents = new ArrayList<>();

        int emptyLines = 1;
        List<String> baseSideBar = buildBaseSideBar(emptyLines, linesIndexes, linesContents);
        sbData.createSideBar(messages.getScoreboardDisplayName(), baseSideBar);

        return sbData;
    }

}
