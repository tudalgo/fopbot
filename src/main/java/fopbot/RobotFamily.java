package fopbot;

/**
 * An enumeration of robot families.
 * A family of robots is uniquely in terms of their appearance.
 */
public enum RobotFamily {

    TRIANGLE_BLUE,
    SQUARE_AQUA,
    SQUARE_BLUE,
    SQUARE_GREEN,
    SQUARE_ORANGE,
    SQUARE_PURPLE,
    SQUARE_RED,
    SQUARE_YELLOW,
    SQUARE_BLACK,
    SQUARE_WHITE;

    /**
     * Returns the identifier of this robot family.
     *
     * @return the identifier of this robot family
     */
    public String getIdentifier() {
        return name().toLowerCase();
    }
}
