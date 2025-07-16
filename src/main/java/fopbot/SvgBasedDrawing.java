package fopbot;

import org.jetbrains.annotations.NotNull;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;

/**
 * A base class for SVG-based drawing implementations.
 *
 * <p>This class is designed to facilitate the drawing of field entities in a scalable manner using SVG images.
 * It handles the loading and scaling of images for various entities, and ensures that the drawing is updated
 * whenever the size changes. Subclasses must implement specific methods for handling the images related
 * to the entity type.
 *
 * @param <E> the type of {@link FieldEntity} this drawing is responsible for
 */
public abstract class SvgBasedDrawing<E extends FieldEntity> implements Drawable<E> {

    /**
     * The file extension used for the images (SVG format).
     */
    public static final String EXTENSION = ".svg";

    /**
     * Array of images corresponding to different states or views of the field entity.
     */
    private final @NotNull Image[] images;

    /**
     * The size of the images being used for drawing, or -1 if the size is not set.
     */
    private int imageSize = -1;

    /**
     * Constructs a new SvgBasedDrawing instance with the specified number of images.
     *
     * @param size the number of images that will be used for drawing (usually corresponding to different states)
     */
    public SvgBasedDrawing(final int size) {
        this.images = new BufferedImage[size];
    }

    /**
     * Retrieves the image at the specified index.
     *
     * @param index the index of the image to retrieve
     *
     * @return the image at the specified index
     */
    public Image getImage(final int index) {
        return images[index];
    }

    /**
     * Sets the image at the specified index.
     *
     * @param index the index where the image will be set
     * @param image the image to set at the specified index
     */
    protected void setImage(final int index, final @NotNull Image image) {
        this.images[index] = image;
    }

    @Override
    public void draw(final @NotNull Graphics g, final @NotNull DrawingContext<? extends E> context) {
        final ColorProfile profile = context.colorProfile();
        final int targetSize = scale(profile.fieldInnerSize() - profile.fieldInnerOffset() * 2, context);
        if (imageSize != targetSize) {
            loadImages(targetSize, context);
            imageSize = targetSize;
        }
        final Point upperLeft = context.upperLeftCorner();
        g.drawImage(
            getCurrentDrawingImage(context.entity()),
            scale(upperLeft.x, context),
            scale(upperLeft.y, context),
            null);
    }

    /**
     * Retrieves the image that corresponds to the current drawing of the entity.
     *
     * @param entity the entity to draw
     *
     * @return the image representing the current drawing of the entity
     */
    protected abstract Image getCurrentDrawingImage(E entity);

    /**
     * Loads images based on the target size and drawing context.
     *
     * @param targetSize the size to scale the images to
     * @param context    the context used for drawing, including color profile and entity details
     */
    protected abstract void loadImages(int targetSize, @NotNull DrawingContext<? extends E> context);
}
