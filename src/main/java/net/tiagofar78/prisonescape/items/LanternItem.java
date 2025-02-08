package net.tiagofar78.prisonescape.items;

import net.tiagofar78.prisonescape.game.Guard;
import net.tiagofar78.prisonescape.game.PEGame;
import net.tiagofar78.prisonescape.game.PEPlayer;
import net.tiagofar78.prisonescape.managers.ConfigManager;
import net.tiagofar78.prisonescape.managers.MessageLanguageManager;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class LanternItem extends FunctionalItem implements Craftable {

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
        return Material.LANTERN;
    }

    @Override
    public void use(PEGame game, PEPlayer peplayer, PlayerInteractEvent e) {
        Player player = e.getPlayer();
        String playerName = player.getName();

        Location loc = e.getPlayer().getLocation();

        ConfigManager config = ConfigManager.getInstance();
        int blindingDistanceSquared = (int) Math.pow(config.getBlindingDistance(), 2);
        int blindnessSeconds = config.getBlindnessSeconds();

        int affectedGuards = 0;
        for (Guard guard : game.getGuardsTeam().getMembers()) {
            if (guard.getLocation().distanceSquared(loc) <= blindingDistanceSquared) {
                guard.setEffect(PotionEffectType.BLINDNESS, blindnessSeconds, 100);
                affectedGuards++;
            }
        }

        game.getPEPlayer(playerName).removeItem(player.getInventory().getHeldItemSlot());
        MessageLanguageManager messages = MessageLanguageManager.getInstanceByPlayer(playerName);
        peplayer.sendChatMessage(messages.getBlindnessAppliedMessage(affectedGuards));
    }

    @Override
    public List<Item> getCratingItems() {
        List<Item> items = new ArrayList<>();

        items.add(new CopperItem());
        items.add(new BatteryItem());

        return items;
    }

}
