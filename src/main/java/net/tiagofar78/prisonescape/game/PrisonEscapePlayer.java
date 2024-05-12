package net.tiagofar78.prisonescape.game;

import net.tiagofar78.prisonescape.bukkit.BukkitMenu;
import net.tiagofar78.prisonescape.items.Item;
import net.tiagofar78.prisonescape.items.NullItem;
import net.tiagofar78.prisonescape.items.ToolItem;
import net.tiagofar78.prisonescape.kits.Kit;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.List;

public abstract class PrisonEscapePlayer {

    private static final int INVENTORY_SIZE = 4;

    private String _name;
    private boolean _isOnline;
    private List<Item> _inventory;
    private Kit _currentKit;

    public PrisonEscapePlayer(String name) {
        _name = name;
        _isOnline = true;
        _inventory = createInventory();
    }

    public boolean isPrisioner() {
        return false;
    }

    public boolean isGuard() {
        return false;
    }

    public boolean isWaiting() {
        return false;
    }

    private List<Item> createInventory() {
        List<Item> list = new ArrayList<>();

        for (int i = 0; i < INVENTORY_SIZE; i++) {
            list.add(new NullItem());
        }

        return list;
    }

    public String getName() {
        return _name;
    }

//  ########################################
//  #                  Kit                 #
//  ########################################

    public Kit getKit() {
        return _currentKit;
    }

    public void setKit(Kit kit) {
        _currentKit = kit;
        kit.give(getName());
    }

//	########################################
//	#                Online                #
//	########################################

    public boolean isOnline() {
        return _isOnline;
    }

    public void playerLeft() {
        _isOnline = false;
    }

    public void playerRejoined() {
        _isOnline = true;
    }

//	#########################################
//	#               Inventory               #
//	#########################################

    public Item getItemAt(int slot) {
        Item item = _currentKit.getItemAt(slot);
        if (item != null) {
            return item;
        }

        int index = BukkitMenu.convertToIndexPlayerInventory(slot);
        if (index < 0 || index >= INVENTORY_SIZE) {
            return new NullItem();
        }

        return _inventory.get(index);
    }

    /**
     * @return 0 if success<br>
     *         -1 if full inventory
     */
    public int giveItem(Item item) {
        for (int i = 0; i < INVENTORY_SIZE; i++) {
            if (_inventory.get(i) instanceof NullItem) {
                setItem(i, item);
                return 0;
            }
        }

        return -1;
    }

    public void setItem(int index, Item item) {
        _inventory.set(index, item);

        BukkitMenu.setItem(_name, index, item);
    }


    /**
     * @return 0 if success<br>
     *         -1 if cannot remove item
     */
    public int removeItem(int slot) {
        int index = BukkitMenu.convertToIndexPlayerInventory(slot);
        if (index == -1) {
            return -1;
        }

        _inventory.set(index, new NullItem());
        BukkitMenu.setItem(_name, index, new NullItem());
        return 0;
    }

    public boolean hasIllegalItems() {
        for (Item item : _inventory) {
            if (item.isIllegal()) {
                return true;
            }
        }

        return false;
    }

    public boolean hasMetalItems() {
        for (Item item : _inventory) {
            if (item.isMetalic()) {
                return true;
            }
        }

        return false;
    }

    public void updateInventory() {
        for (int i = 0; i < INVENTORY_SIZE; i++) {
            Item item = _inventory.get(i);
            if (item.isTool() && ((ToolItem) item).isBroken()) {
                setItem(i, new NullItem());
            }

            BukkitMenu.setItem(getName(), i, _inventory.get(i));
        }
    }

//  ########################################
//  #              Scoreboard              #
//  ########################################

    public void setScoreboard(Scoreboard scoreboard) {
        Player player = Bukkit.getPlayer(getName());
        if (player != null && player.isOnline()) {
            player.setScoreboard(scoreboard);
        }
    }

    public void removeScoreboard() {
        setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
    }

//	########################################
//	#                 Util                 #
//	########################################

    public void setGameMode(GameMode gameMode) {
        Player player = Bukkit.getPlayer(getName());
        if (player != null && player.isOnline()) {
            player.setGameMode(gameMode);
        }
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof PrisonEscapePlayer && ((PrisonEscapePlayer) o).getName().equals(this.getName());
    }

}
