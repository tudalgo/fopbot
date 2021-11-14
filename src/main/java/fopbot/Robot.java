package fopbot;

import fopbot.Transition.RobotAction;

public class Robot extends FieldEntity {

  private String id;
  private String imageId;
  private int numberOfCoins = 0;

  @Override
  public String toString() {
    return "Robot{" +
      "id='" + id + '\'' +
      ", at=[" + getX() + '/' + getY() +
      "], numberOfCoins=" + numberOfCoins +
      ", direction=" + direction +
      '}';
  }

  private Direction direction = Direction.UP;
  private boolean printTrace;
  private boolean off = false;
  private KarelWorld world;

  /**
   * Creates and spawns a new robot at field (x,y) in the world Default direction: NORTH ; numberOfCoins = 0
   */
  public Robot(int x, int y) {
    super(x, y);
    setGlobalWorld();

    world.checkXCoordinate(x);
    world.checkYCoordinate(y);

    world.addRobot(this);
  }

  /**
   * Creates and spawns a new robot at field (x,y) in the world
   */
  public Robot(int x, int y, Direction direction, int numberOfCoins) {
    super(x, y);
    this.numberOfCoins = numberOfCoins;
    this.direction = direction;
    setGlobalWorld();

    world.checkXCoordinate(x);
    world.checkYCoordinate(y);
    world.checkNumberOfCoins(numberOfCoins);

    world.addRobot(this);
  }

  /**
   * Creates and spawns a new robot at field (x,y) in the given world
   */
  public Robot(KarelWorld world, int x, int y) {
    super(x, y);
    this.world = world;

    world.checkXCoordinate(x);
    world.checkYCoordinate(y);

    world.addRobot(this);
  }

  /**
   * Creates and spawns a new robot at field (x,y) in the given world
   */
  public Robot(KarelWorld world, int x, int y, Direction direction, int numberOfCoins) {
    super(x, y);
    this.numberOfCoins = numberOfCoins;
    this.direction = direction;
    this.world = world;

    world.checkXCoordinate(x);
    world.checkYCoordinate(y);
    world.checkNumberOfCoins(numberOfCoins);

    world.addRobot(this);
  }

  /**
   * Copy constructor
   */
  protected Robot(Robot robot) {
    super(robot.getX(), robot.getY());
    this.numberOfCoins = robot.numberOfCoins;
    this.direction = robot.direction;
    this.id = robot.id;
    this.imageId = robot.imageId;
    this.printTrace = robot.printTrace;
    this.off = robot.off;
    this.world = robot.world;
  }

  /**
   * Copy constructor
   */
  protected Robot(boolean copy, int x, int y, Direction direction, int numberOfCoins) {
    super(x, y);
    this.numberOfCoins = numberOfCoins;
    this.direction = direction;
  }

  /**
   * Turns the robots body to the left
   */
  public void turnLeft() {
    world.trace(this, RobotAction.TURN_LEFT);
    if (off) {
      return;
    }

    switch (direction) {
      case UP:
        direction = Direction.LEFT;
        break;
      case LEFT:
        direction = Direction.DOWN;
        break;
      case DOWN:
        direction = Direction.RIGHT;
        break;
      case RIGHT:
        direction = Direction.UP;
        break;
    }
    if (printTrace) {
      printTrace();
    }
    world.triggerUpdate();
    world.sleep();
  }

  /**
   * Move the robot one step forward in the current direction
   */
  public void move() {
    world.trace(this, RobotAction.MOVE);
    if (off) {
      return;
    }

    if (!isFrontClear()) {
      crash();
    }

    int oldX = getX();
    int oldY = getY();

    switch (direction) {
      case UP:
        setYRobot(getY() + 1);
        break;
      case LEFT:
        setXRobot(getX() - 1);
        break;
      case DOWN:
        setYRobot(getY() - 1);
        break;
      case RIGHT:
        setXRobot(getX() + 1);
        break;
    }

    if (printTrace) {
      printTrace();
    }
    world.updateRobotField(this, oldX, oldY);
    world.triggerUpdate();
    world.sleep();
  }

  /**
   * Puts down one coin at the current position
   */
  public void putCoin() {
    world.trace(this, RobotAction.PUT_COIN);
    if (off) {
      return;
    }
    if (numberOfCoins > 0) {
      numberOfCoins--;
      world.putCoins(getX(), getY(), 1);
    } else {
      crash();
    }
    world.sleep();
  }

  /**
   * Picks up one coin at the current position
   */
  public void pickCoin() {
    world.trace(this, RobotAction.PICK_COIN);
    if (off) {
      return;
    }
    if (world.pickCoin(getX(), getY())) {
      numberOfCoins++;
    } else {
      crash();
    }
    world.sleep();
  }

  /**
   * Prints the robot trace
   */
  private void printTrace() {
    String onOff = off ? "off" : "on";
    String trace = "Robot(" + this.getClass().getName() + ") is at (" + getX() + "," + getY() + ") facing "
      + direction.toString() + " with " + numberOfCoins + " coins (Turned " + onOff + ").";
    System.out.println(trace);
  }

  /**
   * @return the current direction of the robot
   */
  public Direction getDirection() {
    return direction;
  }

  /**
   * @return the number of coins the robot possesses
   */
  public int getNumberOfCoins() {
    return numberOfCoins;
  }

  /**
   * @return true if the robot has any coins
   */
  public boolean hasAnyCoins() {
    return numberOfCoins > 0;
  }

  /**
   * @return true if the robot is facing up
   */
  public boolean isFacingUp() {
    return direction == Direction.UP;
  }

  /**
   * @return true if the robot is facing down
   */
  public boolean isFacingDown() {
    return direction == Direction.DOWN;
  }

