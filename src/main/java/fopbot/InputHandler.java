package fopbot;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The InputHandler is responsible for handling input events of the {@link GuiPanel}.
 */
public class InputHandler {
    // --Variables-- //

    /**
     * The keys that were pressed during the last frame.
     * When a key is pressed, it is added to this list after the onKeyPressed event is handled.
     */
    private final Set<Integer> keysPressed = new HashSet<>();

    /**
     * A List of event handlers that are called when a key is pressed.
     */
    private final List<KeyListener> onKeyPressed = new ArrayList<>();

    /**
     * A List of event handlers that are called when a key is released.
     */

    private final List<KeyListener> onKeyReleased = new ArrayList<>();
    /**
     * A List of event handlers that are called when a key is typed.
     */
    private final List<KeyListener> onKeyTyped = new ArrayList<>();

    // --Constructors-- //

    /**
     * Creates a new GameInputHandler.
     *
     * @param panel The panel to handle input for.
     */
    public InputHandler(final GuiPanel panel) {
        handleKeyboardInputs(panel);
    }

    // --Getters and Setters-- //

    /**
     * Gets the value of {@link #keysPressed} field.
     *
     * @return the value of the {@link #keysPressed} field.
     * @see #keysPressed
     */
    public Set<Integer> getKeysPressed() {
        return this.keysPressed;
    }

    // --Methods-- //

    /**
     * Adds the given event handler to the {@link #onKeyPressed} list.
     *
     * @param eventHandler The key to add.
     * @see #onKeyPressed
     */
    public void addOnKeyPressed(final KeyListener eventHandler) {
        this.onKeyPressed.add(eventHandler);
    }

    /**
     * Adds the given event handler to the {@link #onKeyReleased} list.
     *
     * @param eventHandler The key to add.
     * @see #onKeyReleased
     */
    public void addOnKeyReleased(final KeyListener eventHandler) {
        this.onKeyReleased.add(eventHandler);
    }

    /**
     * Adds the given event handler to the {@link #onKeyTyped} list.
     *
     * @param eventHandler The key to add.
     * @see #onKeyTyped
     */
    public void addOnKeyTyped(final KeyListener eventHandler) {
        this.onKeyTyped.add(eventHandler);
    }

    /**
     * Set up the keyboard handlers for the given panel.
     *
     * @param panel The panel to handle input for.
     */
    private void handleKeyboardInputs(final GuiPanel panel) {
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(final KeyEvent e) {
                InputHandler.this.keysPressed.add(e.getKeyCode());
                InputHandler.this.onKeyPressed.forEach(keyListener -> keyListener.keyPressed(e));
            }

            @Override
            public void keyReleased(final KeyEvent e) {
                InputHandler.this.keysPressed.remove(e.getKeyCode());
                InputHandler.this.onKeyReleased.forEach(keyListener -> keyListener.keyReleased(e));
            }

            @Override
            public void keyTyped(final KeyEvent e) {
                InputHandler.this.onKeyTyped.forEach(keyListener -> keyListener.keyTyped(e));
            }
        });
    }
}
