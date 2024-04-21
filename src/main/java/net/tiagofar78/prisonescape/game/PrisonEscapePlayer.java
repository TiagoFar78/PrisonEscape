package net.tiagofar78.prisonescape.game;

import net.tiagofar78.prisonescape.bukkit.BukkitMenu;
import net.tiagofar78.prisonescape.items.CameraItem;
import net.tiagofar78.prisonescape.items.Item;
import net.tiagofar78.prisonescape.items.SensorItem;
import net.tiagofar78.prisonescape.items.TrapItem;
import net.tiagofar78.prisonescape.managers.ConfigManager;

import java.util.ArrayList;
import java.util.List;

public class PrisonEscapePlayer {

    private static final int INVENTORY_SIZE = 4;

    private String _name;
    private TeamPreference _preference;
    private boolean _isWanted;
    private boolean _inRestrictedArea;
    private boolean _isOnline;
    private boolean _hasEscaped;
    private List<Item> _inventory;
    private int _balance;

    private int _numOfCamerasBought = 0;
    private int _numOfSensorsBought = 0;
    private int _numOfTrapsBought = 0;

    public PrisonEscapePlayer(String name) {
        _name = name;
        _preference = TeamPreference.RANDOM;
        _isWanted = false;
        _inRestrictedArea = false;
        _isOnline = true;
        _inventory = createInventory();
        _balance = ConfigManager.getInstance().getStartingBalance();
    }

    private List<Item> createInventory() {
        List<Item> list = new ArrayList<>();

        for (int i = 0; i < INVENTORY_SIZE; i++) {
            list.add(null);
        }

        return list;
    }

    public String getName() {
        return _name;
    }

    public TeamPreference getPreference() {
        return _preference;
    }

    public void setPreference(TeamPreference preference) {
        this._preference = preference;
    }

//	########################################
//	#                Escape                #
//	########################################

    public boolean hasEscaped() {
        return _hasEscaped;
    }

    public void escaped() {
        _hasEscaped = true;
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

    /**
     * @return 0 if success<br>
     *         -1 if full inventory
     */
    public int giveItem(Item item) {
        for (int i = 0; i < INVENTORY_SIZE; i++) {
            if (_inventory.get(i) == null) {
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

    public void removeItem(int slot) {
        int index = BukkitMenu.convertToIndexPlayerInventory(slot);
        if (index == -1) {
            return;
        }
        _inventory.set(index, null);
    }

    public boolean hasIllegalItems() {
        return false; // TODO
    }

    public boolean hasMetalItems() {
        return false; // TODO
    }

//	########################################
//	#                Wanted                #
//	########################################

    public boolean isWanted() {
        return _isWanted;
    }

    public void setWanted() {
        _isWanted = true;
    }

    public void removeWanted() {
        _isWanted = false;
    }

    public boolean isInRestrictedArea() {
        return _inRestrictedArea;
    }

    public void enteredRestrictedArea() {
        _inRestrictedArea = true;
    }

    public void leftRestrictedArea() {
        _inRestrictedArea = false;
    }

    public boolean canBeArrested() {
        return _isWanted || _inRestrictedArea;
    }

//	########################################
//	#                Balance               #
//	########################################

    public int getBalance() {
        return _balance;
    }

    public void setBalance(int balance) {
        _balance = balance;
    }

    public void increaseBalance(int amount) {
        _balance += amount;
    }

    public void decreaseBalance(int amount) {
        _balance -= amount;
    }

    public int buyItem(Item item, int price) {
        if (!canBuyItem(item)) {
            return -1;
        }
        if (price > _balance) {
            return -2;
        }

        if (giveItem(item) == -1) {
            return -3;
        }

        decreaseBalance(price);
        updateItemCount(item);
        return 0;
    }

    private boolean canBuyItem(Item item) {
        if (item instanceof TrapItem && _numOfTrapsBought >= ((TrapItem) item).getLimit()) {
            return false;
        } else if (item instanceof CameraItem && _numOfCamerasBought >= ((CameraItem) item).getLimit()) {
            return false;
        } else if (item instanceof SensorItem && _numOfSensorsBought >= ((SensorItem) item).getLimit()) {
            return false;
        }
        return true;
    }

    private void updateItemCount(Item item) {
        if (item instanceof TrapItem) {
            _numOfTrapsBought++;
        } else if (item instanceof CameraItem) {
            _numOfCamerasBought++;
        } else if (item instanceof SensorItem) {
            _numOfSensorsBought++;
        }
    }

//	########################################
//	#                 Util                 #
//	########################################

    @Override
    public boolean equals(Object o) {
        return o instanceof PrisonEscapePlayer && ((PrisonEscapePlayer) o).getName().equals(this.getName());
    }


}
