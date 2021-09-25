package fopbot.trace;

import fopbot.Robot;
import fopbot.decorate.DecoratingRobot;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TracingRobot extends DecoratingRobot implements Iterable<RobotState> {

  private final List<RobotState> trace = new ArrayList<>();

  public TracingRobot(Robot theRobot) {
    super(theRobot);
    recordState(RobotAction.SPAWNED);
  }

  @Override
  public void turnLeft() {
    super.turnLeft();
    recordState(RobotAction.TURNED_LEFT);
  }

  @Override
  public void move() {
    super.move();
    recordState(RobotAction.MOVED);
  }

  @Override
  public void putCoin() {
    super.putCoin();
    recordState(RobotAction.PUT_COIN);
  }

  @Override
  public void pickCoin() {
    super.pickCoin();
    recordState(RobotAction.PICKED_COIN);
  }

  @Override
  public void turnOff() {
    super.turnOff();
    recordState(RobotAction.TURNED_OFF);
  }

  @Override
  public void setField(int x, int y) {
    super.setField(x, y);
    recordState(RobotAction.SET_FIELD);
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
