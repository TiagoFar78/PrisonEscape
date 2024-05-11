package net.tiagofar78.prisonescape.game.prisonbuilding;

import net.tiagofar78.prisonescape.bukkit.BukkitMenu;
import net.tiagofar78.prisonescape.bukkit.BukkitWorldEditor;
import net.tiagofar78.prisonescape.game.Prisioner;
import net.tiagofar78.prisonescape.game.PrisonEscapePlayer;
import net.tiagofar78.prisonescape.items.Item;
import net.tiagofar78.prisonescape.items.NullItem;
import net.tiagofar78.prisonescape.managers.ConfigManager;
import net.tiagofar78.prisonescape.menus.ClickReturnAction;
import net.tiagofar78.prisonescape.menus.Clickable;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Sign;
import org.bukkit.block.data.Directional;
import org.bukkit.block.sign.Side;

import java.util.ArrayList;
import java.util.List;

public class Vault implements Clickable {

    private static final int NON_HIDDEN_SIZE = 4;
    private static final int HIDDEN_SIZE = 1;
    private static final int SIGN_OWNER_NAME_LINE__INDEX = 1;

    private List<Item> _nonHiddenContents;
    private List<Item> _hiddenContents;
    private boolean _isOpen;

    private Prisioner _owner;

    private PrisonEscapeLocation _location;

    public Vault(Prisioner owner, PrisonEscapeLocation location) {
        _nonHiddenContents = createContentsList(NON_HIDDEN_SIZE);
        _hiddenContents = createContentsList(HIDDEN_SIZE);
        _isOpen = false;

        _owner = owner;

        _location = location;
        createWorldVault(location);
        createWorldSignAboveVault(location, _owner.getName());
    }

    public boolean isIn(PrisonEscapeLocation location) {
        return _location.equals(location);
    }

    private List<Item> createContentsList(int size) {
        List<Item> list = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            list.add(new NullItem());
        }

        return list;
    }

    public Prisioner getOwner() {
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
    public void open(PrisonEscapePlayer player) {
        BukkitMenu.openVault(player.getName(), _nonHiddenContents, _hiddenContents);
        _isOpen = true;
    }

    @Override
    public void close() {
        _isOpen = false;
    }

    @Override
    public boolean isOpened() {
        return _isOpen;
    }

    @Override
    public ClickReturnAction click(PrisonEscapePlayer player, int slot, Item itemHeld, boolean clickedPlayerInv) {
        if (clickedPlayerInv) {
            int index = BukkitMenu.convertToIndexPlayerInventory(slot);
            if (index == -1) {
                return ClickReturnAction.NOTHING;
            }

            player.setItem(index, itemHeld);
            return ClickReturnAction.CHANGE_HOLD_AND_SELECTED;
        }

        int itemIndex = BukkitMenu.convertToIndexVault(slot);
        if (itemIndex == -1) {
            return ClickReturnAction.NOTHING;
        }

        boolean isHidden = BukkitMenu.isHiddenIndexVault(slot);

        setItem(isHidden, itemIndex, itemHeld);
        return ClickReturnAction.CHANGE_HOLD_AND_SELECTED;
    }

//  #########################################
//  #                 World                 #
//  #########################################

    private void createWorldVault(PrisonEscapeLocation location) {
        World world = BukkitWorldEditor.getWorld();
        Location bukkitLocation = new Location(world, location.getX(), location.getY(), location.getZ());

        Block block = bukkitLocation.getBlock();
        block.setType(Material.CHEST);

        rotate(block);
    }

    private void createWorldSignAboveVault(PrisonEscapeLocation location, String text) {
        World world = BukkitWorldEditor.getWorld();
        Location bukkitLocation = new Location(world, location.getX(), location.getY() + 1, location.getZ());
        Block block = bukkitLocation.getBlock();
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
        World world = BukkitWorldEditor.getWorld();
        Location vaultLocation = new Location(world, _location.getX(), _location.getY(), _location.getZ());
        Location signLocation = new Location(world, _location.getX(), _location.getY() + 1, _location.getZ());

        vaultLocation.getBlock().setType(Material.AIR);
        signLocation.getBlock().setType(Material.AIR);
    }

}