  /**
   * @return true if the robot is facing left
   */
  public boolean isFacingLeft() {
    return direction == Direction.LEFT;
  }

  /**
   * @return true if the robot is facing right
   */
  public boolean isFacingRight() {
    return direction == Direction.RIGHT;
  }

  /**
   * enables/disables printing a trace to System.out after each action of the
   * robot
   */
  public void setPrintTrace(boolean printTrace) {
    this.printTrace = printTrace;
  }

  /**
   * @return true if print tracing is enabled
   */
  public boolean isPrintTraceEnabled() {
    return printTrace;
  }

  /**
   * Turn off the robot
   */
  public void turnOff() {
    world.trace(this, RobotAction.TURN_OFF);
    off = true;
    world.triggerUpdate();
  }

  /**
   * Crashes the robot. Never terminates normally and always throws an exception.
   */
  protected void crash() {
    turnOff();
    System.err.println("Robot crashed!");
    throw new RuntimeException("Robot crashed!");
  }

  /**
   * @return true if the robot is turned off
   */
  public boolean isTurnedOff() {
    return off;
  }

  /**
   * @return true if the robot is turned on
   */
  public boolean isTurnedOn() {
    return !off;
  }

  /**
   * @return true if the robot is standing on a coin/if at least one coin is at
   * the same position as the robot's position
   */
  public boolean isOnACoin() {
    return world.isCoinInField(getX(), getY());
  }

  /**
   * @return true if the robot is standing on a coin/if at least one coin is at
   * the same position as the robot's position
   * @deprecated Confusing name, use {@link #isOnACoin()} instead.
   */
  @Deprecated(since = "0.3.0")
  public boolean isNextToACoin() {
    return isOnACoin();
  }

  /**
   * @return true if at least another robot is at the same position as the robot's position
   */
  public boolean isOnAnotherRobot() {
    return world.isAnotherRobotInField(getX(), getY(), this);
  }

  /**
   * @return true if at least another robot is at the same position as the robot's
   * position
   * @deprecated Confusing name, use {@link #isOnAnotherRobot()} instead.
   */
  @Deprecated(since = "0.3.0")
  public boolean isNextToARobot() {
    return isOnAnotherRobot();
  }

  /**
   * Sets the id of the robot
   */
  protected void setId(String id) {
    this.id = id;
  }

  /**
   * @return the id of the robot
   */
  public String getId() {
    return id;
  }

  /**
   * Sets the image id of the robot
   */
  protected void setImageId(String id) {
    this.imageId = id;
  }

  /**
   * @return the image id of the robot
   */
  protected String getImageId() {
    return imageId;
  }

  public void setX(int x) {
    world.trace(this, RobotAction.SET_X);
    setXRobot(x);
  }

  /**
   * Sets the robot's x-coordinate
   */
  private void setXRobot(int x) {
    world.checkXCoordinate(x);
    if (off) {
      return;
    }
    int oldX = getX();
    super.setX(x);
    if (printTrace) {
      printTrace();
    }
    world.updateRobotField(this, oldX, getY());
    world.triggerUpdate();
    world.sleep();
  }

  public void setY(int y) {
    world.trace(this, RobotAction.SET_Y);
    setYRobot(y);
  }

  /**
   * Sets the robot's y-coordinate
   */
  private void setYRobot(int y) {
    world.checkYCoordinate(y);

    if (off) {
      return;
    }

    int oldY = getY();

    super.setY(y);

    if (printTrace) {
      printTrace();
    }
    world.updateRobotField(this, getX(), oldY);
    world.triggerUpdate();
    world.sleep();
  }

  /**
   * Sets the robot's field (x,y)
   */
  public void setField(int x, int y) {
    world.trace(this, RobotAction.SET_X);
    world.trace(this, RobotAction.SET_Y);
    
    world.checkXCoordinate(x);
    world.checkYCoordinate(y);

    if (off) {
      return;
    }

    int oldX = getX();
    int oldY = getY();

    super.setX(x);
    super.setY(y);

    if (printTrace) {
      printTrace();
    }
    world.updateRobotField(this, oldX, oldY);
    world.triggerUpdate();
    world.sleep();
  }

  /**
   * @return true if the robot is not facing an object that is standing in the way (eg. wall)
   */
  public boolean isFrontClear() {
    int x = getX();
    int y = getY();

    switch (direction) {
      case UP:
        if (y == world.getHeight() - 1) {
          return false;
        }
        if (world.isBlockInField(x, y + 1)) {
          return false;
        }
        if (world.isWallInField(x, y, true)) {
          return false;
        }
        break;
      case LEFT:
        if (x == 0) {
          return false;
        }
        if (world.isBlockInField(x - 1, y)) {
          return false;
        }
        if (world.isWallInField(x - 1, y, false)) {
          return false;
        }
        break;
      case DOWN:
        if (y == 0) {
          return false;
        }
        if (world.isBlockInField(x, y - 1)) {
          return false;
        }
        if (world.isWallInField(x, y - 1, true)) {
          return false;
        }
        break;
      case RIGHT:
        if (x == world.getWidth() - 1) {
          return false;
        }
        if (world.isBlockInField(x + 1, y)) {
          return false;
        }
        if (world.isWallInField(x, y, false)) {
          return false;
        }
        break;
    }

    return true;
  }

  /**
   * Global world setter
   */
  private void setGlobalWorld() {
    if (World.isGlobal()) {
      this.world = World.getGlobalWorld();
    } else {
      System.err.println("No global world initialized! Cannot create robot!");
      throw new RuntimeException("No global world initialized! Cannot create robot!");
    }
  }
}
