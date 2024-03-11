package net.tiagofar78.prisonescape.game;

import org.bukkit.inventory.ItemStack;

public class PrisonEscapePlayer {
	
	private String _name;
	private TeamPreference _preference;
	private boolean _isWanted;
	private boolean _isOnline;
	private boolean _hasEscaped;
	
	public PrisonEscapePlayer(String name) {
		_name = name;
		_preference = TeamPreference.RANDOM;
		_isWanted = false;
		_isOnline = true;
	}
	
	public String getName() {
		return _name;
	}
	
	public TeamPreference getPreference() {
		return _preference;
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
	 * @return 		0 if success
	 * 				-1 if full inventory
	 */
	public int giveItem(ItemStack item) {
		return 0;
	}
	
	public void giveLeavingPrisonItem() {
		
	}
	
	public void removeLeavingPrisonItem() {
		
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
	
//	########################################
//	#                 Util                 #
//	########################################
	
	@Override
	public boolean equals(Object o) {		
		return o instanceof PrisonEscapePlayer && ((PrisonEscapePlayer) o).getName().equals(this.getName());
	}

}
