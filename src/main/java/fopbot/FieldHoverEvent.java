package fopbot;

import org.jetbrains.annotations.Nullable;

/**
 * An event representing a hover action over a field in the virtual world.
 *
 * <p>This event captures the field currently being hovered and the field that was hovered previously,
 * if any.
 */
public class FieldHoverEvent {

    /**
     * The field currently being hovered, or {@code null} if the cursor left the world.
     */
    private final @Nullable Field field;

    /**
     * The field that was previously hovered, or {@code null} if there was none.
     */
    private final @Nullable Field previousField;

    /**
     * Constructs a new {@code FieldHoverEvent} with the current and previous hovered fields.
     *
     * @param field         the currently hovered field, may be {@code null}
     * @param previousField the previously hovered field, may be {@code null}
     */
    public FieldHoverEvent(final @Nullable Field field, final @Nullable Field previousField) {
        this.field = field;
        this.previousField = previousField;
    }

    /**
     * Returns the currently hovered field.
     *
     * @return the current field, or {@code null} if none
     */
    public @Nullable Field getField() {
        return field;
    }

    /**
     * Returns the previously hovered field.
     *
     * @return the previous field, or {@code null} if none
     */
    public @Nullable Field getPreviousField() {
        return previousField;
    }
}

