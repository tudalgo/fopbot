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
    public void draw(Graphics g, DrawingContext<Coin> context) {
        Graphics2D g2d = (Graphics2D) g;
        Coin coin = context.entity();
        Rectangle2D fieldBounds = scale(getFieldBounds(coin, context.world()), context);
        ColorProfile profile = context.colorProfile();
        boolean isRobotOnField = context.field().contains(Robot.class);
        Color oldColor = g.getColor();

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

    private void drawCoinCount(Graphics2D g2d, DrawingContext<Coin> context, int count,
                               Rectangle2D fieldBounds, boolean isRobotOnField) {
        ColorProfile profile = context.colorProfile();
        double borderWidth = scale((double) profile.fieldBorderThickness(), context);
        double padding = scale((double) profile.fieldInnerOffset(), context);
        double wantedSize = isRobotOnField ? scale(20d, context) : fieldBounds.getWidth();

        Point2D center = isRobotOnField
            ? new Point2D.Double(fieldBounds.getMaxX() - wantedSize / 2d, fieldBounds.getY() + wantedSize / 2d)
            : new Point2D.Double(fieldBounds.getCenterX(), fieldBounds.getCenterY());

        String text = Integer.toString(count);
        Font font = g2d.getFont().deriveFont((float) scale(16d, context));
        Shape scaledText = scaleTextToWidth(
            g2d, context.bounds(), wantedSize, borderWidth + padding, text, font, false
        );

        // Center text
        AffineTransform at = AffineTransform.getTranslateInstance(
            center.getX() - scaledText.getBounds2D().getCenterX(),
            center.getY() - scaledText.getBounds2D().getCenterY()
        );
        Shape textShape = at.createTransformedShape(scaledText);

        if (isRobotOnField) {
            // Draw overlay circle
            double radius = wantedSize / 2d;
            Ellipse2D.Double ellipse = new Ellipse2D.Double(
                center.getX() - radius, center.getY() - radius, wantedSize, wantedSize
            );

            g2d.setColor(profile.getCoinColor());
            g2d.fill(ellipse);

            g2d.setColor(Color.BLACK);
            Stroke oldStroke = g2d.getStroke();
            g2d.setStroke(new BasicStroke(scale(2, context)));
            g2d.draw(ellipse);
            g2d.setStroke(oldStroke);
        }

        g2d.setColor(Color.BLACK);
        g2d.fill(textShape);
    }
}
