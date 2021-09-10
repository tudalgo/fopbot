package fopbot.anim;

import fopbot.World;
import fopbot.anim.paz.PanningAndZooming;
import fopbot.anim.paz.PanningAndZoomingTarget;
import fopbot.anim.paz.Vector;
import fopbot.impl.Grid;

import javax.swing.*;
import java.awt.*;

public class AnimatedWorldFrame extends JFrame implements PanningAndZoomingTarget {

  public static final double CELL_SIZE = 120;
  public static final double CELL_PADDING = 32;
  private static final double FRAME_DELAY = 1000.0 / 60;

  private final AnimatedWorld world;
  private final PanningAndZooming panningAndZooming;

  public AnimatedWorldFrame(Grid grid) {
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
      CELL_SIZE * (world.getWidth() - 1),
      CELL_SIZE * (world.getHeight() - 1)));
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
    var width = world.getWidth() * CELL_SIZE;
    var height = world.getHeight() * CELL_SIZE;

    d.fill(Color.GRAY);
    d.rect(0, 0, width, height);

    d.fill(Color.DARK_GRAY);
    d.strokeWeight(4);
    for (int i = 1; i < world.getWidth(); i++) {
      var x = i * CELL_SIZE;
      d.line(x, 0, x, world.getHeight() * CELL_SIZE);
    }

    for (int i = 1; i < world.getHeight(); i++) {
      var y = i * CELL_SIZE;
      d.line(0, y, world.getWidth() * CELL_SIZE, y);
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
    double last = System.currentTimeMillis();

    while (world.isRunning()) {
      double current = System.currentTimeMillis();
      double dt = current - last;
      last = current;

      world.update(dt);
      repaint();

      try {
        Thread.sleep((long) FRAME_DELAY);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void onMousePressed(int button, Vector v) {
  }
}
