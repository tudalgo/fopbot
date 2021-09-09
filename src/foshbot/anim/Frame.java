package foshbot.anim;

import foshbot.World;
import foshbot.anim.paz.PanningAndZooming;
import foshbot.anim.paz.PanningAndZoomingTarget;
import foshbot.anim.paz.Vector;
import foshbot.impl.Grid;

import javax.swing.*;
import java.awt.*;

public class Frame extends JFrame implements PanningAndZoomingTarget {

    public static final double CELL_SIZE = 120;
    public static final double CELL_PADDING = 32;
    private static final double  FRAME_DELAY = 1000.0 / 30;

    private final AnimatedWorld world;
    private final PanningAndZooming panningAndZooming;

    public Frame(Grid grid) {
        this.world = new AnimatedWorld(grid);

        this.panningAndZooming = new PanningAndZooming(this);
        panningAndZooming.setMaxScale(4);
        panningAndZooming.setMinScale(0.01);

        addMouseListener(panningAndZooming);
        addMouseMotionListener(panningAndZooming);
        addMouseWheelListener(panningAndZooming);

        add(new Canvas(this::draw));
        pack();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        panningAndZooming.setMinTopLeft(new Vector(-CELL_SIZE, -CELL_SIZE));
        panningAndZooming.setMaxTopLeft(new Vector(
            CELL_SIZE * (grid.getWidth() - 1),
            CELL_SIZE * (grid.getHeight() - 1)));
    }

    private void draw(Drawable d) {
        d.fill(Color.BLACK);
        d.rect(0, 0, getWidth(), getHeight());

        panningAndZooming.draw(d, () -> {
            drawGrid(d);
            world.drawWalls(d);
            world.drawEntities(d);
        });
    }

    private void drawGrid(Drawable d) {
        var width = world.getWidth()*CELL_SIZE;
        var height = world.getHeight()*CELL_SIZE;

        d.fill(Color.GRAY);
        d.rect(0, 0, width, height);

        d.fill(Color.DARK_GRAY);
        d.strokeWeight(4);
        for (int i = 1; i < world.getWidth(); i++) {
            var x = i * CELL_SIZE;
            d.line(x, 0, x, world.getHeight()*CELL_SIZE);
        }

        for (int i = 1; i < world.getHeight(); i++) {
            var y = i * CELL_SIZE;
            d.line(0, y, world.getWidth()*CELL_SIZE, y);
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

    @Override
    public void onMousePressed(int button, Vector v) {}
}
