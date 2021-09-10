package foshbot.anim;

import foshbot.anim.resources.Resources;
import foshbot.impl.Block;

import java.awt.*;

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
            x * Frame.CELL_SIZE,
            y * Frame.CELL_SIZE,
            Frame.CELL_SIZE,
            Frame.CELL_SIZE);
    }

    @Override
    public boolean update(double dt) {
        return true;
    }
}