package fopbot.examples;

import fopbot.Direction;
import fopbot.Scene;
import fopbot.World;
import fopbot.impl.Grid;

import java.util.Random;

public class BlockMazeExample implements Scene {

  private final Random random = new Random();

  @Override
  public Grid getGrid() {
    return new Grid(9, 9);
  }

  @Override
  public void init(World world) {
    recursiveDivide(world, 1, 1, world.getWidth() - 1, world.getHeight() - 1, random.nextBoolean());
  }

  private void recursiveDivide(World world, int x1, int y1, int x2, int y2, boolean horizontal) {
    boolean horizontalPossible = y2 - y1 > 1;
    boolean verticalPossible = x2 - x1 > 1;

    if (!horizontalPossible || !verticalPossible) {
      return;
    }

    var x = random.nextInt(x2 - x1) + x1;
    var y = random.nextInt(y2 - y1) + y1;

    if (horizontal) {
      for (int cx = x1; cx <= x2; cx++) {
        if (cx != x) {
          world.putBlock(cx, y);
        }
      }
      recursiveDivide(world, x1, y1, x2, y - 1, false);
      recursiveDivide(world, x1, y + 1, x2, y2, false);
    } else {
      for (int cy = y1; cy <= y2; cy++) {
        if (cy != y) {
          world.putBlock(x, cy);
        }
      }
      recursiveDivide(world, x1, y1, x - 1, y2, true);
      recursiveDivide(world, x + 1, y1, x2, y2, true);
    }
  }

  @Override
  public void run(World world) {
    var r = world.newRobot(0, 0, Direction.NORTH, 999);
    Maze.solveMaze(world, r);
  }
}
