package foshbot.anim;

import foshbot.anim.paz.Vector;
import foshbot.anim.resources.Resources;
import foshbot.impl.CoinStack;

import java.awt.*;

public class AnimatedCoinStack extends CoinStack implements Animatable {

    public static final String RESOURCE = "/fopbot/coin.png";

    private static final double UPDATE_EPSILON = 0.5;
    private static final double VEL_SCALAR = 0.007;

    private static final double WIDTH = Frame.CELL_PADDING * 0.8;

    private int currentNumberOfCoins;

    private final Vector stack;
    private final Vector spawn;
    private Vector pos;

    public AnimatedCoinStack(int x, int y) {
        super(x, y);
        currentNumberOfCoins = 0;
        stack = new Vector(
            x * Frame.CELL_SIZE + (Frame.CELL_PADDING - WIDTH),
            y * Frame.CELL_SIZE + (Frame.CELL_PADDING - WIDTH));
        spawn = new Vector(x, y)
            .mul(Frame.CELL_SIZE)
            .add(Frame.CELL_SIZE/2, Frame.CELL_SIZE/2);
    }

    @Override
    public void putCoins(int numberOfCoins) {
        pos = spawn.copy();
        super.putCoins(numberOfCoins);
    }

    @Override
    public void pickCoins(int numberOfCoins) {
        pos = stack.copy();
        super.pickCoins(numberOfCoins);
    }

    @Override
    public void draw(Drawable d) {
        if (currentNumberOfCoins != getNumberOfCoins()) {
            var w = WIDTH * (spawn.dist(pos) / spawn.dist(stack));
            d.image(
                Resources.getImages().get(RESOURCE),
                pos.x, pos.y,
                w, w);
        }

        if (getNumberOfCoins() > 0 && currentNumberOfCoins > 0) {
            d.image(
                Resources.getImages().get(RESOURCE),
                stack.x, stack.y,
                WIDTH, WIDTH);

            if (getNumberOfCoins() > 1 && currentNumberOfCoins > 1) {
                d.textSize(18);
                d.fill(Color.BLACK);
                d.text(stack.x + WIDTH, stack.y, "x" + currentNumberOfCoins);
            }
        }
    }

    @Override
    public boolean update(double dt) {
        if (currentNumberOfCoins > getNumberOfCoins()) {
            if (spawn.dist(pos) < UPDATE_EPSILON) {
                currentNumberOfCoins--;
                pos = stack.copy();
            } else {
                moveTowards(dt, spawn);
            }
        }

        if (currentNumberOfCoins < getNumberOfCoins()) {
            if (stack.dist(pos) < UPDATE_EPSILON) {
                currentNumberOfCoins++;
                pos = spawn.copy();
            } else {
                moveTowards(dt, stack);
            }
        }

        return currentNumberOfCoins == getNumberOfCoins();
    }

    private void moveTowards(double dt, Vector target) {
        pos.add(target
            .copy()
            .sub(pos)
            .mul(VEL_SCALAR * dt));
    }
}