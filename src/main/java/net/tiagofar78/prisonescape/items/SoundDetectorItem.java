package net.tiagofar78.prisonescape.items;

import net.tiagofar78.prisonescape.bukkit.BukkitMessageSender;
import net.tiagofar78.prisonescape.game.PrisonEscapeGame;
import net.tiagofar78.prisonescape.game.prisonbuilding.PrisonEscapeLocation;
import net.tiagofar78.prisonescape.managers.ConfigManager;
import net.tiagofar78.prisonescape.managers.GameManager;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

public class SoundDetectorItem extends FunctionalItem implements Buyable {

    @Override
    public int getPrice() {
        return ConfigManager.getInstance().getSensorPrice();
    }

    @Override
    public int getLimit() {
        return ConfigManager.getInstance().getSensorLimit();
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
        return Material.LIGHTNING_ROD;
    }

    @Override
    public boolean isBuyable() {
        return true;
    }

    @Override
    public void use(PlayerInteractEvent e) {
        Location loc = e.getPlayer().getLocation();
        PrisonEscapeLocation peLocation = new PrisonEscapeLocation(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());

        PrisonEscapeGame game = GameManager.getGame();
        game.getPrison().addSoundDetector(peLocation);

        String playerName = e.getPlayer().getName();

        game.getPrisonEscapePlayer(playerName).removeItem(e.getPlayer().getInventory().getHeldItemSlot());

        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(playerName);
        BukkitMessageSender.sendChatMessage(playerName, messages.getSoundDetectorPlacedMessage());
    }

}
