package fopbot;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

/**
 * Defines the drawing for a {@link Block} on the screen.
 */
public class BlockDrawing implements Drawable<Block> {

    @Override
    public void draw(Graphics g, DrawingContext<? extends Block> context) {
        final Color oldColor = g.getColor();
        final ColorProfile profile = context.colorProfile();
        final Point upperLeft = context.upperLeftCorner();

        g.setColor(profile.getBlockColor());

        final int rawSize = profile.fieldInnerSize() - profile.fieldInnerOffset() * 2;
        final int scaledSize = scale(rawSize, context);

        g.fillRect(
            scale(upperLeft.x, context),
            scale(upperLeft.y, context),
            scaledSize,
            scaledSize
        );

        g.setColor(oldColor);
    }
}
