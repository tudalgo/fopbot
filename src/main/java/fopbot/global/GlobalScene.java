package fopbot.global;

import fopbot.World;
import fopbot.anim.AnimatedWorldFrame;
import fopbot.anim.resources.Resources;
import fopbot.impl.Grid;

import java.io.IOException;

public class GlobalScene {

  public static final GlobalScene INSTANCE = new GlobalScene();

  private World world;

  public World getWorld() {
    if (world == null) {

      try {
        Resources.loadAll();
      } catch (IOException e) {
        e.printStackTrace();
      }

      var frame = new AnimatedWorldFrame(new Grid(10, 10));
      world = frame.getWorld();

      world.start();
      frame.startUpdateThread();
    }

    return world;
  }
}
