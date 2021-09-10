package fopbot.trace;

import fopbot.Direction;

public class RobotState {

  private final RobotAction lastAction;
  private final int x;
  private final int y;
  private final boolean turnedOn;
  private final int numberOfCoins;
  private final Direction direction;

  public RobotState(RobotAction lastAction, int x, int y, boolean turnedOn, int numberOfCoins, Direction direction) {
    this.lastAction = lastAction;
    this.x = x;
    this.y = y;
    this.turnedOn = turnedOn;
    this.numberOfCoins = numberOfCoins;
    this.direction = direction;
  }

  public RobotAction getLastAction() {
    return lastAction;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public boolean isTurnedOn() {
    return turnedOn;
  }

  public int getNumberOfCoins() {
    return numberOfCoins;
  }

  public Direction getDirection() {
    return direction;
  }
}
