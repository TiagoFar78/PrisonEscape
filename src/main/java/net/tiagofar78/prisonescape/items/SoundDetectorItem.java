package net.tiagofar78.prisonescape.items;

import net.tiagofar78.prisonescape.bukkit.BukkitMessageSender;
import net.tiagofar78.prisonescape.game.PEGame;
import net.tiagofar78.prisonescape.game.PEPlayer;
import net.tiagofar78.prisonescape.managers.ConfigManager;
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
    public void use(PEGame game, PEPlayer player, PlayerInteractEvent e) {
        String playerName = e.getPlayer().getName();
        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(playerName);

        Location loc = e.getPlayer().getLocation();
        if (loc.getBlock().getType() != Material.AIR) {
            BukkitMessageSender.sendChatMessage(playerName, messages.getInvalidSoundDetectorLocMessage());
            return;
        }

        game.getPrison().addSoundDetector(loc);
        player.removeItem(e.getPlayer().getInventory().getHeldItemSlot());

        BukkitMessageSender.sendChatMessage(playerName, messages.getSoundDetectorPlacedMessage());
    }

}
