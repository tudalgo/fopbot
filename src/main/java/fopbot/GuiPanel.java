package fopbot;

import com.jthemedetecor.OsThemeDetector;
import lombok.Getter;
import org.jetbrains.annotations.ApiStatus;

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
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
     * The default mapping of {@link FieldEntity} types to their corresponding {@link FieldEntityRenderer} instances.
     * <p>
     * This map provides the standard renderers used to visually represent each known {@link FieldEntity} subtype
     * on the board. It serves as the baseline configuration for rendering and can be extended or overridden
     * to support custom entities.
     */
    public static final Map<Class<? extends FieldEntity>, FieldEntityRenderer<?>> DEFAULT_ENTITY_RENDERS = Map.ofEntries(
        Map.entry(Coin.class, new CoinRenderer()),
        Map.entry(Block.class, new BlockRenderer()),
        Map.entry(Wall.class, new WallRenderer()),
        Map.entry(Robot.class, new RobotRenderer())
    );

    /**
     * The default rendering order used to sort {@link FieldEntity} instances before drawing.
     * <p>
     * Entities are ordered based on their render priority as defined: Walls < Robots < Coins < Blocks.
     * Lower values indicate lower layers (drawn first), and higher values indicate upper layers (drawn later).
     */
    public static final Comparator<FieldEntity> DEFAULT_RENDER_ORDER = Comparator.comparingInt(GuiPanel::getRenderPriority);

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
     * -- GETTER --
     * Gets the
     * of this
     * .
     *
     * @return the {@link InputHandler} of this {@code Gui}
     */
    @Getter
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
     * -- GETTER --
     * Returns whether the dark mode is enabled.
     *
     * @return true if the dark mode is enabled, false otherwise
     */
    @Getter
    private boolean darkMode = osThemeDetector.isDark();

    /**
     * Listeners for dark mode changes.
     */
    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);


    /**
     * Maps each {@link FieldEntity} subclass to its corresponding {@link FieldEntityRenderer},
     * which is responsible for drawing that specific type of entity on the GUI.
     * <p>
     * This allows for type-specific rendering logic and easy extensibility by adding new entity-renderer pairs.
     */
    private final Map<Class<? extends FieldEntity>, FieldEntityRenderer<?>> entityRenderers = new HashMap<>(DEFAULT_ENTITY_RENDERS);

    /**
     * Defines the drawing order of {@link FieldEntity} objects on the board.
     * <p>
     * Entities are sorted based on visual layering priority, ensuring correct rendering:
     * Default layering priority: Walls are drawn first, followed by Robots, Coins, and finally Blocks.
     * This prevents visual overlap issues.
     */
    @Getter
    private Comparator<? super FieldEntity> renderOrder = DEFAULT_RENDER_ORDER;

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
     * Returns the default rendering order for {@link FieldEntity} objects.
     *
     * @param entity the {@link FieldEntity} to get the render priority for
     *
     * @return the render priority of the {@link FieldEntity}
     */
    private static int getRenderPriority(FieldEntity entity) {
        if (entity instanceof Wall) return 0;
        if (entity instanceof Robot) return 1;
        if (entity instanceof Coin) return 2;
        if (entity instanceof Block) return 3;
        return Integer.MAX_VALUE; // unknown types last
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
     * Sets the render order for the {@link FieldEntity} objects.
     *
     * @param renderOrder the {@link Comparator} that defines the render order for the {@link FieldEntity} objects
     */
    public void setRenderOrder(Comparator<? super FieldEntity> renderOrder) {
        this.renderOrder = renderOrder;
        updateGui();
    }

    /**
     * Configures the rendering system by setting the render order and the associated {@link FieldEntity} renderers.
     * <p>
     * This method allows for customizing the order in which {@link FieldEntity} objects are rendered on the screen
     * and specifying the renderers responsible for drawing each entity type. Calling this method will replace any
     * previously configured renderers with the new ones provided in the {@code renderers} map. The render order,
     * defined by the {@link Comparator}, controls the layering of entities, with entities of lower priority being drawn first.
     *
     * <p><strong>Important:</strong> If you register custom renderers, ensure that the {@link #renderOrder} comparator
     * is updated accordingly to reflect the intended rendering layer. Failing to update the comparator may result in
     * entities being drawn in the wrong order.
     *
     * @param renderOrder a {@link Comparator} that defines the order in which entities should be rendered.
     *                    Entities with a lower priority value will be rendered first.
     * @param renderers   a map of {@link Class} to {@link FieldEntityRenderer} that associates each {@link FieldEntity}
     *                    type with its corresponding renderer. This will replace any existing renderers in the system.
     */
    public void setRenderConfiguration(
        Comparator<FieldEntity> renderOrder,
        Map<Class<? extends FieldEntity>, FieldEntityRenderer<?>> renderers
    ) {
        this.renderOrder = renderOrder;
        entityRenderers.clear();
        entityRenderers.putAll(renderers);
        updateGui();
    }

    /**
     * Registers or replaces the {@link FieldEntityRenderer} for a given {@link FieldEntity} type.
     * <p>
     * If a renderer is already registered for the specified entity class, it will be replaced.
     * This enables dynamic customization or extension of the rendering system to support
     * additional or user-defined {@link FieldEntity} types.
     * <p>
     * <strong>Note:</strong> The rendering order is determined by the {@link #renderOrder} comparator.
     * If you register a new {@link FieldEntityRenderer} for a custom entity type,
     * you must also update the comparator to ensure it is drawn in the correct layer.
     * Otherwise, it will be rendered last by default.
     *
     * @param entityClass the class of the {@link FieldEntity} to associate with a renderer
     * @param renderer    the {@link FieldEntityRenderer} responsible for rendering the given entity type
     */
    public void registerFieldEntityRenderer(
        final Class<? extends FieldEntity> entityClass,
        final FieldEntityRenderer<? extends FieldEntity> renderer
    ) {
        entityRenderers.put(entityClass, renderer);
        updateGui();
    }

    /**
     * Registers or replaces the renderers for a given entities type.
     * <p>
     * If a renderer is already registered for the specified entity class, it will be replaced.
     * This enables dynamic customization or extension of the rendering system to support
     * additional or user-defined {@link FieldEntity} types.
     * <p>
     * <strong>Note:</strong> The rendering order is determined by the {@link #renderOrder} comparator.
     * If you register a new {@link FieldEntityRenderer} for a custom entity type,
     * you must also update the comparator to ensure it is drawn in the correct layer.
     * Otherwise, it will be rendered last by default.
     *
     * @param renderers a map of {@link Class} to {@link FieldEntityRenderer} that associates each {@link FieldEntity}
     */
    public void registerFieldEntityRenderers(Map<Class<? extends FieldEntity>, FieldEntityRenderer<?>> renderers) {
        entityRenderers.putAll(renderers);
        updateGui();
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
    protected void draw(final Graphics g) {
        drawBoard(g);

        for (final Field field : world.getFields()) {
            List<FieldEntity> entities = field.getEntities();
            if (entities.isEmpty()) {
                continue;
            }

            // Sort entities based on rendering priority (e.g., Wall < Robot < Coin < Block)
            List<FieldEntity> sortedEntities = new ArrayList<>(entities);
            sortedEntities.sort(renderOrder);

            boolean isRobotOnField = sortedEntities.stream().anyMatch(Robot.class::isInstance);

            for (final FieldEntity entity : sortedEntities) {
                FieldEntityRenderer<FieldEntity> renderer = getRenderer(entity);

                if (renderer != null) {
                    FieldEntityRenderContext<FieldEntity> context = new FieldEntityRenderContext<>(
                        entity,
                        world,
                        getBounds(),
                        getColorProfile(),
                        scaleFactor,
                        isRobotOnField
                    );
                    renderer.draw(g, context);
                } else {
                    System.err.printf("No renderer found for FieldEntity of type: %s%n", entity.getClass().getName());
                }
            }
        }
    }

    /**
     * Returns the {@link FieldEntityRenderer} for the given {@link FieldEntity}.
     *
     * @param entity the {@link FieldEntity} to get the renderer for
     *
     * @return the {@link FieldEntityRenderer} for the given {@link FieldEntity}
     */
    @SuppressWarnings("unchecked")
    private FieldEntityRenderer<FieldEntity> getRenderer(FieldEntity entity) {
        return (FieldEntityRenderer<FieldEntity>) entityRenderers.get(entity.getClass());
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
