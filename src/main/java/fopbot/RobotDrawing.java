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
        final Robot robot = context.entity();

        if (robot.isTurnedOff() && !context.world().isDrawTurnedOffRobots()) {
            return;
        }

        final ColorProfile profile = context.colorProfile();
        final Point upperLeft = context.upperLeftCorner();

        final int rotationDegrees = robot.getDirection().ordinal() * 90;
        final int robotSize = scale(profile.fieldInnerSize() - profile.fieldInnerOffset() * 2, context);

        final Image robotImage = robot.getRobotFamily().render(
            robotSize,
            rotationDegrees,
            robot.isTurnedOff()
        );

        g.drawImage(
            robotImage,
            scale(upperLeft.x, context),
            scale(upperLeft.y, context),
            null
        );
    }

}
