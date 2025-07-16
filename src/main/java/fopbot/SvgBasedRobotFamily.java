package fopbot;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * A robot family that is based on SVG images.
 */
@Getter
public class SvgBasedRobotFamily implements RobotFamily {
    /**
     * The name of this {@link RobotFamily}.
     */
    private final @NotNull String name;

    /**
     * The path to the SVG image of the robot when it is turned on.
     */
    private final @NotNull String svgPathOn;

    /**
     * The path to the SVG image of the robot when it is turned off.
     */
    private final @NotNull String svgPathOff;

    /**
     * The color of this {@link RobotFamily}.
     */
    private final @NotNull Color color;

    /**
     * The size of the last rendered image.
     */
    private int imageSize = -1;

    /**
     * The base rotation of the "on" images from the SVG file. Use this if the images are not "UP" by default.
     * The value is in degrees and rotation is clockwise.
     */
    private final int rotationOffsetOn;

    /**
     * The base rotation of the "off" images from the SVG file. Use this if the images are not "UP" by default.
     * The value is in degrees and rotation is clockwise.
     */
    private final int rotationOffsetOff;

    /**
     * The images of the robot in all four rotations and turned on and off.
     */
    private final @NotNull BufferedImage[] images = new BufferedImage[8];

    /**
     * Creates a new {@link SvgBasedRobotFamily} with a rotation offset of 0.
     *
     * @param name       the name of the robot family
     * @param svgPathOn  the path to the SVG image of the robot when it is turned on
     * @param svgPathOff the path to the SVG image of the robot when it is turned off
     * @param color      the color of the robot family
     */
    public SvgBasedRobotFamily(
        final @NotNull String name,
        final @NotNull String svgPathOn,
        final @NotNull String svgPathOff,
        final @NotNull Color color
    ) {
        this(name, svgPathOn, svgPathOff, color, 0, 0);
    }

    /**
     * Creates a new {@link SvgBasedRobotFamily}.
     *
     * @param name              the name of the robot family
     * @param svgPathOn         the path to the SVG image of the robot when it is turned on
     * @param svgPathOff        the path to the SVG image of the robot when it is turned off
     * @param color             the color of the robot family
     * @param rotationOffsetOn  the base rotation of the "on" images from the SVG file
     * @param rotationOffsetOff the base rotation of the "off" images from the SVG file
     */
    public SvgBasedRobotFamily(
        final @NotNull String name,
        final @NotNull String svgPathOn,
        final @NotNull String svgPathOff,
        final @NotNull Color color,
        final int rotationOffsetOn,
        final int rotationOffsetOff
    ) {
        this.name = name;
        this.svgPathOn = svgPathOn;
        this.svgPathOff = svgPathOff;
        this.color = color;
        this.rotationOffsetOn = rotationOffsetOn;
        this.rotationOffsetOff = rotationOffsetOff;
    }

    @Override
    public void setColor(final @NotNull Color color) {
        throw new UnsupportedOperationException("SVG based RobotFamilies do not support color changes.");
    }

    /**
     * Returns the correct image for the given rotation offset and turned on state.
     *
     * @param rotationOffset the rotation offset in degrees. Rounds to the multiple of 90 degrees.
     * @param turnedOff      whether the robot is turned off
     *
     * @return the correct image
     */
    public BufferedImage getImage(final int rotationOffset, final boolean turnedOff) {
        return images[rotationOffset / 90 + (turnedOff ? 4 : 0)];
    }

    @Override
    public BufferedImage render(
        final int targetSize,
        final int rotationOffset,
        final boolean turnedOff
    ) {
        if (targetSize == imageSize && images.length == 8) {
            return getImage(rotationOffset, turnedOff);
        }
        // render the SVG images in all four rotations

        System.arraycopy(
            PaintUtils.loadScaleRotateFieldImage(
                getClass().getResourceAsStream(svgPathOn),
                rotationOffsetOn,
                targetSize
            ),
            0,
            this.images,
            0,
            4
        );
        System.arraycopy(
            PaintUtils.loadScaleRotateFieldImage(
                getClass().getResourceAsStream(svgPathOff),
                rotationOffsetOff,
                targetSize
            ),
            0,
            this.images,
            4,
            4
        );

        imageSize = targetSize;
        return getImage(rotationOffset, turnedOff);
    }

    @Override
    public String toString() {
        return getName();
    }
}
