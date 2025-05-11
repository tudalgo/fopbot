package fopbot;

import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

/**
 * This interface is responsible for drawing a {@link FieldEntity} on the screen.
 * It provides methods to scale values and shapes based on a context and includes functionality for drawing.
 *
 * @param <E> the type of {@link FieldEntity} to be drawn
 */
@FunctionalInterface
public interface Drawable<E extends FieldEntity> {

    /**
     * Scales a given value based on the provided drawing context's scale factor.
     *
     * @param value   the value to be scaled
     * @param context the context containing scale factor information
     *
     * @return the scaled value
     */
    default double scale(double value, DrawingContext<? extends E> context) {
        return value * context.scaleFactor();
    }

    /**
     * Scales a given value based on the provided drawing context's scale factor.
     *
     * @param value   the value to be scaled
     * @param context the context containing scale factor information
     *
     * @return the scaled value
     */
    default float scale(float value, DrawingContext<? extends E> context) {
        return (float) (value * context.scaleFactor());
    }

    /**
     * Scales a given value based on the provided drawing context's scale factor.
     *
     * @param value   the value to be scaled
     * @param context the context containing scale factor information
     *
     * @return the scaled value
     */
    default int scale(int value, DrawingContext<? extends E> context) {
        return (int) (value * context.scaleFactor());
    }

    /**
     * Scales the given rectangle to the scale factor defined in the drawing context.
     *
     * @param rect    the rectangle to be scaled
     * @param context the context containing scale factor information
     *
     * @return a scaled {@link Rectangle2D} object
     */
    default Rectangle2D scale(final Rectangle2D rect, DrawingContext<? extends E> context) {
        double scaleFactor = context.scaleFactor();
        return new Rectangle2D.Double(
            rect.getX() * scaleFactor,
            rect.getY() * scaleFactor,
            rect.getWidth() * scaleFactor,
            rect.getHeight() * scaleFactor
        );
    }

    /**
     * Scales the given text to fit within the specified width and bounds while maintaining the aspect ratio.
     * The scaling ensures that the text fits within the defined width, optionally adjusting if it already fits.
     *
     * @param g2d             the graphics object used to measure and transform the text
     * @param bounds          the bounding rectangle for the text
     * @param width           the target width for the text
     * @param borderWidth     the border width around the text
     * @param text            the text to be scaled
     * @param f               the font used for the text
     * @param scaleEvenIfFits determines whether to scale the text even if it fits within the bounds
     *
     * @return a scaled {@link Shape} representing the text outline
     */
    default Shape scaleTextToWidth(
        Graphics2D g2d,
        Rectangle bounds,
        double width,
        double borderWidth,
        String text,
        Font f,
        boolean scaleEvenIfFits
    ) {
        // Store current g2d Configuration
        final Font oldFont = g2d.getFont();

        // graphics configuration
        g2d.setFont(f);

        // Prepare Shape creation
        final TextLayout tl = new TextLayout(text, f, g2d.getFontRenderContext());
        final Rectangle2D fontBounds = f.createGlyphVector(
            g2d.getFontRenderContext(),
            text
        ).getVisualBounds();

        // Calculate scale Factor
        final double factor = (width - borderWidth) / fontBounds.getWidth();

        if (!scaleEvenIfFits && factor >= 1) {
            // Restore graphics configuration
            g2d.setFont(oldFont);
            return tl.getOutline(null);
        }

        // Transform
        final AffineTransform tf = new AffineTransform();
        tf.scale(factor, factor);

        // Center
        tf.translate(
            bounds.getCenterX() / factor - fontBounds.getCenterX(),
            bounds.getCenterY() / factor - fontBounds.getCenterY()
        );
        final Shape outline = tl.getOutline(tf);

        // Restore graphics configuration
        g2d.setFont(oldFont);
        return outline;
    }

    /**
     * Draws the {@link FieldEntity} on the provided {@link Graphics} object.
     *
     * @param g       the graphics object on which to draw
     * @param context the context containing information about the entity and drawing environment
     */
    void draw(Graphics g, DrawingContext<? extends E> context);
}
