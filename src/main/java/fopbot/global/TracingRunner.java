package fopbot.global;

import fopbot.World;
import fopbot.impl.Grid;
import fopbot.trace.TracingRobot;
import fopbot.trace.TracingWorld;

import java.util.List;

public class TracingRunner implements GlobalRunner {

  private final GlobalRunner runner;

  private List<TracingRobot> traces;

  public TracingRunner(GlobalRunner runner) {
    this.runner = runner;
  }

  @Override
  public World createWorld(Grid grid) {
    var world = new TracingWorld(runner.createWorld(grid));
    traces = world.getTraces();
    return world;
  }

  public List<TracingRobot> getTraces() {
    return traces;
  }
}
