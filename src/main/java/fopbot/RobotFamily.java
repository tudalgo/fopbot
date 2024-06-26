package fopbot;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * An enumeration of robot families.
 * A family of robots is uniquely in terms of their appearance.
 */
public interface RobotFamily {

    /**
     * Returns the color of this robot family.
     *
     * @return the color of this robot family
     */
    Color getColor();

    /**
     * Sets the color of this robot family.
     *
     * @param color the new color
     */
    void setColor(Color color);

    /**
     * Renders a robot of this family.
     *
     * @param targetSize     the target size of the rendered image in pixels
     * @param rotationOffset the rotation offset in degrees
     * @param turnedOff      whether the robot is turned off
     * @return the rendered robot
     */
    BufferedImage render(
        int targetSize,
        int rotationOffset,
        boolean turnedOff
    );

    /**
     * Returns the name of this robot family.
     *
     * @return the name of this robot family
     */
    String getName();

    // default Families

    /**
     * A teal square robot.
     */
    RobotFamily SQUARE_AQUA = new SvgBasedRobotFamily(
        "SQUARE_AQUA",
        "/robots/square_aqua_on.svg",
        "/robots/square_aqua_off.svg",
        Color.CYAN
    );
    /**
     * A black square robot.
     */
    RobotFamily SQUARE_BLACK = new SvgBasedRobotFamily(
        "SQUARE_BLACK",
        "/robots/square_black_on.svg",
        "/robots/square_black_off.svg",
        Color.BLACK
    );
    /**
     * A blue square robot.
     */
    RobotFamily SQUARE_BLUE = new SvgBasedRobotFamily(
        "SQUARE_BLUE",
        "/robots/square_blue_on.svg",
        "/robots/square_blue_off.svg",
        Color.BLUE
    );
    /**
     * A brown square robot.
     */
    RobotFamily SQUARE_GREEN = new SvgBasedRobotFamily(
        "SQUARE_GREEN",
        "/robots/square_green_on.svg",
        "/robots/square_green_off.svg",
        Color.GREEN
    );
    /**
     * A gray square robot.
     */
    RobotFamily SQUARE_ORANGE = new SvgBasedRobotFamily(
        "SQUARE_ORANGE",
        "/robots/square_orange_on.svg",
        "/robots/square_orange_off.svg",
        Color.ORANGE
    );
    /**
     * A pink square robot.
     */
    RobotFamily SQUARE_PURPLE = new SvgBasedRobotFamily(
        "SQUARE_PURPLE",
        "/robots/square_purple_on.svg",
        "/robots/square_purple_off.svg",
        Color.MAGENTA
    );
    /**
     * A red square robot.
     */
    RobotFamily SQUARE_RED = new SvgBasedRobotFamily(
        "SQUARE_RED",
        "/robots/square_red_on.svg",
        "/robots/square_red_off.svg",
        Color.RED
    );
    /**
     * A white square robot.
     */
    RobotFamily SQUARE_WHITE = new SvgBasedRobotFamily(
        "SQUARE_WHITE",
        "/robots/square_white_on.svg",
        "/robots/square_white_off.svg",
        Color.WHITE
    );
    /**
     * A yellow square robot.
     */
    RobotFamily SQUARE_YELLOW = new SvgBasedRobotFamily(
        "SQUARE_YELLOW",
        "/robots/square_yellow_on.svg",
        "/robots/square_yellow_off.svg",
        Color.YELLOW
    );
    /**
     * A teal triangle robot.
     */
    RobotFamily TRIANGLE_BLUE = new SvgBasedRobotFamily(
        "TRIANGLE_BLUE",
        "/robots/triangle_blue_on.svg",
        "/robots/triangle_blue_off.svg",
        Color.BLUE
    );
}
