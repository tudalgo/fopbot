package fopbot.headless;

import fopbot.Direction;
import fopbot.World;
import fopbot.impl.AbstractRobot;

public class HeadlessRobot extends AbstractRobot {

  public HeadlessRobot(int x, int y, Direction dir, int numberOfCoins, World world) {
    super(x, y, dir, numberOfCoins, world);
  }
}
