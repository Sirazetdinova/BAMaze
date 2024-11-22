package backend.academy.generation;

import backend.academy.cell.Cell;
import backend.academy.cell.CellType;
import backend.academy.maze.Maze;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PrimGenerationAlgorithm implements GenerationAlgorithm {
    private final Random random = new Random();

    @Override
    public Maze generate(int height, int width) {
        Cell[][] grid = new Cell[height][width];
        List<Cell> walls = new ArrayList<>();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                grid[y][x] = new Cell(x, y, CellType.WALL);
            }
        }
        int startX = random.nextInt(width / 2) * 2 + 1;
        int startY = random.nextInt(height / 2) * 2 + 1;
        grid[startY][startX].type(CellType.EMPTY);
        addWalls(grid, walls, startX, startY);

        while (!walls.isEmpty()) {
            int index = random.nextInt(walls.size());
            Cell wall = walls.remove(index);

            if (canBePassage(grid, wall)) {
                wall.type(CellType.EMPTY);
                addWalls(grid, walls, wall.x(), wall.y());
            }
        }

        return new Maze(height, width, grid);
    }

    private void addWalls(Cell[][] grid, List<Cell> walls, int x, int y) {
        if (x > 1 && grid[y][x - 2].type() == CellType.WALL && !walls.contains(grid[y][x - 1])) {
            walls.add(grid[y][x - 1]);
        }
        if (x < grid[0].length - 2 && grid[y][x + 2].type() == CellType.WALL && !walls.contains(grid[y][x + 1])) {
            walls.add(grid[y][x + 1]);
        }
        if (y > 1 && grid[y - 2][x].type() == CellType.WALL && !walls.contains(grid[y - 1][x])) {
            walls.add(grid[y - 1][x]);
        }
        if (y < grid.length - 2 && grid[y + 2][x].type() == CellType.WALL && !walls.contains(grid[y + 1][x])) {
            walls.add(grid[y + 1][x]);
        }
    }

    private boolean canBePassage(Cell[][] grid, Cell wall) {
        int x = wall.x();
        int y = wall.y();
        int passages = 0;

        if (x > 0 && grid[y][x - 1].type() == CellType.EMPTY) {
            passages++;
        }
        if (x < grid[0].length - 1 && grid[y][x + 1].type() == CellType.EMPTY) {
            passages++;
        }
        if (y > 0 && grid[y - 1][x].type() == CellType.EMPTY) {
            passages++;
        }
        if (y < grid.length - 1 && grid[y + 1][x].type() == CellType.EMPTY) {
            passages++;
        }

        return passages == 1;
    }
}
