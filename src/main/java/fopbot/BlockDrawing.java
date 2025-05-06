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
        final Color oldColor = g.getColor();
        final ColorProfile colorProfile = context.colorProfile();
        final Point upperLeftCorner = context.upperLeftCorner();

        // Set the color for the block
        g.setColor(colorProfile.getBlockColor());

        // Calculate the size of the block based on the color profile
        final int size = colorProfile.fieldInnerSize() - colorProfile.fieldInnerOffset() * 2;

        // Draw the block as a filled rectangle
        g.fillRect(
            scale(upperLeftCorner.x, context),
            scale(upperLeftCorner.y, context),
            scale(size, context),
            scale(size, context)
        );

        // Restore the original color of the graphics object
        g.setColor(oldColor);
    }
}
