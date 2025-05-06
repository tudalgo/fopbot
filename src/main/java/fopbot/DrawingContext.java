package fopbot;

import java.awt.Point;
import java.awt.Rectangle;

/**
 * Provides context information required for drawing a {@link FieldEntity} within the {@link KarelWorld}.
 *
 * @param <E>            the type of {@link FieldEntity} to be drawn
 * @param entity         the field entity being drawn
 * @param world          the world in which the entity exists
 * @param bounds         the pixel bounds of the entity on the canvas
 * @param colorProfile   the color profile used for rendering
 * @param scaleFactor    the scaling factor applied to the drawing
 * @param isRobotOnField indicates whether a robot is currently on the same field
 */
public record DrawingContext<E extends FieldEntity>(
    E entity,
    KarelWorld world,
    Rectangle bounds,
    ColorProfile colorProfile,
    double scaleFactor,
    boolean isRobotOnField
) {

    /**
     * Returns the upper-left corner coordinates of the specific field the entity is currently standing on.
     * The coordinates are calculated based on the world grid and the entity's position.
     *
     * @return the upper-left corner {@link Point} of the field the entity is standing on
     */
    public Point upperLeftCorner() {
        return PaintUtils.getUpperLeftCornerInField(entity, world);
    }
}
