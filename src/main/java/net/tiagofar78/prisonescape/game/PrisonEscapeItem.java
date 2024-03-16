package net.tiagofar78.prisonescape.game;

import net.tiagofar78.prisonescape.managers.ConfigManager;

public enum PrisonEscapeItem {
	
	// TODO: for now all illegals are set to false
	// also need to re-check metal items and rare items
    SEARCH(false, false, false),
    HANDCUFS(true, false, false),
    SPOON(false, false, false),
    SHOVEL(false, false, false),
    MATCHES(false, false, false),
    BOLTS(true, false, false),
    DUCTTAPE(false, false, false),
    WIRE(true, false, false),
    PLASTICPLATE(false, false, false),
    OIL(false, false, false),
    STICK(false, false, false),
    COFFEE(false, false, false),
    ENERGYDRINK(false, false, false);

    private final boolean _isMetal;
    private final boolean _isIllegal;
    private final boolean _isRare;
    
	PrisonEscapeItem(boolean isMetal, boolean isIllegal, boolean isRare) {
        _isMetal = isMetal;
        _isIllegal = isIllegal;
        _isRare = isRare;
    }

    public boolean isMetal() {
        return _isMetal;
    }

    public boolean isIllegal() {
        return _isIllegal;
    }

    public boolean isRare() {
        return _isRare;
    }

    public double getProbability() {
        ConfigManager config = ConfigManager.getInstance();
        if (this.isRare()) {
            return config.getRareItemsProbability();
        }
        return config.getCommonItemsProbability();
    }
}
