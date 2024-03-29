package net.tiagofar78.prisonescape.game;

import net.tiagofar78.prisonescape.managers.ConfigManager;

public class Settings {

    private int _secondsInSolitary;
    private int _daysAmount;
    private int _dayDuration;
    private int _nightDuration;

    public Settings() {
        ConfigManager config = ConfigManager.getInstance();

        _secondsInSolitary = config.getSecondsInSolitary();
        _daysAmount = config.getDaysAmount();
        _dayDuration = config.getDayDuration();
        _nightDuration = config.getNightDuration();
    }

    public int getSecondsInSolitary() {
        return _secondsInSolitary;
    }

    public int getDays() {
        return _daysAmount;
    }

    public int getDayDuration() {
        return _dayDuration;
    }

    public int getNightDuration() {
        return _nightDuration;
    }

}
