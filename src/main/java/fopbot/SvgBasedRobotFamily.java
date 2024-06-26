package fopbot;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * A robot family that is based on SVG images.
 */
@Getter
@RequiredArgsConstructor
public class SvgBasedRobotFamily implements RobotFamily {
    /**
     * The name of this {@link RobotFamily}.
     */
    private final String name;

    /**
     * The path to the SVG image of the robot when it is turned on.
     */
    private final String svgPathOn;

    /**
     * The path to the SVG image of the robot when it is turned off.
     */
    private final String svgPathOff;

    /**
     * The color of this {@link RobotFamily}.
     */
    private final Color color;

    /**
     * The size of the last rendered image.
     */
    private int imageSize = -1;

    private final BufferedImage[] images = new BufferedImage[8];

    @Override
    public void setColor(final Color color) {
        throw new UnsupportedOperationException("SVG based RobotFamilies do not support color changes.");
    }

    /**
     * Returns the correct image for the given rotation offset and turned on state.
     *
     * @param rotationOffset the rotation offset in degrees. Rounds to the multiple of 90 degrees.
     * @param turnedOff      whether the robot is turned off
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
                0,
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
                0,
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
}
