package fopbot.examples;

import fopbot.Direction;
import fopbot.Scene;
import fopbot.World;
import fopbot.impl.Grid;

import java.util.Random;

public class WallMazeExample implements Scene {

  private final Random random = new Random();

  @Override
  public Grid getGrid() {
    var grid = new Grid(5, 5);

    recursiveDivide(grid, 0, 0, grid.getWidth() - 1, grid.getHeight() - 1, random.nextBoolean());

    return grid;
  }

  private void recursiveDivide(Grid grid, int x1, int y1, int x2, int y2, boolean horizontal) {
    boolean horizontalPossible = y2 - y1 > 0;
    boolean verticalPossible = x2 - x1 > 0;

    if (!horizontalPossible || !verticalPossible) {
      return;
    }

    var x = random.nextInt(x2 - x1) + x1;
    var y = random.nextInt(y2 - y1) + y1;

    if (horizontal) {
      for (int cx = x1; cx <= x2; cx++) {
        if (cx != x) {
          grid.setSouthWall(cx, y, true);
        }
      }
      recursiveDivide(grid, x1, y1, x2, y, false);
      recursiveDivide(grid, x1, y + 1, x2, y2, false);
    } else {
      for (int cy = y1; cy <= y2; cy++) {
        if (cy != y) {
          grid.setEastWall(x, cy, true);
        }
      }
      recursiveDivide(grid, x1, y1, x, y2, true);
      recursiveDivide(grid, x + 1, y1, x2, y2, true);
    }
  }

  @Override
  public void init(World world) {
  }

  @Override
  public void run(World world) {
    var r = world.newRobot(0, 0, Direction.NORTH, 999);
    Maze.solveMaze(world, r);
  }
}
