package fopbot.global;

import fopbot.decorate.DecoratingWorld;

public class World extends DecoratingWorld {

  public World(int width, int height) {
    super(GlobalScene.INSTANCE.createWorld(width, height));
  }
}
