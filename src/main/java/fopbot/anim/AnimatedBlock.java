package fopbot.anim;

import fopbot.anim.resources.Resources;
import fopbot.impl.Block;

import java.awt.*;

import static fopbot.anim.AnimatedWorldFrame.CELL_SIZE;

public class AnimatedBlock extends Block implements Animatable {

  public static final String RESOURCE = "/fopbot/wall.png";

  public AnimatedBlock(int x, int y) {
    super(x, y);
  }

  @Override
  public void draw(Drawable d) {
    d.fill(Color.BLACK);
    d.image(
      Resources.getImages().get(RESOURCE),
      x * CELL_SIZE,
      y * CELL_SIZE,
      CELL_SIZE,
      CELL_SIZE);
  }

  @Override
  public boolean update(double dt) {
    return true;
  }
}
