package fopbot.global;

import fopbot.Direction;
import fopbot.decorate.DecoratingRobot;

public class Robot extends DecoratingRobot {

  public Robot(int x, int y, Direction dir, int numberOfCoins) {
    super(GlobalScene.INSTANCE.getWorld().newRobot(x, y, dir, numberOfCoins));
  }
}
