package fopbot.examples;

import fopbot.Robot;
import fopbot.World;

public class Maze {

  public static void solveMaze(World world, Robot r) {
    while (r.hasAnyCoins()) {
      if (r.getX() == world.getWidth() - 1 && r.getY() == world.getHeight() - 1) {
        break;
      }

      if (r.isFrontClear()) {
        r.move();
        if (r.isNextToACoin()) {
          r.turnLeft();
          r.turnLeft();
          r.move();
          r.turnLeft();
          r.turnLeft();
        } else {
          continue;
        }
      }
      r.turnLeft();

      if (r.isFrontClear()) {
        r.move();
        if (r.isNextToACoin()) {
          r.turnLeft();
          r.turnLeft();
          r.move();
        } else {
          continue;
        }
      } else {
        r.turnLeft();
        r.turnLeft();
      }

      if (r.isFrontClear()) {
        r.move();
        if (r.isNextToACoin()) {
          r.turnLeft();
          r.turnLeft();
          r.move();
          r.turnLeft();
        } else {
          continue;
        }
      } else {
        r.turnLeft();
        r.turnLeft();
        r.turnLeft();
      }

      r.putCoin();
      r.move();
    }

    while (true) {
      if (r.hasAnyCoins()) {
        r.putCoin();
      }
      r.turnLeft();
    }
  }
}
