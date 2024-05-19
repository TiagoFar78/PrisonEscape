package net.tiagofar78.prisonescape.menus;

import net.tiagofar78.prisonescape.game.Prisioner;
import net.tiagofar78.prisonescape.game.PrisonEscapePlayer;
import net.tiagofar78.prisonescape.items.GlassItem;
import net.tiagofar78.prisonescape.items.Item;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TradeMenu implements Clickable {

    private Prisioner _player1;
    private Prisioner _player2;

    public TradeMenu(Prisioner player1, Prisioner player2) {
        _player1 = player1;
        _player2 = player2;

        _player1.openMenu(this);
        _player2.openMenu(this);
    }

    @Override
    public void close() {
        _player1.closeMenu();
        _player2.closeMenu();
    }

    @Override
    public ClickReturnAction click(
            PrisonEscapePlayer player,
            int slot,
            Item itemHeld,
            boolean clickedPlayerInv
    ) {
        // TODO make click interactions
        return null;
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

        int[] clearIndexes = {1 * 9 + 1, 1 * 9 + 2, 2 * 9 + 1, 2 * 9 + 2};
        int player2ClearIndexesDiff = 5;

        for (int i : clearIndexes) {
            inv.setItem(i, null);
            inv.setItem(i + player2ClearIndexesDiff, null);
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

        int player1WoolIndex = 4 * 9 + 1;
        int player2WoolIndex = 4 * 9 + 6;

        inv.setItem(player1WoolIndex, wool);
        inv.setItem(player2WoolIndex, wool);
    }

}
