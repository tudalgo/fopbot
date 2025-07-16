package fopbot;

/**
 * Defines the (viewing) direction that an entity can have.
 */
public enum Direction {
    /**
     * The direction upwards or direction of view "north".
     */
    UP(0, 1),
    /**
     * The direction to the right or direction of view "east".
     */
    RIGHT(1, 0),
    /**
     * The direction downwards or direction of view "south".
     */
    DOWN(0, -1),
    /**
     * The direction to the left or direction of view "west".
     */
    LEFT(-1, 0);
    /**
     * The x offset of the {@link Direction}.
     */
    final int dx;
    /**
     * The y offset of the {@link Direction}.
     */
    final int dy;

    /**
     * Creates a new direction.
     *
     * @param dx The x-offset of the {@link Direction}.
     * @param dy The y-offset of the {@link Direction}.
     */
    Direction(final int dx, final int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    /**
     * Gets the x offset of the {@link Direction}.
     *
     * @return The x offset of the {@link Direction}.
     */
    public int getDx() {
        return dx;
    }

    /**
     * Gets the y offset of the {@link Direction}.
     *
     * @return The y offset of the {@link Direction}.
     */
    public int getDy() {
        return dy;
    }

    /**
     * Checks whether this direction is a horizontal direction.
     *
     * @return {@code true} if this direction is a horizontal direction.
     */
    public boolean isHorizontal() {
        return dy == 0;
    }

    /**
     * Checks whether this direction is a vertical direction.
     *
     * @return {@code true} if this direction is a vertical direction.
     */
    public boolean isVertical() {
        return dx == 0;
    }
}
