import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**
 * A program Project4
 *
 * <p>Purdue University -- CS18000 -- Spring 2024 -- Project4-- </p>
 *
 * @author Dong Wang Purdue CS
 * @version Mar 27, 2024
 */

public class MazeSolver {
    private Maze maze;

    public MazeSolver() {

    }

    public void readMaze(String filename) throws InvalidMazeException, IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String name = reader.readLine().trim();
            if (name.isEmpty()) {
                throw new InvalidMazeException("Maze name is missing");
            }

            String startLine = reader.readLine().trim();
            if (!startLine.matches("Start: \\d+-\\d+")) {
                throw new InvalidMazeException("Invalid start format");
            }

            String endLine = reader.readLine().trim();
            if (!endLine.matches("End: \\d+-\\d+")) {
                throw new InvalidMazeException("Invalid end format");
            }

            int[] start = parseCoordinates(startLine.substring(startLine.indexOf(":") + 1).trim());
            int[] end = parseCoordinates(endLine.substring(endLine.indexOf(":") + 1).trim());

            if (Arrays.equals(start, end)) {
                throw new InvalidMazeException("Start and end coordinates cannot be the same");
            }

            List<char[]> rowList = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) break;
                String[] cells = line.split(",");
                char[] row = new char[cells.length];
                for (int i = 0; i < cells.length; i++) {
                    row[i] = cells[i].charAt(0);
                }
                rowList.add(row);
            }

            char[][] grid = new char[rowList.size()][];
            grid = rowList.toArray(grid);

            if (grid.length == 0 || grid[0].length == 0) {
                throw new InvalidMazeException("Empty maze grid");
            }

            if (start[0] < 0 || start[0] >= grid.length || start[1] < 0 || start[1] >= grid[0].length ||
                    end[0] < 0 || end[0] >= grid.length || end[1] < 0 || end[1] >= grid[0].length) {
                throw new InvalidMazeException("Start or end coordinates out of maze bounds");
            }

            maze = new Maze(name, grid, start, end);
        } catch (IOException e) {
            throw new IOException("Error reading file: " + filename, e);
        }
    }

    public void solveMaze() {
        int[][] directions = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        Map<String, String> parent = new HashMap<>();
        Set<String> visited = new HashSet<>();
        Queue<int[]> queue = new LinkedList<>();

        int[] start = maze.getStart();
        queue.add(start);
        String startKey = rowColToString(start[0], start[1]);
        parent.put(startKey, null);
        visited.add(startKey);

        while (!queue.isEmpty()) {
            int[] current = queue.poll();

            if (Arrays.equals(current, maze.getEnd())) {
                buildPath(current, parent);
                return;
            }

            for (int[] direction : directions) {
                int[] next = new int[]{current[0] + direction[0], current[1] + direction[1]};
                String nextKey = rowColToString(next[0], next[1]);

                if (isValid(next) && !visited.contains(nextKey)) {
                    queue.add(next);
                    parent.put(nextKey, rowColToString(current[0], current[1])); // Set parent to current
                    visited.add(nextKey);
                }
            }
        }

        // Handle no solution
        maze.setPath(new int[0][0]);
    }

    private boolean isValid(int[] position) {
        int row = position[0];
        int col = position[1];
        char[][] grid = maze.getGrid();
        return row >= 0 && row < grid.length && col >= 0 && col < grid[0].length && grid[row][col] == 'P';
    }

    private void buildPath(int[] end, Map<String, String> parent) {
        List<int[]> path = new ArrayList<>();
        String key = rowColToString(end[0], end[1]);

        while (key != null) {
            String[] parts = key.split("-");
            int row = Integer.parseInt(parts[0]);
            int col = Integer.parseInt(parts[1]);
            path.add(0, new int[]{row, col});
            key = parent.get(key);
        }

        int[][] finalPath = new int[path.size()][2];
        for (int i = 0; i < path.size(); i++) {
            finalPath[i] = path.get(i);
        }
        maze.setPath(finalPath);
    }

    private String rowColToString(int row, int col) {
        return row + "-" + col;
    }


    private int[] parseCoordinates(String coordinateString) throws InvalidMazeException {
        String[] parts = coordinateString.split("-");
        if (parts.length != 2) {
            throw new InvalidMazeException("Invalid coordinate format");
        }
        try {
            int row = Integer.parseInt(parts[0]);
            int col = Integer.parseInt(parts[1]);
            return new int[]{row, col};
        } catch (NumberFormatException e) {
            throw new InvalidMazeException("Non-integer coordinates");
        }
    }


    private boolean isValid(int row, int col) {
        return row >= 0 && row < maze.getGrid().length && col >= 0 && col < maze.getGrid()[0].length;
    }

    public void writeSolution(String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            writer.write(maze.pathString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
