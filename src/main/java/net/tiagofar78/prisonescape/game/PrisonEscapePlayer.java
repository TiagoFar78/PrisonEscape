package net.tiagofar78.prisonescape.game;

import java.util.List;

public class PrisonEscapePlayer {
	
	private String _name;
	private TeamPreference _preference;
	private boolean _isWanted;
	private boolean _inRestrictedArea;
	private boolean _isOnline;
	private boolean _hasEscaped;
	private List<PrisonEscapeItem> _inventory;
	
	public PrisonEscapePlayer(String name) {
		_name = name;
		_preference = TeamPreference.RANDOM;
		_isWanted = false;
		_inRestrictedArea = false;
		_isOnline = true;
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

	public List<PrisonEscapeItem> getInventory() {
		return _inventory;
	}

	public void addItem(PrisonEscapeItem item) {
		_inventory.add(item);
	}

	public void deleteItem(PrisonEscapeItem item) {
		if (_inventory.contains(item)) {
			_inventory.remove(item);
		}
	}

	public void clearInventory() {
		_inventory.clear();
	}
	
	/**
	 * @return 		0 if success
	 * 				-1 if full inventory
	 */
	public int giveItem(PrisonEscapeItem item) {
		return 0; // TODO
	}
	
	public boolean hasIllegalItems() {
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
//	#                 Util                 #
//	########################################
	
	@Override
	public boolean equals(Object o) {		
		return o instanceof PrisonEscapePlayer && ((PrisonEscapePlayer) o).getName().equals(this.getName());
	}

}
