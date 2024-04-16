package net.tiagofar78.prisonescape.game.prisonbuilding;

import net.tiagofar78.prisonescape.bukkit.BukkitWorldEditor;
import net.tiagofar78.prisonescape.managers.ConfigManager;

import org.joml.Math;

import java.util.List;
import java.util.Random;

public class Wall {

    private static final int WALL_HEIGHT = 5;
    private static final int MIN_CRACK_DISTANCE = 10;
    private static final int MAX_CRACK_DISTANCE = 30;

    private static final char CRACK_CHAR = '#';
    private static final String[] CRACK_FORMAT = {"O###O", "#####", "#####", "#####", "O###O"};

    private List<PrisonEscapeLocation> _cornersLocations;

    public Wall() {
        _cornersLocations = ConfigManager.getInstance().getWallCornersLocations();
    }

    public void raiseFixedWall() {
        for (int i = 0; i < _cornersLocations.size() - 1; i++) {
            PrisonEscapeLocation loc1 = _cornersLocations.get(i);
            PrisonEscapeLocation loc2 = new PrisonEscapeLocation(_cornersLocations.get(i + 1)).add(
                    0,
                    WALL_HEIGHT - 1,
                    0
            );
            BukkitWorldEditor.buildWall(loc1, loc2);
        }
    }

    public void putRandomCracks() {
        if (_cornersLocations.size() == 0) {
            return;
        }

        int accumulatedDistance = MIN_CRACK_DISTANCE;
        for (int i = 0; i < _cornersLocations.size() - 1; i++) {
            PrisonEscapeLocation corner1 = _cornersLocations.get(i);
            PrisonEscapeLocation corner2 = _cornersLocations.get(i + 1);

            int xDiff = corner2.getX() - corner1.getX();
            int xAbsDiff = Math.abs(xDiff);
            int zDiff = corner2.getZ() - corner1.getZ();
            int zAbsDiff = Math.abs(zDiff);

            int xDirection = xDiff == 0 ? 0 : xDiff > 0 ? 1 : -1;
            int zDirection = zDiff == 0 ? 0 : zDiff > 0 ? 1 : -1;

            int crackPos = 0;
            while (true) {
                int prevCrackPos = crackPos;
                crackPos += getRandomDistance(accumulatedDistance);
                accumulatedDistance = 0;

                int nextX = crackPos * xDirection;
                int nextZ = crackPos * zDirection;
                int crackLength = CRACK_FORMAT[0].length();
                boolean isXAfterNextCorner = Math.abs(nextX + crackLength * xDirection) > xAbsDiff;
                boolean isZAfterNextCorner = Math.abs(nextZ + crackLength * zDirection) > zAbsDiff;
                if (isXAfterNextCorner) {
                    accumulatedDistance = xAbsDiff - prevCrackPos;
                    break;
                } else if (isZAfterNextCorner) {
                    accumulatedDistance = zAbsDiff - prevCrackPos;
                    break;
                }

                PrisonEscapeLocation crackLoc = new PrisonEscapeLocation(corner1).add(nextX, 0, nextZ);
                putCrack(crackLoc, CRACK_FORMAT, xDirection, zDirection);
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

    private void putCrack(PrisonEscapeLocation crackLocation, String[] crackFormat, int xDirection, int zDirection) {
        int crackLength = crackFormat[0].length();
        int crackHeight = crackFormat.length;

        for (int l = 0; l < crackLength; l++) {
            for (int h = 0; h < crackHeight; h++) {
                if (crackFormat[crackHeight - 1 - h].charAt(l) == CRACK_CHAR) {
                    int x = crackLocation.getX() + xDirection * l;
                    int y = crackLocation.getY() + h;
                    int z = crackLocation.getZ() + zDirection * l;

                    BukkitWorldEditor.putCrackOnWall(x, y, z);
                }
            }
        }
    }

    public void crackedBlocksExploded(List<PrisonEscapeLocation> explodedBlocksLocations) {
        for (PrisonEscapeLocation location : explodedBlocksLocations) {
            BukkitWorldEditor.removeWallBlock(location);
        }
    }

}
