package fopbot;

import fopbot.Transition.RobotAction;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class KarelWorld {

  private static final boolean doScreenshots = false;
  private static final boolean saveStates = true;

  private List<Field> entityStates;

  private final int height;
  private final int width;
  private int delay = 100;
  private int robotCount;

  private JFrame guiFrame;
  private GuiPanel guiGp;

  private final Field[][] fields;

  private final Map<Class<? extends Robot>, Map<String, Image[]>> robotImages;
  private final Map<String, Map<String, Image[]>> robotImagesById;
  private final Map<String, RobotTrace> traces = new HashMap<>();

  /**
   * Creates a new world with the given width and height
   *
   * @param width  the width of the world
   * @param height the height of the world
   */
  public KarelWorld(int width, int height) {
    if (width < 1 || height < 1) {
      throw new RuntimeException("Invalid world size: " + width + "x" + height);
    }

    this.height = height;
    this.width = width;

    robotImages = new HashMap<>();
    setAndLoadRobotImages(Robot.class, getClass().getResourceAsStream("/trianglebot.png"),
      getClass().getResourceAsStream("/trianglebot.png"), 0, 0);
    robotImagesById = new HashMap<>();

    fields = new Field[height][width];
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < width; x++) {
        fields[y][x] = new Field();
      }
    }
  }

  /**
   * @return the worlds height
   */
  public int getHeight() {
    return height;
  }

  /**
   * @return the worlds width
   */
  public int getWidth() {
    return width;
  }

  /**
   * Sets the delay a robot has to wait after each action
   *
   * @param delay (in ms)
   */
  public void setDelay(int delay) {
    this.delay = delay;
  }

  /**
   * @return the current delay (in ms)
   */
  public int getDelay() {
    return delay;
  }

  /**
   * @return a list of all field entities at field (x,y)
   */
  protected Field getField(int x, int y) {
    return fields[y][x];
  }

  /**
   * @return a list of all field entities in the world
   */
  public List<FieldEntity> getAllFieldEntities() {
    List<FieldEntity> all = new LinkedList<>();
    for (int y = 0; y < height; y++) {
      for (int x = 0; x < height; x++) {
        all.addAll(fields[y][x].getEntities());
      }
    }
    return all;
  }

  /**
   * @return true if a block is in field (x,y)
   */
  protected boolean isBlockInField(int x, int y) {
    for (FieldEntity entity : fields[y][x].getEntities()) {
      if (entity instanceof Block) {
        return true;
      }
    }
    return false;
  }

  /**
   * @return true if a wall is in field (x,y) and if wall.isHorizontal() == horizontal
   */
  protected boolean isWallInField(int x, int y, boolean horizontal) {
    for (FieldEntity entity : fields[y][x].getEntities()) {
      if (entity instanceof Wall) {
        Wall w = (Wall) entity;
        if (w.isHorizontal() == horizontal) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * @return true if at least one coin is in field (x,y)
   */
  protected boolean isCoinInField(int x, int y) {
    return fields[y][x].getEntities().stream().anyMatch(Coin.class::isInstance);
  }

  /**
   * @return true if another robot is in field (x,y)
   */
  protected boolean isAnotherRobotInField(int x, int y, Robot r) {
    return fields[y][x].getEntities().stream().anyMatch(e -> e instanceof Robot && e != r);
  }

  /**
   * @return Tries to remove one coin from the field (x,y) and returns true if a coin got removed
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
   * Puts down N coins at field (x,y)
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
   * Updates the entity array to be in sync with the robots x- and y-Coordinates
   */
  protected void updateRobotField(Robot r, int oldX, int oldY) {
    if (fields[oldY][oldX].getEntities().removeIf(entity -> entity == r)) {
      fields[r.getY()][r.getX()].getEntities().add(r);
    }
  }

  /**
   * Places a block at field (x,y)
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
   * Adds a robot to the world
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
   * Places a wall at field (x,y) with (wall.getHorizontal() == horizontal) ==
   * true
   */
  private void placeWall(int x, int y, boolean horizontal) {
    checkXCoordinate(x);
    checkYCoordinate(y);
    if (fields[y][x].getEntities().stream()
      .anyMatch(e -> e instanceof Wall && ((Wall) e).isHorizontal() == horizontal)) {
      return;
    }
    fields[y][x].getEntities().add(new Wall(x, y, horizontal));
    triggerUpdate();
  }

  /**
   * Places a horizontal wall at field (x,y)
   */
  public void placeHorizontalWall(int x, int y) {
    placeWall(x, y, true);
  }

  /**
   * Places a vertical wall at field (x,y)
   */
  public void placeVerticalWall(int x, int y) {
    placeWall(x, y, false);
  }

  /**
   * Sleep for delay ms
   */
  protected void sleep() {
    try {
      Thread.sleep(delay);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  /**
   * @return all entity states
   */
  public List<Field> getEntityStates() {
    return entityStates;
  }

  /**
   * Saves the current entity state
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
   * Triggers an update of the gui's window
   */
  protected void triggerUpdate() {
    if (saveStates) {
      saveEntityState();
    }

    updateGui();
  }

  /**
   * Updates the gui's window
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
   * Reset the world (remove all entities)
   */
  public void reset() {
    Stream.of(fields).flatMap(Stream::of).map(Field::getEntities).forEach(Collection::clear);
    triggerUpdate();
  }

  /**
   * If true, show the world's gui
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
   * @return true if the gui is visible
   */
  public boolean isVisible() {
    return guiFrame != null && guiFrame.isVisible();
  }

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
   * Loads robot images for a specific robot class
   */
  public void setAndLoadRobotImages(Class<? extends Robot> robotClass, InputStream turnedOn, InputStream turnedOff,
                                    int rotationOffsetOn, int rotationOffsetOff) {
    robotImages.put(robotClass, setAndLoadRobotImages(turnedOn, turnedOff, rotationOffsetOn, rotationOffsetOff));
  }

  /**
   * Loads robot images for a specific image id
   */
  public void setAndLoadRobotImagesById(String imageId, InputStream turnedOn, InputStream turnedOff,
                                        int rotationOffsetOn, int rotationOffsetOff) {
    robotImagesById.put(imageId, setAndLoadRobotImages(turnedOn, turnedOff, rotationOffsetOn, rotationOffsetOff));
  }

  /**
   * @return a robot image map for a specific robot class
   */
  protected Map<String, Image[]> getRobotImageMap(Class<? extends Robot> robotClass) {
    return robotImages.get(robotClass);
  }

  /**
   * @return a robot image map for a specific image id
   */
  protected Map<String, Image[]> getRobotImageMapById(String imageId) {
    return robotImagesById.get(imageId);
  }

  /**
   * Validates a given x-coordinate
   */
  protected void checkXCoordinate(int x) {
    if (x > World.getWidth() - 1 || x < 0) {
      throw new RuntimeException("Invalid x-coordinate: " + x);
    }
  }

  /**
   * Validates a given y-coordinate
   */
  protected void checkYCoordinate(int y) {
    if (y > World.getHeight() - 1 || y < 0) {
      throw new RuntimeException("Invalid y-coordinate: " + y);
    }
  }

  /**
   * Validates numberOfCoins
   */
  protected void checkNumberOfCoins(int numberOfCoins) {
    if (numberOfCoins < 0) {
      throw new RuntimeException("Number of coins must be greater than -1!");
    }
  }

  public GuiPanel getGuiPanel() {
    return guiGp;
  }

  public void setGuiPanel(GuiPanel guiPanel) {
    this.guiGp = guiPanel;
  }

  void trace(Robot r, RobotAction robotAction) {
    var robotTrace = this.traces.get(r.getId());
    robotTrace.trace(r, robotAction);
  }

  public RobotTrace getTrace(Robot r) {
    if (r == null) {
      return null;
    }
    var robotTrace = new RobotTrace(this.traces.get(r.getId()));
    robotTrace.trace(r, RobotAction.NONE);
    return robotTrace;
  }

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
