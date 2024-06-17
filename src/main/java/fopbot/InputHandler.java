package fopbot;

import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    private final Point lastFieldHovered = new Point(-1, -1);

    /**
     * A List of event handlers that are called when a keyboard event is fired.
     */
    private final List<KeyListener> listeners = new ArrayList<>();

    private final List<KeyPressListener> keyPressListeners = new ArrayList<>();

    private final List<FieldClickListener> fieldClickListeners = new ArrayList<>();

    private final List<FieldHoverListener> fieldHoverListeners = new ArrayList<>();

    /**
     * An executor service for executing input events.
     */
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    /**
     * Returns the input handler of the global world.
     *
     * @return the input handler
     */
    public static InputHandler getInputHandler() {
        return World.getGlobalWorld().getInputHandler();
    }

    // --Constructors-- //

    /**
     * Creates a new GameInputHandler.
     *
     * @param panel The panel to handle input for.
     */
    public InputHandler(final GuiPanel panel) {
        handleKeyboardInputs(panel);
        handleFieldMouseEvents(panel);
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
    public void addFieldClickListener(final FieldClickListener screenListener) {
        this.fieldClickListeners.add(screenListener);
    }

    /**
     * Adds the given key press listener to this input handler.
     *
     * @param listener the key press listener
     */
    public void addKeyPressListener(final KeyPressListener listener) {
        this.keyPressListeners.add(listener);
    }

    /**
     * Adds the given field hover listener to this input handler.
     *
     * @param listener the field hover listener
     */
    public void addFieldHoverListener(final FieldHoverListener listener) {
        this.fieldHoverListeners.add(listener);
    }

    /**
     * Set up the keyboard handlers for the given panel.
     *
     * @param panel The panel to handle input for.
     */
    private void handleKeyboardInputs(final GuiPanel panel) {
        final var world = panel.world;
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(final KeyEvent e) {
                InputHandler.this.keysPressed.add(e.getKeyCode());
                InputHandler.this.listeners.forEach(keyListener -> executorService.submit(() -> keyListener.keyPressed(e)));
                Arrays.stream(Key.values()).filter(k -> k.getKeyCode() == e.getKeyCode()).findFirst().ifPresent(k -> {
                    final var event = new KeyPressEvent(world, k);
                    keyPressListeners.forEach(l -> {
                        try {
                            executorService.submit(() -> l.onKeyPress(event)).get();
                        } catch (final ExecutionException | InterruptedException exc) {
                            if (exc.getCause() instanceof RuntimeException) {
                                throw (RuntimeException) exc.getCause();
                            }
                            throw new RuntimeException(exc.getCause());
                        }
                    });
                });
            }

            @Override
            public void keyReleased(final KeyEvent e) {
                InputHandler.this.keysPressed.remove(e.getKeyCode());
                InputHandler.this.listeners.forEach(keyListener -> executorService.submit(() -> keyListener.keyReleased(e)));
            }

            @Override
            public void keyTyped(final KeyEvent e) {
                InputHandler.this.listeners.forEach(keyListener -> executorService.submit(() -> keyListener.keyTyped(e)));
            }
        });
    }

    /**
     * Checks if the given coordinates are valid.
     *
     * @param x     the x-coordinate
     * @param y     the y-coordinate
     * @param world the world
     * @return true if the coordinates are valid, false otherwise
     */
    private boolean isValidCoordinate(final int x, final int y, final KarelWorld world) {
        return x >= 0 && y >= 0 && x < world.getWidth() && y < world.getHeight();
    }

    private void handleFieldMouseEvents(final GuiPanel panel) {
        final var world = panel.world;
        panel.addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(final MouseEvent e) {
                final var pos = PaintUtils.getMouseTilePosition(panel, e.getPoint());
                if (!isValidCoordinate((int) pos.getX(), (int) pos.getY(), world)) {
                    return;
                }
                final var event = new FieldClickEvent(world.getField(pos.x, pos.y));
                fieldClickListeners.forEach(l -> {
                    try {
                        executorService.submit(() -> l.onFieldClick(event)).get();
                    } catch (final ExecutionException | InterruptedException exc) {
                        if (exc.getCause() instanceof RuntimeException) {
                            throw (RuntimeException) exc.getCause();
                        }
                        throw new RuntimeException(exc.getCause());
                    }
                });
            }

            @Override
            public void mouseMoved(final MouseEvent e) {
                final var pos = PaintUtils.getMouseTilePosition(panel, e.getPoint());
                if (!isValidCoordinate((int) pos.getX(), (int) pos.getY(), world)) {
                    return;
                }
                if (lastFieldHovered.x == pos.x && lastFieldHovered.y == pos.y) {
                    return;
                }
                lastFieldHovered.setLocation(pos);
                final var event = new FieldHoverEvent(world.getField(pos.x, pos.y));
                fieldHoverListeners.forEach(l -> {
                    try {
                        executorService.submit(() -> l.onFieldHover(event)).get();
                    } catch (final ExecutionException | InterruptedException exc) {
                        if (exc.getCause() instanceof RuntimeException) {
                            throw (RuntimeException) exc.getCause();
                        }
                        throw new RuntimeException(exc.getCause());
                    }
                });
            }
        });
    }
}
