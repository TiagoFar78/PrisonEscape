package net.tiagofar78.prisonescape.items;

import org.bukkit.inventory.ItemStack;

public class ItemFactory {

    private static Item[] items =
            {new AntenaItem(), new BatteryItem(), new BoltsItem(), new CircuitBoardItem(), new CopperItem(), new DuctTapeItem(), new EnergyDrinkItem(), new GoldBarItem(), new HandcuffsItem(), new MatchesItem(), new MetalPlateItem(), new NotePartItem(), new OilItem(), new PlasticPlateItem(), new SearchItem(), new SelectNoneTeamItem(), new SelectPoliceTeamItem(), new SelectPrisionerTeamItem(), new StickItem()};

    public static Item createItem(ItemStack bukkitItem) {
        for (Item item : items) {
            if (item.matches(bukkitItem)) {
                return item;
            }
        }

        return new NullItem();
    }

}
