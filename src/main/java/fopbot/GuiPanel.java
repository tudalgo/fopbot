package fopbot;

import com.jthemedetecor.OsThemeDetector;
import org.jetbrains.annotations.ApiStatus;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.nio.file.Path;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import static fopbot.PaintUtils.getBoardSize;
import static fopbot.PaintUtils.getFieldBounds;
import static fopbot.PaintUtils.getUpperLeftCornerInField;
import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;

/**
 * The graphical user interface in which the FoPBot world is represented.
 */
@ApiStatus.Internal
public class GuiPanel extends JPanel {

    /**
     * The world that is to be displayed on the graphical user interface.
     */
    protected KarelWorld world;

    /**
     * The counter of how many screenshots were made.
     */
    protected long screenshotCounter = 0L;

    /**
     * The date of the first saved world as an image.
     */
    protected String startDate;

    /**
     * The input handler that handles the input of the user.
     */
    protected InputHandler inputHandler;

    /**
     * The current scale factor of the graphical user interface.
     */
    private double scaleFactor = 1.0;

    /**
     * The operating system theme detector.
     */
    final OsThemeDetector osThemeDetector = OsThemeDetector.getDetector();

    /**
     * Whether the dark mode is enabled.
     */
    private boolean darkMode = osThemeDetector.isDark();

