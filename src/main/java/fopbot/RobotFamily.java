package fopbot;

import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * Represents a family of robots distinguished by their visual appearance.
 *
 * <p>Each robot family provides a unique style and rendering behavior
 * based on its shape, color, and visual assets and state of the robot.
 *
 * @see Robot
 */
public interface RobotFamily {

    /**
     * A robot with a square shape and teal color.
     */
    @NotNull RobotFamily SQUARE_AQUA = new SvgBasedRobotFamily(
        "SQUARE_AQUA",
        "/robots/square_aqua_on.svg",
        "/robots/square_aqua_off.svg",
        Color.CYAN
    );
    /**
     * A robot with a square shape and black color.
     */
    @NotNull RobotFamily SQUARE_BLACK = new SvgBasedRobotFamily(
        "SQUARE_BLACK",
        "/robots/square_black_on.svg",
        "/robots/square_black_off.svg",
        Color.BLACK
    );
    /**
     * A robot with a square shape and blue color.
     */
    @NotNull RobotFamily SQUARE_BLUE = new SvgBasedRobotFamily(
        "SQUARE_BLUE",
        "/robots/square_blue_on.svg",
        "/robots/square_blue_off.svg",
        Color.BLUE
    );
    /**
     * A robot with a square shape and green color.
     */
    @NotNull RobotFamily SQUARE_GREEN = new SvgBasedRobotFamily(
        "SQUARE_GREEN",
        "/robots/square_green_on.svg",
        "/robots/square_green_off.svg",
        Color.GREEN
    );

    // --- Default robot families ---
    /**
     * A robot with a square shape and orange color.
     */
    @NotNull RobotFamily SQUARE_ORANGE = new SvgBasedRobotFamily(
        "SQUARE_ORANGE",
        "/robots/square_orange_on.svg",
        "/robots/square_orange_off.svg",
        Color.ORANGE
    );
    /**
     * A robot with a square shape and purple color.
     */
    @NotNull RobotFamily SQUARE_PURPLE = new SvgBasedRobotFamily(
        "SQUARE_PURPLE",
        "/robots/square_purple_on.svg",
        "/robots/square_purple_off.svg",
        Color.MAGENTA
    );
    /**
     * A robot with a square shape and red color.
     */
    @NotNull RobotFamily SQUARE_RED = new SvgBasedRobotFamily(
        "SQUARE_RED",
        "/robots/square_red_on.svg",
        "/robots/square_red_off.svg",
        Color.RED
    );
    /**
     * A robot with a square shape and white color.
     */
    @NotNull RobotFamily SQUARE_WHITE = new SvgBasedRobotFamily(
        "SQUARE_WHITE",
        "/robots/square_white_on.svg",
        "/robots/square_white_off.svg",
        Color.WHITE
    );
    /**
     * A robot with a square shape and yellow color.
     */
    @NotNull RobotFamily SQUARE_YELLOW = new SvgBasedRobotFamily(
        "SQUARE_YELLOW",
        "/robots/square_yellow_on.svg",
        "/robots/square_yellow_off.svg",
        Color.YELLOW
    );
    /**
     * A robot with a triangle shape and blue color.
     */
    @NotNull RobotFamily TRIANGLE_BLUE = new SvgBasedRobotFamily(
        "TRIANGLE_BLUE",
        "/robots/triangle_blue_on.svg",
        "/robots/triangle_blue_off.svg",
        Color.BLUE
    );

    /**
     * Returns the base color of this robot family.
     *
     * @return the {@link Color} associated with this robot family
     */
    @NotNull Color getColor();

    /**
     * Sets the base color of this robot family.
     *
     * @param color the new {@link Color} to associate with this robot family
     */
    void setColor(@NotNull Color color);

    /**
     * Renders a robot image based on the specified properties.
     *
     * @param targetSize     the desired size of the rendered image in pixels
     * @param rotationOffset the rotation offset in degrees (e.g. 0, 90, 180, 270)
     * @param turnedOff      {@code true} if the robot is turned off; otherwise {@code false}
     *
     * @return the rendered {@link BufferedImage} representing the robot
     */
    @NotNull BufferedImage render(int targetSize, int rotationOffset, boolean turnedOff);

    /**
     * Returns the internal name or identifier of this robot family.
     *
     * @return the name of this robot family
     */
    @NotNull String getName();
}

