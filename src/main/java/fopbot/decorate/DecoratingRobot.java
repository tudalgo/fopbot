package fopbot.decorate;

import fopbot.Direction;
import fopbot.Robot;

public class DecoratingRobot implements Robot {

  private final Robot theRobot;

  public DecoratingRobot(Robot theRobot) {
    this.theRobot = theRobot;
  }

  @Override
  public void turnLeft() {
    theRobot.turnLeft();
  }

  @Override
  public void move() {
    theRobot.move();
  }

  @Override
  public void putCoin() {
    theRobot.putCoin();
  }

  @Override
  public void pickCoin() {
    theRobot.pickCoin();
  }

  @Override
  public Direction getDirection() {
    return theRobot.getDirection();
  }

  @Override
  public int getNumberOfCoins() {
    return theRobot.getNumberOfCoins();
  }

  @Override
  public void turnOff() {
    theRobot.turnOff();
  }

  @Override
  public boolean isTurnedOff() {
    return theRobot.isTurnedOff();
  }

  @Override
  public boolean isNextToACoin() {
    return theRobot.isNextToACoin();
  }

  @Override
  public boolean isNextToARobot() {
    return theRobot.isNextToARobot();
  }

  @Override
  public int getX() {
    return theRobot.getX();
  }

  @Override
  public int getY() {
    return theRobot.getY();
  }

  @Override
  public void setField(int x, int y) {
    theRobot.setField(x, y);
  }

  @Override
  public boolean isFrontClear() {
    return theRobot.isFrontClear();
  }
}
