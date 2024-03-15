package net.tiagofar78.prisonescape.game;

public enum PrisonEscapeItem {
	
	// TODO: for now all illegals are set to false
	// also need to re-check metal items
    SEARCH(false, false),
    HANDCUFS(true, false),
    SPOON(false, false),
    SHOVEL(false, false),
    MATCHES(false, false),
    BOLTS(true, false),
    DUCTTAPE(false, false),
    WIRE(true, false),
    PLASTICPLATE(false, false),
    OIL(false, false),
    STICK(false, false),
    COFFEE(false, false),
    ENERGYDRINK(false, false);

    private final boolean isMetal;
    private final boolean isIllegal;
    
	PrisonEscapeItem(boolean isMetal, boolean isIllegal) {
        this.isMetal = isMetal;
        this.isIllegal = isIllegal;
    }

    public boolean isMetal() {
        return isMetal;
    }

    public boolean isIllegal() {
        return isIllegal;
    }
}
