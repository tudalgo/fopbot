package fopbot;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

/**
 * Defines the drawing for a {@link Wall} on the screen.
 */
public class WallDrawing implements Drawable<Wall> {

    @Override
    public void draw(Graphics g, DrawingContext<Wall> context) {
        final Color oldColor = g.getColor();
        final ColorProfile colorProfile = context.colorProfile();
        final Point upperLeft = context.upperLeftCorner();

        g.setColor(colorProfile.getWallColor());
        if (context.entity().isHorizontal()) {
            final int x = upperLeft.x - colorProfile.fieldInnerOffset() * 2;
            final int y = upperLeft.y - colorProfile.fieldInnerOffset() - colorProfile.fieldBorderThickness();
            g.fillRect(
                scale(x, context),
                scale(y, context),
                scale(colorProfile.fieldInnerSize() + colorProfile.fieldInnerOffset() * 2, context),
                scale(colorProfile.fieldBorderThickness(), context)
            );
        } else {
            final int x = upperLeft.x - colorProfile.fieldInnerOffset() + colorProfile.fieldInnerSize();
            final int y = upperLeft.y - colorProfile.fieldInnerOffset() * 2;
            g.fillRect(
                scale(x, context),
                scale(y, context),
                scale(colorProfile.fieldBorderThickness(), context),
                scale(colorProfile.fieldInnerSize() + colorProfile.fieldInnerOffset() * 2, context)
            );
        }

        g.setColor(oldColor);
    }
}
