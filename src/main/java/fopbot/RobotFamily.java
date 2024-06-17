package fopbot;

import java.awt.Color;

/**
 * An enumeration of robot families.
 * A family of robots is uniquely in terms of their appearance.
 */
public enum RobotFamily {
    /**
     * A Blue Triangle robot.
     */
    TRIANGLE_BLUE(Color.BLUE),
    /**
     * A Teal Square robot.
     */
    SQUARE_AQUA(Color.CYAN),
    /**
     * A Black Square robot.
     */
    SQUARE_BLUE(Color.BLUE),
    /**
     * A Green Square robot.
     */
    SQUARE_GREEN(Color.GREEN),
    /**
     * An Orange Square robot.
     */
    SQUARE_ORANGE(Color.ORANGE),
    /**
     * A Purple Square robot.
     */
    SQUARE_PURPLE(Color.MAGENTA),
    /**
     * A Red Square robot.
     */
    SQUARE_RED(Color.RED),
    /**
     * A Yellow Square robot.
     */
    SQUARE_YELLOW(Color.YELLOW),
    /**
     * A Black Square robot.
     */
    SQUARE_BLACK(Color.BLACK),
    /**
     * A White Square robot.
     */
    SQUARE_WHITE(Color.WHITE);

    /**
     * The color of this robot family.
     */
    private final Color color;

    /**
     * Creates a new robot family with the given color.
     *
     * @param color the color of the robot family
     */
    RobotFamily(final Color color) {
        this.color = color;
    }

    /**
     * Returns the color of this robot family.
     *
     * @return the color of this robot family
     */
    public Color getColor() {
        return color;
    }

    /**
     * Returns the identifier of this robot family.
     *
     * @return the identifier of this robot family
     */
    public String getIdentifier() {
        return name().toLowerCase();
    }
}
