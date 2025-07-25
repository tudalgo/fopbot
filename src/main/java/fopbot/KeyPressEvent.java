package fopbot;

import org.jetbrains.annotations.NotNull;

/**
 * An event that indicates a key has been pressed in the virtual world.
 *
 * <p>This event is passed to objects that implement the {@link KeyPressListener}
 * interface when a key press occurs.
 */
public final class KeyPressEvent {

    /**
     * The world in which the key press occurred.
     */
    private final @NotNull KarelWorld world;

    /**
     * The key that was pressed.
     */
    private final @NotNull Key key;

    /**
     * Constructs a new {@code KeyPressEvent} with the given world and key.
     *
     * @param world the world in which the key was pressed
     * @param key   the key that was pressed
     */
    public KeyPressEvent(final @NotNull KarelWorld world, final @NotNull Key key) {
        this.world = world;
        this.key = key;
    }

    /**
     * Returns the world in which the key was pressed.
     *
     * @return the world associated with this event
     */
    public @NotNull KarelWorld getWorld() {
        return world;
    }

    /**
     * Returns the key that was pressed.
     *
     * @return the key associated with this event
     */
    public @NotNull Key getKey() {
        return key;
    }
}

