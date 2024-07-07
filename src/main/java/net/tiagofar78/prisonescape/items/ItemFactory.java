package net.tiagofar78.prisonescape.items;

import net.tiagofar78.prisonescape.dataobjects.ItemProbability;

import java.util.List;
import java.util.Random;

public class ItemFactory {

    private static Item[] items = {
            new AntenaItem(),
            new BatteryItem(),
            new BoltsItem(),
            new BombItem(),
            new CellPhoneItem(),
            new CircuitBoardItem(),
            new CopperItem(),
            new DoorCodeItem(),
            new DuctTapeItem(),
            new EnergyDrinkItem(),
            new GoldBarItem(),
            new GoldenKeyItem(),
            new GrayKeyItem(),
            new HandcuffsItem(),
            new MatchesItem(),
            new MetalPlateItem(),
            new MetalShovelItem(),
            new MetalSpoonItem(),
            new NotePartItem(),
            new OilItem(),
            new PlasticPlateItem(),
            new PlasticShovelItem(),
            new PlasticSpoonItem(),
            new SearchItem(),
            new SelectNoneTeamItem(),
            new SelectPoliceTeamItem(),
            new SelectPrisonerTeamItem(),
            new StickItem(),
            new WireCutterItem(),
            new WrenchItem(),
            new ShopItem(),
            new TrapItem(),
            new CameraItem(),
            new SoundDetectorItem(),
            new RadarItem(),
            new OpenCamerasItem(),
            new MissionsItem()};

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

    public static Item getRandomItem(List<ItemProbability> itemsProbability) {
        double randomValue = new Random().nextDouble();

        double cumulativeWeight = 0;
        for (ItemProbability itemProbability : itemsProbability) {
            cumulativeWeight += itemProbability.getProbability();
            if (randomValue < cumulativeWeight) {
                return ItemFactory.createItem(itemProbability.getItemName());
            }
        }

        return new NullItem();
    }

}
