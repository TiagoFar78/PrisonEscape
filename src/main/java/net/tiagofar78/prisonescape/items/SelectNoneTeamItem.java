package net.tiagofar78.prisonescape.items;

import net.tiagofar78.prisonescape.game.PEGame;
import net.tiagofar78.prisonescape.game.PEPlayer;
import net.tiagofar78.prisonescape.game.TeamPreference;
import net.tiagofar78.prisonescape.game.WaitingPlayer;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class SelectNoneTeamItem extends FunctionalItem {

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
    public ItemStack toItemStack(PEPlayer player) {
        WaitingPlayer waitingPlayer = (WaitingPlayer) player;
        ItemStack item = super.toItemStack(player);

        if (waitingPlayer.getPreference() == TeamPreference.RANDOM) {
            item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        } else {
            item.removeEnchantment(Enchantment.DURABILITY);
        }

        return item;
    }

    @Override
    public void use(PEGame game, PEPlayer player, PlayerInteractEvent e) {
        String playerName = e.getPlayer().getName();

        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(playerName);
        String message = messages.getRemovedTeamPreferenceMessage();

        game.updateTeamPreference(playerName, message, TeamPreference.RANDOM, PEGame.WAITING_TEAM_NAME);
    }

}
