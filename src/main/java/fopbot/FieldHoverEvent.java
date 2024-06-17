package fopbot;

/**
 * A {@link FieldHoverEvent} represents a hover over a field.
 */
public class FieldHoverEvent {

    private final Field field;

    /**
     * Constructs a {@link FieldHoverEvent} with the given field.
     *
     * @param field the field
     */
    public FieldHoverEvent(final Field field) {
        this.field = field;
    }

    /**
     * Returns the hovered field.
     *
     * @return the field
     */
    public Field getField() {
        return field;
    }

    /**
     * Returns the world of the hovered field.
     *
     * @return the world
     */
    public KarelWorld getWorld() {
        return field.getWorld();
    }
}
