package net.tiagofar78.prisonescape.listener;

import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.TNTPrimed;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.hanging.HangingBreakEvent;
import org.bukkit.event.weather.WeatherChangeEvent;

import net.tiagofar78.prisonescape.PrisonEscape;
import net.tiagofar78.prisonescape.dataobjects.PlayerInGame;
import net.tiagofar78.prisonescape.game.PEGame;
import net.tiagofar78.prisonescape.managers.ConfigManager;
import net.tiagofar78.prisonescape.managers.GameManager;

public class WorldListener implements Listener {

    public static final EntityType[] ALLOWED_ENTITIES = {
            EntityType.PRIMED_TNT,
            EntityType.PAINTING,
            EntityType.ARMOR_STAND,
            EntityType.ITEM_FRAME
        };

    @EventHandler
    public void onPlayerLoseHealth(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            World world = player.getWorld();

            if (world.getName().equals(ConfigManager.getInstance().getWorldName())) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent e) {
        if (e.getEntity().getWorld().getName().equals(ConfigManager.getInstance().getWorldName())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent e) {
        if (e.toWeatherState()) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onMobSpawn(EntitySpawnEvent e) {
        Entity entity = e.getEntity();
        if (entity instanceof Player) {
            return;
        }

        EntityType eType = e.getEntityType();
        for (EntityType allowedEntity : ALLOWED_ENTITIES) {
            if (eType == allowedEntity) {
                return;
            }
        }

        if (entity.getWorld().getName().equals(ConfigManager.getInstance().getWorldName())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        PlayerInGame playerInGame = GameManager.getPlayerInGame(e.getPlayer().getName());
        if (playerInGame == null && e.getPlayer().hasPermission(PrisonEscape.ADMIN_PERMISSION)) {
            return;
        }

        if (e.getPlayer().getWorld().getName().equals(ConfigManager.getInstance().getWorldName())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        PlayerInGame playerInGame = GameManager.getPlayerInGame(e.getPlayer().getName());
        if (playerInGame == null && e.getPlayer().hasPermission(PrisonEscape.ADMIN_PERMISSION)) {
            return;
        }

        if (e.getPlayer().getWorld().getName().equals(ConfigManager.getInstance().getWorldName())) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockHangingBreak(HangingBreakEvent e) {
        EntityType type = e.getEntity().getType();
        for (EntityType eType : ALLOWED_ENTITIES) {
            if (eType == type) {
                e.setCancelled(true);
                break;
            }
        }
    }

    @EventHandler
    public void onExplosion(EntityExplodeEvent e) {
        Entity entity = e.getEntity();
        String worldName = ConfigManager.getInstance().getWorldName();
        if (!entity.getWorld().getName().equals(worldName) || entity.getType() != EntityType.PRIMED_TNT) {
            return;
        }

        e.setCancelled(true);
        
        TNTPrimed bomb = (TNTPrimed) entity;
        PEGame game = GameManager.getGamePlayerWas(bomb.getSource().getName());
        if (game == null) {
            return;
        }

        game.getPrison().removeExplodedBlocks(e.blockList());
    }

}
