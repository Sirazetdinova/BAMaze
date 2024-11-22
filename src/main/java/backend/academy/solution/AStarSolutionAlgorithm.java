package backend.academy.solution;

import backend.academy.cell.Cell;
import backend.academy.cell.CellType;
import backend.academy.maze.Maze;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

public class AStarSolutionAlgorithm implements SolutionAlgorithm {
    private int visitedCellsCount;
    private int passagesCount;
    int optimalPathLength;

    private void updateMazeStats(Maze maze) {
        maze.visitedCellsCount(visitedCellsCount);
        maze.passageCellsCount(passagesCount);
        maze.optimalPathLength(optimalPathLength);
    }

    private int countPassages(Cell[][] grid) {
        int count = 0;
        for (Cell[] row : grid) {
            for (Cell cell : row) {
                if (cell.type() == CellType.EMPTY) {
                    count++;
                }
            }
        }
        return count;
    }

    private int heuristic(Cell a, Cell b) {
        return Math.abs(a.y() - b.y()) + Math.abs(a.x() - b.x());
    }

    private void markPath(Node endNode) {
        Node current = endNode;
        while (current != null) {
            current.cell.type(CellType.PATH);
            current = current.parent;
            optimalPathLength++;
        }
    }

    private List<Cell> getNeighbors(Cell cell, Cell[][] grid) {
        List<Cell> neighbors = new ArrayList<>();
        int row = cell.y();
        int col = cell.x();

        if (row > 0) {
            neighbors.add(grid[row - 1][col]);
        }
        if (row < grid.length - 1) {
            neighbors.add(grid[row + 1][col]);
        }
        if (col > 0) {
            neighbors.add(grid[row][col - 1]);
        }
        if (col < grid[0].length - 1) {
            neighbors.add(grid[row][col + 1]);
        }

        return neighbors;
    }

    @Override
    public boolean solve(Maze maze) {
        Cell start = maze.start();
        Cell end = maze.end();

        PriorityQueue<Node> openList = new PriorityQueue<>();
        Set<Cell> closedList = new HashSet<>();

        openList.add(new Node(start, null, 0, heuristic(start, end)));
        visitedCellsCount = 0;
        passagesCount = countPassages(maze.grid());

        while (!openList.isEmpty()) {
            Node current = openList.poll();
            if (current.cell.equals(end)) {
                markPath(current);
                updateMazeStats(maze);
                return true;
            }

            closedList.add(current.cell);

            for (Cell neighbor : getNeighbors(current.cell, maze.grid())) {
                if (closedList.contains(neighbor) || neighbor.type() != CellType.EMPTY) {
                    continue;
                }

                int tentativeG = current.g + 1;
                Node neighborNode = new Node(neighbor, current, tentativeG, heuristic(neighbor, end));

                if (openList.contains(neighborNode) && tentativeG >= neighborNode.g) {
                    continue;
                }

                openList.add(neighborNode);
                neighbor.type(CellType.VISITED);
                visitedCellsCount++;
            }
        }
        updateMazeStats(maze);
        return false;
    }

    private static class Node implements Comparable<Node> {
        Cell cell;
        Node parent;
        int g; // Cost from start to this node
        int h; // Heuristic cost to the end

        Node(Cell cell, Node parent, int g, int h) {
            this.cell = cell;
            this.parent = parent;
            this.g = g;
            this.h = h;
        }

        int getF() {
            return g + h;
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.getF(), other.getF());
        }
    }

}
