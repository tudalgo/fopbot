package fopbot.examples;

import fopbot.Direction;
import fopbot.Scene;
import fopbot.World;
import fopbot.impl.Grid;

public class LangtonsAntExample implements Scene {
  @Override
  public Grid getGrid() {
    return new Grid(100, 100);
  }

  @Override
  public void init(World world) {
  }

  @Override
  public void run(World world) {
    var ant = world.newRobot(world.getWidth() / 2, world.getHeight() / 2, Direction.NORTH, 999);

    while (ant.hasAnyCoins() && ant.isFrontClear()) {
      if (ant.isNextToACoin()) {
        ant.pickCoin();
        ant.pickCoin();

        ant.turnLeft();
        ant.turnLeft();
        ant.turnLeft();
      } else {
        ant.putCoin();
        ant.putCoin();

        ant.turnLeft();

        ant.pickCoin();
        ant.pickCoin();

        ant.putCoin();
        ant.putCoin();
      }
      ant.move();
    }
  }
}
