package net.tiagofar78.prisonescape.items;

import org.bukkit.inventory.ItemStack;

public class ItemFactory {

    private static Item[] items =
            {new AntenaItem(), new BatteryItem(), new BoltsItem(), new BombItem(), new CellPhoneItem(), new CircuitBoardItem(), new CopperItem(), new DoorCodeItem(), new DuctTapeItem(), new EnergyDrinkItem(), new GoldBarItem(), new GoldenKeyItem(), new GrayKeyItem(), new HandcuffsItem(), new MatchesItem(), new MetalPlateItem(), new MetalShovelItem(), new MetalSpoonItem(), new NotePartItem(), new OilItem(), new PlasticPlateItem(), new PlasticShovelItem(), new PlasticSpoonItem(), new SearchItem(), new SelectNoneTeamItem(), new SelectPoliceTeamItem(), new SelectPrisionerTeamItem(), new StickItem(), new WireCutterItem(), new WrenchItem(), new ShopItem(), new TrapItem(), new CameraItem(), new SensorItem(), new RadarItem(), new OpenCamerasItem(), new MissionsItem()};

    public static Item createItem(ItemStack bukkitItem) {
        for (Item item : items) {
            if (item.matches(bukkitItem)) {
                return instantiateItem(item.getClass());
            }
        }

        return new NullItem();
    }

    public static Item createItem(String name) {
        for (Item item : items) {
            if (item.getConfigName().equals(name)) {
                return instantiateItem(item.getClass());
            }
        }

        return new NullItem();
    }

    private static Item instantiateItem(Class<? extends Item> itemClass) {
        try {
            return itemClass.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return new NullItem();
        }
    }

}
