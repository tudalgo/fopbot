package foshbot.anim;

import foshbot.World;

import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame {

    public static final int CELL_SIZE = 80;
    public static final int CELL_PADDING = 10;
    private static final int FRAME_DELAY = 1000 / 30;

    private final AnimatedWorld world;

    public Frame(int width, int height) {
        this.world = new AnimatedWorld(width, height);

        setSize(world.getWidth() * CELL_SIZE, world.getHeight() * CELL_SIZE);
        setPreferredSize(getSize());

        add(new Canvas(this::draw));
        pack();
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void draw(Drawable d) {
        drawGrid(d);
        world.drawWalls(d);
        world.drawEntities(d);
    }

    private void drawGrid(Drawable d) {
        d.fill(Color.GRAY);
        d.rect(0, 0, getWidth(), getHeight());

        d.fill(Color.DARK_GRAY);
        d.strokeWeight(4);

        for (int i = 1; i < world.getWidth(); i++) {
            var x = i * CELL_SIZE;
            d.line(x, 0, x, getHeight());
        }

        for (int i = 1; i < world.getHeight(); i++) {
            var y = i * CELL_SIZE;
            d.line(0, y, getWidth(), y);
        }
    }

    public World getWorld() {
        return world;
    }

    public void startUpdateThread() {
        new Thread(this::updateLoop).start();
        setVisible(true);
    }

    public void updateLoop() {
        var last = System.currentTimeMillis();
        var acc = 0;

        while (world.isRunning()) {
            while (acc > FRAME_DELAY) {
                world.update(FRAME_DELAY);
                acc -= FRAME_DELAY;
            }
            repaint();

            var current = System.currentTimeMillis();
            acc += current - last;
            last = current;
        }
    }
}
