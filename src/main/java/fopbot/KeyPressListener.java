package fopbot;

import java.util.EventListener;

/**
 * Listener interface for receiving key press events.
 *
 * <p>Implement this interface to handle {@link KeyPressEvent}s triggered by keyboard input.
 */
public interface KeyPressListener extends EventListener {

    /**
     * Invoked when a key is pressed.
     *
     * @param event the event object containing details about the key press
     */
    void onKeyPress(KeyPressEvent event);
}

