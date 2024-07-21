package net.tiagofar78.prisonescape.game.prisonbuilding.placeables;

import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.trait.SkinTrait;
import net.tiagofar78.prisonescape.PrisonEscape;
import net.tiagofar78.prisonescape.game.Guard;
import net.tiagofar78.prisonescape.managers.ConfigManager;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class Camera {

    private static final int WATCH_CAMERA_DELAY = 10;

    private NPC _camera;

    public Camera(Location location) {
        _camera = createCameraRepresentation(location);
    }

    public void addWatcher(Guard player) {
        Player bukkitPlayer = Bukkit.getPlayer(player.getName());
        bukkitPlayer.setGameMode(GameMode.SPECTATOR);

        bukkitPlayer.teleport(_camera.getStoredLocation());
        Bukkit.getScheduler().runTaskLater(PrisonEscape.getPrisonEscape(), new Runnable() {

            @Override
            public void run() {
                bukkitPlayer.setSpectatorTarget(_camera.getEntity());
            }
        }, WATCH_CAMERA_DELAY);
    }

    public void delete() {
        _camera.despawn();
        _camera.destroy();
    }

    private NPC createCameraRepresentation(Location location) {
        ConfigManager config = ConfigManager.getInstance();
        String skinSignature = config.getCameraSkinSignature();
        String skinTexture = config.getCameraSkinTexture();

        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.PLAYER, "");
        npc.getOrAddTrait(SkinTrait.class).setSkinPersistent("Camera", skinSignature, skinTexture);
        npc.spawn(location);

        return npc;
    }

}
