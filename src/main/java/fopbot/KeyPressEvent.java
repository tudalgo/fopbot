package fopbot;

/**
 * A {@link KeyPressEvent} represents a key press registered by the {@link InputHandler}.
 */
public final class KeyPressEvent {

    private final KarelWorld world;

    private final Key key;

    /**
     * Constructs an <code>KeyPressEvent</code> with the given world and key code.
     * The world represents the world displayed by the interface that triggered this event.
     * The key code is equal to the key code of an object of {@link java.awt.event.KeyEvent}.
     *
     * @param world the world
     * @param key   the key code
     * @see java.awt.event.KeyEvent
     */
    public KeyPressEvent(final KarelWorld world, final Key key) {
        this.world = world;
        this.key = key;
    }

    /**
     * Returns the world displayed by the interface that triggered this <code>KeyPressEvent</code>.
     *
     * @return the world
     */
    public KarelWorld getWorld() {
        return world;
    }

    /**
     * Returns the key code of the key that triggered this <code>KeyPressEvent</code>.
     *
     * @return the key code
     */
    public Key getKey() {
        return key;
    }
}
