package net.tiagofar78.prisonescape.items;

import net.tiagofar78.prisonescape.managers.GameManager;

import org.bukkit.Material;
import org.bukkit.entity.Player;

public class SelectPrisionerTeamItem extends UsableItem {

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
        return Material.ORANGE_WOOL;
    }

    @Override
    public void use(Player player) {
        GameManager.getGame().playerSelectPrisionersTeam(player.getName());
    }

}
