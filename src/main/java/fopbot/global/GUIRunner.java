package fopbot.global;

import fopbot.World;
import fopbot.anim.AnimatedWorldFrame;
import fopbot.anim.resources.Resources;
import fopbot.impl.Grid;

import java.io.IOException;

public class GUIRunner implements GlobalRunner {

  @Override
  public World createWorld(Grid grid) {
    try {
      Resources.loadAll();
    } catch (IOException e) {
      e.printStackTrace();
    }

    var frame = new AnimatedWorldFrame(grid);
    var world = frame.getWorld();

    world.start();
    frame.startUpdateThread();
    return world;
  }
}
