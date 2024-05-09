package net.tiagofar78.prisonescape.game;

public class Prisioner extends PrisonEscapePlayer {

    private boolean _hasEscaped;
    private boolean _isWanted;
    private boolean _inRestrictedArea;

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
