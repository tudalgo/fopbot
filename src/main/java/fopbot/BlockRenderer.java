package fopbot;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

/**
 * Renderer for {@link Block} entities.
 */
class BlockRenderer implements FieldEntityRenderer<Block> {

    @Override
    public void draw(Graphics g, FieldEntityRenderContext<Block> context) {
      final  Color oldColor = g.getColor();
  final      ColorProfile colorProfile = context.colorProfile();
   final     Point upperLeft = context.upperLeft();

        g.setColor(colorProfile.getBlockColor());
        final int size = colorProfile.fieldInnerSize() - colorProfile.fieldInnerOffset() * 2;
        g.fillRect(scale(upperLeft.x, context), scale(upperLeft.y, context), scale(size, context), scale(size, context));

        g.setColor(oldColor);
    }
}
