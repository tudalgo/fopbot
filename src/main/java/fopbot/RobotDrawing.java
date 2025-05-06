package fopbot;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

/**
 * Defines the drawing for a {@link Robot} on the screen.
 */
public class RobotDrawing implements Drawable<Robot> {

    @Override
    public void draw(Graphics g, DrawingContext<Robot> context) {
        final Robot r = context.entity();

        if (r.isTurnedOff() && !context.world().isDrawTurnedOffRobots()) {
            return;
        }

        final ColorProfile colorProfile = context.colorProfile();
        final Point upperLeftCorner = context.upperLeftCorner();

        final int directionIndex = r.getDirection().ordinal();
        final int targetRobotImageSize = scale(colorProfile.fieldInnerSize() - colorProfile.fieldInnerOffset() * 2, context);
        final Image robotImage = r.getRobotFamily().render(
            targetRobotImageSize,
            directionIndex * 90,
            r.isTurnedOff()
        );
        g.drawImage(robotImage, scale(upperLeftCorner.x, context), scale(upperLeftCorner.y, context), null);
    }
}
