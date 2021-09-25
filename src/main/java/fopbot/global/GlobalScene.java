package fopbot.global;

import fopbot.World;
import fopbot.impl.Grid;

public class GlobalScene {

  public static final GlobalScene INSTANCE = new GlobalScene();

  private GlobalRunner runner;

  private Grid grid;

  private World world;

  public World createWorld(int width, int height) {
    if (world != null) {
      throw new IllegalStateException("Cannot create multiple worlds");
    }
    grid = new Grid(width, height);
    return getWorld();
  }

  public World getWorld() {
    if (world == null) {
      if (runner == null) {
        runner = new GUIRunner();
      }

      if (grid == null) {
        grid = new Grid(10, 10);
      }

      world = runner.createWorld(grid);
    }

    return world;
  }

  public void setRunner(GlobalRunner runner) {
    this.runner = runner;
  }
}
