package net.tiagofar78.prisonescape.items;

import net.tiagofar78.prisonescape.bukkit.BukkitMessageSender;
import net.tiagofar78.prisonescape.game.Guard;
import net.tiagofar78.prisonescape.game.PEGame;
import net.tiagofar78.prisonescape.game.PEPlayer;
import net.tiagofar78.prisonescape.managers.ConfigManager;
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
        return Material.POWERED_RAIL;
    }

    @Override
    public boolean isBuyable() {
        return true;
    }

    @Override
    public void use(PEGame game, PEPlayer peplayer, PlayerInteractEvent e) {
        Player player = e.getPlayer();
        String playerName = player.getName();

        Location loc = e.getPlayer().getLocation();

        Guard placer = (Guard) peplayer;
        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(playerName);
        if (game.getPrison().addTrap(placer, loc) != 0) {
            BukkitMessageSender.sendChatMessage(playerName, messages.getCannotPlaceTrapMessage());
            return;
        }

        game.getPEPlayer(playerName).removeItem(player.getInventory().getHeldItemSlot());
        BukkitMessageSender.sendChatMessage(playerName, messages.getTrapPlacedMessage());
    }

}
