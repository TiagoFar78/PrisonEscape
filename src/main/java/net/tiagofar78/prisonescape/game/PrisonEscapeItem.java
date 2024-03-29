package net.tiagofar78.prisonescape.game;

import net.tiagofar78.prisonescape.managers.ConfigManager;

public enum PrisonEscapeItem {

    SELECT_PRISIONER_TEAM(false, false, false),
    SELECT_POLICE_TEAM(false, false, false),
    SELECT_NONE_TEAM(false, false, false),

    // TODO: for now all illegals are set to false
    // also need to re-check metal items and rare items
    SEARCH(false, false, false),
    HANDCUFS(true, false, false),
    PLASTIC_SPOON(false, false, false),
    METAL_SPOON(true, false, false),
    PLASTIC_SHOVEL(false, false, false),
    METAL_SHOVEL(false, false, false),
    MATCHES(false, false, false),
    BOLTS(true, false, false),
    DUCTTAPE(false, false, false),
    WIRE(true, false, false),
    PLASTIC_PLATE(false, false, false),
    OIL(false, false, false),
    STICK(false, false, false),
    COFFEE(false, false, false),
    ENERGY_DRINK(false, false, false);

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

    @Override
    public String toString() {
        return name().toLowerCase().replace("_", " ");
    }
}
