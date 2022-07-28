package fopbot;

import javax.swing.JFrame;
import javax.swing.WindowConstants;
import java.awt.Image;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toSet;

import fopbot.Transition.RobotAction;

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
     * The fields of this world.
     */
    private List<Field> entityStates;

    /**
     * The height of this world.
     */
    private final int height;

    /**
     * The width of this world.
     */
    private final int width;

    /**
     * The delay in milliseconds of this world.
     */
    private int delay = 100;

    /**
     * The number of robots on this world.
     */
    private int robotCount;

    /**
     * The graphical user interface window on which the FOP Bot world panel is visible.
     */
    private JFrame guiFrame;
    /**
     * The graphical user interface panel on which this world is drawn.
     */
    private GuiPanel guiGp;

    /**
     * The world fields as 2D coordinate system.
     */
    private final Field[][] fields;

    /**
     * The robot images by class instances.
     */
    private final Map<Class<? extends Robot>, Map<String, Image[]>> robotImages;

    /**
     * The robot images by image identification.
     */
    private final Map<String, Map<String, Image[]>> robotImagesById;

    /**
     * The robot tracing of robot actions.
     */
    private final Map<String, RobotTrace> traces = new HashMap<>();

    /**
     * Constructs and initializes a world with the specified size.
     *
     * @param width  the width of the newly constructed world
     * @param height the height of the newly constructed world
     * @throws RuntimeException if the world size is smaller than one
     */
    public KarelWorld(int width, int height) {
        if (width < 1 || height < 1) {
            throw new RuntimeException("Invalid world size: " + width + "x" + height);
        }

        this.height = height;
        this.width = width;
        robotImages = new HashMap<>();
        robotImagesById = new HashMap<>();

        // find robot images
        var pattern = Pattern.compile("^(?<file>(?<identifier>.*?)\\.(png))$");
        var resourcePathStream = getClass().getResourceAsStream("/robots/");
        requireNonNull(resourcePathStream, "cannot load robot images");
        try (resourcePathStream) {
            BufferedReader resourcePathReader = new BufferedReader(new InputStreamReader(resourcePathStream));
            var matches = resourcePathReader.lines().map(pattern::matcher).filter(Matcher::matches).collect(toSet());
            for (var match : matches) {
                var file = match.group("file");
                var identifier = match.group("identifier");
                var streamOn = getClass().getResourceAsStream(String.format("/robots/%s", file));
                var streamOff = getClass().getResourceAsStream(String.format("/robots/%s", file));
                setAndLoadRobotImagesById(identifier, streamOn, streamOff, 0, 0);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        setAndLoadRobotImages(Robot.class, getClass().getResourceAsStream("/robots/trianglebot.png"),
            getClass().getResourceAsStream("/robots/trianglebot.png"), 0, 0);

        fields = new Field[height][width];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                fields[y][x] = new Field();
            }
        }
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
     * Returns the width of this world.
     *
     * @return the width of this world.
     */
    public int getWidth() {
        return width;
    }

    /**
     * Sets the delay that delays all robot actions after their execution.
     *
     * @param delay the delay value in milliseconds
     */
    public void setDelay(int delay) {
        this.delay = delay;
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
     * Returns the field of this world at the specified coordinate.
     *
     * @param x the X coordinate of the field.
     * @param y the Y coordinate of the field.
     * @return the field of this world at the specified coordinate
     */
    protected Field getField(int x, int y) {
        return fields[y][x];
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
            .collect(Collectors.toUnmodifiableList());
    }

    /**
     * Returns {@code true} if a block is at the specified coordinate.
     *
     * @param x the X coordinate to check
     * @param y the Y coordinate to check
     * @return {@code true} if a block is at the specified coordinate
     */
    protected boolean isBlockInField(int x, int y) {
        return fields[y][x].getEntities().stream()
            .anyMatch(Block.class::isInstance);
    }

    /**
     * Returns {@code true} if a wall and its orientation is on the specified field.
     *
     * @param x          the X coordinate to check
     * @param y          the Y coordinate to check
     * @param horizontal if {@code true} check its horizontal orientation
     * @return {@code true} if the specified wall and its orientation are on the specified field
     */
    protected boolean isWallInField(int x, int y, boolean horizontal) {
        return fields[y][x].getEntities().stream()
            .anyMatch(e -> e instanceof Wall && ((Wall) e).isHorizontal() == horizontal);
    }

    /**
     * Returns {@code true} if at least one coin is on the specified coordinate.
     *
     * @param x the X coordinate to check
     * @param y the Y coordinate to check
     * @return {@code true} if at least one coin is on the specified coordinate
     */
    protected boolean isCoinInField(int x, int y) {
        return fields[y][x].getEntities().stream()
            .anyMatch(Coin.class::isInstance);
    }

    /**
     * Returns {@code true} if the specified robot is located at the specified coordinate.
     *
     * @param x the X coordinate to check with the robot X coordinate
     * @param y the Y coordinate to check with the robot Y coordinate
     * @param r the robot to check with the coordinate
     * @return {@code true} if the specified robot is located at the specified coordinate
     */
    protected boolean isAnotherRobotInField(int x, int y, Robot r) {
        return fields[y][x].getEntities().stream()
            .anyMatch(e -> e instanceof Robot && e != r);
    }

    /**
     * Picks up a coin from the specified coordinate and return {@code true} if a coin was removed at
     * the specified coordinate after this call.
     *
     * @param x the X coordinate to pick up the coin
     * @param y the Y coordinate to pick up the coin
     * @return {@code true} if a coin was removed at the specified coordinate after this call
     */
    protected boolean pickCoin(int x, int y) {
        Iterator<FieldEntity> iterator = fields[y][x].getEntities().iterator();
        while (iterator.hasNext()) {
            FieldEntity entity = iterator.next();
            if (entity instanceof Coin) {
                // if coins already placed in this field, decrease number
                Coin c = (Coin) entity;
                if (c.getCount() > 1) {
                    c.setCount(c.getCount() - 1);
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
     * Places the specified number of coins at the specified coordinate.
     *
     * @param x             the X coordinate of the coin
     * @param y             the Y coordinate of the coin
     * @param numberOfCoins the number of coins to place
     * @throws RuntimeException if the number of coins is smaller than 1
     */
    public void putCoins(int x, int y, int numberOfCoins) {
        checkXCoordinate(x);
        checkYCoordinate(y);
        checkNumberOfCoins(numberOfCoins);
        if (numberOfCoins < 1) {
            throw new RuntimeException("Number of coins must be greater than 0!");
        }

        for (FieldEntity entity : fields[y][x].getEntities()) {
            if (entity instanceof Coin) {
                // if coins already placed in this field, increase number
                Coin c = (Coin) entity;
                c.setCount(c.getCount() + numberOfCoins);
                triggerUpdate();
                return;
            }
        }
        // else place first coin
        Coin c = new Coin(x, y, numberOfCoins);
        fields[y][x].getEntities().add(c);
        triggerUpdate();
    }

    /**
     * Updates the entity array to be in sync with the robots X and Y coordinates.
     *
     * @param r    the robot to sync with
     * @param oldX the old X coordinate of the robot
     * @param oldY the old Y coordinate of the robot
     */
    protected void updateRobotField(Robot r, int oldX, int oldY) {
        if (fields[oldY][oldX].getEntities().removeIf(entity -> entity == r)) {
            fields[r.getY()][r.getX()].getEntities().add(r);
        }
    }

    /**
     * Places a block at the specified coordinate.
     *
     * @param x the X coordinate of the block
     * @param y the Y coordinate of the block
     */
    public void placeBlock(int x, int y) {
        checkXCoordinate(x);
        checkYCoordinate(y);
        if (fields[y][x].getEntities().stream().anyMatch(Block.class::isInstance)) {
            return;
        }
        fields[y][x].getEntities().add(new Block(x, y));
        triggerUpdate();
    }

    /**
     * Adds the specified robot to this world.
     *
     * @param r the robot to place
     */
    public void addRobot(Robot r) {
        fields[r.getY()][r.getX()].getEntities().add(r);
        r.setId(Integer.toString(robotCount));
        robotCount++;
        traces.put(r.getId(), new RobotTrace());
        triggerUpdate();
        sleep();
    }

    /**
     * Places a wall at the specified coordinate.
     *
     * @param x          the X coordinate of the wall
     * @param y          the Y coordinate of the wall
     * @param horizontal if {@code true} horizontal wall will be placed
     */
    private void placeWall(int x, int y, boolean horizontal) {
        checkXCoordinate(x);
        checkYCoordinate(y);
        if (isWallInField(x, y, horizontal)) {
            return;
        }
        fields[y][x].getEntities().add(new Wall(x, y, horizontal));
        triggerUpdate();
    }

    /**
     * Places a horizontal wall at the specified coordinate.
     *
     * @param x the X coordinate of the horizontal wall
     * @param y the Y coordinate of the horizontal wall
     */
    public void placeHorizontalWall(int x, int y) {
        placeWall(x, y, true);
    }

    /**
     * Places a vertical wall at the specified coordinate.
     *
     * @param x the X coordinate of the vertical wall
     * @param y the Y coordinate of the vertical wall
     */
    public void placeVerticalWall(int x, int y) {
        placeWall(x, y, false);
    }

    /**
     * Puts this world to sleep for the specified amount time given by {@link #delay} (in
     * milliseconds).
     */
    protected void sleep() {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
     * Saves the current entity state (fields) of this world.
     */
    private void saveEntityState() {
        if (entityStates == null) {
            entityStates = new LinkedList<>();
        }
        // copy entities
        List<FieldEntity> allEntities = getAllFieldEntities();
        List<FieldEntity> allEntitiesCopy = new LinkedList<>();
        for (FieldEntity fe : allEntities) {
            if (fe instanceof Robot) {
                Robot r = (Robot) fe;
                Robot copy = new Robot(true, r.getX(), r.getY(), r.getDirection(), r.getNumberOfCoins());
                copy.setId(String.valueOf(r.hashCode()));
                allEntitiesCopy.add(copy);
            }

            if (fe instanceof Coin) {
                Coin c = (Coin) fe;
                Coin copy = new Coin(c.getX(), c.getY(), c.getCount());
                allEntitiesCopy.add(copy);
            }

            if (fe instanceof Block) {
                Block b = (Block) fe;
                Block copy = new Block(b.getX(), b.getY());
                allEntitiesCopy.add(copy);
            }

            if (fe instanceof Wall) {
                Wall w = (Wall) fe;
                Wall copy = new Wall(w.getX(), w.getY(), w.isHorizontal());
                allEntitiesCopy.add(copy);
            }
        }
        entityStates.add(new Field(allEntitiesCopy));
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
     * Resets this world by removing all entities from the fields.
     */
    public void reset() {
        Stream.of(fields).flatMap(Stream::of)
            .map(Field::getEntities)
            .forEach(Collection::clear);
        triggerUpdate();
    }

    /**
     * Sets the visibility of the world on the graphical user interface to the specified visibility
     * value.
     *
     * @param visible if {@code true} this world will be visible on the graphical user interface
     */
    public void setVisible(boolean visible) {

        if (visible && guiFrame == null) {
            guiFrame = new JFrame("FopBot");
            guiFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            if (guiGp == null) {
                guiGp = new GuiPanel(this);
            }
            guiFrame.add(guiGp);
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
     * Returns {@code true} if this world is visible on the graphical user interface.
     *
     * @return {@code true} if this world is visible on the graphical user interface.
     */
    public boolean isVisible() {
        return guiFrame != null && guiFrame.isVisible();
    }

    /**
     * Loads robot images.
     *
     * @param turnedOn          the image of the robot turned on
     * @param turnedOff         the image of the robot turned off
     * @param rotationOffsetOn  the rotation offset of the turned on robot in degree
     * @param rotationOffsetOff the rotation offset of the turned off robot in degree
     * @return the loaded robot images
     */
    private Map<String, Image[]> setAndLoadRobotImages(InputStream turnedOn, InputStream turnedOff,
                                                       int rotationOffsetOn, int rotationOffsetOff) {
        Image[] turnedOnImages = null;
        Image[] turnedOffImages = null;

        try {
            turnedOnImages = PaintUtils.loadScaleRotateFieldImage(turnedOn, rotationOffsetOn);
        } catch (IOException e) {
            System.err.println("Could not load robot image! " + turnedOn);
        }

        try {
            turnedOffImages = PaintUtils.loadScaleRotateFieldImage(turnedOff, rotationOffsetOff);
        } catch (IOException e) {
            System.err.println("Could not load robot image! " + turnedOff);
        }

        Map<String, Image[]> imageMap = new HashMap<>();
        imageMap.put("on", turnedOnImages);
        imageMap.put("off", turnedOffImages);
        return imageMap;
    }

    /**
     * Loads robot images for a specific robot class.
     *
     * @param robotClass        the robot class instance
     * @param turnedOn          the image of the robot turned on
     * @param turnedOff         the image of the robot turned off
     * @param rotationOffsetOn  the rotation offset of the turned on robot in degree
     * @param rotationOffsetOff the rotation offset of the turned off robot in degree
     */
    public void setAndLoadRobotImages(Class<? extends Robot> robotClass, InputStream turnedOn, InputStream turnedOff,
                                      int rotationOffsetOn, int rotationOffsetOff) {
        robotImages.put(robotClass, setAndLoadRobotImages(turnedOn, turnedOff, rotationOffsetOn, rotationOffsetOff));
    }

    /**
     * Loads robot images for a specific image identification.
     *
     * @param imageId           the image identification
     * @param turnedOn          the image of the robot turned on
     * @param turnedOff         the image of the robot turned off
     * @param rotationOffsetOn  the rotation offset of the turned on robot in degree
     * @param rotationOffsetOff the rotation offset of the turned off robot in degree
     */
    public void setAndLoadRobotImagesById(String imageId, InputStream turnedOn, InputStream turnedOff,
                                          int rotationOffsetOn, int rotationOffsetOff) {
        robotImagesById.put(imageId, setAndLoadRobotImages(turnedOn, turnedOff, rotationOffsetOn, rotationOffsetOff));
    }

    /**
     * Returns robot image map related to the specified robot class instance.
     *
     * @param robotClass the class instance related to the map
     * @return robot image map related to the specified robot class instance
     * @deprecated use robot families instead
     */
    @Deprecated
    protected Map<String, Image[]> getRobotImageMap(Class<? extends Robot> robotClass) {
        return robotImages.get(robotClass);
    }

    /**
     * Returns robot image map related to the specified image identification.
     *
     * @param imageId the image identification related to the map
     * @return robot image map related to the specified image identification.
     */
    protected Map<String, Image[]> getRobotImageMapById(String imageId) {
        return robotImagesById.get(imageId);
    }

    /**
     * Validates if the specified X coordinate is within the world.
     *
     * @param x the X coordinate to validate
     * @throws RuntimeException if the X coordinate is outside the world borders
     */
    protected void checkXCoordinate(int x) {
        if (x > World.getWidth() - 1 || x < 0) {
            throw new RuntimeException("Invalid x-coordinate: " + x);
        }
    }

    /**
     * Validates if the specified Y coordinate is within the world.
     *
     * @param y the Y coordinate to validate
     * @throws RuntimeException if the Y coordinate is outside the world borders
     */
    protected void checkYCoordinate(int y) {
        if (y > World.getHeight() - 1 || y < 0) {
            throw new RuntimeException("Invalid y-coordinate: " + y);
        }
    }

    /**
     * Validates that the number of coins is not negative.
     *
     * @param numberOfCoins the number of coins to check
     * @throws RuntimeException if the number of coins is negative
     */
    protected void checkNumberOfCoins(int numberOfCoins) {
        if (numberOfCoins < 0) {
            throw new RuntimeException("Number of coins must be greater than -1!");
        }
    }

    /**
     * Traces the action of the specified robot.
     *
     * @param r           the robot to trace
     * @param robotAction the action of the robot to trace
     */
    void trace(Robot r, RobotAction robotAction) {
        var robotTrace = this.traces.get(r.getId());
        robotTrace.trace(r, robotAction);
    }

    /**
     * Returns the previous robot tracing of the specified robot.
     *
     * @param r the robot to retrieve its tracing
     * @return the previous robot tracing of the specified robot
     */
    public RobotTrace getTrace(Robot r) {
        if (r == null) {
            return null;
        }
        var robotTrace = new RobotTrace(this.traces.get(r.getId()));
        robotTrace.trace(r, RobotAction.NONE);
        return robotTrace;
    }

    /**
     * Returns the previous robots tracing.
     *
     * @return the previous robots tracing
     */
    public List<RobotTrace> getTraces() {
        var traces = new ArrayList<RobotTrace>();
        var entities = getAllFieldEntities().stream()
            .filter(Robot.class::isInstance)
            .map(Robot.class::cast)
            .collect(Collectors.toList());
        for (var id : this.traces.keySet()) {
            Robot lastState = entities.stream().filter(en -> en.getId().equals(id)).findAny().orElse(null);
            traces.add(getTrace(lastState));
        }
        return traces;
    }
}
