package fopbot;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import static fopbot.PaintUtils.getFieldBounds;

/**
 * Renderer for {@link Coin} entities.
 */
public class CoinRenderer implements FieldEntityRenderer<Coin> {

    @Override
    public void draw(Graphics g, FieldEntityRenderContext<Coin> context) {
        Color oldColor = g.getColor();
        var g2d = (Graphics2D) g;
        Coin c = context.entity();
        var text = Integer.toString(c.getCount());
        Rectangle2D fieldBounds = scale(getFieldBounds(c, context.world()), context);
        ColorProfile colorProfile = context.colorProfile();
        boolean isRobotOnField = context.isRobotOnField();
        g.setColor(colorProfile.getCoinColor());

        g.setColor(colorProfile.getCoinColor());
        if (!isRobotOnField) {
            g2d.fill(
                new Ellipse2D.Double(
                    fieldBounds.getX(),
                    fieldBounds.getY(),
                    fieldBounds.getWidth(),
                    fieldBounds.getHeight()
                )
            );
        }

        // Draw the count of the coin in the middle of the coin
        final double borderWidth = scale((double) colorProfile.fieldBorderThickness(), context);
        final double padding = scale((double) colorProfile.fieldInnerOffset(), context);
        final double wantedSize = isRobotOnField ? scale(20d, context) : fieldBounds.getWidth();
        final Point2D wantedCenter =
            isRobotOnField ? new Point2D.Double(
                fieldBounds.getMaxX() - wantedSize / 2d,
                fieldBounds.getY() + wantedSize / 2d
            ) : new Point2D.Double(
                fieldBounds.getCenterX(),
                fieldBounds.getCenterY()
            );

        final var scaledText = scaleTextToWidth(
            g2d,
            context.bounds(),
            wantedSize,
            borderWidth + padding,
            text,
            g.getFont().deriveFont((float) scale(16d, context)),
            false
        );

        // Center text
        final var at = new AffineTransform();
        at.translate(
            wantedCenter.getX() - scaledText.getBounds2D().getCenterX(),
            wantedCenter.getY() - scaledText.getBounds2D().getCenterY()
        );
        final var textShape = at.createTransformedShape(scaledText);

        if (isRobotOnField) {
            // Draw white box background
            g.setColor(colorProfile.getCoinColor());
            g2d.fill(
                new Ellipse2D.Double(
                    wantedCenter.getX() - wantedSize / 2d,
                    wantedCenter.getY() - wantedSize / 2d,
                    wantedSize,
                    wantedSize
                )
            );
            g.setColor(Color.BLACK);
            final var oldStroke = g2d.getStroke();
            g2d.setStroke(new BasicStroke(scale(2, context)));
            g2d.draw(
                new Ellipse2D.Double(
                    wantedCenter.getX() - wantedSize / 2d,
                    wantedCenter.getY() - wantedSize / 2d,
                    wantedSize,
                    wantedSize
                )
            );
            g2d.setStroke(oldStroke);
            g.setColor(Color.BLACK);
        }

        // Draw the text
        g.setColor(Color.BLACK);
        g2d.fill(textShape);

        g.setColor(oldColor);
    }
}
