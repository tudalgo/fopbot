package fopbot;

import org.jetbrains.annotations.Nullable;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

/**
 * A single field in a 2D world where field entities can be placed.
 *
 * @see FieldEntity
 */
public class Field {

    /**
     * Contains all entities that are on this field.
     */
    private final List<FieldEntity> entities;

    private @Nullable Color fieldColor;

    private final KarelWorld world;

    private final int x;

    private final int y;

    /**
     * Constructs a field with the given coordinate and no entities on it.
     *
     * @param x the x-coordinate
     * @param y the x-coordinate
     */
    public Field(KarelWorld world, int x, int y) {
        entities = new LinkedList<>();
        this.world = world;
        this.x = x;
        this.y = y;
    }

    /**
     * Constructs a field with the given coordinate and the specified entities on it.
     *
     * @param x        the x-coordinate
     * @param y        the x-coordinate
     * @param entities the entities
     */
    public Field(KarelWorld world, int x, int y, List<FieldEntity> entities) {
        this(world, x, y);
        this.entities.addAll(entities);
    }

    /**
     * Returns the entities that are on this field.
     *
     * @return the entities that are on this field
     */
    public List<FieldEntity> getEntities() {
        return entities;
    }

    /**
     * Sets the background color of this {@link Field} to the specified color.
     * <p>If the specified color is {@code null}, the background color of this {@link Field}
     * will be reset to the default color.</p>
     *
     * @param fieldColor the new background color of this {@link Field}
     */
    public void setFieldColor(final @Nullable Color fieldColor) {
        this.fieldColor = fieldColor;
    }

    /**
     * Returns the background color of this {@link Field}.
     *
     * @return the background color of this {@link Field}
     */
    public @Nullable Color getFieldColor() {
        return fieldColor;
    }

    /**
     * Returns the x-coordinate of this field.
     *
     * @return the x-coordinate.
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y-coordinate of this field.
     *
     * @return the y-coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Returns the world of this field.
     *
     * @return the world
     */
    public KarelWorld getWorld() {
        return world;
    }
}
