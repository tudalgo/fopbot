package fopbot;

/**
 * A {@link KeyPressListener} is a listener for key press events registered by the {@link InputHandler}.
 */
public interface KeyPressListener {

    /**
     * Notifies this key press listener about the given key press event.
     *
     * @param event the key press event
     */
    void onKeyPress(KeyPressEvent event);
}
