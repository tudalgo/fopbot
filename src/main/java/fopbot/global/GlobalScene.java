package fopbot.global;

import fopbot.World;
import fopbot.anim.AnimatedWorldFrame;
import fopbot.anim.resources.Resources;
import fopbot.impl.Grid;

import java.io.IOException;

public class GlobalScene {

  public static final GlobalScene INSTANCE = new GlobalScene();

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

      try {
        Resources.loadAll();
      } catch (IOException e) {
        e.printStackTrace();
      }

      if (grid == null) {
        grid = new Grid(10, 10);
      }

      var frame = new AnimatedWorldFrame(grid);
      world = frame.getWorld();

      world.start();
      frame.startUpdateThread();
    }

    return world;
  }
}
