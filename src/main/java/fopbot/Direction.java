package fopbot;

import java.util.Arrays;

/**
 * Defines the (viewing) direction that an entity can have.
 */
public enum Direction {
    /**
     * The neutral direction or direction of view "none".
     */
    NONE(0, 0),
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

    /**
     * Calculates the length of a given vector of an arbitrary dimension.
     *
     * @param components the components of the vector.
     * @return the length of the vector.
     */
    private double vectorLength(final double... components) {
        return Math.sqrt(Arrays.stream(components).map(c -> Math.pow(c, 2)).sum());
    }

    /**
     * Returns the direction who best matches the given vector.
     *
     * @param x The x-component of the vector.
     * @param y The y-component of the vector.
     * @return Returns the direction who best matches the given vector.
     */
    public Direction fromVector(final double x, final double y) {
        if (x == 0 && y == 0) {
            return NONE;
        }
        return Arrays
            .stream(values())
            .filter(d -> d != NONE)
            .min(
                // calculate the angle between the given vector and the direction vector
                (d1, d2) -> Double.compare(
                    Math.acos(
                        (d1.dx * x + d1.dy * y) / (vectorLength(d1.dx, d1.dy) * vectorLength(x, y))
                    ),
                    Math.acos(
                        (d2.dx * x + d2.dy * y) / (vectorLength(d2.dx, d2.dy) * vectorLength(x, y))
                    )
                )
            )
            .orElse(NONE);
    }

    /**
     * Returns the direction opposite to this direction.
     *
     * @return the direction opposite to this direction.
     */
    public Direction getOpposite() {
        return fromVector(-dx, -dy);
    }

    /**
     * Checks whether the given direction is opposite to this direction.
     *
     * @param direction The direction to check.
     * @return {@code true} if the given direction is opposite to this direction.
     */
    public boolean isOpposite(final Direction direction) {
        return equals(direction.getOpposite());
    }

    /**
     * Returns the direction that is the result of rotating this direction by the given angle in degrees counterclockwise.
     *
     * @param angle The angle to rotate this direction by.
     * @return The direction that is the result of rotating this direction by the given angle in degrees counterclockwise.
     */
    public Direction rotate(final double angle) {
        // convert to radians
        final var radAngle = Math.toRadians(angle);
        final double cos = Math.cos(radAngle);
        final double sin = Math.sin(radAngle);
        return fromVector(dx * cos - dy * sin, dx * sin + dy * cos);
    }
}
