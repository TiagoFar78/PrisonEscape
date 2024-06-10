package net.tiagofar78.prisonescape.menus;

import net.tiagofar78.prisonescape.game.PEPlayer;
import net.tiagofar78.prisonescape.items.AntenaItem;
import net.tiagofar78.prisonescape.items.BombItem;
import net.tiagofar78.prisonescape.items.CellPhoneItem;
import net.tiagofar78.prisonescape.items.CircuitBoardItem;
import net.tiagofar78.prisonescape.items.DoorCodeItem;
import net.tiagofar78.prisonescape.items.GoldenKeyItem;
import net.tiagofar78.prisonescape.items.GrayKeyItem;
import net.tiagofar78.prisonescape.items.Item;
import net.tiagofar78.prisonescape.items.MetalPlateItem;
import net.tiagofar78.prisonescape.items.MetalShovelItem;
import net.tiagofar78.prisonescape.items.MetalSpoonItem;
import net.tiagofar78.prisonescape.items.PlasticShovelItem;
import net.tiagofar78.prisonescape.items.WireCutterItem;
import net.tiagofar78.prisonescape.items.WrenchItem;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class CraftingMenu implements Clickable {

    private static final int[] ITEMS_SLOTS = {
            9 + 1,
            9 + 2,
            9 + 3,
            9 + 4,
            9 + 5,
            9 + 6,
            9 + 7,
            18 + 1,
            18 + 2,
            18 + 3,
            18 + 4,
            18 + 5,
            18 + 6,
            18 + 7};

    @Override
    public Inventory toInventory(MessageLanguageManager messages) {
        int lines = 6;
        String title = messages.getCraftingMenuTitle();
        Inventory inv = Bukkit.createInventory(null, lines * 9, title);

        placeGlasses(inv, lines);
        placeItems(inv, messages);

        return inv;
    }

    private void placeGlasses(Inventory inv, int lines) {
        ItemStack glass = new ItemStack(Material.BLACK_STAINED_GLASS_PANE);
        ItemMeta glassMeta = glass.getItemMeta();
        glassMeta.setDisplayName(" ");
        glass.setItemMeta(glassMeta);

        for (int i = 0; i < lines; i++) {
            for (int j = 0; j < 9; j++) {
                inv.setItem(i * 9 + j, glass);
            }
        }
    }

    private void placeItems(Inventory inv, MessageLanguageManager messages) {
        Item[] items = {
                new MetalSpoonItem(),
                new PlasticShovelItem(),
                new MetalShovelItem(),
                new MetalPlateItem(),
                new GrayKeyItem(),
                new GoldenKeyItem(),
                new DoorCodeItem(),
                new CircuitBoardItem(),
                new BombItem(),
                new AntenaItem(),
                new CellPhoneItem(),
                new WireCutterItem(),
                new WrenchItem()};

        for (int i = 0; i < items.length; i++) {
            inv.setItem(ITEMS_SLOTS[i], items[i].toItemStack(messages));
        }
    }

    @Override
    public ClickReturnAction click(PEPlayer player, int slot, Item itemHeld, boolean clickedPlayerInv) {
        // TODO Auto-generated method stub
        return null;
    }

}
