package net.tiagofar78.prisonescape.game.prisonbuilding;

import net.tiagofar78.prisonescape.bukkit.BukkitWorldEditor;
import net.tiagofar78.prisonescape.managers.ConfigManager;
import net.tiagofar78.prisonescape.managers.MapManager;

import org.bukkit.Location;
import org.joml.Math;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Wall {

    private static final int WALL_HEIGHT = 5;
    private static final int MIN_CRACK_DISTANCE = 10;
    private static final int MAX_CRACK_DISTANCE = 30;

    private List<Location> _cornersLocations;
    private List<WallCrack> _cracks;

    public Wall(MapManager map) {
        _cornersLocations = map.getWallCornersLocations();
        _cracks = new ArrayList<>();
    }

    public void raiseFixedWall() {
        for (int i = 0; i < _cornersLocations.size() - 1; i++) {
            Location loc1 = _cornersLocations.get(i);
            Location loc2 = _cornersLocations.get(i + 1).clone().add(0, WALL_HEIGHT - 1, 0);
            BukkitWorldEditor.buildWall(loc1, loc2);
        }
    }

    public void putRandomCracks() {
        if (_cornersLocations.size() == 0) {
            return;
        }

        List<List<String>> crackFormats = ConfigManager.getInstance().getWallCrackFormats();

        int accumulatedDistance = MIN_CRACK_DISTANCE;
        for (int i = 0; i < _cornersLocations.size() - 1; i++) {
            Location corner1 = _cornersLocations.get(i);
            Location corner2 = _cornersLocations.get(i + 1);

            int xDiff = corner2.getBlockX() - corner1.getBlockX();
            int xAbsDiff = Math.abs(xDiff);
            int zDiff = corner2.getBlockZ() - corner1.getBlockZ();
            int zAbsDiff = Math.abs(zDiff);

            int xDirection = xDiff == 0 ? 0 : xDiff > 0 ? 1 : -1;
            int zDirection = zDiff == 0 ? 0 : zDiff > 0 ? 1 : -1;

            int crackPos = 0;
            while (true) {
                List<String> crackFormat = getRandomCrackFormat(crackFormats);

                int prevCrackPos = crackPos;
                crackPos += getRandomDistance(accumulatedDistance);
                accumulatedDistance = 0;

                int nextX = crackPos * xDirection;
                int nextZ = crackPos * zDirection;
                int crackLength = crackFormat.get(0).length();
                boolean isXAfterNextCorner = Math.abs(nextX + crackLength * xDirection) > xAbsDiff;
                boolean isZAfterNextCorner = Math.abs(nextZ + crackLength * zDirection) > zAbsDiff;
                if (isXAfterNextCorner) {
                    accumulatedDistance = xAbsDiff - prevCrackPos;
                    break;
                } else if (isZAfterNextCorner) {
                    accumulatedDistance = zAbsDiff - prevCrackPos;
                    break;
                }

                Location crackLoc = corner1.clone().add(nextX, 0, nextZ);
                WallCrack crack = new WallCrack(crackLoc, crackFormat, xDirection, zDirection);
                _cracks.add(crack);
                crack.putCrackOnWall();

                crackPos += crackLength;
            }
        }
    }

    private int getRandomDistance(int accumulatedDistance) {
        int minDistance = MIN_CRACK_DISTANCE - accumulatedDistance;
        if (minDistance < 0) {
            minDistance = 0;
        }

        int maxDistance = MAX_CRACK_DISTANCE - accumulatedDistance;
        if (maxDistance < 0) {
            return 0;
        }

        Random random = new Random();
        return random.nextInt(minDistance, maxDistance + 1);
    }

    private List<String> getRandomCrackFormat(List<List<String>> formats) {
        int randomIndex = new Random().nextInt(formats.size());
        List<String> randomFormat = formats.get(randomIndex);

        if (!isValidFormat(randomFormat)) {
            throw new IllegalArgumentException("The specified format is not valid. All lines must have equal length");
        }

        return randomFormat;
    }

    private boolean isValidFormat(List<String> format) {
        int formatHeight = format.size();
        if (formatHeight == 0) {
            return false;
        }

        int formatLength = format.get(0).length();
        for (int i = 1; i < format.size(); i++) {
            if (format.get(i).length() != formatLength) {
                return false;
            }
        }

        return true;
    }

    public void crackedBlocksExploded(List<Location> explodedBlocksLocations) {
        for (WallCrack crack : _cracks) {
            if (crack.contains(explodedBlocksLocations)) {
                crack.exploded();
            }
        }

        for (Location location : explodedBlocksLocations) {
            BukkitWorldEditor.removeWallBlock(location);
        }
    }

    public WallCrack getAffectedCrack(Location location) {
        for (WallCrack crack : _cracks) {
            if (crack.contains(location)) {
                return crack;
            }
        }

        return null;
    }

}
