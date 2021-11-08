package fopbot;

import fopbot.Transition.RobotAction;

import java.awt.*;
import java.io.InputStream;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

public class KarelWorld {

  private static final boolean doScreenshots = false;
  private static final boolean saveStates = true;

  private List<Field> entityStates;

  private final int height;
  private final int width;
  private int robotCount;

  private final Field[][] entities;

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

    entities = new Field[width][height];
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        entities[i][j] = new Field();
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
  }

  /**
   * @return the current delay (in ms)
   */
  public int getDelay() {
    return 0;
  }

  /**
   * @return a list of all field entities at field (x,y)
   */
  protected Field getFieldEntities(int x, int y) {
    return entities[x][y];
  }

  /**
   * @return a list of all field entities in the world
   */
  public List<FieldEntity> getAllFieldEntities() {
    List<FieldEntity> all = new LinkedList<>();
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        Field field = entities[i][j];
        if (field.size() > 0) {
          all.addAll(field.getEntities());
        }
      }
    }
    return all;
  }

  /**
   * @return true if a block is in field (x,y)
   */
  protected boolean isBlockInField(int x, int y) {
    for (FieldEntity ce : entities[x][y]) {
      if (ce instanceof Block) {
        return true;
      }
    }
    return false;
  }

  /**
   * @return true if a wall is in field (x,y) and if wall.isHorizontal() == horizontal
   */
  protected boolean isWallInField(int x, int y, boolean horizontal) {
    for (FieldEntity ce : entities[x][y]) {
      if (ce instanceof Wall) {
        Wall w = (Wall) ce;
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
    for (FieldEntity ce : entities[x][y]) {
      if (ce instanceof Coin) {
        return true;
      }
    }
    return false;
  }

  /**
   * @return true if another robot is in field (x,y)
   */
  protected boolean isAnotherRobotInField(int x, int y, Robot r) {
    for (FieldEntity ce : entities[x][y]) {
      if (ce instanceof Robot) {
        Robot a = (Robot) ce;
        if (a != r) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * @return Tries to remove one coin from the field (x,y) and returns true if a coin got removed
   */
  protected boolean pickCoin(int x, int y) {
    Field field = entities[x][y];
    for (FieldEntity ce : field) {
      if (ce instanceof Coin) {
        // if coins already placed in this field, decrease number
        Coin c = (Coin) ce;
        if (c.getCount() > 1) {
          c.setCount(c.getCount() - 1);
        } else {
          field.remove(ce);
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

    Field field = entities[x][y];
    for (FieldEntity ce : field) {
      if (ce instanceof Coin) {
        // if coins already placed in this field, increase number
        Coin c = (Coin) ce;
        c.setCount(c.getCount() + numberOfCoins);
        triggerUpdate();
        return;
      }
    }
    // else place first coin
    Coin c = new Coin(x, y, numberOfCoins);
    field.add(c);
    triggerUpdate();
  }

  /**
   * Updates the entity array to be in sync with the robots x- and y-Coordinates
   */
  protected void updateRobotField(Robot r, int oldX, int oldY) {
    for (int i = 0; i < entities[oldX][oldY].size(); i++) {
      if (entities[oldX][oldY].get(i) == r) {
        entities[oldX][oldY].remove(i);
        entities[r.getX()][r.getY()].add(r);
        return;
      }
    }
  }

  /**
   * Places a block at field (x,y)
   */
  public void placeBlock(int x, int y) {
    checkXCoordinate(x);
    checkYCoordinate(y);

    Field field = entities[x][y];
    for (FieldEntity ce : field) {
      if (ce instanceof Block) {
        return;
      }
    }
    Block b = new Block(x, y);
    field.add(b);
    triggerUpdate();
  }

  /**
   * Adds a robot to the world
   */
  public void addRobot(Robot r) {
    Field field = entities[r.getX()][r.getY()];
    field.add(r);
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

    Field field = entities[x][y];
    for (FieldEntity ce : field) {
      if (ce instanceof Wall) {
        Wall cew = (Wall) ce;
        if (cew.isHorizontal() == horizontal) {
          return;
        }
      }
    }
    Wall w = new Wall(x, y, horizontal);
    field.add(w);
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
  }

  /**
   * Reset the world (remove all entities)
   */
  public void reset() {
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        entities[i][j].clear();
      }
    }
    triggerUpdate();
  }

  /**
   * If true, show the world's gui
   */
  public void setVisible(boolean visible) {
  }

  /**
   * @return true if the gui is visible
   */
  public boolean isVisible() {
    return false;
  }

  /**
   * Loads robot images for a specific robot class
   */
  public void setAndLoadRobotImages(Class<? extends Robot> robotClass, InputStream turnedOn, InputStream turnedOff,
                                    int rotationOffsetOn, int rotationOffsetOff) {
  }

  /**
   * Loads robot images for a specific image id
   */
  public void setAndLoadRobotImagesById(String imageId, InputStream turnedOn, InputStream turnedOff,
                                        int rotationOffsetOn, int rotationOffsetOff) {
  }

  /**
   * @return a robot image map for a specific robot class
   */
  protected Map<String, Image[]> getRobotImageMap(Class<? extends Robot> robotClass) {
    return Collections.emptyMap();
  }

  /**
   * @return a robot image map for a specific image id
   */
  protected Map<String, Image[]> getRobotImageMapById(String imageId) {
    return Collections.emptyMap();
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
    return null;
  }

  public void setGuiPanel(GuiPanel guiPanel) {
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
