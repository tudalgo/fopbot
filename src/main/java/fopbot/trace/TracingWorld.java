package fopbot.trace;

import fopbot.Direction;
import fopbot.Entity;
import fopbot.Robot;
import fopbot.World;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class TracingWorld implements World {

  private final World theWorld;

  private final List<TracingRobot> traces = new ArrayList<>();

  public TracingWorld(World theWorld) {
    this.theWorld = theWorld;
  }

  @Override
  public int getWidth() {
    return theWorld.getWidth();
  }

  @Override
  public int getHeight() {
    return theWorld.getHeight();
  }

  @Override
  public Collection<Entity> getEntities(int x, int y) {
    return theWorld.getEntities(x, y);
  }

  @Override
  public boolean hasBlockInField(int x, int y) {
    return theWorld.hasBlockInField(x, y);
  }

  @Override
  public boolean fieldHasWallInDirection(int x, int y, Direction dir) {
    return theWorld.fieldHasWallInDirection(x, y, dir);
  }

  @Override
  public boolean hasCoinInField(int x, int y) {
    return theWorld.hasCoinInField(x, y);
  }

  @Override
  public boolean hasAnotherRobotInField(int x, int y, Robot robot) {
    return theWorld.hasAnotherRobotInField(x, y, robot);
  }

  @Override
  public void putCoins(int x, int y, int numberOfCoins) {
    theWorld.putCoins(x, y, numberOfCoins);
  }

  @Override
  public boolean pickCoin(int x, int y) {
    return theWorld.pickCoin(x, y);
  }

  @Override
  public void putBlock(int x, int y) {
    theWorld.putBlock(x, y);
  }

  @Override
  public Robot newRobot(int x, int y, Direction dir, int numberOfCoins) {
    var r = new TracingRobot(theWorld.newRobot(x, y, dir, numberOfCoins));
    traces.add(r);
    return r;
  }

  @Override
  public void reset() {
    theWorld.reset();
  }

  @Override
  public void start() {
    theWorld.start();
  }

  @Override
  public boolean isRunning() {
    return theWorld.isRunning();
  }

  @Override
  public void stop() {
    theWorld.stop();
  }

  public List<TracingRobot> getTraces() {
    return traces;
  }
}
