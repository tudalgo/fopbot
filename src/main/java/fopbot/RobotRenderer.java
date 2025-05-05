package fopbot;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

/**
 * Renderer for {@link Robot} entities.
 */
public class RobotRenderer implements FieldEntityRenderer<Robot> {

    @Override
    public void draw(Graphics g, FieldEntityRenderContext<Robot> context) {
        Robot r = context.entity();

        if (r.isTurnedOff() && !context.world().isDrawTurnedOffRobots()) {
            return;
        }

        ColorProfile colorProfile = context.colorProfile();
        Point upperLeft = context.upperLeft();

        final int directionIndex = r.getDirection().ordinal();
        final int targetRobotImageSize = scale(colorProfile.fieldInnerSize() - colorProfile.fieldInnerOffset() * 2, context);
        final Image robotImage = r.getRobotFamily().render(
            targetRobotImageSize,
            directionIndex * 90,
            r.isTurnedOff()
        );
        g.drawImage(robotImage, scale(upperLeft.x, context), scale(upperLeft.y, context), null);
    }
}
