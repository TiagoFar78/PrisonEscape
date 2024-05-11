package net.tiagofar78.prisonescape.items;

import net.tiagofar78.prisonescape.bukkit.BukkitMessageSender;
import net.tiagofar78.prisonescape.game.Guard;
import net.tiagofar78.prisonescape.game.PrisonEscapeGame;
import net.tiagofar78.prisonescape.game.PrisonEscapePlayer;
import net.tiagofar78.prisonescape.game.prisonbuilding.Camera;
import net.tiagofar78.prisonescape.managers.GameManager;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.List;

public class OpenCamerasItem extends FunctionalItem {

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
        return Material.ENDER_EYE;
    }

    @Override
    public void use(PlayerInteractEvent e) {
        String playerName = e.getPlayer().getName();

        PrisonEscapeGame game = GameManager.getGame();

        List<Camera> cameras = game.getPrison().getCameras();
        if (cameras.size() == 0) {
            MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(playerName);
            BukkitMessageSender.sendChatMessage(playerName, messages.getNoCamerasPlacedMessage());
            return;
        }

        PrisonEscapePlayer player = game.getPrisonEscapePlayer(playerName);
        cameras.get(0).addWatcher((Guard) player);
    }

}
