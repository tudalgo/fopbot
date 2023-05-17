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

    /**
     * Constructs and initializes a field with no entities on it.
     */
    public Field() {
        entities = new LinkedList<>();
    }

    /**
     * Constructs and initializes a field with the specified entities on it.
     *
     * @param entities the  entities that are on the newly constructed field
     */
    public Field(List<FieldEntity> entities) {
        this.entities = entities;
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
}
