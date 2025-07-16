package fopbot;

import com.jthemedetecor.OsThemeDetector;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import static fopbot.PaintUtils.getBoardSize;
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
     */
    protected void draw(final @NotNull Graphics g) {
        drawBoard(g);
        final var config = world.getDrawingRegistry();
        final var drawingOrder = config.order();
        final Rectangle bounds = getBounds();
        final ColorProfile colorProfile = getColorProfile();
        world.getFields().forEach(field -> {
            final List<FieldEntity> sorted = new ArrayList<>(field.getEntities());
            sorted.sort(drawingOrder);
            sorted.forEach(entity -> {
                final DrawingContext<FieldEntity> context = new DrawingContext<>(
                    entity,
                    world,
                    bounds,
                    colorProfile,
                    scaleFactor,
                    field
                );
                @SuppressWarnings("unchecked") final Drawable<FieldEntity> drawable =
                    (Drawable<FieldEntity>) config.getDrawing(entity.getClass());
                drawable.draw(g, context);
            });
        });
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
     *
     * @return the scaled value
     */
    public double scale(final double value) {
        return value * scaleFactor;
    }

    /**
     * Scales the given value with the current scale factor.
     *
     * @param value the value to scale
     *
     * @return the scaled value
     */
    public float scale(final float value) {
        return (float) (value * scaleFactor);
    }

    /**
     * Scales the given value with the current scale factor.
     *
     * @param value the value to scale
     *
     * @return the scaled value
     */
    public int scale(final int value) {
        return (int) (value * scaleFactor);
    }

    /**
     * Returns a new {@link Rectangle2D} that is a scaled version of the given {@link Rectangle2D}.
     *
     * @param rect the {@link Rectangle2D} to scale
     *
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
