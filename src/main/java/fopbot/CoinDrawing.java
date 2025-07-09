package fopbot;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

import static fopbot.PaintUtils.getFieldBounds;

/**
 * Defines the drawing for a {@link Coin} on the screen.
 */
public class CoinDrawing implements Drawable<Coin> {

    @Override
    public void draw(Graphics g, DrawingContext<? extends Coin> context) {
        final Graphics2D g2d = (Graphics2D) g;
        final Coin coin = context.entity();
        final Rectangle2D fieldBounds = scale(getFieldBounds(coin, context.world()), context);
        final ColorProfile profile = context.colorProfile();
        final boolean isRobotOnField = context.field().containsEntity(Robot.class);
        final Color oldColor = g.getColor();

        if (!isRobotOnField) {
            g.setColor(profile.getCoinColor());
            g2d.fill(new Ellipse2D.Double(
                fieldBounds.getX(), fieldBounds.getY(),
                fieldBounds.getWidth(), fieldBounds.getHeight()
            ));
        }

        drawCoinCount(g2d, context, coin.getCount(), fieldBounds, isRobotOnField);

        g.setColor(oldColor);
    }

    /**
     * Draws the coin count on the field.
     *
     * @param g2d            the graphics context to draw on
     * @param context        the drawing context containing the coin and field information
     * @param count          the number of coins to display
     * @param fieldBounds    the bounds of the field where the coin is located
     * @param isRobotOnField true if a robot is currently on the field, false otherwise
     */
    private void drawCoinCount(Graphics2D g2d, DrawingContext<? extends Coin> context, int count,
                               Rectangle2D fieldBounds, boolean isRobotOnField) {
        final ColorProfile profile = context.colorProfile();
        final double borderWidth = scale((double) profile.fieldBorderThickness(), context);
        final double padding = scale((double) profile.fieldInnerOffset(), context);
        final double wantedSize = isRobotOnField ? scale(20d, context) : fieldBounds.getWidth();

        final Point2D center = isRobotOnField
            ? new Point2D.Double(fieldBounds.getMaxX() - wantedSize / 2d, fieldBounds.getY() + wantedSize / 2d)
            : new Point2D.Double(fieldBounds.getCenterX(), fieldBounds.getCenterY());

        final String text = Integer.toString(count);
        final Font font = g2d.getFont().deriveFont((float) scale(16d, context));
        final Shape scaledText = scaleTextToWidth(
            g2d, context.bounds(), wantedSize, borderWidth + padding, text, font, false
        );

        // Center text
        final AffineTransform at = AffineTransform.getTranslateInstance(
            center.getX() - scaledText.getBounds2D().getCenterX(),
            center.getY() - scaledText.getBounds2D().getCenterY()
        );
        final Shape textShape = at.createTransformedShape(scaledText);

        if (isRobotOnField) {
            // Draw overlay circle
            final double radius = wantedSize / 2d;
            final Ellipse2D.Double ellipse = new Ellipse2D.Double(
                center.getX() - radius, center.getY() - radius, wantedSize, wantedSize
            );

            g2d.setColor(profile.getCoinColor());
            g2d.fill(ellipse);

            g2d.setColor(Color.BLACK);
            final Stroke oldStroke = g2d.getStroke();
            g2d.setStroke(new BasicStroke(scale(2, context)));
            g2d.draw(ellipse);
            g2d.setStroke(oldStroke);
        }

        g2d.setColor(Color.BLACK);
        g2d.fill(textShape);
    }
}
