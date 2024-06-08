package net.tiagofar78.prisonescape.game.prisonbuilding;

import net.tiagofar78.prisonescape.bukkit.BukkitWorldEditor;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Maze {

    private static final int CELL_SIDE_SIZE = 5;

    private class Cell {
        public int start;
        public int end;

        public Cell(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public String toString() {
            return "( " + start + ", " + end + ")";
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Cell))
                return false;

            Cell vec = (Cell) obj;
            return vec.start == start && vec.end == end;
        }

        @Override
        public int hashCode() {
            return this.toString().hashCode();
        }
    }

    public List<Dirt> buildMaze(Location upperCornerLocation, List<String> mazeFormat) {
        if (!isValidFormat(mazeFormat)) {
            throw new IllegalArgumentException("Illegal maze format. All rows must have same length.");
        }

        int height = mazeFormat.size();
        int width = mazeFormat.get(0).length();

        fillWithDirt(upperCornerLocation, width, height);
        raiseWalls(upperCornerLocation, width, height);
        clearExits(upperCornerLocation, mazeFormat);
        clearSpawnPoints(upperCornerLocation, mazeFormat);

        return getDirts(upperCornerLocation, mazeFormat);
    }

    private void fillWithDirt(Location upperCornerLocation, int width, int height) {
        Location lowerCornerLocation = upperCornerLocation.clone().add(
                -width * CELL_SIDE_SIZE + 1,
                -2,
                -height * CELL_SIDE_SIZE + 1
        );

        Location dirtUpperCorner = upperCornerLocation.clone().add(-1, 0, -1);
        Location dirtLowerCorner = lowerCornerLocation.clone().add(1, 0, 1);
        BukkitWorldEditor.fillMazeWithDirt(dirtUpperCorner, dirtLowerCorner);
    }

    private void raiseWalls(Location upperCornerLocation, int width, int height) {
        List<Cell> mazeSteped = generateMaze(width, height);
        for (int z = 0; z < height; z++) {
            for (int x = 0; x < width; x++) {
                int current = (z * width) + x;
                int lower = ((z + 1) * width) + x;
                if (!mazeSteped.contains(new Cell(current, lower)) && z != height - 1) { // Check if there should be a horizontal wall
                    Location upperCorner = upperCornerLocation.clone().add(
                            -x * CELL_SIDE_SIZE,
                            0,
                            (-z - 1) * CELL_SIDE_SIZE + 1
                    );
                    Location lowerCorner = upperCornerLocation.clone().add(
                            (-x - 1) * CELL_SIDE_SIZE + 1,
                            -2,
                            (-z - 1) * CELL_SIDE_SIZE
                    );

                    BukkitWorldEditor.raiseMazeWall(upperCorner, lowerCorner);
                }

                if (!mazeSteped.contains(new Cell(current, current + 1)) && x != width - 1) {// Check if there should be a veritcal wall
                    Location upperCorner = upperCornerLocation.clone().add(
                            (-x - 1) * CELL_SIDE_SIZE + 1,
                            0,
                            -z * CELL_SIDE_SIZE
                    );
                    Location lowerCorner = upperCornerLocation.clone().add(
                            (-x - 1) * CELL_SIDE_SIZE,
                            -2,
                            (-z - 1) * CELL_SIDE_SIZE + 1
                    );

                    BukkitWorldEditor.raiseMazeWall(upperCorner, lowerCorner);
                }
            }
        }
    }

    private void clearExits(Location upperCornerLocation, List<String> mazeFormat) {
        int height = mazeFormat.size();
        int width = mazeFormat.get(0).length();

        for (int z = 0; z < height; z++) {
            String line = mazeFormat.get(z);
            for (int x = 0; x < width; x++) {
                if (line.charAt(x) == 'E') {
                    Location upperSpawnLoc = getPartUpperLocation(upperCornerLocation, x, z);
                    Location lowerSpawnLoc = getPartLowerLocation(upperCornerLocation, x, z, width, height);

                    BukkitWorldEditor.clearDirtFromMazePart(upperSpawnLoc, lowerSpawnLoc);
                }
            }
        }
    }

    private void clearSpawnPoints(Location upperCornerLocation, List<String> mazeFormat) {
        int height = mazeFormat.size();
        int width = mazeFormat.get(0).length();

        for (int z = 0; z < height; z++) {
            String line = mazeFormat.get(z);
            for (int x = 0; x < width; x++) {
                if (line.charAt(x) == 'S') {
                    Location upperSpawnLoc = getPartUpperLocation(upperCornerLocation, x, z);
                    Location lowerSpawnLoc = getPartLowerLocation(upperCornerLocation, x, z, width, height);

                    BukkitWorldEditor.clearMazePart(upperSpawnLoc, lowerSpawnLoc);
                }
            }
        }
    }

    private Location getPartUpperLocation(Location upperMazeLocation, int x, int z) {
        int upperX = x == 0 ? -x * CELL_SIDE_SIZE - 1 : -x * CELL_SIDE_SIZE;
        int upperZ = z == 0 ? -z * CELL_SIDE_SIZE - 1 : -z * CELL_SIDE_SIZE;

        return upperMazeLocation.clone().add(upperX, 0, upperZ);
    }

    private Location getPartLowerLocation(Location upperMazeLocation, int x, int z, int width, int height) {
        int lowerX = x == width - 1 ? (-x - 1) * CELL_SIDE_SIZE + 2 : (-x - 1) * CELL_SIDE_SIZE + 1;
        int lowerZ = z == height - 1 ? (-z - 1) * CELL_SIDE_SIZE + 2 : (-z - 1) * CELL_SIDE_SIZE + 1;

        return upperMazeLocation.clone().add(lowerX, -2, lowerZ);
    }

    private boolean isValidFormat(List<String> format) {
        if (format.size() == 0) {
            return false;
        }

        int width = format.get(0).length();
        for (String row : format) {
            if (row.length() != width) {
                return false;
            }
        }

        return true;
    }

    private List<Dirt> getDirts(Location upperCornerLocation, List<String> mazeFormat) {
        List<Dirt> dirts = new ArrayList<>();

        int height = mazeFormat.size();
        int width = mazeFormat.get(0).length();

        for (int z = 0; z < height; z++) {
            String line = mazeFormat.get(z);
            for (int x = 0; x < width; x++) {
                if (line.charAt(x) == '#') {
                    Location upperDirtLoc = getPartUpperLocation(upperCornerLocation, x, z);
                    Location lowerDirtLoc = getPartLowerLocation(upperCornerLocation, x, z, width, height);

                    dirts.add(new Dirt(upperDirtLoc, lowerDirtLoc));
                }
            }
        }

        return dirts;
    }

    // Prim algorithm
    private List<Cell> generateMaze(int width, int height) {
        List<Cell> maze = new ArrayList<>();

        List<Integer> visited = new ArrayList<>();
        List<Cell> toVisit = new ArrayList<>();

        visited.add(0);
        toVisit.add(new Cell(0, 1));
        toVisit.add(new Cell(0, width));

        Random rand = new Random();

        while (toVisit.size() > 0) {
            int randomIndex = rand.nextInt(toVisit.size());
            Cell nextPath = toVisit.remove(randomIndex);

            if (visited.contains(nextPath.end))
                continue;

            if (nextPath.start > nextPath.end)
                maze.add(new Cell(nextPath.end, nextPath.start));
            else
                maze.add(nextPath);

            visited.add(nextPath.end);

            int above = nextPath.end - width;
            if (above > 0 && !visited.contains(above))
                toVisit.add(new Cell(nextPath.end, above));

            int left = nextPath.end - 1;
            if (left % width != width - 1 && !visited.contains(left))
                toVisit.add(new Cell(nextPath.end, left));

            int right = nextPath.end + 1;
            if (right % width != 0 && !visited.contains(right))
                toVisit.add(new Cell(nextPath.end, right));

            int below = nextPath.end + width;
            if (below < width * height && !visited.contains(below))
                toVisit.add(new Cell(nextPath.end, below));
        }

        return maze;
    }
}
