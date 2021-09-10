package fopbot.trace;

import fopbot.Direction;
import fopbot.Robot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TracingRobot implements Robot, Iterable<RobotState> {

  private final List<RobotState> trace = new ArrayList<>();

  private final Robot theRobot;

  public TracingRobot(Robot theRobot) {
    this.theRobot = theRobot;
    recordState(RobotAction.SPAWNED);
  }

  @Override
  public void turnLeft() {
    theRobot.turnLeft();
    recordState(RobotAction.TURNED_LEFT);
  }

  @Override
  public void move() {
    theRobot.move();
    recordState(RobotAction.MOVED);
  }

  @Override
  public void putCoin() {
    theRobot.putCoin();
    recordState(RobotAction.PUT_COIN);
  }

  @Override
  public void pickCoin() {
    theRobot.pickCoin();
    recordState(RobotAction.PICKED_COIN);
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
    recordState(RobotAction.TURNED_OFF);
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
    recordState(RobotAction.SET_FIELD);
  }

  @Override
  public boolean isFrontClear() {
    return theRobot.isFrontClear();
  }

  public List<RobotState> getTrace() {
    return trace;
  }

  @Override
  public Iterator<RobotState> iterator() {
    return trace.iterator();
  }

  private void recordState(RobotAction lastAction) {
    var s = new RobotState(lastAction, getX(), getY(), isTurnedOn(), getNumberOfCoins(), getDirection());
    trace.add(s);
  }
}
