package net.tiagofar78.prisonescape.game.prisonbuilding;

import net.tiagofar78.prisonescape.game.PrisonEscapePlayer;
import net.tiagofar78.prisonescape.managers.ConfigManager;

import java.util.ArrayList;
import java.util.List;

public class SoundDetector {

    private PrisonEscapeLocation _location;
    private List<PrisonEscapePlayer> _playersInRange;

    public SoundDetector(PrisonEscapeLocation location) {
        _location = location;
        _playersInRange = new ArrayList<>();
    }

    public boolean isDetectingSound() {
        return _playersInRange.size() != 0;
    }

    public void playerMoved(PrisonEscapePlayer player, PrisonEscapeLocation playerLocation) {
        if (isInRange(playerLocation) && !_playersInRange.contains(player)) {
            _playersInRange.add(player);
        } else if (!isInRange(playerLocation) && _playersInRange.contains(player)) {
            _playersInRange.remove(player);
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

}
