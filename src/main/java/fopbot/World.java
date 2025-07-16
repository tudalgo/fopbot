package fopbot;

/**
 * A wrapper of a {@code KarelWorld} which represents a global world.
 */
public final class World {

    /**
     * The state of whether a global world exists.
     *
     * @deprecated This field is no longer needed, as the global world is always created when needed.
     */
    @Deprecated(since = "0.8.2", forRemoval = true)
    private static boolean global = true;

    /**
     * The global world on which entities can be placed.
     */
    private static KarelWorld world;

    /**
     * Don't let anyone instantiate this class.
     * Since there is only one global world, it is not necessary to instantiate this class.
     */
    private World() {
    }

    /**
     * Sets the visibility of the world to the specified visibility value. If no global world exists,
     * create a global world with the size {@code (10, 10)}.
     *
     * @param visible if {@code true} the world will be visible on the graphical user interface
     */
    public static void setVisible(final boolean visible) {
        getGlobalWorld().setVisible(visible);
    }

    /**
     * Returns the height of the global {@code World}. If no global world exists, create a global
     * world with the size {@code (10, 10)}.
     *
     * @return the height of the global {@code World}.
     */
    public static int getHeight() {
        return getGlobalWorld().getHeight();
    }

    /**
     * Returns the width of the global {@code World}. If no global world exists, create a global world
     * with the size {@code (10, 10)}.
     *
     * @return the width of the global {@code World}.
     */
    public static int getWidth() {
        return getGlobalWorld().getWidth();
    }

    /**
     * Creates a new global world with the specified world size.
     *
     * @param width  the width of the global world
     * @param height the height of the global world
     */
    public static void setSize(final int width, final int height) {
        final var newWorld = new KarelWorld(width, height);
        // Copy settings from the previous world if it exists
        if (world != null) {
            newWorld.setDelay(world.getDelay());
            newWorld.setColorProfile(world.getColorProfile());
            newWorld.setDrawingRegistry(world.getDrawingRegistry());
            newWorld.setActionLimit(world.getActionLimit());
            newWorld.setDrawTurnedOffRobots(world.isDrawTurnedOffRobots());
        }
        world = newWorld;
    }

    /**
     * Sets the delay that delays all robot actions after their execution.
     *
     * @param delay the delay value in milliseconds
     */
    public static void setDelay(final int delay) {
        getGlobalWorld().setDelay(delay);
    }

    /**
     * Returns the current delay value that delays all robot actions after their execution.
     *
     * @return the current delay value
     */
    public static int getDelay() {
        return getGlobalWorld().getDelay();
    }

    /**
     * Resets the world by creating a new global world and thus indirectly removes all entities.
     */
    public static void reset() {
        getGlobalWorld().reset();
    }

    /**
     * Places a horizontal wall at the specified coordinate.
     *
     * @param x the X coordinate of the horizontal wall
     * @param y the Y coordinate of the horizontal wall
     */
    public static void placeHorizontalWall(final int x, final int y) {
        getGlobalWorld().placeHorizontalWall(x, y);
    }

    /**
     * Places a vertical wall at the specified coordinate.
     *
     * @param x the X coordinate of the vertical wall
     * @param y the Y coordinate of the vertical wall
     */
    public static void placeVerticalWall(final int x, final int y) {
        getGlobalWorld().placeVerticalWall(x, y);
    }

    /**
     * Places a block at the specified coordinate.
     *
     * @param x the X coordinate of the block
     * @param y the Y coordinate of the block
     */
    public static void placeBlock(final int x, final int y) {
        getGlobalWorld().placeBlock(x, y);
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
    public static void putCoins(final int x, final int y, final int numberOfCoins) throws IllegalArgumentException {
        getGlobalWorld().putCoins(x, y, numberOfCoins);
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
    public static void addKeyPressListener(final KeyPressListener keyPressListener) {
        getInputHandler().addKeyPressListener(keyPressListener);
    }

    /**
     * Adds the given field click listener to the input handler of the global world.
     *
     * @param fieldClickListener the field click listener
     */
    public static void addFieldClickListener(final FieldClickListener fieldClickListener) {
        getInputHandler().addFieldClickListener(fieldClickListener);
    }

    /**
     * Returns {@code true} if a global world exists.
     *
     * @return {@code true} if a global world exists
     * @deprecated No longer needed, as the global world is always created when needed.
     */
    @Deprecated(since = "0.8.2", forRemoval = true)
    protected static boolean isGlobal() {
        return global;
    }

    /**
     * Returns an instance of the global world.
     *
     * @return an instance of the global world
     */
    public static KarelWorld getGlobalWorld() {
        if (world == null) {
            setSize(10, 10);
        }
        return world;
    }
}
