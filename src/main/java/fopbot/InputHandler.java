package fopbot;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
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
     * A List of event handlers that are called when a keyboard event is fired.
     */
    private final List<KeyListener> listeners = new ArrayList<>();

    private final List<FieldClickListener> fieldClickListeners = new ArrayList<>();

    // --Constructors-- //

    /**
     * Creates a new GameInputHandler.
     *
     * @param panel The panel to handle input for.
     */
    public InputHandler(final GuiPanel panel) {
        handleKeyboardInputs(panel);
        handleFieldClickEvents(panel);
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
     * Adds the given event handler to the {@link #listeners} list.
     *
     * @param eventHandler The key to add.
     * @see #listeners
     */
    public void addKeyListener(final KeyListener eventHandler) {
        this.listeners.add(eventHandler);
    }

    /**
     * Adds the screen listener to this input handler.
     *
     * @param screenListener the screen listener
     */
    public void addFieldClickListener(FieldClickListener screenListener) {
        this.fieldClickListeners.add(screenListener);
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
                InputHandler.this.listeners.forEach(keyListener -> keyListener.keyPressed(e));
            }

            @Override
            public void keyReleased(final KeyEvent e) {
                InputHandler.this.keysPressed.remove(e.getKeyCode());
                InputHandler.this.listeners.forEach(keyListener -> keyListener.keyReleased(e));
            }

            @Override
            public void keyTyped(final KeyEvent e) {
                InputHandler.this.listeners.forEach(keyListener -> keyListener.keyTyped(e));
            }
        });
    }

    private void handleFieldClickEvents(GuiPanel panel) {
        var world = panel.world;
        panel.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                var transform = PaintUtils.getPanelWorldTransform(panel);
                var point = new Point2D.Double();
                try {
                    transform.inverseTransform(PaintUtils.toPoint2D(e.getPoint()), point);
                } catch (NoninvertibleTransformException ex) {
                    throw new RuntimeException(ex);
                }
                var x = point.getX();
                var y = point.getY();
                if (x < 0 || y < 0 || x >= world.getWidth() || y >= world.getHeight()) {
                    return;
                }
                var event = new FieldClickEvent(world.getField((int) x, (int) y));
                fieldClickListeners.forEach(l -> l.onFieldClick(event));
            }
        });
    }
}
