package fopbot;

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
     */
    public static void putCoins(int x, int y, int numberOfCoins) {
        if (world == null) {
            setSize(10, 10);
        }
        world.putCoins(x, y, numberOfCoins);
    }

    /**
     * Removes the specified {@link Robot} from the current world.
     *
     * @param robot the robot to remove
     */
    public static void removeRobot(Robot robot){
        world.removeRobot(robot);
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
