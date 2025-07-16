package fopbot;

import org.jetbrains.annotations.NotNull;

import java.awt.Point;
import java.awt.Rectangle;

/**
 * Provides context information required for drawing a {@link FieldEntity} within the {@link KarelWorld}.
 *
 * @param <E>          the type of {@link FieldEntity} to be drawn
 * @param entity       the field entity being drawn
 * @param world        the world in which the entity exists
 * @param bounds       the pixel bounds of the entity on the canvas
 * @param colorProfile the color profile used for rendering
 * @param scaleFactor  the scaling factor applied to the drawing
 * @param field        the field the entity is currently on
 */
public record DrawingContext<E extends FieldEntity>(
    @NotNull E entity,
    @NotNull KarelWorld world,
    @NotNull Rectangle bounds,
    @NotNull ColorProfile colorProfile,
    double scaleFactor,
    @NotNull Field field
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
