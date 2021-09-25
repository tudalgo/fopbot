package fopbot.trace;

import fopbot.Direction;
import fopbot.Robot;
import fopbot.World;
import fopbot.decorate.DecoratingWorld;

import java.util.ArrayList;
import java.util.List;

public class TracingWorld extends DecoratingWorld {

  private final List<TracingRobot> traces = new ArrayList<>();

  public TracingWorld(World theWorld) {
    super(theWorld);
  }

  @Override
  public Robot newRobot(int x, int y, Direction dir, int numberOfCoins) {
    var r = new TracingRobot(super.newRobot(x, y, dir, numberOfCoins));
    traces.add(r);
    return r;
  }

  public List<TracingRobot> getTraces() {
    return traces;
  }
}
