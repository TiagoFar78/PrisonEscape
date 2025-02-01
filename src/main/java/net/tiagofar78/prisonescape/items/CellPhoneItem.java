package net.tiagofar78.prisonescape.items;

import net.tiagofar78.prisonescape.bukkit.BukkitMessageSender;
import net.tiagofar78.prisonescape.game.PEGame;
import net.tiagofar78.prisonescape.game.PEPlayer;
import net.tiagofar78.prisonescape.game.prisonbuilding.PrisonBuilding;
import net.tiagofar78.prisonescape.managers.ConfigManager;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.ArrayList;
import java.util.List;

public class CellPhoneItem extends FunctionalItem implements Craftable {

    @Override
    public boolean isMetalic() {
        return true;
    }

    @Override
    public boolean isIllegal() {
        return true;
    }

    @Override
    public Material getMaterial() {
        return Material.GOAT_HORN;
    }

    @Override
    public void use(PEGame game, PEPlayer player, PlayerInteractEvent e) {
        Player bukkitPlayer = e.getPlayer();
        Location location = bukkitPlayer.getLocation();
        String playerName = bukkitPlayer.getName();

        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(playerName);

        PrisonBuilding prison = game.getPrison();
        if (!prison.hasCellPhoneCoverage(location)) {
            BukkitMessageSender.sendChatMessage(player, messages.getNoCellPhoneCoverageMessage());
            return;
        }

        int helicopterSpawnDelay = ConfigManager.getInstance().getHelicopterSpawnDelay();
        BukkitMessageSender.sendChatMessage(player, messages.getHelicopterOnTheWayMessage(helicopterSpawnDelay));

        int itemSlot = bukkitPlayer.getInventory().getHeldItemSlot();
        player.removeItem(itemSlot);
        player.updateInventory();

        prison.callHelicopter();
    }

    @Override
    public List<Item> getCratingItems() {
        List<Item> items = new ArrayList<>();

        items.add(new BatteryItem());
        items.add(new AntenaItem());
        items.add(new CircuitBoardItem());

        return items;
    }

}
