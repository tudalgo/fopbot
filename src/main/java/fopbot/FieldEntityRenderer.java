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
 * A renderer interface for drawing {@link FieldEntity} instances.
 *
 * <p>This interface provides methods for scaling and rendering {@link FieldEntity} objects.
 * Implementations of this interface should handle specific rendering logic for each type
 * of {@link FieldEntity}.
 *
 * @param <E> the type of {@link FieldEntity} that this renderer handles
 */
public interface FieldEntityRenderer<E extends FieldEntity> {

    /**
     * Scales a numeric value based on the provided {@link FieldEntityRenderContext}.
     *
     * @param value   the value to scale
     * @param context the {@link FieldEntityRenderContext} providing the scale factor
     *
     * @return the scaled value
     */
    default double scale(double value, FieldEntityRenderContext<E> context) {
        return value * context.scaleFactor();
    }

    /**
     * Scales a numeric value (float) based on the provided {@link FieldEntityRenderContext}.
     *
     * @param value   the value to scale
     * @param context the {@link FieldEntityRenderContext} providing the scale factor
     *
     * @return the scaled value
     */
    default float scale(float value, FieldEntityRenderContext<E> context) {
        return (float) (value * context.scaleFactor());
    }

    /**
     * Scales a numeric value (int) based on the provided {@link FieldEntityRenderContext}.
     *
     * @param value   the value to scale
     * @param context the {@link FieldEntityRenderContext} providing the scale factor
     *
     * @return the scaled value
     */
    default int scale(int value, FieldEntityRenderContext<E> context) {
        return (int) (value * context.scaleFactor());
    }

    /**
     * Scales a {@link Rectangle2D} based on the provided {@link FieldEntityRenderContext}.
     *
     * @param rect    the {@link Rectangle2D} to scale
     * @param context the {@link FieldEntityRenderContext} providing the scale factor
     *
     * @return a new scaled {@link Rectangle2D}
     */
    default Rectangle2D scale(final Rectangle2D rect, FieldEntityRenderContext<E> context) {
        double scaleFactor = context.scaleFactor();
        return new Rectangle2D.Double(
            rect.getX() * scaleFactor,
            rect.getY() * scaleFactor,
            rect.getWidth() * scaleFactor,
            rect.getHeight() * scaleFactor
        );
    }

    /**
     * Scales a text to fit within a specified width and bounds.
     *
     * <p>This method resizes the text so that it fits within the specified width, adjusting the font size
     * if necessary. It also ensures that the text is centered within the provided bounds.
     *
     * @param g2d             the {@link Graphics2D} object used for rendering
     * @param bounds          the bounding {@link Rectangle} within which the text should fit
     * @param width           the target width to scale the text to
     * @param borderWidth     the border width around the text (to exclude from scaling)
     * @param text            the text to scale
     * @param f               the {@link Font} used for the text
     * @param scaleEvenIfFits whether to scale even if the text already fits within the width
     *
     * @return the scaled {@link Shape} representing the text outline
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
     * Draws the {@link FieldEntity} onto the provided {@link Graphics} object.
     *
     * @param g       the {@link Graphics} object used to draw the entity
     * @param context the {@link FieldEntityRenderContext} providing additional drawing context
     */
    void draw(Graphics g, FieldEntityRenderContext<E> context);
}
