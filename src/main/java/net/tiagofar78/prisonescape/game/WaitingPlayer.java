package net.tiagofar78.prisonescape.game;

public class WaitingPlayer extends PrisonEscapePlayer {

    private TeamPreference _preference;

    public WaitingPlayer(String name) {
        super(name);
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

}
