package net.tiagofar78.prisonescape.items;

import net.tiagofar78.prisonescape.managers.GameManager;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public class SelectNoneTeamItem extends UsableItem {

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
        return Material.GRAY_WOOL;
    }

    @Override
    public void use(Player player) {
        GameManager.getGame().playerRemovedTeamPreference(player.getName());
    }

}
