package net.tiagofar78.prisonescape.game.prisonbuilding;

import net.tiagofar78.prisonescape.bukkit.BukkitWorldEditor;
import net.tiagofar78.prisonescape.game.Guard;
import net.tiagofar78.prisonescape.game.PrisonEscapeGame;
import net.tiagofar78.prisonescape.game.PrisonEscapePlayer;
import net.tiagofar78.prisonescape.managers.ConfigManager;
import net.tiagofar78.prisonescape.managers.GameManager;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.List;

public class SoundDetector {

    private static final int MAX_VALUE = 10;
    private static final int MIN_VALUE = 1;

    private int _index;
    private PrisonEscapeLocation _location;
    private List<PrisonEscapePlayer> _playersInRange;

    public SoundDetector(int index, PrisonEscapeLocation location) {
        _index = index;
        _location = location;
        _playersInRange = new ArrayList<>();

        createOnWorld();

        PrisonEscapeGame game = GameManager.getGame();
        List<Guard> guards = game.getGuardsTeam().getMembers();
        for (Guard guard : guards) {
            guard.addSoundDetectorLine(MIN_VALUE);
        }
    }

    public boolean isDetectingSound() {
        return _playersInRange.size() != 0;
    }

    public void playerMoved(PrisonEscapePlayer player, PrisonEscapeLocation playerLocation) {
        if (isInRange(playerLocation) && !_playersInRange.contains(player)) {
            _playersInRange.add(player);
            updateValue();
        } else if (!isInRange(playerLocation) && _playersInRange.contains(player)) {
            _playersInRange.remove(player);
            updateValue();
        }
    }

    private boolean isInRange(PrisonEscapeLocation location) {
        double range = ConfigManager.getInstance().getSoundDetectorRange();
        return distance(_location, location) <= range;
    }

    private double distance(PrisonEscapeLocation loc1, PrisonEscapeLocation loc2) {
        double xComponent = Math.pow(loc1.getX() - loc2.getX(), 2);
        double yComponent = Math.pow(loc1.getY() - loc2.getY(), 2);
        double zComponent = Math.pow(loc1.getZ() - loc2.getZ(), 2);

        return Math.sqrt(xComponent + yComponent + zComponent);
    }

    public void delete() {
        deleteFromWorld();
    }

    private void createOnWorld() {
        World world = BukkitWorldEditor.getWorld();
        Location location = new Location(world, _location.getX(), _location.getY(), _location.getZ());

        location.getBlock().setType(Material.LIGHTNING_ROD);
    }

    private void deleteFromWorld() {
        World world = BukkitWorldEditor.getWorld();
        Location location = new Location(world, _location.getX(), _location.getY(), _location.getZ());
        location.getBlock().setType(Material.AIR);
    }

    private void updateValue() {
        int value = isDetectingSound() ? MAX_VALUE : MIN_VALUE;

        List<Guard> guards = GameManager.getGame().getGuardsTeam().getMembers();
        for (Guard guard : guards) {
            guard.updateSoundDetectorValue(_index, value);
        }
    }

}
