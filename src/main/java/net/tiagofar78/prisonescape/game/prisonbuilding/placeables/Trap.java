package net.tiagofar78.prisonescape.game.prisonbuilding.placeables;

import net.tiagofar78.prisonescape.bukkit.BukkitMessageSender;
import net.tiagofar78.prisonescape.bukkit.BukkitScheduler;
import net.tiagofar78.prisonescape.game.PEPlayer;
import net.tiagofar78.prisonescape.managers.ConfigManager;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class Trap {

    private static final int TICKS_PER_SECOND = 20;

    private Location _location;
    private boolean _caughtAPrisoner;
    private boolean _placed;
    private Block trap;

    public Trap(Location location) {
        _location = location;
        _caughtAPrisoner = false;
        create();
    }

    public Location getLocation() {
        return _location;
    }

    public boolean wasPlaced() {
        return _placed;
    }

    public void create() {
        trap = _location.clone().add(0, 1, 0).getBlock();
        if (!trap.getType().equals(Material.AIR)) {
            _placed = false;
            return;
        }
        trap.setType(Material.POWERED_RAIL);
        _placed = true;
    }

    public void delete() {
        trap.setType(Material.AIR);
    }

    public void triggerTrap(PEPlayer player) {
        if (_caughtAPrisoner) {
            return;
        }
        _caughtAPrisoner = true;
        player.restrictMovement();

        int trapDuration = ConfigManager.getInstance().getTrapDuration();
        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(player.getName());
        BukkitMessageSender.sendChatMessage(player, messages.getCaughtInATrapMessage(trapDuration));

        BukkitScheduler.runSchedulerLater(new Runnable() {
            @Override
            public void run() {
                player.allowMovement();
                BukkitMessageSender.sendChatMessage(player, messages.getCanMoveFreelyMessage());
            }
        }, trapDuration * TICKS_PER_SECOND);

        this.delete();
    }

}
