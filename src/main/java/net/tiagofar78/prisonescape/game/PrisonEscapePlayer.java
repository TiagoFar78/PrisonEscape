package net.tiagofar78.prisonescape.game;

import net.tiagofar78.prisonescape.bukkit.BukkitMenu;

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
    private List<PrisonEscapeItem> _inventory;
    private int _balance;

    public PrisonEscapePlayer(String name) {
        _name = name;
        _preference = TeamPreference.RANDOM;
        _isWanted = false;
        _inRestrictedArea = false;
        _isOnline = true;
        _inventory = createInventory();
        _balance = 0;
    }

    private List<PrisonEscapeItem> createInventory() {
        List<PrisonEscapeItem> list = new ArrayList<>();

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
    public int giveItem(PrisonEscapeItem item) {
        for (int i = 0; i < INVENTORY_SIZE; i++) {
            if (_inventory.get(i) == null) {
                setItem(i, item);
                return 0;
            }
        }

        return -1;
    }

    public void setItem(int index, PrisonEscapeItem item) {
        _inventory.set(index, item);

        BukkitMenu.setItem(_name, index, item);
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
//	#                Money                 #
//	########################################

    public int getBalance() {
        return _balance;
    }

    public void setBalance(int balance) {
        _balance = balance;
    }

    public void increaseBalance(int missionPay) {
        _balance += missionPay;
    }

    public void decreaseBalance(int itemPrice) {
        _balance -= itemPrice;
    }

//	########################################
//	#                 Util                 #
//	########################################

    @Override
    public boolean equals(Object o) {
        return o instanceof PrisonEscapePlayer && ((PrisonEscapePlayer) o).getName().equals(this.getName());
    }

}
