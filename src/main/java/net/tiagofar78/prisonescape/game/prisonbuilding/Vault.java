package net.tiagofar78.prisonescape.game.prisonbuilding;

import java.util.ArrayList;
import java.util.List;

import net.tiagofar78.prisonescape.game.PrisonEscapeItem;
import net.tiagofar78.prisonescape.managers.ConfigManager;

public class Vault {
	
	private int _nonHiddenSize;
	private List<PrisonEscapeItem> _nonHiddenContents;
	private int _hiddenSize;
	private List<PrisonEscapeItem> _hiddenContents;

	public Vault() {
		ConfigManager config = ConfigManager.getInstance();
		
		_nonHiddenSize = config.getVaultNonHiddenSize();
		_nonHiddenContents = createContentsList(_nonHiddenSize);
		_hiddenSize = config.getVaultHiddenSize();
		_hiddenContents = createContentsList(_hiddenSize);
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
		int size = isHidden ? _hiddenSize : _nonHiddenSize;
		
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
				clearContents(_nonHiddenContents, _nonHiddenSize);
				clearContents(_hiddenContents, _hiddenSize);
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
	
}
