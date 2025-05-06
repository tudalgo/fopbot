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
        final ColorProfile profile = context.colorProfile();
        final Point upperLeft = context.upperLeftCorner();

        final int offset = profile.fieldInnerOffset();
        final int thickness = profile.fieldBorderThickness();
        final int innerSize = profile.fieldInnerSize();

        g.setColor(profile.getWallColor());

        int x;
        int y;
        int width;
        int height;
        if (context.entity().isHorizontal()) {
            x = upperLeft.x - offset * 2;
            y = upperLeft.y - offset - thickness;
            width = innerSize + offset * 2;
            height = thickness;
        } else {
            x = upperLeft.x - offset + innerSize;
            y = upperLeft.y - offset * 2;
            width = thickness;
            height = innerSize + offset * 2;
        }
        g.fillRect(
            scale(x, context),
            scale(y, context),
            scale(width, context),
            scale(height, context)
        );

        g.setColor(oldColor);
    }

}
