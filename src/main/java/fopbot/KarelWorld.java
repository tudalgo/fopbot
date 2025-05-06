package fopbot;


import fopbot.Transition.RobotAction;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.PrimitiveIterator;
import java.util.function.Predicate;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * Represents the FOP Bot world on a graphical user interface.
 */
public class KarelWorld {

    /**
     * The state whether a screenshot should be done before a graphical user interface update.
     */
    private static final boolean doScreenshots = false;

    /**
     * The state of the entities should they be saved after a graphical user interface update has been
     * triggered.
     */
    private static final boolean saveStates = true;
    /**
     * The height of this world.
     */
    private final int height;
    /**
     * The width of this world.
     */
    private final int width;
    /**
     * The world fields as 2D coordinate system.
     */
    private final Field[][] fields;

    /**
     * The maximum number of actions that can be performed in this world.
     */
    private long actionLimit = -1;
    /**
     * The robot tracing of robot actions.
     */
    private final Map<String, RobotTrace> traces = new HashMap<>();

    /**
     * The fields of this world.
     */
    private List<Field> entityStates;

    /**
     * The delay in milliseconds of this world.
     */
    private int delay = 100;

    /**
     * The robot ID generator.
     */
    private final PrimitiveIterator.OfInt robotIdGenerator = IntStream.iterate(0, i -> i + 1).iterator();

    /**
     * The color profile of the graphical user interface.
     */
    private ColorProfile colorProfile = ColorProfile.DEFAULT;

    /**
     * The {@link DrawingRegistry} used for configuring the drawing order and associated drawables for different field
     * entities.
     * This registry determines how field entities are drawn, including their order and appearance.
     */
    private DrawingRegistry drawingRegistry = DrawingRegistry.DEFAULT;

    /**
     * The graphical user interface window on which the FOP Bot world panel is visible.
     */
    private JFrame guiFrame;

    /**
     * The graphical user interface panel on which this world is drawn.
     */
    private GuiPanel guiGp;

    /**
     * Whether to draw turned off robots.
     */
    private boolean drawTurnedOffRobots = true;

