package fopbot;

import java.awt.Point;
import java.awt.Rectangle;

/**
 * A context object containing the necessary information for rendering a {@link FieldEntity}.
 * <p>
 * This record stores information about the specific {@link FieldEntity}, the world in which it exists,
 * the rendering bounds, the color profile to be used for rendering, the scale factor for scaling graphics,
 * and a flag indicating whether a robot is present on the field. This context is passed to renderers to provide
 * the required information for drawing the entity on the screen.
 *
 * @param <E>            the type of {@link FieldEntity} that this context is associated with
 * @param entity         the {@link FieldEntity} being rendered
 * @param world          the {@link KarelWorld} in which the entity resides
 * @param bounds         the bounds within which the entity is rendered
 * @param colorProfile   the {@link ColorProfile} used for the rendering
 * @param scaleFactor    the factor by which the graphics should be scaled
 * @param isRobotOnField a flag indicating whether a robot is present on the same field as the entity
 */
public record FieldEntityRenderContext<E extends FieldEntity>(
    E entity,
    KarelWorld world,
    Rectangle bounds,
    ColorProfile colorProfile,
    double scaleFactor,
    boolean isRobotOnField
) {

    /**
     * Calculates and returns the upper-left corner position of the {@link FieldEntity} in the field.
     * <p>
     * This method uses the entity and the world to determine the exact position where the entity should
     * be drawn within the field.
     *
     * @return the {@link Point} representing the upper-left corner of the entity's location on the field
     */
    public Point upperLeft() {
        return PaintUtils.getUpperLeftCornerInField(entity, world);
    }
}



