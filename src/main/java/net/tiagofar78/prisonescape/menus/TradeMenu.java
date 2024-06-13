package net.tiagofar78.prisonescape.menus;

import net.tiagofar78.prisonescape.bukkit.BukkitMessageSender;
import net.tiagofar78.prisonescape.game.PEPlayer;
import net.tiagofar78.prisonescape.game.Prisoner;
import net.tiagofar78.prisonescape.items.GlassItem;
import net.tiagofar78.prisonescape.items.Item;
import net.tiagofar78.prisonescape.items.NullItem;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class TradeMenu implements Clickable {

    private static final int[] OFFERED_ITEMS_SLOTS = {1 * 9 + 1, 1 * 9 + 2, 2 * 9 + 1, 2 * 9 + 2};
    private static final int PLAYER_2_OFFERED_ITEMS_SLOTS_DISTANCE = 5;
    private static final int STATUS_WOOL_SLOT = 4 * 9 + 1;
    private static final int STATUS_GLASS_SLOT = 4 * 9 + 6;

    private Prisoner _player1;
    private Prisoner _player2;

    private boolean _hasPlayer1Accepted = false;
    private boolean _hasPlayer2Accepted = false;

    private List<Item> _offeredItemsPlayer1 = createEmptyItemList();
    private List<Item> _offeredItemsPlayer2 = createEmptyItemList();

    private boolean _isClosed = false;

    private List<Item> createEmptyItemList() {
        List<Item> list = new ArrayList<>();

        for (int i = 0; i < OFFERED_ITEMS_SLOTS.length; i++) {
            list.add(new NullItem());
        }

        return list;
    }

    public TradeMenu(Prisoner player1, Prisoner player2) {
        _player1 = player1;
        _player2 = player2;

        _player1.openMenu(this);
        _player2.openMenu(this);
    }

    private void commitTrade() {
        _isClosed = true;

        _player1.closeMenu(true);
        _player2.closeMenu(true);

        for (Item item : _offeredItemsPlayer1) {
            _player2.giveItem(item);
        }

        for (Item item : _offeredItemsPlayer2) {
            _player1.giveItem(item);
        }
    }

    private void addItem(List<Item> offeredItems, Item item) {
        for (int i = 0; i < OFFERED_ITEMS_SLOTS.length; i++) {
            if (offeredItems.get(i) instanceof NullItem) {
                offeredItems.set(i, item);
                return;
            }
        }

        throw new IndexOutOfBoundsException(); // Should never reach here
    }

    @Override
    public void close(PEPlayer firstCloser) {
        if (_isClosed) {
            return;
        }

        _isClosed = true;

        if (firstCloser.equals(_player1)) {
            _player2.closeMenu(true);
        } else {
            _player1.closeMenu(true);
        }

        for (Item item : _offeredItemsPlayer1) {
            _player1.giveItem(item);
        }

        for (Item item : _offeredItemsPlayer2) {
            _player2.giveItem(item);
        }
    }

    @Override
    public ClickReturnAction click(PEPlayer player, int slot, boolean clickedPlayerInv) {
        return clickedPlayerInv ? clickPlayerInv(player, slot) : clickViewInv(player, slot);
    }

    private ClickReturnAction clickPlayerInv(PEPlayer player, int slot) {
        int index = player.convertToInventoryIndex(slot);
        if (index == -1) {
            return ClickReturnAction.NOTHING;
        }

        Item offeredItem = player.getItemAt(slot);
        if (offeredItem instanceof NullItem) {
            return ClickReturnAction.NOTHING;
        }

        List<Item> offeredItems = player.equals(_player1) ? _offeredItemsPlayer1 : _offeredItemsPlayer2;
        addItem(offeredItems, offeredItem);

        player.setItem(index, new NullItem());

        _player1.updateView();
        _player2.updateView();

        return ClickReturnAction.DELETE_HOLD_AND_SELECTED;
    }

    private ClickReturnAction clickViewInv(PEPlayer player, int slot) {
        int index = convertSlotToIndex(slot);
        if (index == -1) {
            if (slot == STATUS_WOOL_SLOT) {
                clickStatusWool(player);
            }

            return ClickReturnAction.NOTHING;
        }

        List<Item> offeredItems = player.equals(_player1) ? _offeredItemsPlayer1 : _offeredItemsPlayer2;
        player.giveItem(offeredItems.get(index));

        offeredItems.set(index, new NullItem());

        _player1.updateView();
        _player2.updateView();

        return null;
    }

    private void clickStatusWool(PEPlayer player) {
        if (!isValidTrade()) {
            player.playSound(Sound.ENTITY_VILLAGER_NO);
        }

        boolean isPlayer1 = player.equals(_player1);

        boolean hasOtherPlayerAccepted = isPlayer1 ? _hasPlayer2Accepted : _hasPlayer1Accepted;
        if (hasOtherPlayerAccepted) {
            commitTrade();
            return;
        }

        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(player.getName());

        if (isPlayer1) {
            if (_hasPlayer1Accepted) {
                BukkitMessageSender.sendChatMessage(player, messages.getTradeAlreadyAcceptedMessage());
            }

            _hasPlayer1Accepted = true;
        } else {
            if (_hasPlayer2Accepted) {
                BukkitMessageSender.sendChatMessage(player, messages.getTradeAlreadyAcceptedMessage());
            }

            _hasPlayer2Accepted = true;
        }

        _player1.updateView();
        _player2.updateView();
    }

    @Override
    public Inventory toInventory(MessageLanguageManager messages) {
        int lines = 5;
        String title = messages.getTradeTitle();
        Inventory inv = Bukkit.createInventory(null, lines * 9, title);

        placeGlasses(inv, lines, messages);
        placeStatusWool(inv, messages, Material.RED_WOOL);

        return inv;
    }

    private void placeGlasses(Inventory inv, int lines, MessageLanguageManager messages) {
        Item glass = new GlassItem();
        for (int line = 0; line < lines; line++) {
            for (int col = 0; col < 4; col++) {
                inv.setItem(line * 9 + col, glass.toItemStack(messages));
            }

            for (int col = 5; col < 9; col++) {
                inv.setItem(line * 9 + col, glass.toItemStack(messages));
            }
        }

        ItemStack redGlass = new ItemStack(Material.RED_STAINED_GLASS);
        for (int line = 0; line < lines; line++) {
            inv.setItem(line * 9 + 4, redGlass);
        }

        for (int i : OFFERED_ITEMS_SLOTS) {
            inv.setItem(i, null);
            inv.setItem(i + PLAYER_2_OFFERED_ITEMS_SLOTS_DISTANCE, null);
        }
    }

    private void placeStatusWool(Inventory inv, MessageLanguageManager messages, Material type) {
        placeStatusWool(inv, messages, type, 1);
    }

    private void placeStatusWool(Inventory inv, MessageLanguageManager messages, Material type, int amount) {
        ItemStack wool = new ItemStack(type);
        wool.setAmount(amount);

        ItemMeta meta = wool.getItemMeta();
        meta.setDisplayName(messages.getTradeInvalidWoolName());
        wool.setItemMeta(meta);

        inv.setItem(STATUS_WOOL_SLOT, wool);
    }

    @Override
    public void updateInventory(Inventory inv, PEPlayer player) {
        List<Item> leftOfferedItems;
        List<Item> rightOfferedItems;

        boolean isPlayer1 = player.equals(_player1);
        if (isPlayer1) {
            leftOfferedItems = _offeredItemsPlayer1;
            rightOfferedItems = _offeredItemsPlayer2;
        } else {
            leftOfferedItems = _offeredItemsPlayer2;
            rightOfferedItems = _offeredItemsPlayer1;
        }

        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(player.getName());

        for (int i = 0; i < leftOfferedItems.size(); i++) {
            inv.setItem(OFFERED_ITEMS_SLOTS[i], leftOfferedItems.get(i).toItemStack(messages));
        }

        for (int i = 0; i < rightOfferedItems.size(); i++) {
            int slot = OFFERED_ITEMS_SLOTS[i] + PLAYER_2_OFFERED_ITEMS_SLOTS_DISTANCE;
            inv.setItem(slot, rightOfferedItems.get(i).toItemStack(messages));
        }

        if (!isValidTrade()) {
            _hasPlayer1Accepted = false;
            _hasPlayer2Accepted = false;
            placeRedWool(inv, messages);
            placeRedGlass(inv, messages);

            return;
        }

        if (isPlayer1) {
            if (_hasPlayer1Accepted) {
                placeGreenWool(inv, messages);
            } else {
                placeYellowWool(inv, messages);
            }

            if (_hasPlayer2Accepted) {
                placeGreenGlass(inv, messages);
            } else {
                placeRedGlass(inv, messages);
            }
        } else {
            if (_hasPlayer1Accepted) {
                placeGreenGlass(inv, messages);
            } else {
                placeRedGlass(inv, messages);
            }

            if (_hasPlayer2Accepted) {
                placeGreenWool(inv, messages);
            } else {
                placeYellowWool(inv, messages);
            }
        }
    }

    private void placeRedGlass(Inventory inv, MessageLanguageManager messages) {
        ItemStack glass = createStatusItem(Material.RED_STAINED_GLASS_PANE, messages.getTradeNotAcceptedGlassName());
        inv.setItem(STATUS_GLASS_SLOT, glass);
    }

    private void placeRedWool(Inventory inv, MessageLanguageManager messages) {
        ItemStack wool = createStatusItem(Material.RED_WOOL, messages.getTradeInvalidWoolName());
        inv.setItem(STATUS_WOOL_SLOT, wool);
    }

    private void placeYellowWool(Inventory inv, MessageLanguageManager messages) {
        ItemStack wool = createStatusItem(Material.YELLOW_WOOL, messages.getTradeAcceptWoolName());
        inv.setItem(STATUS_WOOL_SLOT, wool);
    }

    private void placeGreenGlass(Inventory inv, MessageLanguageManager messages) {
        ItemStack glass = createStatusItem(Material.GREEN_STAINED_GLASS_PANE, messages.getTradeAcceptedGlassName());
        inv.setItem(STATUS_GLASS_SLOT, glass);
    }

    private void placeGreenWool(Inventory inv, MessageLanguageManager messages) {
        ItemStack wool = createStatusItem(Material.GREEN_WOOL, messages.getTradeAcceptedWoolName());
        inv.setItem(STATUS_WOOL_SLOT, wool);
    }

    private ItemStack createStatusItem(Material material, String name) {
        ItemStack wool = new ItemStack(material);

        ItemMeta meta = wool.getItemMeta();
        meta.setDisplayName(name);
        wool.setItemMeta(meta);

        return wool;
    }

    private int convertSlotToIndex(int slot) {
        for (int i = 0; i < OFFERED_ITEMS_SLOTS.length; i++) {
            if (OFFERED_ITEMS_SLOTS[i] == slot) {
                return i;
            }
        }

        return -1;
    }

    private int size(List<Item> items) {
        int count = 0;

        for (Item item : items) {
            if (!(item instanceof NullItem)) {
                count++;
            }
        }

        return count;
    }

    private boolean isValidTrade() {
        int offeredItems1Size = size(_offeredItemsPlayer1);
        int offeredItems2Size = size(_offeredItemsPlayer2);

        boolean condition1 = offeredItems1Size != 0 || offeredItems2Size != 0;
        boolean condition2 = _player1.inventoryEmptySlots() >= offeredItems2Size;
        boolean condition3 = _player2.inventoryEmptySlots() >= offeredItems1Size;

        return condition1 && condition2 && condition3;
    }

}
