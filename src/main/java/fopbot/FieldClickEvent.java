package fopbot;

/**
 * A {@link FieldClickEvent} represents a click on a field.
 */
public final class FieldClickEvent {

    private final Field field;

    /**
     * Constructs a {@link FieldClickEvent} with the given field.
     *
     * @param field the field
     */
    public FieldClickEvent(Field field) {
        this.field = field;
    }

    /**
     * Returns the clicked field.
     *
     * @return the field
     */
    public Field getField() {
        return field;
    }
}
