package net.tiagofar78.prisonescape.items;

import net.tiagofar78.prisonescape.bukkit.BukkitMessageSender;
import net.tiagofar78.prisonescape.game.Guard;
import net.tiagofar78.prisonescape.game.PEGame;
import net.tiagofar78.prisonescape.game.prisonbuilding.Camera;
import net.tiagofar78.prisonescape.managers.GameManager;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class OpenCamerasItem extends FunctionalItem {

    private int _currentCameraIndex = 0;

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
    public ItemStack toItemStack(MessageLanguageManager messages) {
        ItemStack item = super.toItemStack(messages);
        item.setAmount(_currentCameraIndex + 1);
        return item;
    }

    @Override
    public void use(PlayerInteractEvent e) {
        String playerName = e.getPlayer().getName();
        PEGame game = GameManager.getGame();

        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(playerName);

        List<Camera> cameras = game.getPrison().getCameras();
        if (cameras.size() == 0) {
            BukkitMessageSender.sendChatMessage(playerName, messages.getNoCamerasPlacedMessage());
            return;
        }

        Guard guard = (Guard) game.getPEPlayer(playerName);

        Action action = e.getAction();
        if (action == Action.LEFT_CLICK_BLOCK || action == Action.LEFT_CLICK_AIR) {
            _currentCameraIndex = _currentCameraIndex + 1 == cameras.size() ? 0 : _currentCameraIndex + 1;
            guard.getKit().update(playerName);
        } else if (action == Action.RIGHT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR) {
            Location loc = e.getPlayer().getLocation();

            cameras.get(_currentCameraIndex).addWatcher(guard);
            guard.startedWatchingCamera(loc);

            BukkitMessageSender.sendTitleMessage(playerName, "", messages.getSneakToLeaveCameraMessage());
        }
    }

}
