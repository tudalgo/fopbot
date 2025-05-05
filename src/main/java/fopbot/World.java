package fopbot;

import java.util.Comparator;
import java.util.Map;

/**
 * A wrapper of a {@code KarelWorld} which represents a global world.
 */
public class World {

    /**
     * The state of whether a global world exists.
     */
    private static boolean global = false;

    /**
     * The global world on which entities can be placed.
     */
    private static KarelWorld world;

    /**
     * Sets the visibility of the world to the specified visibility value. If no global world exists,
     * create a global world with the size {@code (10, 10)}.
     *
     * @param visible if {@code true} the world will be visible on the graphical user interface
     */
    public static void setVisible(boolean visible) {
        if (visible && world == null) {
            setSize(10, 10);
        }
        world.setVisible(visible);
    }

    /**
     * Returns the height of the global {@code World}. If no global world exists, create a global
     * world with the size {@code (10, 10)}.
     *
     * @return the height of the global {@code World}.
     */
    public static int getHeight() {
        if (world == null) {
            setSize(10, 10);
        }
        return world.getHeight();
    }

    /**
     * Returns the width of the global {@code World}. If no global world exists, create a global world
     * with the size {@code (10, 10)}.
     *
     * @return the width of the global {@code World}.
     */
    public static int getWidth() {
        if (world == null) {
            setSize(10, 10);
        }
        return world.getWidth();
    }

    /**
     * Creates a new global world with the specified world size.
     *
     * @param width  the width of the global world
     * @param height the height of the global world
     */
    public static void setSize(int width, int height) {
        var newWorld = new KarelWorld(width, height);
        if (world != null) {
            newWorld.setDelay(world.getDelay());
        }
        world = newWorld;
        global = true;
    }

    /**
     * Sets the delay that delays all robot actions after their execution.
     *
     * @param delay the delay value in milliseconds
     */
    public static void setDelay(int delay) {
        if (world == null) {
            setSize(10, 10);
        }
        world.setDelay(delay);
    }

    /**
     * Returns the current delay value that delays all robot actions after their execution.
     *
     * @return the current delay value
     */
    public static int getDelay() {
        if (world == null) {
            setSize(10, 10);
        }
        return world.getDelay();
    }

    /**
     * Resets the world by creating a new global world and thus indirectly removes all entities.
     */
    public static void reset() {
        if (world == null) {
            setSize(10, 10);
        }
        world.reset();
    }

    /**
     * Places a horizontal wall at the specified coordinate.
     *
     * @param x the X coordinate of the horizontal wall
     * @param y the Y coordinate of the horizontal wall
     */
    public static void placeHorizontalWall(int x, int y) {
        if (world == null) {
            setSize(10, 10);
        }
        world.placeHorizontalWall(x, y);
    }

    /**
     * Places a vertical wall at the specified coordinate.
     *
     * @param x the X coordinate of the vertical wall
     * @param y the Y coordinate of the vertical wall
     */
    public static void placeVerticalWall(int x, int y) {
        if (world == null) {
            setSize(10, 10);
        }
        world.placeVerticalWall(x, y);
    }

    /**
     * Places a block at the specified coordinate.
     *
     * @param x the X coordinate of the block
     * @param y the Y coordinate of the block
     */
    public static void placeBlock(int x, int y) {
        if (world == null) {
            setSize(10, 10);
        }
        world.placeBlock(x, y);
    }

    /**
     * Places the specified number of coins at the specified coordinate.
     *
     * @param x             the X coordinate of the coins to place
     * @param y             the Y coordinate of the coins to place
     * @param numberOfCoins the number of coins to place
     *
     * @throws IllegalArgumentException if the number of coins is smaller than 1 or the position is invalid
     */
    public static void putCoins(int x, int y, int numberOfCoins) throws IllegalArgumentException {
        if (world == null) {
            setSize(10, 10);
        }
        world.putCoins(x, y, numberOfCoins);
    }

    /**
     * Returns the input handler used of the global world.
     *
     * @return the input handler
     */
    private static InputHandler getInputHandler() {
        return getGlobalWorld().getInputHandler();
    }

    /**
     * Adds the given key press listener to the input handler of the global world.
     *
     * @param keyPressListener the key press listener
     */
    public static void addKeyPressListener(KeyPressListener keyPressListener) {
        getInputHandler().addKeyPressListener(keyPressListener);
    }

    /**
     * Adds the given field click listener to the input handler of the global world.
     *
     * @param fieldClickListener the field click listener
     */
    public static void addFieldClickListener(FieldClickListener fieldClickListener) {
        getInputHandler().addFieldClickListener(fieldClickListener);
    }

