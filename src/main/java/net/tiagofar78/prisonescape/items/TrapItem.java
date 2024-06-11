package net.tiagofar78.prisonescape.items;

import net.tiagofar78.prisonescape.bukkit.BukkitMessageSender;
import net.tiagofar78.prisonescape.game.PEGame;
import net.tiagofar78.prisonescape.managers.ConfigManager;
import net.tiagofar78.prisonescape.managers.GameManager;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

public class TrapItem extends FunctionalItem implements Buyable {

    @Override
    public int getPrice() {
        return ConfigManager.getInstance().getTrapPrice();
    }

    @Override
    public int getLimit() {
        return ConfigManager.getInstance().getTrapLimit();
    }

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
        return Material.COBWEB;
    }

    @Override
    public boolean isBuyable() {
        return true;
    }

    @Override
    public void use(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        String playerName = player.getName();

        Location loc = player.getLocation();

        PEGame game = GameManager.getGame();
        game.getPrison().addTrap(loc);
        game.getPEPlayer(playerName).removeItem(player.getInventory().getHeldItemSlot());

        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(playerName);
        BukkitMessageSender.sendChatMessage(playerName, messages.getTrapPlacedMessage());
    }

}
