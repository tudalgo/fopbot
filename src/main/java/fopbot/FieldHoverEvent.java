package fopbot;

import org.jetbrains.annotations.Nullable;

/**
 * A {@link FieldHoverEvent} represents a hover over a field.
 */
public class FieldHoverEvent {

    private final @Nullable Field field;

    private final @Nullable Field previousField;

    /**
     * Constructs a {@link FieldHoverEvent} with the given field.
     *
     * @param field the field
     * @param previousField the previously hovered field
     */
    public FieldHoverEvent(final @Nullable Field field, final @Nullable Field previousField) {
        this.field = field;
        this.previousField = previousField;
    }

    /**
     * Returns the hovered field.
     *
     * @return the field
     */
    public @Nullable Field getField() {
        return field;
    }

    /**
     * Returns the previously hovered field, or null if there was none.
     *
     * @return the field
     */
    public @Nullable Field getPreviousField() {
        return previousField;
    }
}
