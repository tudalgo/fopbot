package foshbot.anim;

import foshbot.anim.resources.Resources;
import foshbot.impl.CoinStack;

import java.awt.*;

public class AnimatedCoinStack extends CoinStack implements Animatable {

    public static final String RESOURCE = "/fopbot/coin.png";

    public AnimatedCoinStack(int x, int y) {
        super(x, y);
    }

    @Override
    public void draw(Drawable d) {
        var w = Frame.CELL_PADDING * 0.8;
        var x = this.x * Frame.CELL_SIZE + (Frame.CELL_PADDING - w);
        var y = this.y * Frame.CELL_SIZE + (Frame.CELL_PADDING - w);

        d.image(
            Resources.getImages().get(RESOURCE),
            x, y,
            w, w);

        d.textSize(18);
        d.fill(Color.BLACK);
        d.text(x+w, y, "x" + numberOfCoins);
    }

    @Override
    public boolean update(double dt) {
        return true;
    }
}
