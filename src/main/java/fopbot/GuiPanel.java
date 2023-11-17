package fopbot;

import org.jetbrains.annotations.ApiStatus;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
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
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import static fopbot.PaintUtils.BOARD_OFFSET;
import static fopbot.PaintUtils.FIELD_BORDER_THICKNESS;
import static fopbot.PaintUtils.FIELD_INNER_OFFSET;
import static fopbot.PaintUtils.FIELD_INNER_SIZE;
import static fopbot.PaintUtils.getBoardSize;
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
     * Whether the dark mode is enabled.
     */
    private boolean darkMode = false;

    /**
     * Constructs and initializes graphical use interface to represent the FOP Bot world.
     *
     * @param world the FOP Bot world to represent on the graphical user interface
     */
    public GuiPanel(final KarelWorld world) {
        this.world = world;
        this.inputHandler = new InputHandler(this);
        setSize(getPreferredSize());
        setFocusable(true);

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
     * Returns the unscaled size of world board size.
     *
     * @return the unscaled size of world board size
     */
    protected Dimension getUnscaledSize() {
        final Point p = getBoardSize(world);
        int width = p.x;
        int height = p.y;
        width += 2 * BOARD_OFFSET;
        height += 2 * BOARD_OFFSET;
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

        BufferedImage image = new BufferedImage(getUnscaledSize().width, getUnscaledSize().height, BufferedImage.TYPE_INT_ARGB);
        final Graphics g = image.getGraphics();
        draw(g);
        image = image.getSubimage(BOARD_OFFSET, BOARD_OFFSET,
                                  getUnscaledSize().width - BOARD_OFFSET * 2,
                                  getUnscaledSize().height - BOARD_OFFSET * 2
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
        g.setColor(darkMode ? Color.BLACK : Color.WHITE);
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
     * @see #drawCoin(Coin, Graphics)
     * @see #drawBlock(Block, Graphics)
     * @see #drawWall(Wall, Graphics)
     * @see #drawRobot(Robot, Graphics)
     */
    protected void draw(final Graphics g) {
        drawBoard(g);

        final List<FieldEntity> entities = world.getAllFieldEntities();
        final List<Robot> robots = new LinkedList<>();
        for (final FieldEntity ce : entities) {
            if (ce instanceof Robot) {
                // collect robots, so they can be drawn last
                // robots are always on top of every other field object
                robots.add((Robot) ce);
                continue;
            }

            if (ce instanceof Coin) {
                drawCoin((Coin) ce, g);
                continue;
            }

            if (ce instanceof Block) {
                drawBlock((Block) ce, g);
                continue;
            }

            if (ce instanceof Wall) {
                drawWall((Wall) ce, g);
                continue;
            }

            System.err.println("Could not draw FieldEntity of Type: " + ce.getClass().getName());
        }

        // draw robots last
        for (final Robot r : robots) {
            drawRobot(r, g);
        }
    }

    /**
     * Draws the world board with its fields (borders, fields).
     *
     * @param g the {@code Graphics} context in which to paint
     */
    protected void drawBoard(final Graphics g) {
        // draw outer borders
        int width = BOARD_OFFSET;
        int height = BOARD_OFFSET;
        g.setColor(darkMode ? Color.WHITE : Color.BLACK);
        final Point p = getBoardSize(world);
        g.fillRect(scale(width), scale(height), scale(p.x), scale(p.y));

        // draw inner borders
        width = BOARD_OFFSET + FIELD_BORDER_THICKNESS;
        height = BOARD_OFFSET + FIELD_BORDER_THICKNESS;
        g.setColor(darkMode ? new Color(60, 60, 60) : Color.GRAY);
        g.fillRect(
            scale(width),
            scale(height),
            scale(p.x - FIELD_BORDER_THICKNESS * 2),
            scale(p.y - FIELD_BORDER_THICKNESS * 2)
        );

        // draw fields
        g.setColor(darkMode ? new Color(25, 25, 30) : Color.LIGHT_GRAY);
        for (int h = 0; h < world.getHeight(); h++) {
            for (int w = 0; w < world.getWidth(); w++) {
                if (world.getField(w, World.getHeight() - h - 1).getFieldColor() != null) {
                    g.setColor(world.getField(w, World.getHeight() - h - 1).getFieldColor());
                }
                g.fillRect(scale(width), scale(height), scale(FIELD_INNER_SIZE), scale(FIELD_INNER_SIZE));
                g.setColor(darkMode ? new Color(25, 25, 30) : Color.LIGHT_GRAY);

                if (h == 99) {
                    g.setColor(Color.GREEN);
                    g.drawString(width + ";" + height, width, height);
                    g.setColor(darkMode ? new Color(25, 25, 30) : Color.LIGHT_GRAY);
                }

                width += FIELD_BORDER_THICKNESS + FIELD_INNER_SIZE;
            }
            width = BOARD_OFFSET + FIELD_BORDER_THICKNESS;
            height += FIELD_BORDER_THICKNESS + FIELD_INNER_SIZE;
        }
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
        final Point upperLeft = getUpperLeftCornerInField(r, world.getHeight());

        final int directionIndex = r.getDirection().ordinal();

        final int targetRobotImageSize = scale(FIELD_INNER_SIZE - FIELD_INNER_OFFSET * 2);
        if (world.getRobotImageSize() != targetRobotImageSize) {
            world.rescaleRobotImages(targetRobotImageSize);
        }

        final Map<String, Image[]> imageMapById = world.getRobotImageMapById(r.getRobotFamily().getIdentifier());
        Objects.requireNonNull(imageMapById, "robot image was not found");
        final Image robotImage = imageMapById.get(r.isTurnedOff() ? "off" : "on")[directionIndex];

        g.drawImage(robotImage, scale(upperLeft.x), scale(upperLeft.y), null);

        final Color cBackup = g.getColor();

        g.setColor(cBackup);
    }

    /**
     * Draws the specified {@code Coin} on the graphical user interface.
     *
     * @param c the {@code Coin} to draw
     * @param g the {@code Graphics} context in which to paint
     */
    protected void drawCoin(final Coin c, final Graphics g) {
        final Color cBackup = g.getColor();

        final Point upperLeft = getUpperLeftCornerInField(c, world.getHeight());
        g.setColor(darkMode ? new Color(255, 140, 26) : Color.RED);
        final int size = FIELD_INNER_SIZE - FIELD_INNER_OFFSET * 2;
        g.fillOval(scale(upperLeft.x), scale(upperLeft.y), scale(size), scale(size));
        g.setColor(Color.BLACK);
        g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, scale(16)));
        g.drawString(
            Integer.toString(c.getCount()),
            scale(upperLeft.x + size / 2),
            scale(upperLeft.y + size / 2)
        );

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

        final Point upperLeft = getUpperLeftCornerInField(b, world.getHeight());
        g.setColor(Color.BLACK);
        final int size = FIELD_INNER_SIZE - FIELD_INNER_OFFSET * 2;
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
        g.setColor(darkMode ? Color.WHITE : Color.BLACK);

        final Point upperLeft = getUpperLeftCornerInField(w, world.getHeight());
        if (w.isHorizontal()) {
            final int x = upperLeft.x - FIELD_INNER_OFFSET * 2;
            final int y = upperLeft.y - FIELD_INNER_OFFSET - FIELD_BORDER_THICKNESS;
            g.fillRect(
                scale(x),
                scale(y),
                scale(FIELD_INNER_SIZE + FIELD_INNER_OFFSET * 2),
                scale(FIELD_BORDER_THICKNESS)
            );
        } else {
            final int x = upperLeft.x - FIELD_INNER_OFFSET + FIELD_INNER_SIZE;
            final int y = upperLeft.y - FIELD_INNER_OFFSET * 2;
            g.fillRect(
                scale(x),
                scale(y),
                scale(FIELD_BORDER_THICKNESS),
                scale(FIELD_INNER_SIZE + FIELD_INNER_OFFSET * 2)
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
    private double scale(final double value) {
        return value * scaleFactor;
    }

    /**
     * Scales the given value with the current scale factor.
     *
     * @param value the value to scale
     * @return the scaled value
     */
    private int scale(final int value) {
        return (int) (value * scaleFactor);
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
     * Sets whether the dark mode is enabled.
     *
     * @param darkMode true if the dark mode is enabled, false otherwise
     */
    public void setDarkMode(final boolean darkMode) {
        this.darkMode = darkMode;
        updateGui();
    }

    /**
     * Toggles the dark mode.
     */
    public void toggleDarkMode() {
        setDarkMode(!isDarkMode());
    }
}
