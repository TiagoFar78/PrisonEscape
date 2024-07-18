package net.tiagofar78.prisonescape.items;

import net.tiagofar78.prisonescape.game.PEGame;
import net.tiagofar78.prisonescape.game.PEPlayer;
import net.tiagofar78.prisonescape.game.TeamPreference;
import net.tiagofar78.prisonescape.game.WaitingPlayer;
import net.tiagofar78.prisonescape.managers.GameManager;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class SelectPoliceTeamItem extends FunctionalItem {

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
    public ItemStack toItemStack(PEPlayer player) {
        WaitingPlayer waitingPlayer = (WaitingPlayer) player;
        ItemStack item = super.toItemStack(player);

        if (waitingPlayer.getPreference() == TeamPreference.POLICE) {
            item.addUnsafeEnchantment(Enchantment.DURABILITY, 1);
        } else {
            item.removeEnchantment(Enchantment.DURABILITY);
        }

        return item;
    }

    @Override
    public void use(PlayerInteractEvent e) {
        PEGame game = GameManager.getGame();
        String playerName = e.getPlayer().getName();

        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(playerName);
        String message = messages.getSelectedPoliceTeamMessage();

        game.updateTeamPreference(playerName, message, TeamPreference.POLICE, PEGame.GUARDS_TEAM_NAME);
    }

}
