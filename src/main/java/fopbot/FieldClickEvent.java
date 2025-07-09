package fopbot;

/**
 * Represents an event that occurs when a field in the virtual world is clicked.
 *
 * <p>This event provides access to the clicked {@link Field} as well as the {@link KarelWorld} the field belongs to.
 */
public final class FieldClickEvent {

    /**
     * The field that was clicked.
     */
    private final Field field;

    /**
     * Constructs a new {@code FieldClickEvent} for the specified field.
     *
     * @param field the field that was clicked
     */
    public FieldClickEvent(final Field field) {
        this.field = field;
    }

    /**
     * Returns the field that was clicked.
     *
     * @return the clicked {@link Field}
     */
    public Field getField() {
        return field;
    }

    /**
     * Returns the world to which the clicked field belongs.
     *
     * @return the {@link KarelWorld} of the clicked field
     */
    public KarelWorld getWorld() {
        return field.getWorld();
    }
}

