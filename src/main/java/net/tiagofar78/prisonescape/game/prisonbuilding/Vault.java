package net.tiagofar78.prisonescape.game.prisonbuilding;

import java.util.ArrayList;
import java.util.List;

import net.tiagofar78.prisonescape.bukkit.BukkitMenu;
import net.tiagofar78.prisonescape.game.PrisonEscapeItem;

public class Vault {
	
	private static final int NON_HIDDEN_SIZE = 4;
	private static final int HIDDEN_SIZE = 1;	
	
	private List<PrisonEscapeItem> _nonHiddenContents;
	private List<PrisonEscapeItem> _hiddenContents;

	public Vault() {
		_nonHiddenContents = createContentsList(NON_HIDDEN_SIZE);
		_hiddenContents = createContentsList(HIDDEN_SIZE);
	}
	
	private List<PrisonEscapeItem> createContentsList(int size) {
		List<PrisonEscapeItem> list = new ArrayList<>();
		
		for (int i = 0; i < size; i++) {
			list.add(null);
		}
		
		return list;
	}
	
	public void addItem(boolean isHidden, int index, PrisonEscapeItem item) {
		List<PrisonEscapeItem> contents = isHidden ? _hiddenContents : _nonHiddenContents;
		int size = isHidden ? HIDDEN_SIZE : NON_HIDDEN_SIZE;
		
		if (index >= size) {
			throw new IndexOutOfBoundsException();
		}
		
		contents.set(index, item);
	}
	
	public PrisonEscapeItem removeItem(boolean isHidden, int index) {
		List<PrisonEscapeItem> contents = isHidden ? _hiddenContents : _nonHiddenContents;
		
		PrisonEscapeItem item = contents.get(index);
		
		contents.set(index, null);
		return item;
	}
	
	/**
	 * 
	 * @return 	0 if no illegal items were found<br>
	 * 			1 if illegal items were found
	 */
	public int search() {
		for (PrisonEscapeItem item : _nonHiddenContents) {
			if (item != null && item.isIllegal()) {
				clearContents(_nonHiddenContents, NON_HIDDEN_SIZE);
				clearContents(_hiddenContents, HIDDEN_SIZE);
				return -1;
			}
		}
		
		return 0;
	}
	
	private void clearContents(List<PrisonEscapeItem> contents, int size) {
		for (int i = 0; i < size; i++) {
			contents.set(i, null);
		}
	}
	
	public void open(String playerName) {
		BukkitMenu.openVault(playerName, _nonHiddenContents, _hiddenContents);
	}
	
}
