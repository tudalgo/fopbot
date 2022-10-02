package fopbot;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import static fopbot.PaintUtils.BOARD_OFFSET;
import static fopbot.PaintUtils.FIELD_BORDER_THICKNESS;
import static fopbot.PaintUtils.FIELD_INNER_OFFSET;
import static fopbot.PaintUtils.FIELD_INNER_SIZE;
import static fopbot.PaintUtils.getBoardSize;
import static fopbot.PaintUtils.getUpperLeftCornerInField;

/**
 * The graphical user interface in which the FOPBot world is represented.
 */
class GuiPanel extends JPanel {

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
     * Constructs and initializes graphical use interface to represent the FOP Bot world.
     *
     * @param world the FOP Bot world to represent on the graphical user interface
     */
    public GuiPanel(KarelWorld world) {
        this.world = world;

        setSize(getPreferredSize());
    }

    /**
     * Returns the unscaled size of world board size.
     *
     * @return the unscaled size of world board size
     */
    protected Dimension getUnscaledSize() {
        Point p = getBoardSize(world);
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
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
            Date date = new Date();
            startDate = dateFormat.format(date);
            File dir = new File("screenshots/" + startDate);
            if (!dir.mkdir()) {
                throw new RuntimeException("Could not create screenshot directory!");
            }
        }

        String state = Long.toString(screenshotCounter);
        while (state.length() != 4) {
            state = "0" + state;
        }

        String imagePath = "screenshots/" + startDate + "/karel_" + state + ".png";
        screenshotCounter++;

        BufferedImage image = new BufferedImage(getUnscaledSize().width, getUnscaledSize().height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = image.getGraphics();
        draw(g);
        image = image.getSubimage(BOARD_OFFSET, BOARD_OFFSET,
            getUnscaledSize().width - BOARD_OFFSET * 2,
            getUnscaledSize().height - BOARD_OFFSET * 2);
        try {
            ImageIO.write(image, "png", new File(imagePath));
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        Image image = new BufferedImage(getUnscaledSize().width, getUnscaledSize().height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D gImage = (Graphics2D) image.getGraphics();
        gImage.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
        draw(gImage);

        if (getWidth() != getUnscaledSize().width || getHeight() != getUnscaledSize().height) {
            image = image.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
        }

        g.drawImage(image, 0, 0, null);
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
    protected void draw(Graphics g) {
        drawBoard(g);

        List<FieldEntity> entities = world.getAllFieldEntities();
        List<Robot> robots = new LinkedList<>();
        for (FieldEntity ce : entities) {
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
        for (Robot r : robots) {
            drawRobot(r, g);
        }
    }

    /**
     * Draws the world board with its fields (borders, fields).
     *
     * @param g the {@code Graphics} context in which to paint
     */
    protected void drawBoard(Graphics g) {
        // draw outer borders
        int width = BOARD_OFFSET;
        int height = BOARD_OFFSET;
        g.setColor(Color.BLACK);
        Point p = getBoardSize(world);
        g.fillRect(width, height, p.x, p.y);

        // draw inner borders
        width = BOARD_OFFSET + FIELD_BORDER_THICKNESS;
        height = BOARD_OFFSET + FIELD_BORDER_THICKNESS;
        g.setColor(Color.GRAY);
        g.fillRect(width, height, p.x - FIELD_BORDER_THICKNESS * 2, p.y - FIELD_BORDER_THICKNESS * 2);

        // draw fields
        g.setColor(Color.LIGHT_GRAY);
        for (int h = 0; h < world.getHeight(); h++) {
            for (int w = 0; w < world.getWidth(); w++) {
                g.fillRect(width, height, FIELD_INNER_SIZE, FIELD_INNER_SIZE);

                if (h == 99) {
                    g.setColor(Color.GREEN);
                    g.drawString(width + ";" + height, width, height);
                    g.setColor(Color.LIGHT_GRAY);
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
    protected void drawRobot(Robot r, Graphics g) {
        Point upperLeft = getUpperLeftCornerInField(r, world.getHeight());

        int directionIndex = r.getDirection().ordinal();

        Map<String, Image[]> imageMapById = world.getRobotImageMapById(r.getFamily().getIdentifier());
        Objects.requireNonNull(imageMapById, "robot image was not found");
        Image robotImage = imageMapById.get(r.isTurnedOff() ? "off" : "on")[directionIndex];

        g.drawImage(robotImage, upperLeft.x, upperLeft.y, null);

        Color cBackup = g.getColor();

        g.setColor(cBackup);
    }

    /**
     * Draws the specified {@code Coin} on the graphical user interface.
     *
     * @param c the {@code Coin} to draw
     * @param g the {@code Graphics} context in which to paint
     */
    protected void drawCoin(Coin c, Graphics g) {
        Color cBackup = g.getColor();

        Point upperLeft = getUpperLeftCornerInField(c, world.getHeight());
        g.setColor(Color.RED);
        int size = FIELD_INNER_SIZE - FIELD_INNER_OFFSET * 2;
        g.fillOval(upperLeft.x, upperLeft.y, size, size);
        g.setColor(Color.BLACK);
        g.setFont(new Font(g.getFont().getFontName(), Font.PLAIN, 16));
        g.drawString(Integer.toString(c.getCount()), upperLeft.x + size / 2, upperLeft.y + size / 2);

        g.setColor(cBackup);
    }

    /**
     * Draws the specified {@code Block} on the graphical user interface.
     *
     * @param b the {@code Block} to draw
     * @param g the {@code Graphics} context in which to paint
     */
    protected void drawBlock(Block b, Graphics g) {
        Color cBackup = g.getColor();

        Point upperLeft = getUpperLeftCornerInField(b, world.getHeight());
        g.setColor(Color.BLACK);
        int size = FIELD_INNER_SIZE - FIELD_INNER_OFFSET * 2;
        g.fillRect(upperLeft.x, upperLeft.y, size, size);

        g.setColor(cBackup);
    }

    /**
     * Draws the specified {@code Wall} on the graphical user interface.
     *
     * @param w the {@code Wall} to draw
     * @param g the {@code Graphics} context in which to paint
     */
    protected void drawWall(Wall w, Graphics g) {
        Color cBackup = g.getColor();
        g.setColor(Color.BLACK);

        Point upperLeft = getUpperLeftCornerInField(w, world.getHeight());
        if (w.isHorizontal()) {
            int x = upperLeft.x - FIELD_INNER_OFFSET * 2;
            int y = upperLeft.y - FIELD_INNER_OFFSET - FIELD_BORDER_THICKNESS;
            g.fillRect(x, y, FIELD_INNER_SIZE + FIELD_INNER_OFFSET * 2, FIELD_BORDER_THICKNESS);
        } else {
            int x = upperLeft.x - FIELD_INNER_OFFSET + FIELD_INNER_SIZE;
            int y = upperLeft.y - FIELD_INNER_OFFSET * 2;
            g.fillRect(x, y, FIELD_BORDER_THICKNESS, FIELD_INNER_SIZE + FIELD_INNER_OFFSET * 2);
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
            } catch (InvocationTargetException | InterruptedException e) {
                e.printStackTrace();
            }
        }

        Toolkit.getDefaultToolkit().sync();
    }
}
