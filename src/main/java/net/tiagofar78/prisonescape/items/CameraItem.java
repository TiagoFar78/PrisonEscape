package net.tiagofar78.prisonescape.items;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

import net.tiagofar78.prisonescape.bukkit.BukkitMessageSender;
import net.tiagofar78.prisonescape.game.PEGame;
import net.tiagofar78.prisonescape.game.PEPlayer;
import net.tiagofar78.prisonescape.managers.ConfigManager;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

public class CameraItem extends FunctionalItem implements Buyable {

    @Override
    public int getPrice() {
        return ConfigManager.getInstance().getCameraPrice();
    }

    @Override
    public int getLimit() {
        return ConfigManager.getInstance().getCameraLimit();
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
        return Material.OBSERVER;
    }

    @Override
    public boolean isBuyable() {
        return true;
    }

    @Override
    public void use(PEGame game, PEPlayer player, PlayerInteractEvent e) {
        Location loc = e.getPlayer().getLocation();

        game.getPrison().addCamera(loc);
        player.removeItem(e.getPlayer().getInventory().getHeldItemSlot());

        String playerName = e.getPlayer().getName();
        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(playerName);
        BukkitMessageSender.sendChatMessage(playerName, messages.getCameraPlacedMessage());
    }

}