    /**
     * Listeners for dark mode changes.
     */
    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);


    /**
     * Constructs and initializes graphical use interface to represent the FOP Bot world.
     *
     * @param world the FOP Bot world to represent on the graphical user interface
     */
    public GuiPanel(final KarelWorld world) {
        super();
        this.world = world;
        this.inputHandler = new InputHandler(this);
        setSize(getPreferredSize());
        setFocusable(true);

        // dark mode listener
        osThemeDetector.registerListener(isDark -> {
            if (isDark != darkMode) {
                toggleDarkMode();
            }
        });

        // default key bindings
        inputHandler.addKeyListener(new KeyAdapter() {
            /**
             * A set of keys that were pressed.
             */
            private final Set<Integer> keysWerePressed = new HashSet<>();

            @Override
            public void keyPressed(final KeyEvent e) {
                if (keysWerePressed.contains(e.getKeyCode())) {
                    return;
                }
                // toggle dark mode with F8
                if (e.getKeyCode() == KeyEvent.VK_F8) {
                    toggleDarkMode();
                    System.out.printf("%s dark mode%n", isDarkMode() ? "Enabled" : "Disabled");
                }
                // Record Screenshot with F2
                if (e.getKeyCode() == KeyEvent.VK_F2) {
                    try {
                        saveStateAsPng();
                    } catch (final RuntimeException ex) {
                        ex.printStackTrace();
                    }
                }

                keysWerePressed.add(e.getKeyCode());
            }

            @Override
            public void keyReleased(final KeyEvent e) {
                keysWerePressed.remove(e.getKeyCode());
            }
        });
    }

    /**
     * Returns the current {@link ColorProfile} that is used to draw the world.
     *
     * @return the current {@link ColorProfile} that is used to draw the world
     */
    public ColorProfile getColorProfile() {
        return world.getColorProfile();
    }

    /**
     * Sets the {@link ColorProfile} that is used to draw the world.
     *
     * @param colorProfile the {@link ColorProfile} that is used to draw the world
     */
    public void setColorProfile(final ColorProfile colorProfile) {
        world.setColorProfile(colorProfile);
    }

    /**
     * Returns the unscaled size of world board size.
     *
     * @return the unscaled size of world board size
     */
    protected Dimension getUnscaledSize() {
        final Point p = getBoardSize(world);
        int width = p.x;
        int height = p.y;
        width += 2 * getColorProfile().boardOffset();
        height += 2 * getColorProfile().boardOffset();
        return new Dimension(width, height);
    }

    @Override
    public Dimension getPreferredSize() {
        return getUnscaledSize();
    }

    /**
     * Saves the current world to an image (.png).
     *
     * @throws RuntimeException if the screenshot directory could not be created
     */
    protected void saveStateAsPng() {
        if (screenshotCounter == 0L) {
            final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            final Date date = new Date();
            startDate = dateFormat.format(date);
            final File dir = new File("screenshots/" + startDate);
            if (!dir.mkdirs()) {
                throw new RuntimeException("Could not create screenshot directory!");
            }
        }


        final StringBuilder state = new StringBuilder(Long.toString(screenshotCounter));
        while (state.length() != 4) {
            state.insert(0, "0");
        }

        final String imagePath = "screenshots/" + startDate + "/karel_" + state + ".png";
        screenshotCounter++;

        // get bounds of the world
        final Rectangle bounds = getScaledWorldBounds();
        BufferedImage image = new BufferedImage(bounds.width, bounds.height, BufferedImage.TYPE_INT_ARGB);
        final Graphics g = image.getGraphics();
        draw(g);
        g.dispose();
        image = image.getSubimage(
            scale(getColorProfile().boardOffset()),
            scale(getColorProfile().boardOffset()),
            scale(getBoardSize(world).x),
            scale(getBoardSize(world).y)
        );
        try {
            ImageIO.write(image, "png", new File(imagePath));
            System.out.println("Saved screenshot to " + Path.of(imagePath).toAbsolutePath());
        } catch (final IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    /**
     * Returns the scaled world bounds.
     *
     * @return the scaled world bounds
     */
    public Rectangle getScaledWorldBounds() {
        final Dimension unscaledSize = getUnscaledSize();
        final double targetAspectRatio = unscaledSize.getWidth() / unscaledSize.getHeight();
        final int width = (int) Math.min(getWidth(), getHeight() * targetAspectRatio);
        final int height = (int) Math.min(getHeight(), getWidth() / targetAspectRatio);
        this.scaleFactor = Math.min(
            (double) width / (double) unscaledSize.width,
            (double) height / (double) unscaledSize.height
        );
        final int x = (getWidth() - width) / 2;
        final int y = (getHeight() - height) / 2;
        return new Rectangle(x, y, width, height);
    }

    @Override
    public void paintComponent(final Graphics g) {
        final var bounds = getScaledWorldBounds();

        final BufferedImage image = new BufferedImage(
            bounds.width,
            bounds.height,
            BufferedImage.TYPE_INT_ARGB
        );
        final Graphics2D gImage = (Graphics2D) image.getGraphics();
        gImage.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
        draw(gImage);
        g.setColor(getColorProfile().getBackgroundColor());
        g.fillRect(0, 0, getWidth(), getHeight());
        g.drawImage(
            PaintUtils.scaleImageWithGPU(image, bounds.width, bounds.height),
            bounds.x,
            bounds.y,
            null
        );
    }

    /**
     * Draws all {@code FieldEntity} objects on the graphical user interface.
     *
     * @param g the {@code Graphics} context in which to paint
     * @see #drawBoard(Graphics)
     * @see #drawCoin(Coin, Graphics, boolean)
     * @see #drawBlock(Block, Graphics)
     * @see #drawWall(Wall, Graphics)
     * @see #drawRobot(Robot, Graphics)
     */
    protected void draw(final Graphics g) {
        drawBoard(g);

        final List<Field> fields = world.getFields();
        for (final Field f : fields) {
            // collect different entity types for layering
            final List<FieldEntity> entities = f.getEntities();
            final List<Robot> robots = new LinkedList<>();
            final List<Coin> coins = new LinkedList<>();
            final List<Block> blocks = new LinkedList<>();
            final List<Wall> walls = new LinkedList<>();

            for (final FieldEntity ce : entities) {
                if (ce instanceof final Robot r) {
                    robots.add(r);
                    continue;
                }
                if (ce instanceof final Coin c) {
                    coins.add(c);
                    continue;
                }
                if (ce instanceof final Block b) {
                    blocks.add(b);
                    continue;
                }
                if (ce instanceof final Wall w) {
                    walls.add(w);
                    continue;
                }
                System.err.println("Could not draw FieldEntity of Type: " + ce.getClass().getName());
            }

            // draw coins, but only if no robot is on the same field. If there is, we have to draw the coin later
            if (robots.isEmpty()) {
                for (final Coin c : coins) {
                    drawCoin(c, g, false);
                }
            }

            for (final Block b : blocks) {
                drawBlock(b, g);
            }

            for (final Wall w : walls) {
                drawWall(w, g);
            }

            for (final Robot r : robots) {
                drawRobot(r, g);
            }

            // draw coins, but only if a robot is on the same field
            if (!robots.isEmpty()) {
                for (final Coin c : coins) {
                    drawCoin(c, g, true);
                }
            }
        }
    }

    /**
     * Draws the world board with its fields (borders, fields).
     *
     * @param g the {@code Graphics} context in which to paint
     */
    protected void drawBoard(final Graphics g) {
        final var g2d = (Graphics2D) g;
        int width = getColorProfile().boardOffset();
        int height = getColorProfile().boardOffset();
        final Point p = getBoardSize(world);


        // draw inner borders
        width = getColorProfile().boardOffset() + getColorProfile().fieldBorderThickness();
        height = getColorProfile().boardOffset() + getColorProfile().fieldBorderThickness();
        g.setColor(getColorProfile().getInnerBorderColor());
        g2d.fill(
            new Rectangle2D.Double(
                scale(width),
                scale(height),
                scale(p.x - getColorProfile().fieldBorderThickness() * 2),
                scale(p.y - getColorProfile().fieldBorderThickness() * 2)
            )
        );

        // draw fields
        for (int h = 0; h < world.getHeight(); h++) {
            for (int w = 0; w < world.getWidth(); w++) {
                final var pos = new Point(w, h);
                g2d.setColor(getColorProfile().getFieldColor(pos));
                if (world.getField(w, World.getHeight() - h - 1).getFieldColor() != null) {
                    g2d.setColor(world.getField(w, World.getHeight() - h - 1).getFieldColor());
                }
                g2d.fill(
                    new Rectangle2D.Double(
                        scale(width),
                        scale(height),
                        scale(getColorProfile().fieldInnerSize()),
                        scale(getColorProfile().fieldInnerSize())
                    )
                );
                g2d.setColor(getColorProfile().getFieldColor(pos));

                if (h == 99) {
                    g2d.setColor(Color.GREEN);
                    g2d.drawString(width + ";" + height, width, height);
                    g2d.setColor(getColorProfile().getFieldColor(pos));
                }

                width += getColorProfile().fieldBorderThickness() + getColorProfile().fieldInnerSize();
            }
            width = getColorProfile().boardOffset() + getColorProfile().fieldBorderThickness();
            height += getColorProfile().fieldBorderThickness() + getColorProfile().fieldInnerSize();
        }

        // draw outer borders
        g2d.setColor(getColorProfile().getOuterBorderColor());
        final var oldStroke = g2d.getStroke();
        g2d.setStroke(new BasicStroke(scale((float) getColorProfile().fieldOuterBorderThickness())));
        g2d.draw(
            new Rectangle2D.Double(
                scale(
                    (double) getColorProfile().boardOffset() + getColorProfile().fieldBorderThickness()
                        - (double) getColorProfile().fieldOuterBorderThickness() / 2
                ),
                scale(
                    (double) getColorProfile().boardOffset() + getColorProfile().fieldBorderThickness()
                        - (double) getColorProfile().fieldOuterBorderThickness() / 2
                ),
                scale(
                    p.getX() - getColorProfile().fieldBorderThickness() * 2d
                        + getColorProfile().fieldOuterBorderThickness() - 1d
                ),
                scale(
                    p.getY() - getColorProfile().fieldBorderThickness() * 2d
                        + getColorProfile().fieldOuterBorderThickness() - 1d
                )
            )
        );
        g2d.setStroke(oldStroke);
    }

    /**
     * Draws the specified {@code Robot} on the graphical user interface.
     *
     * @param r the {@code Robot} to draw
     * @param g the {@code Graphics} context in which to paint
     */
    protected void drawRobot(final Robot r, final Graphics g) {
        if (r.isTurnedOff() && !world.isDrawTurnedOffRobots()) {
            return;
        }
        final Point upperLeft = getUpperLeftCornerInField(r, world);

        final int directionIndex = r.getDirection().ordinal();

        final int targetRobotImageSize = scale(getColorProfile().fieldInnerSize() - getColorProfile().fieldInnerOffset() * 2);

        final Image robotImage = r.getRobotFamily().render(
            targetRobotImageSize,
            directionIndex * 90,
            r.isTurnedOff()
        );
        g.drawImage(robotImage, scale(upperLeft.x), scale(upperLeft.y), null);

        final Color cBackup = g.getColor();

        g.setColor(cBackup);
    }

    /**
     * Create A shape with the desired Text and the desired width.
     *
     * @param g2d             the specified Graphics context to draw the font with
     * @param width           the desired text width
     * @param borderWidth     the border width to account for
     * @param text            the string to display
     * @param f               the font used for drawing the string
     * @param scaleEvenIfFits whether to scale the text even if it fits in the desired width
     * @return The Shape of the outline
     */
    public Shape scaleTextToWidth(
        final Graphics2D g2d,
        final double width,
        final double borderWidth,
        final String text,
        final Font f,
        final boolean scaleEvenIfFits
    ) {
        // Get current size
        final Rectangle bounds = getBounds();

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
     * Draws the specified {@code Coin} on the graphical user interface.
     *
     * @param c           the {@code Coin} to draw
     * @param g           the {@code Graphics} context in which to paint
     * @param evadeRobots whether to evade robots. Enable this if a robot is on the same field as the coin
     */
    protected void drawCoin(final Coin c, final Graphics g, final boolean evadeRobots) {
        final Color cBackup = g.getColor();
        final var g2d = (Graphics2D) g;
        final var text = Integer.toString(c.getCount());

        final Rectangle2D fieldBounds = scale(getFieldBounds(c, world));
        g.setColor(getColorProfile().getCoinColor());
        if (!evadeRobots) {
            g2d.fill(
                new Ellipse2D.Double(
                    fieldBounds.getX(),
                    fieldBounds.getY(),
                    fieldBounds.getWidth(),
                    fieldBounds.getHeight()
                )
            );
        }

        // draw the count of the coin in the middle of the coin
        final double borderWidth = scale((double) getColorProfile().fieldBorderThickness());
        final double padding = scale((double) getColorProfile().fieldInnerOffset());
        final double wantedSize = evadeRobots ? scale(20d) : fieldBounds.getWidth();
        final Point2D wantedCenter =
            evadeRobots ? new Point2D.Double(
                fieldBounds.getMaxX() - wantedSize / 2d,
                fieldBounds.getY() + wantedSize / 2d
            ) : new Point2D.Double(
                fieldBounds.getCenterX(),
                fieldBounds.getCenterY()
            );

        final var scaledText = scaleTextToWidth(
            g2d,
            wantedSize,
            borderWidth + padding,
            text,
            g.getFont().deriveFont((float) scale(16d)),
            false
        );
        // center text
        final var at = new AffineTransform();
        at.translate(
            wantedCenter.getX() - scaledText.getBounds2D().getCenterX(),
            wantedCenter.getY() - scaledText.getBounds2D().getCenterY()
        );
        final var textShape = at.createTransformedShape(scaledText);

        if (evadeRobots) {
            // draw white box background
            g.setColor(getColorProfile().getCoinColor());
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
            g2d.setStroke(new BasicStroke(scale(2)));
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
        // draw the string
        g.setColor(Color.BLACK);
        g2d.fill(textShape);

        g.setColor(cBackup);
    }

    /**
     * Draws the specified {@code Block} on the graphical user interface.
     *
     * @param b the {@code Block} to draw
     * @param g the {@code Graphics} context in which to paint
     */
    protected void drawBlock(final Block b, final Graphics g) {
        final Color cBackup = g.getColor();

        final Point upperLeft = getUpperLeftCornerInField(b, world);
        g.setColor(getColorProfile().getBlockColor());
        final int size = getColorProfile().fieldInnerSize() - getColorProfile().fieldInnerOffset() * 2;
        g.fillRect(scale(upperLeft.x), scale(upperLeft.y), scale(size), scale(size));

        g.setColor(cBackup);
    }

    /**
     * Draws the specified {@code Wall} on the graphical user interface.
     *
     * @param w the {@code Wall} to draw
     * @param g the {@code Graphics} context in which to paint
     */
    protected void drawWall(final Wall w, final Graphics g) {
        final Color cBackup = g.getColor();
        g.setColor(getColorProfile().getWallColor());

        final Point upperLeft = getUpperLeftCornerInField(w, world);
        if (w.isHorizontal()) {
            final int x = upperLeft.x - getColorProfile().fieldInnerOffset() * 2;
            final int y = upperLeft.y - getColorProfile().fieldInnerOffset() - getColorProfile().fieldBorderThickness();
            g.fillRect(
                scale(x),
                scale(y),
                scale(getColorProfile().fieldInnerSize() + getColorProfile().fieldInnerOffset() * 2),
                scale(getColorProfile().fieldBorderThickness())
            );
        } else {
            final int x = upperLeft.x - getColorProfile().fieldInnerOffset() + getColorProfile().fieldInnerSize();
            final int y = upperLeft.y - getColorProfile().fieldInnerOffset() * 2;
            g.fillRect(
                scale(x),
                scale(y),
                scale(getColorProfile().fieldBorderThickness()),
                scale(getColorProfile().fieldInnerSize() + getColorProfile().fieldInnerOffset() * 2)
            );
        }

        g.setColor(cBackup);
    }

    /**
     * Updates the content of the graphical user interface.
     */
    public void updateGui() {
        if (SwingUtilities.isEventDispatchThread()) {
            revalidate();
            repaint();
        } else {
            final Runnable r = () -> {
                revalidate();
                repaint();
            };
            try {
                SwingUtilities.invokeAndWait(r);
            } catch (final InvocationTargetException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        Toolkit.getDefaultToolkit().sync();
    }

    /**
     * Scales the given value with the current scale factor.
     *
     * @param value the value to scale
     * @return the scaled value
     */
    public double scale(final double value) {
        return value * scaleFactor;
    }

    /**
     * Scales the given value with the current scale factor.
     *
     * @param value the value to scale
     * @return the scaled value
     */
    public float scale(final float value) {
        return (float) (value * scaleFactor);
    }

    /**
     * Scales the given value with the current scale factor.
     *
     * @param value the value to scale
     * @return the scaled value
     */
    public int scale(final int value) {
        return (int) (value * scaleFactor);
    }

    /**
     * Returns a new {@link Rectangle2D} that is a scaled version of the given {@link Rectangle2D}.
     *
     * @param rect the {@link Rectangle2D} to scale
     * @return the scaled {@link Rectangle2D}
     */
    public Rectangle2D scale(final Rectangle2D rect) {
        return new Rectangle2D.Double(
            rect.getX() * scaleFactor,
            rect.getY() * scaleFactor,
            rect.getWidth() * scaleFactor,
            rect.getHeight() * scaleFactor
        );
    }

    /**
     * Gets the {@link InputHandler} of this {@code Gui}.
     *
     * @return the {@link InputHandler} of this {@code Gui}
     */
    public InputHandler getInputHandler() {
        return inputHandler;
    }

    /**
     * Returns whether the dark mode is enabled.
     *
     * @return true if the dark mode is enabled, false otherwise
     */
    public boolean isDarkMode() {
        return darkMode;
    }

    /**
     * Adds a listener for dark mode changes.
     *
     * @param listener the listener to add
     */
    public void addDarkModeChangeListener(final PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    /**
     * Sets whether the dark mode is enabled.
     *
     * @param darkMode true if the dark mode is enabled, false otherwise
     */
    public void setDarkMode(final boolean darkMode) {
        this.darkMode = darkMode;
        propertyChangeSupport.firePropertyChange("darkMode", !darkMode, darkMode);
        updateGui();
    }

    /**
     * Toggles the dark mode.
     */
    public void toggleDarkMode() {
        setDarkMode(!isDarkMode());
    }
}
