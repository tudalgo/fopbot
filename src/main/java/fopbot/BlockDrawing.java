package fopbot;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

/**
 * Defines the drawing for a {@link Block} on the screen.
 */
public class BlockDrawing implements Drawable<Block> {

    @Override
    public void draw(Graphics g, DrawingContext<Block> context) {
        Color oldColor = g.getColor();
        ColorProfile profile = context.colorProfile();
        Point upperLeftCorner = context.upperLeftCorner();

        g.setColor(profile.getBlockColor());

        int rawSize = profile.fieldInnerSize() - profile.fieldInnerOffset() * 2;
        int scaledSize = scale(rawSize, context);

        g.fillRect(
            scale(upperLeftCorner.x, context),
            scale(upperLeftCorner.y, context),
            scaledSize,
            scaledSize
        );

        g.setColor(oldColor);
    }
}
