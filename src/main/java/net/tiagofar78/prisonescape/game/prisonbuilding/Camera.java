package net.tiagofar78.prisonescape.game.prisonbuilding;

import net.tiagofar78.prisonescape.bukkit.BukkitWorldEditor;
import net.tiagofar78.prisonescape.game.Guard;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class Camera {

    private ArmorStand _armorStand;

    public Camera(PrisonEscapeLocation location) {
        _armorStand = createCameraRepresentation(location);
    }

    public void addWatcher(Guard player) {
        Player bukkitPlayer = Bukkit.getPlayer(player.getName());
        bukkitPlayer.setGameMode(GameMode.SPECTATOR);
        bukkitPlayer.setSpectatorTarget(_armorStand);
    }

    public void delete() {
        _armorStand.remove();
    }

    private ArmorStand createCameraRepresentation(PrisonEscapeLocation location) {
        World world = BukkitWorldEditor.getWorld();

        Location bukkitLoc = new Location(world, location.getX() + 0.5, location.getY(), location.getZ() + 0.5);
        ArmorStand armorStand = (ArmorStand) world.spawnEntity(bukkitLoc, EntityType.ARMOR_STAND);
        armorStand.setVisible(false);
        armorStand.setInvulnerable(true);

        return armorStand;
    }

}
