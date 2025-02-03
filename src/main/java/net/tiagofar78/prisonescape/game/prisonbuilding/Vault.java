package net.tiagofar78.prisonescape.game.prisonbuilding;

import net.tiagofar78.prisonescape.bukkit.BukkitMessageSender;
import net.tiagofar78.prisonescape.game.PEGame;
import net.tiagofar78.prisonescape.game.PEPlayer;
import net.tiagofar78.prisonescape.game.Prisoner;
import net.tiagofar78.prisonescape.items.Item;
import net.tiagofar78.prisonescape.items.NullItem;
import net.tiagofar78.prisonescape.managers.MapManager;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;
import net.tiagofar78.prisonescape.menus.ClickReturnAction;
import net.tiagofar78.prisonescape.menus.Clickable;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Directional;
import org.bukkit.block.sign.Side;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Vault implements Clickable {

    private static final int NON_HIDDEN_SIZE = 4;
    private static final int HIDDEN_SIZE = 1;
    private static final int TEMP_SIZE = 4;
    private static final int SIGN_OWNER_NAME_LINE_INDEX = 1;

    private static final int[] NON_HIDDEN_ITEMS_INDEXES = {9 + 1, 9 * 2 + 1, 9 * 3 + 1, 9 * 4 + 1};
    private static final int HIDDEN_ITEM_INDEX = 9 * 4 + 4;
    private static final int[] TEMP_ITEMS_INDEXES = {9 + 7, 9 * 2 + 7, 9 * 3 + 7, 9 * 4 + 7};

    private MapManager _map;

    private List<Item> _nonHiddenContents;
    private List<Item> _hiddenContents;
    private List<Item> _tempContents;

    private Prisoner _owner;

    private Location _location;

    public Vault(Prisoner owner, Location location, MapManager map) {
        _map = map;

        _nonHiddenContents = createContentsList(NON_HIDDEN_SIZE);
        _hiddenContents = createContentsList(HIDDEN_SIZE);
        _tempContents = createContentsList(TEMP_SIZE);

        _owner = owner;

        _location = location;
        createWorldVault(location);
        createWorldSignAboveVault(location, _owner.getName());
    }

    private List<Item> createContentsList(int size) {
        List<Item> list = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            list.add(new NullItem());
        }

        return list;
    }

    public Prisoner getOwner() {
        return _owner;
    }

    public boolean isIn(Location location) {
        return _location.equals(location);
    }

    private int addItem(List<Item> contents, Item item) {
        Item nullItem = new NullItem();
        for (int i = 0; i < contents.size(); i++) {
            if (contents.get(i).equals(nullItem)) {
                contents.set(i, item);
                return 0;
            }
        }

        return -1;
    }

    /**
     *
     * @return 0 if no illegal items were found<br>
     *         1 if illegal items were found
     */
    public int search() {
        for (Item item : _nonHiddenContents) {
            if (item != null && item.isIllegal()) {
                clearContents(_nonHiddenContents, NON_HIDDEN_SIZE);
                clearContents(_hiddenContents, HIDDEN_SIZE);
                return 1;
            }
        }

        return 0;
    }

    private void clearContents(List<Item> contents, int size) {
        for (int i = 0; i < size; i++) {
            contents.set(i, new NullItem());
        }
    }

    @Override
    public ClickReturnAction click(PEPlayer player, int slot, boolean isPlayerInv, ClickType type) {
        return isPlayerInv ? clickedPlayerInv(player, slot, type) : clickedVaultInv(player, slot, type);
    }

    private ClickReturnAction clickedPlayerInv(PEPlayer player, int slot, ClickType type) {
        int index = player.convertToInventoryIndex(slot);
        if (index == -1) {
            return ClickReturnAction.NOTHING;
        }

        Item item = player.getItemsInInventory().get(index);
        boolean wasItemGiven = clickedPlayerInventoryList(player, item, type);
        if (!wasItemGiven) {
            return ClickReturnAction.NOTHING;
        }

        player.setItem(index, new NullItem());
        player.updateView();
        return ClickReturnAction.DELETE_HOLD_AND_SELECTED;
    }

    private ClickReturnAction clickedVaultInv(PEPlayer player, int slot, ClickType type) {
        int index = convertToIndex(slot);
        if (index == -1) {
            return ClickReturnAction.NOTHING;
        }

        List<Item> contents = null;
        boolean wasItemGiven = false;
        if (isNonHiddenSlot(slot)) {
            contents = _nonHiddenContents;
            wasItemGiven = clickedNonHiddenList(player, contents.get(index), type);
        } else if (isHiddenSlot(slot)) {
            contents = _hiddenContents;
            wasItemGiven = clickedHiddenList(player, contents.get(index), type);
        } else if (isTempSlot(slot)) {
            contents = _tempContents;
            wasItemGiven = clickedTempList(player, contents.get(index), type);
        }

        if (!wasItemGiven) {
            return ClickReturnAction.NOTHING;
        }

        contents.set(index, new NullItem());
        player.updateView();
        return ClickReturnAction.DELETE_HOLD_AND_SELECTED;
    }

    private boolean clickedPlayerInventoryList(PEPlayer player, Item item, ClickType type) {
        switch (type) {
            case LEFT:
                return sendItemToNonHiddenList(player, item);
            case RIGHT:
            case SHIFT_RIGHT:
                return sendItemToHiddenList(player, item);
            case SHIFT_LEFT:
                return sendItemToTempList(player, item);
            default:
                return false;
        }
    }

    private boolean clickedNonHiddenList(PEPlayer player, Item item, ClickType type) {
        switch (type) {
            case LEFT:
                return sendItemToInventory(player, item);
            case RIGHT:
            case SHIFT_RIGHT:
                return sendItemToHiddenList(player, item);
            case SHIFT_LEFT:
                return sendItemToTempList(player, item);
            default:
                return false;
        }
    }

    private boolean clickedHiddenList(PEPlayer player, Item item, ClickType type) {
        switch (type) {
            case LEFT:
                return sendItemToInventory(player, item);
            case RIGHT:
            case SHIFT_RIGHT:
                return sendItemToNonHiddenList(player, item);
            case SHIFT_LEFT:
                return sendItemToTempList(player, item);
            default:
                return false;
        }
    }

    private boolean clickedTempList(PEPlayer player, Item item, ClickType type) {
        switch (type) {
            case LEFT:
                return sendItemToInventory(player, item);
            case RIGHT:
            case SHIFT_RIGHT:
                return sendItemToHiddenList(player, item);
            case SHIFT_LEFT:
                return sendItemToNonHiddenList(player, item);
            default:
                return false;
        }
    }

    private boolean sendItemToInventory(PEPlayer player, Item item) {
        if (player.giveItem(item) == -1) {
            MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(player.getName());
            BukkitMessageSender.sendChatMessage(player, messages.getFullInventoryMessage());
            return false;
        }

        return true;
    }

    private boolean sendItemToNonHiddenList(PEPlayer player, Item item) {
        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(player.getName());
        String message = messages.getNoMoreNonHiddenSlotsMessage();
        return sendItemToVault(player, _nonHiddenContents, item, message);
    }

    private boolean sendItemToHiddenList(PEPlayer player, Item item) {
        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(player.getName());
        String message = messages.getNoMoreHiddenSlotsMessage();
        return sendItemToVault(player, _hiddenContents, item, message);
    }

    private boolean sendItemToTempList(PEPlayer player, Item item) {
        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(player.getName());
        String message = messages.getNoMoreTempSlotsMessage();
        return sendItemToVault(player, _tempContents, item, message);
    }

    private boolean sendItemToVault(PEPlayer player, List<Item> contents, Item item, String message) {
        if (addItem(contents, item) == -1) {
            BukkitMessageSender.sendChatMessage(player, message);
            return false;
        }

        return true;
    }

    @Override
    public void close(PEPlayer player) {
        for (int i = 0; i < _tempContents.size(); i++) {
            Item tempItem = _tempContents.get(i);
            if (tempItem.equals(new NullItem())) {
                continue;
            }

            boolean wasTempItemStored = player.giveItem(tempItem) == 0;

            if (!wasTempItemStored) {
                wasTempItemStored = addItem(_nonHiddenContents, tempItem) == 0;

                if (!wasTempItemStored) {
                    wasTempItemStored = addItem(_hiddenContents, tempItem) == 0;
                }
            }

            if (!wasTempItemStored) {
                throw new RuntimeException("There are more items in inventory + vault now than when it was opened");
            }

            _tempContents.set(i, new NullItem());
        }
    }

    private int convertToIndex(int slot) {
        for (int i = 0; i < NON_HIDDEN_ITEMS_INDEXES.length; i++) {
            if (NON_HIDDEN_ITEMS_INDEXES[i] == slot) {
                return i;
            }
        }

        if (HIDDEN_ITEM_INDEX == slot) {
            return 0;
        }

        for (int i = 0; i < TEMP_ITEMS_INDEXES.length; i++) {
            if (TEMP_ITEMS_INDEXES[i] == slot) {
                return i;
            }
        }

        return -1;
    }

    private boolean isNonHiddenSlot(int slot) {
        for (int i = 0; i < NON_HIDDEN_ITEMS_INDEXES.length; i++) {
            if (NON_HIDDEN_ITEMS_INDEXES[i] == slot) {
                return true;
            }
        }

        return false;
    }

    private boolean isHiddenSlot(int slot) {
        return HIDDEN_ITEM_INDEX == slot;
    }

    private boolean isTempSlot(int slot) {
        for (int i = 0; i < TEMP_ITEMS_INDEXES.length; i++) {
            if (TEMP_ITEMS_INDEXES[i] == slot) {
                return true;
            }
        }

        return false;
    }

