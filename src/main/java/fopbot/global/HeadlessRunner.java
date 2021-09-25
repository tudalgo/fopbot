package fopbot.global;

import fopbot.World;
import fopbot.headless.HeadlessWorld;
import fopbot.impl.Grid;

public class HeadlessRunner implements GlobalRunner {

  @Override
  public World createWorld(Grid grid) {
    return new HeadlessWorld(grid);
  }
}
