package fopbot;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

class PaintUtils {

  public static final int FIELD_INNER_SIZE = 60;
  public static final int FIELD_BORDER_THICKNESS = 4;
  public static final int FIELD_INNER_OFFSET = 4;
  public static final int BOARD_OFFSET = 20;

  /**
   * @return size of the board
   */
  public static Point getBoardSize(KarelWorld world) {
    int w = FIELD_BORDER_THICKNESS * (world.getWidth() + 1) + FIELD_INNER_SIZE * world.getWidth();
    int h = FIELD_BORDER_THICKNESS * (world.getHeight() + 1) + FIELD_INNER_SIZE * world.getHeight();
    return new Point(w, h);
  }

  /**
   * Loads, scales, and rotates the given image
   *
   * @return image
   */
  protected static Image[] loadScaleRotateFieldImage(InputStream inputImage, int upRotationOffset) throws IOException {
    Image[] rotations = new Image[4];

    BufferedImage originalBufferedImage = ImageIO.read(inputImage);

    int imageSize = FIELD_INNER_SIZE - FIELD_INNER_OFFSET * 2;

    int degrees = upRotationOffset;
    for (int i = 0; i < 4; i++) {
      if (i > 0) {
        degrees += 90;
      }
      // rotate image
      AffineTransform af = new AffineTransform();
      af.rotate(Math.toRadians(degrees), originalBufferedImage.getWidth() / 2d, originalBufferedImage.getHeight() / 2d);
      AffineTransformOp afop = new AffineTransformOp(af, AffineTransformOp.TYPE_BILINEAR);
      BufferedImage rotatedImage = afop.filter(originalBufferedImage, null);
      // scale image
      Image scaledImage = rotatedImage.getScaledInstance(imageSize, imageSize, Image.SCALE_SMOOTH);

      if (i == 0) {
        rotations[Direction.UP.ordinal()] = scaledImage;
      }
      if (i == 1) {
        rotations[Direction.RIGHT.ordinal()] = scaledImage;
      }
      if (i == 2) {
        rotations[Direction.DOWN.ordinal()] = scaledImage;
      }
      if (i == 3) {
        rotations[Direction.LEFT.ordinal()] = scaledImage;
      }
    }

    return rotations;
  }

  /**
   * Returns the upper left corner coordinates of a specific field (the fe is standing on)
   */
  protected static Point getUpperLeftCornerInField(FieldEntity fe, int worldHeight) {
    int y_m = Math.abs(fe.getY() - worldHeight + 1);
    int width = BOARD_OFFSET + FIELD_BORDER_THICKNESS;
    int height = BOARD_OFFSET + FIELD_BORDER_THICKNESS;
    width += fe.getX() * (FIELD_BORDER_THICKNESS + FIELD_INNER_SIZE);
    height += y_m * (FIELD_BORDER_THICKNESS + FIELD_INNER_SIZE);
    width += FIELD_INNER_OFFSET;
    height += FIELD_INNER_OFFSET;
    return new Point(width, height);
  }
}