    /**
     * Constructs and initializes a world with the specified size.
     *
     * @param width  the width of the newly constructed world
     * @param height the height of the newly constructed world
     *
     * @throws RuntimeException if the world size is smaller than one
     */
    public KarelWorld(final int width, final int height) {
        System.setProperty("sun.java2d.dpiaware", "false");
        System.setProperty("sun.java2d.uiScale", "1.0");
        if (width < 1 || height < 1) {
            throw new RuntimeException("Invalid world size: " + width + "x" + height);
        }

        this.height = height;
        this.width = width;

        fields = new Field[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                fields[y][x] = new Field(this, x, y);
            }
        }
    }

    /**
     * Adds the specified robot to this world.
     *
     * @param robot the robot to place
     */
    public void addRobot(final Robot robot) {
        fields[robot.getY()][robot.getX()].getEntities().add(robot);
        robot.setId(Integer.toString(robotIdGenerator.nextInt()));
        traces.put(robot.getId(), new RobotTrace());
        triggerUpdate();
        sleep();
    }

    /**
     * Validates that the number of coins is not negative.
     *
     * @param numberOfCoins the number of coins to check
     *
     * @throws IllegalArgumentException if the number of coins is negative
     */
    protected void checkNumberOfCoins(final int numberOfCoins) {
        if (numberOfCoins < 0) {
            throw new IllegalArgumentException("Number of coins must be greater than -1!");
        }
    }

    /**
     * Validates if the specified X coordinate is within the world.
     *
     * @param x the X coordinate to validate
     *
     * @throws IllegalArgumentException if the X coordinate is outside the world borders
     */
    protected void checkXCoordinate(final int x) {
        if (x > getWidth() - 1 || x < 0) {
            throw new IllegalArgumentException("Invalid x-coordinate: " + x);
        }
    }

    /**
     * Validates if the specified Y coordinate is within the world.
     *
     * @param y the Y coordinate to validate
     *
     * @throws IllegalArgumentException if the Y coordinate is outside the world borders
     */
    protected void checkYCoordinate(final int y) {
        if (y > getHeight() - 1 || y < 0) {
            throw new IllegalArgumentException("Invalid y-coordinate: " + y);
        }
    }

    /**
     * Gathers a list of all field entities on this world.
     *
     * @return a list of all field entities on this world
     */
    public List<Field> getFields() {
        return Arrays.stream(fields).flatMap(Arrays::stream).toList();
    }

    /**
     * Returns all field entities on this world.
     *
     * @return all field entities on this world
     */
    public List<FieldEntity> getAllFieldEntities() {
        return Stream.of(fields).flatMap(Stream::of)
            .map(Field::getEntities)
            .flatMap(Collection::stream)
            .toList();
    }

    /**
     * Returns the current delay in milliseconds of this world.
     *
     * @return the current delay in milliseconds of this world
     */
    public int getDelay() {
        return delay;
    }

    /**
     * Sets the delay that delays all robot actions after their execution.
     *
     * @param delay the delay value in milliseconds
     */
    public void setDelay(final int delay) {
        this.delay = delay;
    }

    /**
     * Returns true if turned off robots are drawn.
     *
     * @return true if turned off robots are drawn
     */
    public boolean isDrawTurnedOffRobots() {
        return drawTurnedOffRobots;
    }

    /**
     * Sets whether turned off robots should be drawn.
     *
     * @param drawTurnedOffRobots true if turned off robots should be drawn
     */
    public void setDrawTurnedOffRobots(final boolean drawTurnedOffRobots) {
        this.drawTurnedOffRobots = drawTurnedOffRobots;
    }

    /**
     * Returns all entity states (fields) of this world.
     *
     * @return all entity states (fields) of this world
     */
    public List<Field> getEntityStates() {
        return entityStates;
    }

    /**
     * Returns the field of this world at the specified coordinate.
     *
     * @param x the X coordinate of the field.
     * @param y the Y coordinate of the field.
     *
     * @return the field of this world at the specified coordinate
     */
    public Field getField(final int x, final int y) {
        return fields[y][x];
    }

    /**
     * Returns the height of this world.
     *
     * @return the height of this world.
     */
    public int getHeight() {
        return height;
    }

    /**
     * Returns the previous robot tracing of the specified robot.
     *
     * @param robot the robot to retrieve its tracing
     *
     * @return the previous robot tracing of the specified robot
     */
    public RobotTrace getTrace(final Robot robot) {
        if (robot == null) {
            return null;
        }
        final var robotTrace = new RobotTrace(traces.get(robot.getId()));
        robotTrace.trace(robot, RobotAction.NONE);
        return robotTrace;
    }

    /**
     * Returns the previous robots tracing.
     *
     * @return the previous robots tracing
     */
    public List<RobotTrace> getTraces() {
        final var traces = new ArrayList<RobotTrace>();
        final var entities = getAllFieldEntities().stream()
            .filter(Robot.class::isInstance)
            .map(Robot.class::cast)
            .toList();
        for (final var id : this.traces.keySet()) {
            final Robot lastState = entities.stream().filter(en -> en.getId().equals(id)).findAny().orElse(null);
            traces.add(getTrace(lastState));
        }
        return traces;
    }

    /**
     * Returns the width of this world.
     *
     * @return the width of this world.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Returns {@code true} if the specified robot is located at the specified coordinate.
     *
     * @param x     the X coordinate to check with the robot X coordinate
     * @param y     the Y coordinate to check with the robot Y coordinate
     * @param robot the robot to check with the coordinate
     *
     * @return {@code true} if the specified robot is located at the specified coordinate
     */
    protected boolean isAnotherRobotInField(final int x, final int y, final Robot robot) {
        return fields[y][x].getEntities().stream()
            .anyMatch(e -> e instanceof Robot && e != robot);
    }

    /**
     * Returns {@code true} if a block is at the specified coordinate.
     *
     * @param x the X coordinate to check
     * @param y the Y coordinate to check
     *
     * @return {@code true} if a block is at the specified coordinate
     */
    protected boolean isBlockInField(final int x, final int y) {
        return fields[y][x].getEntities().stream()
            .anyMatch(Block.class::isInstance);
    }

    /**
     * Returns {@code true} if at least one coin is on the specified coordinate.
     *
     * @param x the X coordinate to check
     * @param y the Y coordinate to check
     *
     * @return {@code true} if at least one coin is on the specified coordinate
     */
    protected boolean isCoinInField(final int x, final int y) {
        return fields[y][x].getEntities().stream()
            .anyMatch(Coin.class::isInstance);
    }

    /**
     * Returns {@code true} if this world is visible on the graphical user interface. Returns
     * {@code false} if this world is running in headless mode.
     *
     * @return {@code true} if this world is visible on the graphical user interface or {@code false} if this world is
     *     running in headless mode
     */
    public boolean isVisible() {
        return !GraphicsEnvironment.isHeadless() && guiFrame != null && guiFrame.isVisible();
    }

    /**
     * Sets the visibility of the world on the graphical user interface to the specified visibility
     * value.
     * <p>Does nothing if the world is running in headless mode.</p>
     *
     * @param visible if {@code true} this world will be visible on the graphical user interface
     */
    public void setVisible(final boolean visible) {
        if (GraphicsEnvironment.isHeadless()) {
            System.out.println("Cannot set world visible in headless mode. Ignoring.");
            return;
        }
        if (visible && guiFrame == null) {
            guiFrame = new JFrame("FoPBot");
            guiFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            if (guiGp == null) {
                guiGp = new GuiPanel(this);
            }
            guiFrame.setLayout(new BorderLayout());
            guiFrame.add(guiGp, BorderLayout.CENTER);
            guiFrame.pack();
            guiFrame.setVisible(true);
            triggerUpdate();
        } else if (!visible && guiFrame != null && guiFrame.isVisible()) {
            guiFrame.setVisible(false);
            guiFrame.dispose();
            guiFrame = null;
            guiGp = null;
        }
    }

    /**
     * Returns {@code true} if a wall and its orientation is on the specified field.
     *
     * @param x          the X coordinate to check
     * @param y          the Y coordinate to check
     * @param horizontal if {@code true} check its horizontal orientation
     *
     * @return {@code true} if the specified wall and its orientation are on the specified field
     */
    protected boolean isWallInField(final int x, final int y, final boolean horizontal) {
        return fields[y][x].getEntities().stream()
            .anyMatch(e -> e instanceof Wall && ((Wall) e).isHorizontal() == horizontal);
    }

    /**
     * Picks up a coin from the specified coordinate and return {@code true} if a coin was removed at
     * the specified coordinate after this call.
     *
     * @param x the X coordinate to pick up the coin
     * @param y the Y coordinate to pick up the coin
     *
     * @return {@code true} if a coin was removed at the specified coordinate after this call
     */
    protected boolean pickCoin(final int x, final int y) {
        final Iterator<FieldEntity> iterator = fields[y][x].getEntities().iterator();
        while (iterator.hasNext()) {
            final FieldEntity entity = iterator.next();
            if (entity instanceof final Coin coin) {
                // if coins already placed in this field, decrease number
                if (coin.getCount() > 1) {
                    coin.setCount(coin.getCount() - 1);
                } else {
                    iterator.remove();
                }
                triggerUpdate();
                return true;
            }
        }
        return false;
    }

    /**
     * Places a block at the specified coordinate.
     *
     * @param x the X coordinate of the block
     * @param y the Y coordinate of the block
     */
    public void placeBlock(final int x, final int y) {
        checkXCoordinate(x);
        checkYCoordinate(y);
        if (fields[y][x].getEntities().stream().anyMatch(Block.class::isInstance)) {
            return;
        }
        fields[y][x].getEntities().add(new Block(x, y));
        triggerUpdate();
    }

    /**
     * Places a horizontal wall at the specified coordinate.
     *
     * @param x the X coordinate of the horizontal wall
     * @param y the Y coordinate of the horizontal wall
     */
    public void placeHorizontalWall(final int x, final int y) {
        placeWall(x, y, true);
    }

    /**
     * Places a vertical wall at the specified coordinate.
     *
     * @param x the X coordinate of the vertical wall
     * @param y the Y coordinate of the vertical wall
     */
    public void placeVerticalWall(final int x, final int y) {
        placeWall(x, y, false);
    }

    /**
     * Places a wall at the specified coordinate.
     *
     * @param x          the X coordinate of the wall
     * @param y          the Y coordinate of the wall
     * @param horizontal if {@code true} horizontal wall will be placed
     */
    private void placeWall(final int x, final int y, final boolean horizontal) {
        checkXCoordinate(x);
        checkYCoordinate(y);
        if (isWallInField(x, y, horizontal)) {
            return;
        }
        fields[y][x].getEntities().add(new Wall(x, y, horizontal));
        triggerUpdate();
    }

    /**
     * Places the specified number of coins at the specified coordinate.
     *
     * @param x             the X coordinate of the coin
     * @param y             the Y coordinate of the coin
     * @param numberOfCoins the number of coins to place
     *
     * @throws IllegalArgumentException if the number of coins is smaller than 1 or the position is invalid
     */
    public void putCoins(final int x, final int y, final int numberOfCoins) throws IllegalArgumentException {
        checkXCoordinate(x);
        checkYCoordinate(y);
        if (numberOfCoins < 1) {
            throw new IllegalArgumentException("Number of coins must be greater than 0!");
        }

        for (final FieldEntity entity : fields[y][x].getEntities()) {
            if (entity instanceof final Coin coin) {
                // if coins already placed in this field, increase number
                coin.setCount(coin.getCount() + numberOfCoins);
                triggerUpdate();
                return;
            }
        }
        // else place first coin
        final Coin c = new Coin(x, y, numberOfCoins);
        fields[y][x].getEntities().add(c);
        triggerUpdate();
    }

    /**
     * Places a {@link FieldEntity} on the field at the specified coordinates.
     *
     * @param entity the {@link FieldEntity} to be placed on the field
     *
     * @throws IllegalArgumentException if the coordinates of the entity are out of bounds
     */
    public void placeEntity(FieldEntity entity) {
        int x = entity.getX();
        int y = entity.getY();
        checkXCoordinate(x);
        checkYCoordinate(y);
        fields[y][x].getEntities().add(entity);
        triggerUpdate();
    }

    /**
     * Removes the first {@link FieldEntity} from the field at the specified coordinates
     * that matches the given filter.
     *
     * @param x      the x-coordinate of the field
     * @param y      the y-coordinate of the field
     * @param filter a {@link Predicate} to filter which entity to remove
     *
     * @throws IllegalArgumentException if the coordinates are out of bounds
     */
    public void removeEntity(int x, int y, Predicate<? super FieldEntity> filter) {
        checkXCoordinate(x);
        checkYCoordinate(y);
        var it = fields[y][x].getEntities().iterator();
        while (it.hasNext()) {
            if (filter.test(it.next())) {
                it.remove();
                triggerUpdate();
                return;
            }
        }
    }

    /**
     * Removes the first {@link FieldEntity} from the field at the specified coordinates.
     *
     * @param x     the x-coordinate of the field
     * @param y     the y-coordinate of the field
     * @param clazz the class of the {@link FieldEntity} to remove
     */
    public void removeEntity(int x, int y, Class<? extends FieldEntity> clazz) {
        removeEntity(x, y, e -> e.getClass() == clazz);
    }

    /**
     * Removes the specified {@link FieldEntity} from the field.
     *
     * @param entity the {@link FieldEntity} to be removed from the field
     *
     * @throws IllegalArgumentException if the coordinates of the entity are out of bounds
     */
    public void removeEntity(FieldEntity entity) {
        int x = entity.getX();
        int y = entity.getY();
        checkXCoordinate(x);
        checkYCoordinate(y);
        Field field = fields[y][x];
        if (field.contains(entity)) {
            field.removeEntity(entity);
            triggerUpdate();
        }
    }

    /**
     * Resets this world by removing all entities from the fields.
     */
    public void reset() {
        Stream.of(fields).flatMap(Stream::of)
            .map(Field::getEntities)
            .forEach(Collection::clear);
        triggerUpdate();
    }

    /**
     * Saves the current entity state (fields) of this world.
     */
    private void saveEntityState() {
        if (entityStates == null) {
            entityStates = new LinkedList<>();
        }
        // copy entities
        final List<FieldEntity> allEntities = getAllFieldEntities();
        final List<FieldEntity> allEntitiesCopy = new ArrayList<>(allEntities.size());
        for (final FieldEntity entity : allEntities) {
            if (entity instanceof final Robot robot) {
                final Robot copy = new Robot(true, robot.getX(), robot.getY(), robot.getDirection(), robot.getNumberOfCoins());
                copy.setId(String.valueOf(robot.hashCode()));
                allEntitiesCopy.add(copy);
            }

            if (entity instanceof final Coin coin) {
                final Coin copy = new Coin(coin.getX(), coin.getY(), coin.getCount());
                allEntitiesCopy.add(copy);
            }

            if (entity instanceof final Block block) {
                final Block copy = new Block(block.getX(), block.getY());
                allEntitiesCopy.add(copy);
            }

            if (entity instanceof final Wall wall) {
                final Wall copy = new Wall(wall.getX(), wall.getY(), wall.isHorizontal());
                allEntitiesCopy.add(copy);
            }
        }
        // TODO
        // Using (0, 0) as position could be only a temporary solution.
        // The use of an Field object does not seem appropriate here.
        entityStates.add(new Field(this, 0, 0, allEntitiesCopy));
    }

    /**
     * Puts this world to sleep for the specified amount time given by {@link #delay} (in
     * milliseconds).
     *
     * <p>In headless mode, this method does nothing.</p>
     */
    protected void sleep() {
        if (GraphicsEnvironment.isHeadless()) {
            return;
        }
        try {
            Thread.sleep(delay);
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Traces the action of the specified robot.
     *
     * @param robot       the robot to trace
     * @param robotAction the action of the robot to trace
     */
    void trace(final Robot robot, final RobotAction robotAction) {
        final var robotTrace = traces.get(robot.getId());
        robotTrace.trace(robot, robotAction);
    }

    /**
     * Triggers that an update of the graphical user interface is needed.
     */
    protected void triggerUpdate() {
        if (saveStates) {
            saveEntityState();
        }
        updateGui();
    }

    /**
     * Updates the graphical user interface window.
     * <p>In headless mode, this method does nothing.</p>
     */
    protected void updateGui() {
        if (!isVisible()) {
            return;
        }
        if (doScreenshots) {
            guiGp.saveStateAsPng();
        }
        guiGp.updateGui();
    }

    /**
     * Updates the entity array to be in sync with the robots X and Y coordinates.
     *
     * @param robot the robot to sync with
     * @param oldX  the old X coordinate of the robot
     * @param oldY  the old Y coordinate of the robot
     */
    protected void updateRobotField(final Robot robot, final int oldX, final int oldY) {
        if (fields[oldY][oldX].getEntities().removeIf(entity -> entity == robot)) {
            fields[robot.getY()][robot.getX()].getEntities().add(robot);
        }
    }

    /**
     * Returns the {@link GuiPanel} of this world.
     *
     * <p>In headless mode, this method returns {@code null}.</p>
     *
     * @return the {@link GuiPanel} of this world
     */
    @ApiStatus.Internal
    public GuiPanel getGuiPanel() {
        return guiGp;
    }

    /**
     * Sets the {@link GuiPanel} of this world.
     *
     * @param guiPanel the {@link GuiPanel} of this world
     */
    @ApiStatus.Internal
    public void setGuiPanel(final GuiPanel guiPanel) {
        this.guiGp = guiPanel;
    }

    /**
     * Returns the {@link JFrame} of this world.
     *
     * @return the {@link JFrame} of this world
     */
    @ApiStatus.Internal
    public JFrame getGuiFrame() {
        return guiFrame;
    }

    /**
     * Returns the {@link InputHandler} of this world. This method returns {@code null} in headless mode.
     *
     * @return the {@link InputHandler} of this world or {@code null} in headless mode
     */
    public InputHandler getInputHandler() {
        return GraphicsEnvironment.isHeadless() ? null : getGuiPanel().getInputHandler();
    }

    /**
     * Sets the color of the field at the specified coordinates.
     *
     * @param x     the x coordinate of the field
     * @param y     the y coordinate of the field
     * @param color the color to set
     */
    public void setFieldColor(final int x, final int y, @Nullable final Color color) {
        fields[y][x].setFieldColor(color);
    }

    /**
     * Returns the color of the field at the specified coordinates or {@code null} if no color is set.
     *
     * @param x the x coordinate of the field
     * @param y the y coordinate of the field
     *
     * @return the color of the field at the specified coordinates or {@code null} if no color is set
     */
    public @Nullable Color getFieldColor(final int x, final int y) {
        return fields[y][x].getFieldColor();
    }

    /**
     * Returns the maximum amount of traces to be stored.
     *
     * @return the maximum amount of traces to be stored
     */
    @ApiStatus.Internal
    public long getActionLimit() {
        return actionLimit;
    }

    /**
     * Sets the maximum amount of traces to be stored.
     * <p>Since the action limit is there to prevent infinite loops, choose a reasonable value.</p>
     *
     * @param actionLimit the maximum amount of traces to be stored
     */
    @ApiStatus.Internal
    public void setActionLimit(final long actionLimit) {
        this.actionLimit = actionLimit;
    }

    /**
     * Returns the amount of traces stored. This is equivalent to the amount of actions performed in the world.
     *
     * @return the amount of traces stored
     */
    @ApiStatus.Internal
    public long getActionCount() {
        return traces.values().stream().mapToLong(rt -> rt.getTransitions().size()).sum();
    }

    /**
     * Checks if the action limit is reached and throws an {@link IllegalStateException} if so.
     */
    void checkActionLimit() {
        if (actionLimit >= 0 && getActionCount() >= getActionLimit()) {
            throw new IllegalStateException("Too many traces, please check your program for infinite loops.");
        }
    }

    /**
     * Returns the amount of robots in this world.
     *
     * @return the amount of robots in this world
     */
    public long getRobotCount() {
        return Arrays.stream(fields)
            .flatMap(Arrays::stream)
            .map(Field::getEntities)
            .flatMap(Collection::stream)
            .filter(Robot.class::isInstance)
            .count();
    }

    /**
     * Returns the current {@link ColorProfile} that is used to draw the world.
     *
     * @return the current {@link ColorProfile} that is used to draw the world
     */
    @ApiStatus.Internal
    public ColorProfile getColorProfile() {
        return colorProfile;
    }

    /**
     * Retrieves the current {@link DrawingRegistry} that is used for configuring the drawing order
     * and the associated drawables for different field entities.
     *
     * @return the current {@link DrawingRegistry} instance
     */
    public DrawingRegistry getDrawingRegistry() {
        return drawingRegistry;
    }

    /**
     * Sets a new {@link DrawingRegistry} for configuring the drawing order
     * and the associated drawables for different field entities.
     * This method triggers an update to reflect the new configuration.
     *
     * @param drawingRegistry the new {@link DrawingRegistry} instance to be set
     */
    public void setDrawingRegistry(DrawingRegistry drawingRegistry) {
        this.drawingRegistry = drawingRegistry;
        triggerUpdate();
    }


    /**
     * Sets the {@link ColorProfile} that is used to draw the world to the given value.
     *
     * @param colorProfile the new {@link ColorProfile} to use
     */
    @ApiStatus.Internal
    public void setColorProfile(final ColorProfile colorProfile) {
        this.colorProfile = colorProfile;
    }
}
