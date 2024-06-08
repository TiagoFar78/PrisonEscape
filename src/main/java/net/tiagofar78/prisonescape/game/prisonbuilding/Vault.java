package net.tiagofar78.prisonescape.game.prisonbuilding;

import net.tiagofar78.prisonescape.game.PEPlayer;
import net.tiagofar78.prisonescape.game.Prisoner;
import net.tiagofar78.prisonescape.items.Item;
import net.tiagofar78.prisonescape.items.NullItem;
import net.tiagofar78.prisonescape.managers.ConfigManager;
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
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class Vault implements Clickable {

    private static final int NON_HIDDEN_SIZE = 4;
    private static final int HIDDEN_SIZE = 1;
    private static final int SIGN_OWNER_NAME_LINE__INDEX = 1;

    private static final int[] NON_HIDDEN_ITEMS_INDEXES = {9 + 2, 9 + 3, 9 + 5, 9 + 6};
    private static final int HIDDEN_ITEM_INDEX = 9 * 4 + 4;

    private List<Item> _nonHiddenContents;
    private List<Item> _hiddenContents;

    private Prisoner _owner;

    private Location _location;

    public Vault(Prisoner owner, Location location) {
        _nonHiddenContents = createContentsList(NON_HIDDEN_SIZE);
        _hiddenContents = createContentsList(HIDDEN_SIZE);

        _owner = owner;

        _location = location;
        createWorldVault(location);
        createWorldSignAboveVault(location, _owner.getName());
    }

    public boolean isIn(Location location) {
        return _location.equals(location);
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

    public void setItem(boolean isHidden, int index, Item item) {
        List<Item> contents = isHidden ? _hiddenContents : _nonHiddenContents;
        int size = isHidden ? HIDDEN_SIZE : NON_HIDDEN_SIZE;

        if (index >= size) {
            throw new IndexOutOfBoundsException();
        }

        contents.set(index, item);
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
            contents.set(i, null);
        }
    }

    @Override
    public ClickReturnAction click(PEPlayer player, int slot, Item itemHeld, boolean clickedPlayerInv) {
        if (clickedPlayerInv) {
            int index = player.convertToInventoryIndex(slot);
            if (index == -1) {
                return ClickReturnAction.NOTHING;
            }

            player.setItem(index, itemHeld);
            return ClickReturnAction.CHANGE_HOLD_AND_SELECTED;
        }

        int itemIndex = convertToIndex(slot);
        if (itemIndex == -1) {
            return ClickReturnAction.NOTHING;
        }

        boolean isHidden = isHiddenIndex(slot);

        setItem(isHidden, itemIndex, itemHeld);
        return ClickReturnAction.CHANGE_HOLD_AND_SELECTED;
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

        return -1;
    }

    private boolean isHiddenIndex(int slot) {
        return HIDDEN_ITEM_INDEX == slot;
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
        Block block = location.getBlock();
        block.setType(Material.OAK_WALL_SIGN);

        rotate(block);

        Sign sign = (Sign) block.getState();
        sign.getSide(Side.FRONT).setLine(SIGN_OWNER_NAME_LINE__INDEX, text);
        sign.update();
    }

    private void rotate(Block block) {
        ConfigManager config = ConfigManager.getInstance();

        Directional rotatable = (Directional) block.getBlockData();
        rotatable.setFacing(BlockFace.valueOf(config.getVaultsDirection()));
        block.setBlockData(rotatable);
    }

    public void deleteVaultAndRespectiveSignFromWorld() {
        Location vaultLocation = _location;
        Location signLocation = _location.clone().add(0, 1, 0);

        vaultLocation.getBlock().setType(Material.AIR);
        signLocation.clone().add(0, 1, 0).getBlock().setType(Material.AIR);
    }

    @Override
    public Inventory toInventory(MessageLanguageManager messages) {
        String title = messages.getVaultTitle();
        int lines = 6;
        Inventory inv = Bukkit.createInventory(null, lines * 9, title);

        ItemStack glassItem = createGlassItem();
        for (int i = 0; i < lines * 9; i++) {
            inv.setItem(i, glassItem);
        }

        for (int i = 0; i < _nonHiddenContents.size(); i++) {
            ItemStack item = _nonHiddenContents.get(i).toItemStack(messages);
            inv.setItem(NON_HIDDEN_ITEMS_INDEXES[i], item);
        }

        ItemStack hiddenIndicatorItem = createHiddenIndicatorItem(messages);
        for (int line = 4; line < 7; line++) {
            for (int column = 4; column < 7; column++) {
                int index = (line - 1) * 9 + (column - 1);
                inv.setItem(index, hiddenIndicatorItem);
            }
        }

        ItemStack hiddenItem = _hiddenContents.get(0).toItemStack(messages);
        inv.setItem(HIDDEN_ITEM_INDEX, hiddenItem);

        return inv;
    }

    private static ItemStack createGlassItem() {
        ItemStack item = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(" ");
        item.setItemMeta(itemMeta);

        return item;
    }

    private static ItemStack createHiddenIndicatorItem(MessageLanguageManager messages) {
        ItemStack item = new ItemStack(Material.RED_STAINED_GLASS_PANE);
        ItemMeta itemMeta = item.getItemMeta();
        itemMeta.setDisplayName(messages.getVaultHiddenGlassName());
        item.setItemMeta(itemMeta);

        return item;
    }

}
