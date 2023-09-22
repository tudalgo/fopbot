package fopbot;

import java.awt.Image;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;

/**
 * A utility class that provides useful drawing operations.
 */
class PaintUtils {

    /**
     * The inner size of a field in a 2D world.
     */
    public static final int FIELD_INNER_SIZE = 60;
    /**
     * The thickness of the field borders in a 2D world.
     */
    public static final int FIELD_BORDER_THICKNESS = 4;
    /**
     * The inner offset of the size of a field in a 2D world.
     */
    public static final int FIELD_INNER_OFFSET = 4;
    /**
     * The offset of the board.
     */
    public static final int BOARD_OFFSET = 20;

    /**
     * Returns the size of the board.
     *
     * @param world the world which is necessary for the calculation of the board size
     * @return the size of the board
     */
    public static Point getBoardSize(KarelWorld world) {
        int w = FIELD_BORDER_THICKNESS * (world.getWidth() + 1) + FIELD_INNER_SIZE * world.getWidth();
        int h = FIELD_BORDER_THICKNESS * (world.getHeight() + 1) + FIELD_INNER_SIZE * world.getHeight();
        return new Point(w, h);
    }

    /**
     * Loads, scales and rotates the specified image.
     *
     * @param inputImage       the image to load, scale and rotate
     * @param upRotationOffset the rotation offset in degree
     * @return the loaded, scaled and rotated image.
     * @throws IOException if an error occurs during reading or when not able to create required
     *                     ImageInputStream.
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
            af.rotate(Math.toRadians(degrees),
                originalBufferedImage.getWidth() / 2d,
                originalBufferedImage.getHeight() / 2d);
            AffineTransformOp afop = new AffineTransformOp(af, AffineTransformOp.TYPE_BILINEAR);
            BufferedImage rotatedImage = afop.filter(originalBufferedImage, null);
            // scale image
            Image scaledImage = rotatedImage.getScaledInstance(imageSize, imageSize, Image.SCALE_SMOOTH);

            rotations[i] = scaledImage;
        }

        return rotations;
    }

    /**
     * Returns the upper left corner coordinates of a specific field (the field entity is standing on).
     *
     * @param fe          the entity to check
     * @param worldHeight the height of the world
     * @return the upper left corner coordinates of a specific field (the field entity is standing on)
     */
    protected static Point getUpperLeftCornerInField(FieldEntity fe, int worldHeight) {
        int yM = Math.abs(fe.getY() - worldHeight + 1);
        int width = BOARD_OFFSET + FIELD_BORDER_THICKNESS;
        int height = BOARD_OFFSET + FIELD_BORDER_THICKNESS;
        width += fe.getX() * (FIELD_BORDER_THICKNESS + FIELD_INNER_SIZE);
        height += yM * (FIELD_BORDER_THICKNESS + FIELD_INNER_SIZE);
        width += FIELD_INNER_OFFSET;
        height += FIELD_INNER_OFFSET;
        return new Point(width, height);
    }

    /**
     * Returns a transform for transforming a point in the unscaled state of the given panel
     * to the respective point in the scaled state of the given panel.
     *
     * @param panel the panel
     * @return the transform
     */
    public static AffineTransform getPanelTransform(GuiPanel panel) {
        var unscaled = panel.getUnscaledSize();
        var scaled = panel.getSize();
        return AffineTransform.getScaleInstance(
                (double) scaled.width / (double) unscaled.width,
                (double) scaled.height / (double) unscaled.height
        );
    }
}
