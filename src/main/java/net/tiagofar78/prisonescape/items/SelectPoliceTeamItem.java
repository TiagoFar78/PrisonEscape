package net.tiagofar78.prisonescape.items;

import net.tiagofar78.prisonescape.managers.GameManager;

import org.bukkit.Material;

public class SelectPoliceTeamItem extends UsableItem {

    @Override
    public boolean isMetalic() {
        return false;
    }

    @Override
    public boolean isIllegal() {
        return false;
    }

    @Override
    public Material getMaterial() {
        return Material.BLUE_WOOL;
    }

    @Override
    public void use(String playerName) {
        GameManager.getGame().playerSelectPoliceTeam(playerName);
    }

}
