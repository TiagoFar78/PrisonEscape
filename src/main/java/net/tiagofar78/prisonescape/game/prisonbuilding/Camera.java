package net.tiagofar78.prisonescape.game.prisonbuilding;

import net.tiagofar78.prisonescape.bukkit.BukkitWorldEditor;
import net.tiagofar78.prisonescape.game.PrisonEscapePlayer;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class Camera {

    private ArmorStand _armorStand;

    public Camera(PrisonEscapeLocation location) {
        World world = BukkitWorldEditor.getWorld();

        Location bukkitLoc = new Location(world, location.getX() + 0.5, location.getY(), location.getZ() + 0.5);
        _armorStand = (ArmorStand) world.spawnEntity(bukkitLoc, EntityType.ARMOR_STAND);
        _armorStand.setVisible(false);
        _armorStand.setInvulnerable(true);
    }

    public void addWatcher(PrisonEscapePlayer player) {
        Player bukkitPlayer = Bukkit.getPlayer(player.getName());
        bukkitPlayer.setSpectatorTarget(_armorStand);
    }

}
