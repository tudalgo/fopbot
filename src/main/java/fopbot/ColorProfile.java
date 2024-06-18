package fopbot;

import lombok.Builder;

import java.awt.Color;

/**
 * A color profile for drawing the world.
 *
 * @param backgroundColorLight  the background color in light mode
 * @param backgroundColorDark   the background color in dark mode
 * @param fieldColorLight       the default background color for {@link Field}s in light mode
 * @param fieldColorDark        the default background color for {@link Field}s in dark mode
 * @param outerBorderColorLight the color of the outer border in light mode
 * @param outerBorderColorDark  the color of the outer border in dark mode
 * @param innerBorderColorLight the color of the inner border in light mode (not wall)
 * @param InnerBorderColorDark  the color of the inner border in dark mode (not wall)
 * @param wallColorLight        the color of {@link Wall}s in light mode
 * @param wallColorDark         the color of {@link Wall}s in dark mode
 * @param coinColorLight        the color of {@link Coin}s in light mode
 * @param coinColorDark         the color of {@link Coin}s in dark mode
 * @param blockColorLight       the color of {@link Block}s in light mode
 * @param blockColorDark        the color of {@link Block}s in dark mode
 */
@Builder(toBuilder = true)
public record ColorProfile(
    Color backgroundColorLight,
    Color backgroundColorDark,
    Color fieldColorLight,
    Color fieldColorDark,
    Color outerBorderColorLight,
    Color outerBorderColorDark,
    Color innerBorderColorLight,
    Color InnerBorderColorDark,
    Color wallColorLight,
    Color wallColorDark,
    Color coinColorLight,
    Color coinColorDark,
    Color blockColorLight,
    Color blockColorDark
) {
    /**
     * The default color profile.
     */
    public static ColorProfile DEFAULT = new ColorProfile(
        // Background
        Color.WHITE,
        Color.BLACK,
        // Field
        Color.LIGHT_GRAY,
        new Color(25, 25, 30),
        // Outer border
        Color.BLACK,
        Color.WHITE,
        // Inner border
        Color.GRAY,
        new Color(60, 60, 60),
        // Wall
        Color.BLACK,
        Color.WHITE,
        // Coin
        Color.RED,
        new Color(255, 140, 26),
        // Block
        Color.BLACK,
        Color.WHITE
    );

    /**
     * Returns {@code true} if the world is in dark mode.
     *
     * @return {@code true} if the world is in dark mode
     * @see GuiPanel#isDarkMode()
     */
    private boolean isDarkMode() {
        return World.getGlobalWorld().getGuiPanel().isDarkMode();
    }

    /**
     * Returns the background color of the world.
     *
     * @return the background color of the world
     */
    public Color getBackgroundColor() {
        return isDarkMode() ? backgroundColorDark : backgroundColorLight;
    }

    /**
     * Returns the default background color for {@link Field}s.
     *
     * @return the default background color for {@link Field}s
     */
    public Color getFieldColor() {
        return isDarkMode() ? fieldColorDark : fieldColorLight;
    }

    /**
     * Returns the color of the outer border.
     *
     * @return the color of the outer border
     */
    public Color getOuterBorderColor() {
        return isDarkMode() ? outerBorderColorDark : outerBorderColorLight;
    }

    /**
     * Returns the color of the inner border.
     *
     * @return the color of the inner border
     */
    public Color getInnerBorderColor() {
        return isDarkMode() ? InnerBorderColorDark : innerBorderColorLight;
    }

    /**
     * Returns the color of {@link Wall}s.
     *
     * @return the color of {@link Wall}s
     */
    public Color getWallColor() {
        return isDarkMode() ? wallColorDark : wallColorLight;
    }

    /**
     * Returns the color of {@link Coin}s.
     *
     * @return the color of {@link Coin}s
     */
    public Color getCoinColor() {
        return isDarkMode() ? coinColorDark : coinColorLight;
    }

    /**
     * Returns the color of {@link Block}s.
     *
     * @return the color of {@link Block}s
     */
    public Color getBlockColor() {
        return isDarkMode() ? blockColorDark : blockColorLight;
    }
}