    /**
     * Configures the rendering system by setting the render order and the associated {@link FieldEntity} renderers.
     * <p>
     * This method allows for customizing the order in which {@link FieldEntity} objects are rendered on the screen
     * and specifying the renderers responsible for drawing each entity type. Calling this method will replace any
     * previously configured renderers with the new ones provided in the {@code renderers} map. The render order,
     * defined by the {@link Comparator}, controls the layering of entities, with entities of lower priority being drawn first.
     *
     * <p><strong>Important:</strong> If you register custom renderers, ensure that the comparator
     * is updated accordingly to reflect the intended rendering layer. Failing to update the comparator may result in
     * entities being drawn in the wrong order.
     *
     * @param renderOrder a {@link Comparator} that defines the order in which entities should be rendered.
     *                    Entities with a lower priority value will be rendered first.
     * @param renderers   a map of {@link Class} to {@link FieldEntityRenderer} that associates each {@link FieldEntity}
     *                    type with its corresponding renderer. This will replace any existing renderers in the system.
     */
    public static void setRenderConfiguration(
        Comparator<FieldEntity> renderOrder,
        Map<Class<? extends FieldEntity>, FieldEntityRenderer<?>> renderers
    ) {
        if (world == null) {
            setSize(10, 10);
        }
        world.getGuiPanel().setRenderConfiguration(renderOrder, renderers);
    }

    /**
     * /**
     * Sets the rendering order for all {@link FieldEntity} objects.
     * <p>
     * The provided {@link Comparator} determines the sequence in which entities are drawn
     * on the GUI. Entities with higher priority should appear on top of others. This is
     * particularly important when multiple entities occupy the same field.
     *
     * @param renderOrder a {@link Comparator} that defines the visual stacking order of {@link FieldEntity} instances
     */
    public static void setRenderOrder(Comparator<FieldEntity> renderOrder) {
        if (world == null) {
            setSize(10, 10);
        }
        world.getGuiPanel().setRenderOrder(renderOrder);
        world.triggerUpdate();
    }

    /**
     * Registers or replaces the {@link FieldEntityRenderer} for a given {@link FieldEntity} type.
     * <p>
     * If a renderer is already registered for the specified entity class, it will be replaced.
     * This enables dynamic customization or extension of the rendering system to support
     * additional or user-defined {@link FieldEntity} types.
     * <p>
     * <strong>Note:</strong> The rendering order is determined by the comparator.
     * If you register a new {@link FieldEntityRenderer} for a custom entity type,
     * you must also update the comparator to ensure it is drawn in the correct layer.
     * Otherwise, it will be rendered last by default.
     *
     * @param entityClass the class of the {@link FieldEntity} to associate with a renderer
     * @param renderer    the {@link FieldEntityRenderer} responsible for rendering the given entity type
     */
    public static void registerFieldEntityRenderer(
        final Class<? extends FieldEntity> entityClass,
        final FieldEntityRenderer<? extends FieldEntity> renderer
    ) {
        if (world == null) {
            setSize(10, 10);
        }
        world.getGuiPanel().registerFieldEntityRenderer(entityClass, renderer);
    }

    /**
     * Registers or replaces the renderers for a given entities type.
     * <p>
     * If a renderer is already registered for the specified entity class, it will be replaced.
     * This enables dynamic customization or extension of the rendering system to support
     * additional or user-defined {@link FieldEntity} types.
     * <p>
     * <strong>Note:</strong> The rendering order is determined by the comparator.
     * If you register a new {@link FieldEntityRenderer} for a custom entity type,
     * you must also update the comparator to ensure it is drawn in the correct layer.
     * Otherwise, it will be rendered last by default.
     *
     * @param renderers a map of {@link Class} to {@link FieldEntityRenderer} that associates each {@link FieldEntity}
     */
    public static void registerFieldEntityRenderers(Map<Class<? extends FieldEntity>, FieldEntityRenderer<?>> renderers) {
        if (world == null) {
            setSize(10, 10);
        }
        world.getGuiPanel().registerFieldEntityRenderers(renderers);
    }

    /**
     * Returns {@code true} if a global world exists.
     *
     * @return {@code true} if a global world exists
     */
    protected static boolean isGlobal() {
        return global;
    }

    /**
     * Returns an instance of the global world.
     *
     * @return an instance of the global world
     */
    public static KarelWorld getGlobalWorld() {
        return world;
    }
}
