package fopbot.examples;

import fopbot.Direction;
import fopbot.Scene;
import fopbot.World;
import fopbot.impl.Grid;

public class BasicExample implements Scene {
  @Override
  public Grid getGrid() {
    return new Grid(10, 10);
  }

  @Override
  public void init(World world) {
  }

  @Override
  public void run(World world) {
    for (int i = 0; i < 3; i++) {
      var r = world.newRobot(i, i, Direction.EAST, 2);
      while (r.hasAnyCoins()) {
        r.putCoin();
        r.move();
      }
    }
  }
}
