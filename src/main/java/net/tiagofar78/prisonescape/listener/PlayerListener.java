package net.tiagofar78.prisonescape.listener;

import net.tiagofar78.prisonescape.bukkit.BukkitMessageSender;
import net.tiagofar78.prisonescape.bukkit.BukkitSoundBoard;
import net.tiagofar78.prisonescape.bukkit.BukkitTeleporter;
import net.tiagofar78.prisonescape.game.Guard;
import net.tiagofar78.prisonescape.game.PEGame;
import net.tiagofar78.prisonescape.game.PEPlayer;
import net.tiagofar78.prisonescape.game.Prisoner;
import net.tiagofar78.prisonescape.game.phases.Phase;
import net.tiagofar78.prisonescape.game.prisonbuilding.Chest;
import net.tiagofar78.prisonescape.game.prisonbuilding.Helicopter;
import net.tiagofar78.prisonescape.game.prisonbuilding.Obstacle;
import net.tiagofar78.prisonescape.game.prisonbuilding.PrisonBuilding;
import net.tiagofar78.prisonescape.game.prisonbuilding.Regenerable;
import net.tiagofar78.prisonescape.game.prisonbuilding.Vault;
import net.tiagofar78.prisonescape.game.prisonbuilding.WallCrack;
import net.tiagofar78.prisonescape.game.prisonbuilding.doors.ClickDoorReturnAction;
import net.tiagofar78.prisonescape.game.prisonbuilding.doors.Door;
import net.tiagofar78.prisonescape.game.prisonbuilding.placeables.SoundDetector;
import net.tiagofar78.prisonescape.items.FunctionalItem;
import net.tiagofar78.prisonescape.items.Item;
import net.tiagofar78.prisonescape.items.SearchItem;
import net.tiagofar78.prisonescape.items.ToolItem;
import net.tiagofar78.prisonescape.managers.ConfigManager;
import net.tiagofar78.prisonescape.managers.GameManager;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;
import net.tiagofar78.prisonescape.menus.ClickReturnAction;
import net.tiagofar78.prisonescape.menus.Clickable;
import net.tiagofar78.prisonescape.menus.TradeMenu;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener {

//  ########################################
//  #                 Move                 #
//  ########################################

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {
        PEGame game = GameManager.getGame();
        if (game == null) {
            return;
        }

        String playerName = e.getPlayer().getName();
        PEPlayer player = game.getPEPlayer(playerName);
        if (player == null) {
            return;
        }

        Phase phase = game.getCurrentPhase();
        if (!phase.hasGameStarted() || phase.hasGameEnded()) {
            return;
        }

        Location locTo = e.getTo();
        if (isInSameBlock(e.getFrom(), locTo)) {
            return;
        }

        if (!player.canMove()) {
            e.setCancelled(true);
            return;
        }

        PrisonBuilding prison = game.getPrison();
        for (SoundDetector soundDetector : prison.getSoundDetectors()) {
            soundDetector.playerMoved(player, locTo);
        }

        if (!game.isPrisoner(player)) {
            return;
        }

        Prisoner prisoner = (Prisoner) player;

        if (prisoner.hasEscaped()) {
            return;
        }

        prison.checkIfWalkedOverTrap(locTo, player);

        if (prison.isOutsidePrison(locTo)) {
            game.playerEscaped(prisoner);
        }

        if (prison.isInRestrictedArea(locTo)) {
            prisoner.enteredRestrictedArea();
        } else if (prisoner.isInRestrictedArea()) {
            prisoner.leftRestrictedArea();
        }

        if (prison.checkIfWalkedOverMetalDetector(locTo)) {
            playerWalkedOverMetalDetector(prisoner, locTo);
        }
    }

    private boolean isInSameBlock(Location loc1, Location loc2) {
        return loc1.getBlockX() == loc2.getBlockX() && loc1.getBlockY() == loc2.getBlockY() &&
                loc1.getBlockZ() == loc2.getBlockZ();
    }

    private void playerWalkedOverMetalDetector(Prisoner prisoner, Location loc) {
        if (prisoner.hasMetalItems()) {
            BukkitSoundBoard.playMetalDetectorSound(loc);
        }
    }

//  ########################################
//  #         Interact With Player         #
//  ########################################

    @EventHandler
    public void onPlayerInteractWithPlayer(PlayerInteractEntityEvent e) {
        PEGame game = GameManager.getGame();
        if (game == null) {
            return;
        }

        PEPlayer player = game.getPEPlayer(e.getPlayer().getName());
        if (player == null) {
            return;
        }

        PEPlayer clickedPlayer = game.getPEPlayer(e.getRightClicked().getName());
        if (clickedPlayer != null) {
            if (player.isPrisoner() && clickedPlayer.isPrisoner() && player.isSneaking()) {
                Prisoner sender = (Prisoner) player;
                Prisoner target = (Prisoner) clickedPlayer;

                if (sender.hasBeenRequestedBy(target) && sender.isStillValidRequest()) {
                    sender.clearRequest();
                    target.clearRequest();
                    new TradeMenu(target, sender);
                    return;
                }

                target.sendRequest(sender);

                String senderName = sender.getName();
                String targetName = target.getName();

                MessageLanguageManager senderMessages = MessageLanguageManager.getInstanceByPlayer(senderName);
                BukkitMessageSender.sendChatMessage(sender, senderMessages.getTradeRequestSentMessage(targetName));

                int time = ConfigManager.getInstance().getTradeRequestTimeout();
                MessageLanguageManager targetMessages = MessageLanguageManager.getInstanceByPlayer(targetName);
                BukkitMessageSender.sendChatMessage(
                        target,
                        targetMessages.getTradeRequestReceivedMessage(senderName, time)
                );

                return;
            }
        }

        Item item = player.getItemAt(e.getPlayer().getInventory().getHeldItemSlot());
        if (item.isFunctional()) {
            ((FunctionalItem) item).use(e);
        }
    }

    @EventHandler(ignoreCancelled = false)
    public void onPlayerCombat(EntityDamageByEntityEvent e) {
        PEGame game = GameManager.getGame();
        if (game == null) {
            return;
        }

        Entity eAttacker = e.getDamager();
        if (!(eAttacker instanceof Player)) {
            return;
        }

        EntityType type = e.getEntity().getType();
        for (EntityType mobType : WorldListener.ALLOWED_MOBS) {
            if (mobType == type) {
                e.setCancelled(true);
                break;
            }
        }

        Player pAttacker = (Player) eAttacker;

        PEPlayer player = game.getPEPlayer(pAttacker.getName());
        if (player == null) {
            return;
        }

        Item item = player.getItemAt(pAttacker.getInventory().getHeldItemSlot());

        if (item.isFunctional()) {
            ((FunctionalItem) item).use(e);
        }
    }

//  #########################################
//  #          Interact With Block          #
//  #########################################

    @EventHandler
    public void playerInteractWithPrison(PlayerInteractEvent e) {
        PEGame game = GameManager.getGame();
        if (game == null) {
            return;
        }

        PEPlayer player = game.getPEPlayer(e.getPlayer().getName());
        if (player == null) {
            return;
        }

        if (e.getAction() == Action.PHYSICAL) {
            return;
        }

        if (e.getHand() == EquipmentSlot.OFF_HAND) {
            return;
        }

        int itemSlot = e.getPlayer().getInventory().getHeldItemSlot();

        Block block = e.getClickedBlock();
        Location location = block == null ? null : block.getLocation();

        playerInteract(game, player, location, itemSlot, e);

        e.setCancelled(true);
    }

    private void playerInteract(
            PEGame game,
            PEPlayer player,
            Location blockLocation,
            int itemSlot,
            PlayerInteractEvent e
    ) {
        if (blockLocation != null) {
            if (!interactWithPrison(game, player, blockLocation, itemSlot)) {
                return;
            }
        }

        Item item = player.getItemAt(itemSlot);
        if (item.isFunctional()) {
            ((FunctionalItem) item).use(e);
        }
    }

    /**
     * @return if the item on player's hand should be processed after
     */
    private boolean interactWithPrison(PEGame game, PEPlayer player, Location blockLocation, int itemSlot) {
        PrisonBuilding prison = game.getPrison();
        Item item = player.getItemAt(itemSlot);

        int vaultIndex = prison.getVaultIndex(blockLocation);
        if (vaultIndex != -1) {
            interactWithVault(game, player, prison, vaultIndex, item);
            return false;
        }

        Chest chest = prison.getChest(blockLocation);
        if (chest != null) {
            interactWithChest(game, player, chest);
            return false;
        }

        WallCrack crack = prison.getWallCrack(blockLocation);
        if (crack != null) {
            return interactWithWallCrack(game, player, crack) == -1;
        }

        Helicopter helicopter = prison.getHelicopter(blockLocation);
        if (helicopter != null) {
            helicopter.click(player, prison.getHelicopterExitLocation(), prison.getHelicopterJoinLocation());
            return false;
        }

        Location destination = prison.getSecretPassageDestinationLocation(blockLocation, game.isPrisoner(player));
        if (destination != null) {
            BukkitTeleporter.teleport(player, destination);
            return false;
        }

        Door door = prison.getDoor(blockLocation);
        if (door != null) {
            int index = player.convertToInventoryIndex(itemSlot);
            interactWithDoor(player, index, item, door, blockLocation);
            return false;
        }

        Obstacle obstacle = prison.getObstacle(blockLocation);
        if (obstacle != null) {
            return obstacleTookDamage(game, player, obstacle, item) == -1;
        }

        return true;
    }

    private void interactWithVault(PEGame game, PEPlayer player, PrisonBuilding prison, int vaultIndex, Item item) {
        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(player.getName());

        Vault vault = prison.getVault(vaultIndex);

        if (game.isGuard(player)) {
            if (!(item instanceof SearchItem)) {
                BukkitMessageSender.sendChatMessage(player, messages.getPoliceOpenVaultMessage());
                return;
            }

            policeSearchVault(game, (Guard) player, vault, messages);
            return;
        }

        if (game.getPrisonerTeam().getPlayerIndex(player) != vaultIndex) {
            BukkitMessageSender.sendChatMessage(player, messages.getPrisonerOtherVaultMessage());
            return;
        }

        player.openMenu(vault);
    }

    private void policeSearchVault(PEGame game, Guard guard, Vault vault, MessageLanguageManager messagesPolice) {
        Prisoner vaultOwner = vault.getOwner();
        MessageLanguageManager messagesPrisoner = MessageLanguageManager.getInstanceByPlayer(vaultOwner.getName());

        int returnCode = vault.search();
        if (returnCode == 1) {
            game.setWanted(vaultOwner, guard);

            BukkitMessageSender.sendChatMessage(
                    guard,
                    messagesPolice.getPoliceFoundIllegalItemsMessage(vaultOwner.getName())
            );
            BukkitMessageSender.sendChatMessage(vaultOwner, messagesPrisoner.getPrisonerFoundIllegalItemsMessage());
        } else if (returnCode == 0) {
            BukkitMessageSender.sendChatMessage(guard, messagesPolice.getPoliceNoIllegalItemsFoundMessage());
            BukkitMessageSender.sendChatMessage(vaultOwner, messagesPrisoner.getPrisonerNoIllegalItemsFoundMessage());
            guard.usedSearch();
        }

        return;
    }

    private void interactWithChest(PEGame game, PEPlayer player, Chest chest) {
        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(player.getName());

        if (game.isGuard(player)) {
            BukkitMessageSender.sendChatMessage(player, messages.getPoliceCanNotOpenChestMessage());
            return;
        }

        if (chest.isOpened()) {
            BukkitMessageSender.sendChatMessage(player, messages.getChestAlreadyOpenedMessage());
            return;
        }

        player.openMenu(chest);
    }


    private int interactWithWallCrack(PEGame game, PEPlayer player, WallCrack crack) {
        if (!game.isGuard(player)) {
            return -1;
        }

        int returnCode = crack.fixCrack();
        if (returnCode == -1) {
            MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(player.getName());
            BukkitMessageSender.sendChatMessage(player, messages.getCanOnlyFixHolesMessage());
        }

        return 0;
    }

    private void interactWithDoor(PEPlayer player, int inventoryIndex, Item item, Door door, Location doorLocation) {
        ClickDoorReturnAction returnAction = door.click(player, item);

        if (returnAction == ClickDoorReturnAction.CLOSE_DOOR) {
            door.close(doorLocation);
            return;
        }

        if (returnAction == ClickDoorReturnAction.OPEN_DOOR) {
            door.open(doorLocation);
            player.removeItem(inventoryIndex);
            player.updateInventory();
        }
    }

    public int obstacleTookDamage(PEGame game, PEPlayer player, Obstacle obstacle, Item item) {
        if (game.isGuard(player)) {
            if (obstacle instanceof Regenerable) {
                ((Regenerable) obstacle).regenerate();
                return 0;
            }

            return -1;
        }

        if (!item.isTool()) {
            MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(player.getName());
            BukkitMessageSender.sendChatMessage(player, obstacle.getEffectiveToolMessage(messages));
            return 0;
        }

        double returnCode = obstacle.takeDamage((ToolItem) item);
        if (returnCode == 0) {
            obstacle.removeFromWorld();
        } else if (returnCode == -1) {
            MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(player.getName());
            BukkitMessageSender.sendChatMessage(player, obstacle.getEffectiveToolMessage(messages));
            return 0;
        }

        player.updateInventory();

        return 0;
    }

//  ########################################
//  #             Join & Leave             #
//  ########################################

    @EventHandler
    public void playerLeave(PlayerQuitEvent e) {
        PEGame game = GameManager.getGame();
        if (game == null) {
            return;
        }

        PEPlayer player = game.getPEPlayer(e.getPlayer().getName());
        if (player == null) {
            return;
        }

        player.closeMenu();
    }

//  #########################################
//  #               Inventory               #
//  #########################################

    @EventHandler
    public void playerCloseInventory(InventoryCloseEvent e) {
        PEGame game = GameManager.getGame();
        if (game == null) {
            return;
        }

        InventoryType invType = e.getInventory().getType();
        if (invType == InventoryType.CRAFTING || invType == InventoryType.CREATIVE) {
            return;
        }

        PEPlayer player = game.getPEPlayer(e.getPlayer().getName());
        if (player == null) {
            return;
        }

        player.closeMenu();
    }

    @EventHandler
    public void playerClickInventory(InventoryClickEvent e) {
        PEGame game = GameManager.getGame();
        if (game == null) {
            return;
        }

        Player player = (Player) e.getWhoClicked();
        PEPlayer pePlayer = game.getPEPlayer(player.getName());
        if (pePlayer == null) {
            return;
        }

        if (e.getClickedInventory() == null) {
            return;
        }
        boolean isPlayerInv = e.getClickedInventory().getType() == InventoryType.PLAYER;

        if (isPlayerInv && e.getAction() == InventoryAction.DROP_ONE_SLOT) {
            if (!dropItem(pePlayer, e.getSlot())) {
                e.setCancelled(true);
            }

            return;
        }

        ItemStack cursor = e.getCursor();
        ItemStack current = e.getCurrentItem();

        ClickReturnAction returnAction = playerClickMenu(pePlayer, e.getSlot(), isPlayerInv, e.getClick());
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

    private ClickReturnAction playerClickMenu(PEPlayer player, int slot, boolean isPlayerInv, ClickType type) {
        Clickable menu = player.getOpenedMenu();
        if (menu == null) {
            return ClickReturnAction.NOTHING;
        }

        return menu.click(player, slot, isPlayerInv, type);
    }

//  #########################################
//  #                 Sneak                 #
//  #########################################

    @EventHandler
    public void onPlayerSneak(PlayerToggleSneakEvent e) {
        PEGame game = GameManager.getGame();
        if (game == null) {
            return;
        }

        PEPlayer player = game.getPEPlayer(e.getPlayer().getName());
        if (player == null) {
            return;
        }

        if (!player.isGuard()) {
            return;
        }

        Guard guard = (Guard) player;
        if (guard.isWatchingCamera()) {
            guard.stoppedWatchingCamera();
        }
    }

//  ########################################
//  #                 Drop                 #
//  ########################################

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent e) {
        PEGame game = GameManager.getGame();
        if (game == null) {
            return;
        }

        String playerName = e.getPlayer().getName();
        PEPlayer player = game.getPEPlayer(playerName);
        if (player == null) {
            return;
        }

        int slot = e.getPlayer().getInventory().getHeldItemSlot();

        if (!dropItem(player, slot)) {
            e.setCancelled(true);
            return;
        }

        e.getItemDrop().remove();
    }

    /**
     * @return if the item was dropped successfully
     */
    private boolean dropItem(PEPlayer player, int slot) {
        int returnCode = player.removeItem(slot);
        if (returnCode == -1) {
            MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(player.getName());
            BukkitMessageSender.sendChatMessage(player, messages.getCannotDropThatItemMessage());
        }

        return returnCode == 0;
    }

//  ########################################
//  #                 Chat                 #
//  ########################################

    @EventHandler(ignoreCancelled = true, priority = EventPriority.HIGH)
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        PEGame game = GameManager.getGame();
        if (game == null) {
            return;
        }

        String senderName = e.getPlayer().getName();
        PEPlayer player = game.getPEPlayer(senderName);
        if (player == null) {
            return;
        }

        String teamChatPrefix = ConfigManager.getInstance().getTeamChatPrefix();
        String message = e.getMessage();

        if (!message.startsWith(teamChatPrefix)) {
            sendGeneralMessage(game, senderName, message);
        } else if (game.isPrisoner(player)) {
            sendMessageToPrisonersTeam(game, senderName, message.substring(2).trim());
        } else {
            sendMessageToPoliceTeam(game, senderName, message.substring(2).trim());
        }

        e.setCancelled(true);
    }

    public void sendGeneralMessage(PEGame game, String senderName, String message) {
        for (PEPlayer player : game.getPlayersOnLobby()) {
            MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(player.getName());
            BukkitMessageSender.sendChatMessage(player, messages.getGeneralMessage(senderName, message));
        }
    }

    private void sendMessageToPoliceTeam(PEGame game, String senderName, String message) {
        for (PEPlayer player : game.getGuardsTeam().getMembers()) {
            MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(player.getName());
            BukkitMessageSender.sendChatMessage(player, messages.getPoliceTeamMessage(senderName, message));
        }
    }

    private void sendMessageToPrisonersTeam(PEGame game, String senderName, String message) {
        for (PEPlayer player : game.getPrisonerTeam().getMembers()) {
            MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(player.getName());
            BukkitMessageSender.sendChatMessage(player, messages.getPrisonerTeamMessage(senderName, message));
        }
    }
}
