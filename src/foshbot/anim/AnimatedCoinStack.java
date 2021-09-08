package foshbot.anim;

import foshbot.impl.CoinStack;

import java.awt.*;

public class AnimatedCoinStack extends CoinStack implements Animatable {

    public AnimatedCoinStack(int x, int y) {
        super(x, y);
    }

    @Override
    public void draw(Drawable d) {
        var r = Frame.CELL_PADDING / 2.0;
        var x = this.x * Frame.CELL_SIZE + r;
        var y = this.y * Frame.CELL_SIZE + r;

        d.fill(Color.YELLOW);
        d.ellipse(x, y, r*2, r*2);

        d.textSize(18);
        d.fill(Color.BLACK);
        d.text(x-r, y+r, String.valueOf(numberOfCoins));
    }

    @Override
    public boolean update(double dt) {
        return true;
    }
}
