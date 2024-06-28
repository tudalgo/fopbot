package fopbot;

import com.twelvemonkeys.imageio.plugins.svg.SVGReadParam;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
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
    public static Point getBoardSize(final KarelWorld world) {
        final int w = FIELD_BORDER_THICKNESS * (world.getWidth() + 1) + FIELD_INNER_SIZE * world.getWidth();
        final int h = FIELD_BORDER_THICKNESS * (world.getHeight() + 1) + FIELD_INNER_SIZE * world.getHeight();
        return new Point(w, h);
    }


    /**
     * Loads an optimized image for displaying at the specified width and height.
     * Assumes 1:1 aspect ratio. If the image cannot be loaded, a black square is returned instead.
     *
     * @param inputImage     the image to load
     * @param rotationOffset the rotation offset in degree
     * @return the loaded image
     */
    protected static BufferedImage loadFieldImage(
        final InputStream inputImage,
        final int rotationOffset,
        final int targetSize
    ) {
        try {
            final var reader = ImageIO.getImageReadersByFormatName("SVG").next();
            reader.setInput(ImageIO.createImageInputStream(inputImage), true);
            final ImageReadParam param = new SVGReadParam();
            param.setSourceRenderSize(new java.awt.Dimension(
                targetSize,
                targetSize
            ));
            final var imgIn = reader.read(0, param);
            reader.dispose();
            return rotationOffset == 0 ? imgIn : scaleAndRotateImageWithGPU(
                imgIn,
                targetSize,
                targetSize,
                rotationOffset
            );
        } catch (final IOException e) {
            System.err.println("Could not load robot image! " + inputImage);
            // create black square instead
            final var output = new BufferedImage(
                targetSize,
                targetSize,
                BufferedImage.TYPE_INT_ARGB
            );
            final var graphics = output.getGraphics();
            graphics.setColor(Color.BLACK);
            graphics.fillRect(0, 0, targetSize, targetSize);
            graphics.dispose();
            return output;
        }
    }

    /**
     * Loads, scales and rotates the specified image.
     *
     * @param inputImage       the image to load, scale and rotate
     * @param upRotationOffset the rotation offset in degree
     * @return the loaded, scaled and rotated image.
     */
    protected static BufferedImage[] loadScaleRotateFieldImage(
        final InputStream inputImage,
        final int upRotationOffset,
        final int targetSize
    ) {
        final BufferedImage[] rotations = new BufferedImage[4];
        final BufferedImage originalBufferedImage = loadFieldImage(inputImage, 0, targetSize);

        int degrees = upRotationOffset;
        for (int i = 0; i < 4; i++) {
            if (i > 0) {
                degrees += 90;
            }

            rotations[i] = scaleAndRotateImageWithGPU(
                originalBufferedImage,
                targetSize,
                targetSize,
                degrees
            );
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
    protected static Point getUpperLeftCornerInField(final FieldEntity fe, final int worldHeight) {
        final int yM = Math.abs(fe.getY() - worldHeight + 1);
        int width = BOARD_OFFSET + FIELD_BORDER_THICKNESS;
        int height = BOARD_OFFSET + FIELD_BORDER_THICKNESS;
        width += fe.getX() * (FIELD_BORDER_THICKNESS + FIELD_INNER_SIZE);
        height += yM * (FIELD_BORDER_THICKNESS + FIELD_INNER_SIZE);
        width += FIELD_INNER_OFFSET;
        height += FIELD_INNER_OFFSET;
        return new Point(width, height);
    }

    /**
     * Returns the bounds of a specific field (the field entity is standing on).
     *
     * @param fe          the entity to check
     * @param worldHeight the height of the world
     * @return the bounds of a specific field (the field entity is standing on)
     */
    public static Rectangle getFieldBounds(final FieldEntity fe, final int worldHeight) {
        final var upperLeft = getUpperLeftCornerInField(fe, worldHeight);
        final var size = FIELD_INNER_SIZE - FIELD_INNER_OFFSET * 2;
        return new Rectangle(
            upperLeft.x,
            upperLeft.y,
            size,
            size
        );
    }

    /**
     * Returns a transform for transforming a point in the unscaled state of the given panel
     * to the respective point in the scaled state of the given panel.
     *
     * @param panel the panel
     * @return the transform
     */
    public static AffineTransform getPanelTransform(final GuiPanel panel) {
        final var unscaled = panel.getUnscaledSize();
        final var scaled = panel.getScaledWorldBounds();
        final var transform = new AffineTransform();
        transform.translate(scaled.x, scaled.y);
        transform.scale(
            (double) scaled.width / (double) unscaled.width,
            (double) scaled.height / (double) unscaled.height
        );
        return transform;
    }

    /**
     * Returns a transform for transforming a point in the unscaled state of the given panel
     * to the respective field position in the given world.
     *
     * @param world the world
     * @return the transform
     */
    public static AffineTransform getWorldTransform(final KarelWorld world) {
        final var h = world.getHeight();
        final var transform = new AffineTransform();
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

    /**
     * Returns a transform for transforming a point on the panel to the respective field position in the world.
     *
     * @param panel the panel to get the transform for
     * @return the transform
     */
    public static AffineTransform getPanelWorldTransform(final GuiPanel panel) {
        final var transform = new AffineTransform();
        transform.concatenate(getPanelTransform(panel));
        transform.concatenate(getWorldTransform(panel.world));

        return transform;
    }

    /**
     * Returns the coordinates of the field the mouse is currently pointing at.
     *
     * @param panel the panel
     * @return the coordinates of the field the mouse is currently pointing at
     */
    public static Point getMouseTilePosition(final GuiPanel panel, final Point mousePosition) {
        final var transform = getPanelWorldTransform(panel);
        final var point = new Point2D.Double();
        try {
            transform.inverseTransform(PaintUtils.toPoint2D(mousePosition), point);
        } catch (final NoninvertibleTransformException ex) {
            throw new RuntimeException(ex);
        }
        return new Point((int) point.getX(), (int) point.getY());
    }

    public static Point2D toPoint2D(final Point point) {
        return new Point2D.Double(point.getX(), point.getY());
    }

    /**
     * Returns an image that is optimized for displaying at the specified width and height with an optional rotation.
     *
     * @param image    the image to scale
     * @param width    the width of the scaled image
     * @param height   the height of the scaled image
     * @param rotation the rotation angle in degrees (0-360)
     * @return the scaled and optionally rotated image
     */
    public static BufferedImage scaleAndRotateImageWithGPU(
        final BufferedImage image,
        final int width,
        final int height,
        final double rotation
    ) {
        // obtain the current system graphical settings
        final GraphicsConfiguration gfxConfig = GraphicsEnvironment
            .getLocalGraphicsEnvironment()
            .getDefaultScreenDevice()
            .getDefaultConfiguration();

        /*
         * if image is already compatible and optimized for the target resolution and orientation, simply return it
         */
        if (image.getColorModel().equals(gfxConfig.getColorModel())
            && image.getWidth() == width
            && image.getHeight() == height
            && rotation == 0) {
            return image;
        }

        // image is not optimized, so create a new image that is
        final BufferedImage newImage = gfxConfig.createCompatibleImage(width, height, image.getTransparency());

        // get the graphics context of the new image to draw the old image on
        final Graphics2D g2d = newImage.createGraphics();

        // Rotate the image if the rotation angle is not 0
        if (rotation != 0) {
            final double radians = Math.toRadians(rotation);

            // Calculate new dimensions to ensure entire image fits after rotation
            final AffineTransform transform = AffineTransform.getRotateInstance(
                radians,
                image.getWidth() / 2.0,
                image.getHeight() / 2.0
            );
            final Shape transformedBounds = transform
                .createTransformedShape(new Rectangle(image.getWidth(), image.getHeight()));
            final Rectangle bounds = transformedBounds.getBounds();
            final int newWidth = bounds.width;
            final int newHeight = bounds.height;

            // Set the new dimensions for the rotated image
            newImage.setData(gfxConfig.createCompatibleImage(newWidth, newHeight, image.getTransparency()).getRaster());
            g2d.translate((newWidth - width) / 2, (newHeight - height) / 2);
            g2d.rotate(radians, width / 2.0, height / 2.0);
        }

        // actually draw the image and dispose of context no longer needed
        g2d.drawImage(image, 0, 0, width, height, null);
        g2d.dispose();
        // return the new optimized image
        return newImage;
    }

    /**
     * Returns an image that is optimized for displaying at the specified width and height.
     *
     * @param image  the image to scale
     * @param width  the width of the scaled image
     * @param height the height of the scaled image
     * @return the scaled image
     */
    public static BufferedImage scaleImageWithGPU(final BufferedImage image, final int width, final int height) {
        return scaleAndRotateImageWithGPU(image, width, height, 0);
    }
}
