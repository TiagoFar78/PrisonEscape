package net.tiagofar78.prisonescape.game;

import net.tiagofar78.prisonescape.managers.ConfigManager;

public class Prisioner extends PrisonEscapePlayer {

    private boolean _hasEscaped;
    private boolean _isWanted;
    private boolean _inRestrictedArea;

    private PrisonEscapePlayer _tradeRequestTarget;
    private long _tradeRequestTime;

    public Prisioner(String name) {
        super(name);
        _hasEscaped = false;
        _isWanted = false;
        _inRestrictedArea = false;
    }

    @Override
    public boolean isPrisioner() {
        return true;
    }

    public boolean isImprisioned() {
        return !hasEscaped() && isOnline();
    }

//  ########################################
//  #                Escape                #
//  ########################################

    public boolean hasEscaped() {
        return _hasEscaped;
    }

    public void escaped() {
        _hasEscaped = true;
    }

//  ########################################
//  #              Item Trade              #
//  ########################################

    public boolean hasBeenRequestedBy(PrisonEscapePlayer player) {
        return _tradeRequestTarget != null && player.equals(_tradeRequestTarget);
    }

    public boolean isStillValidRequest() {
        int timeout = ConfigManager.getInstance().getTradeRequestTimeout();
        long secondsPast = (System.currentTimeMillis() - _tradeRequestTime) * 1000;
        return _tradeRequestTime != -1 && secondsPast <= timeout;
    }

    public void sendRequest(PrisonEscapePlayer target) {
        _tradeRequestTarget = target;
        _tradeRequestTime = System.currentTimeMillis();
    }

    public void clearRequest() {
        _tradeRequestTarget = null;
        _tradeRequestTime = -1;
    }

//  ########################################
//  #                Wanted                #
//  ########################################

    public boolean isWanted() {
        return _isWanted;
    }

    public void setWanted() {
        _isWanted = true;
    }

    public void removeWanted() {
        _isWanted = false;
    }

    public boolean isInRestrictedArea() {
        return _inRestrictedArea;
    }

    public void enteredRestrictedArea() {
        _inRestrictedArea = true;
    }

    public void leftRestrictedArea() {
        _inRestrictedArea = false;
    }

    public boolean canBeArrested() {
        return _isWanted || _inRestrictedArea;
    }

}
