package net.tiagofar78.prisonescape.game.prisonbuilding.placeables;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;

import net.tiagofar78.prisonescape.PEResources;
import net.tiagofar78.prisonescape.PrisonEscape;
import net.tiagofar78.prisonescape.bukkit.BukkitMessageSender;
import net.tiagofar78.prisonescape.bukkit.BukkitScheduler;
import net.tiagofar78.prisonescape.game.Guard;
import net.tiagofar78.prisonescape.game.PEPlayer;
import net.tiagofar78.prisonescape.managers.ConfigManager;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

public class Trap {

    private static final int EDGE_SEGMENTS = 4;
    private static final int SQUARE_SEPARATIONS = 3;
    private static final int TICKS_PER_SECOND = 20;

    private Guard _placer;
    private Location _location;
    private boolean _caughtAPrisoner;
    private boolean _isActive;

    public Trap(Guard placer, Location location) {
        _placer = placer;
        _location = location;
        _caughtAPrisoner = false;
        _isActive = true;
        loopCreateParticles(PEResources.getWorld());
    }

    public Location getLocation() {
        return _location;
    }
    
    private void loopCreateParticles(World world) {
        if (!_isActive) {
            return;
        }
        
        int y = _location.getBlockY();
        
        for (int i = 0; i <= EDGE_SEGMENTS; i++) {
            for (int j = 0; j < 3; j += 2) {
                double x = (double) _location.getBlockX() + (double) i / (double) EDGE_SEGMENTS;
                double z = _location.getBlockZ();
                world.spawnParticle(Particle.REDSTONE, x, y + j, z, 1, new Particle.DustOptions(Color.GRAY, 1));
                
                x = _location.getBlockX();
                z = (double) _location.getBlockZ() + (double) i / (double) EDGE_SEGMENTS;
                world.spawnParticle(Particle.REDSTONE, x, y + j, z, 1, new Particle.DustOptions(Color.GRAY, 1));
                
                x = (double) _location.getBlockX() + (double) i / (double) EDGE_SEGMENTS;
                z = _location.getBlockZ() + 1;
                world.spawnParticle(Particle.REDSTONE, x, y + j, z, 1, new Particle.DustOptions(Color.GRAY, 1));
                
                x = _location.getBlockX() + 1;
                z = (double) _location.getBlockZ() + (double) i / (double) EDGE_SEGMENTS;
                world.spawnParticle(Particle.REDSTONE, x, y + j, z, 1, new Particle.DustOptions(Color.GRAY, 1));
            }
            
            for (int j = 1; j < SQUARE_SEPARATIONS; j++) {
                double x = (double) _location.getBlockX() + (double) j / (double) SQUARE_SEPARATIONS;
                double z = (double) _location.getBlockZ() + (double) i / (double) EDGE_SEGMENTS;
                world.spawnParticle(Particle.REDSTONE, x, y + 2, z, 1, new Particle.DustOptions(Color.GRAY, 1));
                
                for (int height = 0; height < 2; height++) {
                    for (int k = 0; k < 2; k++) {
                        double y2 = (double) y + (double) height + (double) i / (double) EDGE_SEGMENTS;
                        
                        x = (double) _location.getBlockX() + (double) j / (double) SQUARE_SEPARATIONS;
                        z = _location.getBlockZ() + k;
                        world.spawnParticle(Particle.REDSTONE, x, y2, z, 1, new Particle.DustOptions(Color.GRAY, 1));
                        
                        x = _location.getBlockX() + k;
                        z = (double) _location.getBlockZ() + (double) j / (double) SQUARE_SEPARATIONS;
                        world.spawnParticle(Particle.REDSTONE, x, y2, z, 1, new Particle.DustOptions(Color.GRAY, 1));
                        
                        x = _location.getBlockX() + k;
                        z = _location.getBlockZ();
                        world.spawnParticle(Particle.REDSTONE, x, y2, z, 1, new Particle.DustOptions(Color.GRAY, 1));

                        x = _location.getBlockX() + k;
                        z = _location.getBlockZ() + 1;
                        world.spawnParticle(Particle.REDSTONE, x, y2, z, 1, new Particle.DustOptions(Color.GRAY, 1));
                    }
                }
            }
        }

        Bukkit.getScheduler().runTaskLater(PrisonEscape.getPrisonEscape(), new Runnable() {

            @Override
            public void run() {
                loopCreateParticles(world);
            }

        }, 10);
    }

    public void delete() {
        _isActive = false;
    }

    public void triggerTrap(List<Guard> guards, PEPlayer player) {
        if (_caughtAPrisoner) {
            return;
        }
        
        _caughtAPrisoner = true;
        player.restrictMovement();
        _placer.increaseTrapLimit();
        
        for (Guard guard : guards) {
            MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(guard.getName());
            BukkitMessageSender.sendChatMessage(guard, messages.getTrapTriggeredMessage());
        }

        int trapDuration = ConfigManager.getInstance().getTrapDuration();
        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(player.getName());
        BukkitMessageSender.sendChatMessage(player, messages.getCaughtInATrapMessage(trapDuration));

        BukkitScheduler.runSchedulerLater(new Runnable() {
            @Override
            public void run() {
                player.allowMovement();
                BukkitMessageSender.sendChatMessage(player, messages.getCanMoveFreelyMessage());
                delete();
            }
        }, trapDuration * TICKS_PER_SECOND);
    }

}