//  #########################################
//  #                 World                 #
//  #########################################

    private void createWorldVault(Location location) {
        Block block = location.getBlock();
        block.setType(Material.CHEST);

        rotate(block);
    }

    private void createWorldSignAboveVault(Location location, String text) {
        Block block = location.clone().add(0, 1, 0).getBlock();
        block.setType(Material.OAK_WALL_SIGN);

        rotate(block);

        Sign sign = (Sign) block.getState();
        sign.getSide(Side.FRONT).setLine(SIGN_OWNER_NAME_LINE_INDEX, text);
        sign.update();
    }

    private void rotate(Block block) {
        Directional rotatable = (Directional) block.getBlockData();
        rotatable.setFacing(BlockFace.valueOf(_map.getVaultsDirection()));
        block.setBlockData(rotatable);
    }

    public void deleteVaultAndRespectiveSignFromWorld() {
        Location vaultLocation = _location;
        Location signLocation = _location.clone().add(0, 1, 0);

        vaultLocation.getBlock().setType(Material.AIR);
        signLocation.getBlock().setType(Material.AIR);
    }

    @Override
    public Inventory toInventory(PEGame game, PEPlayer player) {
        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(player.getName());

        String title = messages.getVaultTitle();
        int lines = 6;
        Inventory inv = Bukkit.createInventory(null, lines * 9, title);

        placeGrayGlasses(inv, lines);
        placeNonHiddenContents(inv, game, player);

        placeTempIndicatorGlasses(inv, lines, messages);
        placeTempContents(inv, game, player);

        placeHiddenIndicatorGlasses(inv, lines, messages);
        placeHiddenContents(inv, game, player);

        placeInforTorchItem(inv, messages);

        return inv;
    }

    @Override
    public void updateInventory(Inventory inv, PEPlayer player) {
        placeNonHiddenContents(inv, player.getGame(), player);
        placeHiddenContents(inv, player.getGame(), player);
        placeTempContents(inv, player.getGame(), player);
    }

    private void placeNonHiddenContents(Inventory inv, PEGame game, PEPlayer player) {
        for (int i = 0; i < _nonHiddenContents.size(); i++) {
            ItemStack item = _nonHiddenContents.get(i).toItemStack(game, player);
            inv.setItem(NON_HIDDEN_ITEMS_INDEXES[i], item);
        }
    }

    private void placeHiddenContents(Inventory inv, PEGame game, PEPlayer player) {
        ItemStack hiddenItem = _hiddenContents.get(0).toItemStack(game, player);
        inv.setItem(HIDDEN_ITEM_INDEX, hiddenItem);
    }

    private void placeTempContents(Inventory inv, PEGame game, PEPlayer player) {
        for (int i = 0; i < _tempContents.size(); i++) {
            ItemStack item = _tempContents.get(i).toItemStack(game, player);
            inv.setItem(TEMP_ITEMS_INDEXES[i], item);
        }
    }

    private void placeGrayGlasses(Inventory inv, int lines) {
        ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(" ");
        item.setItemMeta(itemMeta);

        for (int i = 0; i < lines * 9; i++) {
            inv.setItem(i, item);
        }
    }

    private void placeHiddenIndicatorGlasses(Inventory inv, int lines, MessageLanguageManager messages) {
        ItemStack item = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(messages.getVaultHiddenGlassName());
        item.setItemMeta(itemMeta);

        for (int line = 4; line < 7; line++) {
            for (int column = 4; column < 7; column++) {
                int index = (line - 1) * 9 + (column - 1);
                inv.setItem(index, item);
            }
        }
    }

    private void placeTempIndicatorGlasses(Inventory inv, int lines, MessageLanguageManager messages) {
        ItemStack item = new ItemStack(Material.YELLOW_STAINED_GLASS_PANE);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(messages.getVaultTempGlassName());
        item.setItemMeta(itemMeta);

        for (int line = 1; line < 7; line++) {
            for (int column = 7; column < 10; column++) {
                int index = (line - 1) * 9 + (column - 1);
                inv.setItem(index, item);
            }
        }
    }

    private void placeInforTorchItem(Inventory inv, MessageLanguageManager messages) {
        ItemStack item = new ItemStack(Material.REDSTONE_TORCH);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(messages.getVaultInfoTorchName());
        itemMeta.setLore(messages.getVaultInfoTorchLore());
        item.setItemMeta(itemMeta);

        inv.setItem(9 * 1 + 4, item);
    }

}
