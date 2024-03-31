package net.tiagofar78.prisonescape.game.prisonbuilding;

import net.tiagofar78.prisonescape.bukkit.BukkitMenu;
import net.tiagofar78.prisonescape.game.PrisonEscapeItem;
import net.tiagofar78.prisonescape.game.PrisonEscapePlayer;

import java.util.ArrayList;
import java.util.List;

public class Vault implements Clickable {

    private static final int NON_HIDDEN_SIZE = 4;
    private static final int HIDDEN_SIZE = 1;

    private List<PrisonEscapeItem> _nonHiddenContents;
    private List<PrisonEscapeItem> _hiddenContents;
    private boolean _isOpen;

    private PrisonEscapePlayer _owner;

    public Vault(PrisonEscapePlayer owner) {
        _nonHiddenContents = createContentsList(NON_HIDDEN_SIZE);
        _hiddenContents = createContentsList(HIDDEN_SIZE);
        _isOpen = false;

        this._owner = owner;
    }

    private List<PrisonEscapeItem> createContentsList(int size) {
        List<PrisonEscapeItem> list = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            list.add(null);
        }

        return list;
    }

    public PrisonEscapePlayer getOwner() {
        return _owner;
    }

    public void setItem(boolean isHidden, int index, PrisonEscapeItem item) {
        List<PrisonEscapeItem> contents = isHidden ? _hiddenContents : _nonHiddenContents;
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
        for (PrisonEscapeItem item : _nonHiddenContents) {
            if (item != null && item.isIllegal()) {
                clearContents(_nonHiddenContents, NON_HIDDEN_SIZE);
                clearContents(_hiddenContents, HIDDEN_SIZE);
                return 1;
            }
        }

        return 0;
    }

    private void clearContents(List<PrisonEscapeItem> contents, int size) {
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
    public ClickReturnAction click(PrisonEscapePlayer player, int slot, PrisonEscapeItem itemHeld) {
        int itemIndex = BukkitMenu.convertToIndexVault(slot);
        if (itemIndex == -1) {
            return ClickReturnAction.NOTHING;
        }

        boolean isHidden = BukkitMenu.isHiddenIndexVault(slot);

        setItem(isHidden, itemIndex, itemHeld);
        return ClickReturnAction.CHANGE_HOLD_AND_SELECTED;
    }

}
