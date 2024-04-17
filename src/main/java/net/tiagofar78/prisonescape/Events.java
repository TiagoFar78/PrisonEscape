package net.tiagofar78.prisonescape;

import net.tiagofar78.prisonescape.game.PrisonEscapeGame;
import net.tiagofar78.prisonescape.game.prisonbuilding.ClickReturnAction;
import net.tiagofar78.prisonescape.game.prisonbuilding.PrisonEscapeLocation;
import net.tiagofar78.prisonescape.items.FunctionalItem;
import net.tiagofar78.prisonescape.items.Item;
import net.tiagofar78.prisonescape.items.ItemFactory;
import net.tiagofar78.prisonescape.managers.ConfigManager;
import net.tiagofar78.prisonescape.managers.GameManager;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Events implements Listener {

    @EventHandler
    public void playerMove(PlayerMoveEvent e) {
        PrisonEscapeGame game = GameManager.getGame();
        if (game == null) {
            return;
        }

        Location bukkitLocFrom = e.getFrom();
        int xFrom = bukkitLocFrom.getBlockX();
        int yFrom = bukkitLocFrom.getBlockY();
        int zFrom = bukkitLocFrom.getBlockZ();

        Location bukkitLoc = e.getTo();
        int x = bukkitLoc.getBlockX();
        int y = bukkitLoc.getBlockY();
        int z = bukkitLoc.getBlockZ();

        if (xFrom == x && yFrom == y && zFrom == z) {
            return;
        }

        PrisonEscapeLocation location = new PrisonEscapeLocation(x, y, z);

        game.playerMove(e.getPlayer().getName(), location);
    }

    @EventHandler
    public void playerInteractWithPrison(PlayerInteractEvent e) {
        PrisonEscapeGame game = GameManager.getGame();
        if (game == null) {
            return;
        }

        Block block = e.getClickedBlock();

        if (e.getHand() == EquipmentSlot.OFF_HAND) {
            return;
        }

        @SuppressWarnings("deprecation")
        Item itemInHand = ItemFactory.createItem(e.getPlayer().getItemInHand());
        PrisonEscapeLocation location = block == null
                ? null
                : new PrisonEscapeLocation(block.getX(), block.getY(), block.getZ());

        int returnCode = game.playerInteract(e.getPlayer().getName(), location, itemInHand, e);
        if (returnCode == 0) {
            e.setCancelled(true);
        }
    }

    @EventHandler
    public void playerLeave(PlayerQuitEvent e) {
        PrisonEscapeGame game = GameManager.getGame();
        if (game == null) {
            return;
        }

        game.playerCloseMenu(e.getPlayer().getName());
    }

    @EventHandler
    public void onPlayerLoseHealth(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            World world = player.getWorld();

            if (world.getName().equals(ConfigManager.getInstance().getWorldName())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (player.getWorld().getName().equals(ConfigManager.getInstance().getWorldName())) {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void playerCloseInventory(InventoryCloseEvent e) {
        PrisonEscapeGame game = GameManager.getGame();
        if (game == null) {
            return;
        }

        game.playerCloseMenu(e.getPlayer().getName());
    }

    @EventHandler
    public void playerClickInventory(InventoryClickEvent e) {
        PrisonEscapeGame game = GameManager.getGame();
        if (game == null) {
            return;
        }

        if (e.getClickedInventory() == null) {
            return;
        }

        boolean isPlayerInv = false;
        if (e.getClickedInventory().getType() == InventoryType.PLAYER) {
            Inventory topInv = e.getView().getTopInventory();
            if (topInv == null) {
                e.setCancelled(true);
                return;
            }

            isPlayerInv = true;
        }

        ItemStack cursor = e.getCursor();
        ItemStack current = e.getCurrentItem();

        Player player = (Player) e.getWhoClicked();
        Item item = ItemFactory.createItem(e.getCursor());
        ClickReturnAction returnAction = game.playerClickMenu(player.getName(), e.getSlot(), item, isPlayerInv);
        if (returnAction == ClickReturnAction.IGNORE) {
            return;
        }

        e.setCancelled(true);

        if (returnAction == ClickReturnAction.DELETE_HOLD_AND_SELECTED) {
            player.setItemOnCursor(null);
            e.setCurrentItem(null);
        } else if (returnAction == ClickReturnAction.CHANGE_HOLD_AND_SELECTED) {
            // NOTE: cursor and current variables must be defined before game.playerClickMenu() is executed.
            player.setItemOnCursor(current);
            e.setCurrentItem(cursor);
        }
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        if (event.toWeatherState()) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onMobSpawn(EntitySpawnEvent e) {
        Entity entity = e.getEntity();
        if (entity instanceof Player) {
            return;
        }

        if (entity.getWorld().getName().equals(ConfigManager.getInstance().getWorldName())) {
            e.setCancelled(true);
        }

        if (e.getEntityType() == EntityType.PRIMED_TNT) {
            e.setCancelled(false);
        }
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        PrisonEscapeGame game = GameManager.getGame();
        if (game == null) {
            return;
        }

        String teamChatPrefix = ConfigManager.getInstance().getTeamChatPrefix();
        String playerName = event.getPlayer().getName();
        String message = event.getMessage();

        if (message.startsWith(teamChatPrefix)) {
            game.sendTeamOnlyMessage(playerName, message.replace(teamChatPrefix, ""));
        } else {
            game.sendGeneralMessage(playerName, message);
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (GameManager.getGame() == null && event.getPlayer().hasPermission(PrisonEscape.ADMIN_PERMISSION)) {
            return;
        }

        if (event.getPlayer().getWorld().getName().equals(ConfigManager.getInstance().getWorldName())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (GameManager.getGame() == null && event.getPlayer().hasPermission(PrisonEscape.ADMIN_PERMISSION)) {
            return;
        }

        if (event.getPlayer().getWorld().getName().equals(ConfigManager.getInstance().getWorldName())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onExplosion(EntityExplodeEvent e) {
        if (!e.getEntity().getWorld().getName().equals(ConfigManager.getInstance().getWorldName())) {
            return;
        }

        e.setCancelled(true);

        PrisonEscapeGame game = GameManager.getGame();
        if (game != null) {
            game.explosion(e.blockList());
        }
    }

    @EventHandler
    public void onPlayerInteractWithPlayer(PlayerInteractEntityEvent e) {
        if (GameManager.getGame() == null) {
            return;
        }

        @SuppressWarnings("deprecation")
        Item item = ItemFactory.createItem(e.getPlayer().getItemInHand());

        if (item.isFunctional()) {
            ((FunctionalItem) item).use(e);
        }
    }

    @EventHandler
    public void onPlayerCombat(EntityDamageByEntityEvent e) {
        if (GameManager.getGame() == null) {
            return;
        }

        Entity eAttacker = e.getDamager();
        if (!(eAttacker instanceof Player)) {
            return;
        }

        Player pAttacker = (Player) eAttacker;

        @SuppressWarnings("deprecation")
        Item item = ItemFactory.createItem(pAttacker.getItemInHand());

        if (item.isFunctional()) {
            ((FunctionalItem) item).use(e);
        }
    }

}
