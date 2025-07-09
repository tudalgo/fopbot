package fopbot;

/**
 * Represents a wall that blocks movement between two adjacent fields in virtual the world.
 *
 * <p>A {@code Wall} does not occupy a field itself, but prevents movement across
 * its position, depending on whether it is horizontal or vertical.
 */
public class Wall extends FieldEntity {

    /**
     * Indicates whether the wall is horizontal ({@code true}) or vertical ({@code false}).
     */
    private final boolean horizontal;

    /**
     * Constructs a {@code Wall} at the specified coordinates with the given orientation.
     *
     * @param x          the x-coordinate of the wall
     * @param y          the y-coordinate of the wall
     * @param horizontal {@code true} if the wall is horizontal, {@code false} if vertical
     */
    public Wall(int x, int y, boolean horizontal) {
        super(x, y);
        this.horizontal = horizontal;
    }

    /**
     * Returns whether the wall is horizontal.
     *
     * @return {@code true} if the wall is horizontal, {@code false} if vertical
     */
    public boolean isHorizontal() {
        return horizontal;
    }
}

