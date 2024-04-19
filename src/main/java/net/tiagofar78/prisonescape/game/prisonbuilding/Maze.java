package net.tiagofar78.prisonescape.game.prisonbuilding;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import net.tiagofar78.prisonescape.bukkit.BukkitWorldEditor;

public class Maze extends Canvas {

    private static final Random rand = new Random();
    private static final int CELL_SIDE_SIZE = 5;

    // This can be removed after, only used to generate frame
    private static final int TILE_WIDTH = 20;
    private static final int TILE_HEIGHT = 20;

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

    public void buildMaze(PrisonEscapeLocation upperCornerLocation, List<String> mazeFormat) {
        if (!isValidFormat(mazeFormat)) {
            throw new IllegalArgumentException("Illegal maze format. All rows must have same length.");
        }

        int height = mazeFormat.size();
        int width = mazeFormat.get(0).length();

        fillWithDirt(upperCornerLocation, width, height);
        raiseWalls(upperCornerLocation, width, height);
        clearExits(upperCornerLocation, mazeFormat);
        clearSpawnPoints(upperCornerLocation, mazeFormat);
    }
    
    private void fillWithDirt(PrisonEscapeLocation upperCornerLocation, int width, int height) {
        PrisonEscapeLocation lowerCornerLocation = new PrisonEscapeLocation(upperCornerLocation).add(
                -width * CELL_SIDE_SIZE + 1,
                -2,
                -height * CELL_SIDE_SIZE + 1
        );

        PrisonEscapeLocation dirtUpperCorner = new PrisonEscapeLocation(upperCornerLocation).add(-1, 0, -1);
        PrisonEscapeLocation dirtLowerCorner = new PrisonEscapeLocation(lowerCornerLocation).add(1, 0, 1);
        BukkitWorldEditor.fillMazeWithDirt(dirtUpperCorner, dirtLowerCorner);
    }
    
    private void raiseWalls(PrisonEscapeLocation upperCornerLocation, int width, int height) {
        List<Cell> mazeSteped = generateMaze(width, height);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int current = (y * width) + x;
                int lower = ((y + 1) * width) + x;
                if (!mazeSteped.contains(new Cell(current, lower)) && x != height - 1) {// Check if there should be a horizontal wall
//                    g.drawLine(x * TILE_width, (y + 1) * TILE_height, (x + 1) * TILE_width, (y + 1) * TILE_height); // add horizontal wall
                }
                
                if (!mazeSteped.contains(new Cell(current, current + 1))) {// Check if there should be a veritcal wall
//                    g.drawLine((x + 1) * TILE_width, y * TILE_height, (x + 1) * TILE_width, (y + 1) * TILE_height); // add vertical wall   
                }
            }
        }
    }
    
    private void clearExits(PrisonEscapeLocation upperCornerLocation, List<String> mazeFormat) {
        
    }
    
    private void clearSpawnPoints(PrisonEscapeLocation upperCornerLocation, List<String> mazeFormat) {
        int height = mazeFormat.size();
        int width = mazeFormat.get(0).length();
        
        for (int z = 0; z < height; z++) {
            String line = mazeFormat.get(z);
            for (int x = 0; x < width; x++) {
                if (line.charAt(x) == 'S') {
                    System.out.println("encontrou S na linha " + z + " e na coluna " + x);
                    int upperX = x == 0 ? -x * CELL_SIDE_SIZE - 1 : -x * CELL_SIDE_SIZE;
                    int upperZ = z == 0 ? -z * CELL_SIDE_SIZE - 1 : -z * CELL_SIDE_SIZE;
                    int lowerX = x == width - 1 ? upperX - CELL_SIDE_SIZE + 1 : upperX - CELL_SIDE_SIZE + 2;
                    int lowerZ = z == height - 1 ? upperZ - CELL_SIDE_SIZE + 1 : upperZ - CELL_SIDE_SIZE + 2;
                    
                    PrisonEscapeLocation upperSpawnLoc = new PrisonEscapeLocation(upperCornerLocation).add(upperX, 0, upperZ);
                    PrisonEscapeLocation lowerSpawnLoc = new PrisonEscapeLocation(upperCornerLocation).add(lowerX, 2, lowerZ);
                    
                    BukkitWorldEditor.clearMazePart(upperSpawnLoc, lowerSpawnLoc);
                }
            }
        }
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

 // Leaving this function here so it can serve as an example for buildMaze
    public void paint(Graphics g) {
        super.paint(g);

        // Fill with dirt
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, WIDTH * TILE_WIDTH, HEIGHT * TILE_HEIGHT);

        // Add external walls
        g.setColor(Color.BLACK);
        g.drawLine(0, 0, 0, HEIGHT * TILE_HEIGHT);
        g.drawLine(0, 0, WIDTH * TILE_WIDTH, 0);
        g.drawLine(WIDTH * TILE_WIDTH, 0, WIDTH * TILE_WIDTH, HEIGHT * TILE_HEIGHT);
        g.drawLine(0, HEIGHT * TILE_HEIGHT, WIDTH * TILE_WIDTH, HEIGHT * TILE_HEIGHT);

        // Add horizontal and vertical walls
        List<Cell> mazeSteped = generateMaze(WIDTH, HEIGHT);
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                int current = (y * WIDTH) + x;
                int lower = ((y + 1) * WIDTH) + x;
                if (!mazeSteped.contains(new Cell(current, lower))) // Check if there should be a horizontal wall
                    g.drawLine(x * TILE_WIDTH, (y + 1) * TILE_HEIGHT, (x + 1) * TILE_WIDTH, (y + 1) * TILE_HEIGHT); // add horizontal wall
                if (!mazeSteped.contains(new Cell(current, current + 1))) // Check if there should be a veritcal wall
                    g.drawLine((x + 1) * TILE_WIDTH, y * TILE_HEIGHT, (x + 1) * TILE_WIDTH, (y + 1) * TILE_HEIGHT); // add vertical wall
            }
        }

    }

    // Prim algorithm
    private List<Cell> generateMaze(int width, int height) {
        List<Cell> maze = new ArrayList<>();

        List<Integer> visited = new ArrayList<>();
        List<Cell> toVisit = new ArrayList<>();

        visited.add(0);
        toVisit.add(new Cell(0, 1));
        toVisit.add(new Cell(0, width));

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

    // So we can visualize the maze for now, to delete after completing buildMaze function
    public static void main(String[] args) {
        Maze mazeGen = new Maze();
        mazeGen.generateMaze(WIDTH, HEIGHT);
        mazeGen.setSize(830, 650);
        JFrame frame = new JFrame("Maze Generator");
        frame.add(mazeGen);
        frame.setSize(830, 650);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
