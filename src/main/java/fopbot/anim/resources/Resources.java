package fopbot.anim.resources;


import fopbot.anim.AnimatedBlock;
import fopbot.anim.AnimatedCoinStack;
import fopbot.anim.AnimatedRobot;

import java.awt.*;
import java.io.IOException;

public class Resources {

  private static ResourceCache<Image> images;

  public static void loadAll() throws IOException {
    images = new ImageResourceCache(
      AnimatedRobot.RESOURCE,
      AnimatedCoinStack.RESOURCE,
      AnimatedBlock.RESOURCE);
  }

  public static ResourceCache<Image> getImages() {
    return images;
  }
}
