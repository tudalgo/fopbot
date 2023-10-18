package fopbot;

import com.twelvemonkeys.imageio.plugins.svg.SVGReadParam;

import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Point;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;

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
        final Image[] rotations = new Image[4];

        final ImageReadParam param = new SVGReadParam();
        final var sizeMultiplier = 2;
        param.setSourceRenderSize(new java.awt.Dimension(
            sizeMultiplier * FIELD_INNER_SIZE,
            sizeMultiplier * FIELD_INNER_SIZE
        ));
        final var reader = ImageIO.getImageReadersByFormatName("SVG").next();
        reader.setInput(ImageIO.createImageInputStream(inputImage), true);
        final BufferedImage originalBufferedImage = reader.read(0, param);

        final int imageSize = FIELD_INNER_SIZE - FIELD_INNER_OFFSET * 2;

        int degrees = upRotationOffset;
        for (int i = 0; i < 4; i++) {
            if (i > 0) {
                degrees += 90;
            }
            // rotate image
            final AffineTransform af = new AffineTransform();
            af.rotate(Math.toRadians(degrees),
                originalBufferedImage.getWidth() / 2d,
                originalBufferedImage.getHeight() / 2d);
            final AffineTransformOp afop = new AffineTransformOp(af, AffineTransformOp.TYPE_BILINEAR);
            final BufferedImage rotatedImage = afop.filter(originalBufferedImage, null);
            // scale image
            final String osName = System.getProperty("os.name").toLowerCase();
            final Image scaledImage = rotatedImage.getScaledInstance(
                imageSize,
                imageSize,
                osName.contains("win") ? Image.SCALE_DEFAULT : Image.SCALE_SMOOTH
            );

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

    /**
     * Returns a transform for transforming a point in the unscaled state of the given panel
     * to the respective field position in the given world.
     *
     * @param world the world
     * @return the transform
     */
    public static AffineTransform getWorldTransform(KarelWorld world) {
        var h = world.getHeight();
        var transform = new AffineTransform();
        transform.translate(
                BOARD_OFFSET + .5 * FIELD_BORDER_THICKNESS,
                BOARD_OFFSET + (h + .5) * FIELD_BORDER_THICKNESS + h * FIELD_INNER_SIZE
        );
        transform.scale(
                FIELD_BORDER_THICKNESS + FIELD_INNER_SIZE,
                -1 * (FIELD_BORDER_THICKNESS + FIELD_INNER_SIZE)
        );
        return transform;
    }

    public static AffineTransform getPanelWorldTransform(GuiPanel panel) {
        var transform = new AffineTransform();
        transform.concatenate(getPanelTransform(panel));
        transform.concatenate(getWorldTransform(panel.world));

        return transform;
    }

    public static Point2D toPoint2D(Point point) {
        return new Point2D.Double(point.getX(), point.getY());
    }

    /**
     * Returns an image that is optimized for displaying at the specified width and height.
     *
     * @param image  the image to scale
     * @param width  the width of the scaled image
     * @param height the height of the scaled image
     * @return the scaled image
     */
    static BufferedImage scaleImageWithGPU(final BufferedImage image, final int width, final int height) {
        // obtain the current system graphical settings
        final GraphicsConfiguration gfxConfig = GraphicsEnvironment
            .getLocalGraphicsEnvironment()
            .getDefaultScreenDevice()
            .getDefaultConfiguration();

        /*
         * if image is already compatible and optimized for the target resolution, simply return it
         */
        if (image.getColorModel().equals(gfxConfig.getColorModel())
            && image.getWidth() == width
            && image.getHeight() == height) {
            return image;
        }

        // image is not optimized, so create a new image that is
        final BufferedImage newImage = gfxConfig.createCompatibleImage(width, height, image.getTransparency());

        // get the graphics context of the new image to draw the old image on
        final Graphics2D g2d = newImage.createGraphics();

        // actually draw the image and dispose of context no longer needed
        g2d.drawImage(image, 0, 0, width, height, null);
        g2d.dispose();
        // return the new optimized image
        return newImage;
    }
}
