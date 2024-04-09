/**
 * A program Project4
 *
 * <p>Purdue University -- CS18000 -- Spring 2024 -- Project4-- </p>
 *
 * @author Dong Wang Purdue CS
 * @version Mar 27, 2024
 */
public class Maze {
    private final int[] end;
    private final char[][] grid;
    private final String name;
    private int[][] path;
    private final int[] start;

    public Maze(String name, char[][] grid, int[] start, int[] end) {
        this.name = name;
        this.grid = grid;
        this.start = start;
        this.end = end;
        this.path = null;
    }

    public int[] getEnd() {
        return end;
    }

    public char[][] getGrid() {
        return grid;
    }

    public String getName() {
        return name;
    }

    public int[][] getPath() {
        return path;
    }

    public int[] getStart() {
        return start;
    }

    public void setPath(int[][] path) {
        this.path = path;
    }

    public String pathString() {
        if (path == null || path.length == 0) {
            return "No path found";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(this.name).append("\n");
        sb.append("Moves: ").append(path.length).append("\n");
        sb.append("Start").append("\n");
        for (int i = 0; i < path.length; i++) {
            sb.append(path[i][0]).append("-").append(path[i][1]).append("\n");
        }
        sb.append("End");
        return sb.toString();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.name).append("\n");
        sb.append("Start: ").append(this.start[0]).append("-").append(this.start[1]).append("\n");
        sb.append("End: ").append(this.end[0]).append("-").append(this.end[1]).append("\n");
        for (char[] row : grid) {
            for (char c : row) {
                sb.append(c).append(",");
            }
            sb.deleteCharAt(sb.length() - 1).append("\n");
        }
        return sb.toString();
    }
}

