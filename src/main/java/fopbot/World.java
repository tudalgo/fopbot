package fopbot;

public class World {

    private static boolean global = false;
    private static KarelWorld world;

    /**
     * If true, show the world's gui
     */
    public static void setVisible(boolean visible) {
        if (visible && world == null) {
            setSize(10, 10);
        }
        world.setVisible(visible);
    }

    /**
     * @return the worlds height
     */
    public static int getHeight() {
        if (world == null) {
            setSize(10, 10);
        }
        return world.getHeight();
    }

    /**
     * @return the worlds width
     */
    public static int getWidth() {
        if (world == null) {
            setSize(10, 10);
        }
        return world.getWidth();
    }

    /**
     * Sets the worlds size
     */
    public static void setSize(int width, int height) {
        world = new KarelWorld(width, height);
        global = true;
    }

    /**
     * Sets the delay a robot has to wait after each action
     *
     * @param delay (in ms)
     */
    public static void setDelay(int delay) {
        if (world == null) {
            setSize(10, 10);
        }
        world.setDelay(delay);
    }

    /**
     * @return the current delay (in ms)
     */
    public static int getDelay() {
        if (world == null) {
            setSize(10, 10);
        }
        return world.getDelay();
    }

    /**
     * Reset the world (remove all entities)
     */
    public static void reset() {
        if (world == null) {
            setSize(10, 10);
        }
        world.reset();
    }

    /**
     * Places a horizontal wall at field (x,y)
     */
    public static void placeHorizontalWall(int x, int y) {
        if (world == null) {
            setSize(10, 10);
        }
        world.placeHorizontalWall(x, y);
    }

    /**
     * Places a vertical wall at field (x,y)
     */
    public static void placeVerticalWall(int x, int y) {
        if (world == null) {
            setSize(10, 10);
        }
        world.placeVerticalWall(x, y);
    }

    /**
     * Places a block at field (x,y)
     */
    public static void placeBlock(int x, int y) {
        if (world == null) {
            setSize(10, 10);
        }
        world.placeBlock(x, y);
    }

    /**
     * Puts down N coins at field (x,y)
     */
    public static void putCoins(int x, int y, int numberOfCoins) {
        if (world == null) {
            setSize(10, 10);
        }
        world.putCoins(x, y, numberOfCoins);
    }

    protected static boolean isGlobal() {
        return global;
    }

    public static KarelWorld getGlobalWorld() {
        return world;
    }
}
