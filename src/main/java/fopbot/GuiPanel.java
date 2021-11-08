package fopbot;

import javax.swing.*;
import java.awt.*;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import static fopbot.PaintUtils.*;

public class GuiPanel extends JPanel {

  protected KarelWorld world;

  public GuiPanel(KarelWorld world) {
    this.world = world;

    setSize(getPreferredSize());
  }

  protected Dimension getUnscaledSize() {
    Point p = getBoardSize(world);
    int width = p.x;
    int height = p.y;
    width += 2 * BOARD_OFFSET;
    height += 2 * BOARD_OFFSET;
    return new Dimension(width, height);
  }

  @Override
  public Dimension getPreferredSize() {
    return getUnscaledSize();
  }

  /**
   * Saves the current world to an image (png)
   */
  protected void saveStateAsPng() {
  }

  @Override
  public void paintComponent(Graphics g) {
  }

  /**
   * Draw board (borders, fields)
   */
  protected void drawBoard(Graphics g) {
  }

  /**
   * Draws the given robot
   */
  protected void drawRobot(Robot r, Graphics g) {
  }

  /**
   * Draws the given coin
   */
  protected void drawCoin(Coin c, Graphics g) {
  }

  /**
   * Draws the given block
   */
  protected void drawBlock(Block b, Graphics g) {
  }

  /**
   * Draws the given wall
   */
  protected void drawWall(Wall w, Graphics g) {
  }

  /**
   * Update gui
   */
  public void updateGui() {
  }
}
