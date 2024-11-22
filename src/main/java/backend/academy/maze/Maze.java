package backend.academy.maze;

import backend.academy.cell.Cell;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Maze {
    private int height;
    private int width;
    private Cell[][] grid;
    private Cell start;
    private Cell end;
    private int visitedCellsCount;
    private int passageCellsCount;
    private int optimalPathLength;

    public Maze(int height, int width, Cell[][] grid) {
        this.height = height;
        this.width = width;
        this.grid = grid;
    }
}
