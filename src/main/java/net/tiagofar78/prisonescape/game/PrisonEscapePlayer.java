package net.tiagofar78.prisonescape.game;

import org.bukkit.inventory.ItemStack;

public class PrisonEscapePlayer {
	
	private String _name;
	private TeamPreference _preference;
	private boolean _isWanted;
	
	public PrisonEscapePlayer(String name) {
		_name = name;
		_preference = TeamPreference.RANDOM;
		_isWanted = false;
	}
	
	public String getName() {
		return _name;
	}
	
	public TeamPreference getPreference() {
		return _preference;
	}
	
	/**
	 * @return 		0 if success
	 * 				-1 if full inventory
	 */
	public int giveItem(ItemStack item) {
		return 0;
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

}
