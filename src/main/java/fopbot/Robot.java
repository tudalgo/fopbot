package fopbot;

import fopbot.Transition.RobotAction;

/**
 * A field entity that represents a robot  on a graphical user interface.
 */
public class Robot extends FieldEntity {

    /**
     * The identification of this robot.
     */
    private String id;

    /**
     * The image identification of this robot.
     */
    private String imageId;

    /**
     * The number of coins that this robot currently owns.
     */
    private int numberOfCoins = 0;

    @Override
    public String toString() {
        return "Robot{"
            + "id='" + id + '\''
            + ", at=[" + getX() + '/' + getY()
            + "], numberOfCoins=" + numberOfCoins
            + ", direction=" + direction
            + '}';
    }

    /**
     * The current viewing direction of this robot.
     */
    private Direction direction = Direction.UP;
    /**
     * The indicator whether the console tracing is enabled.
     */
    private boolean printTrace;
    /**
     * The state of the robot, whether it is on or off.
     */
    private boolean off = false;
    /**
     * The world in which this robot is placed.
     */
    private KarelWorld world;

    /**
     * Constructs and initializes a robot at the specified {@code (x,y)} location with the viewing
     * direction {@code UP} and 0 coins.
     *
     * @param x the X coordinate of the newly constructed robot
     * @param y the Y coordinate of the newly constructed robot
     */
    public Robot(int x, int y) {
        super(x, y);
        setGlobalWorld();

        world.checkXCoordinate(x);
        world.checkYCoordinate(y);

        world.addRobot(this);
    }

    /**
     * Constructs and initializes a robot at the specified {@code (x,y)} location, viewing direction
     * and number of coins.
     *
     * @param x             the X coordinate of the newly constructed robot
     * @param y             the Y coordinate of the newly constructed robot
     * @param direction     the viewing direction of the newly constructed robot
     * @param numberOfCoins the number of coins of the newly constructed robot
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
     * Constructs and initializes a robot at the specified {@code (x,y)} location with the viewing
     * direction {@code UP} and 0 coins and places it on the given world.
     *
     * @param world the world to place the newly constructed robot in
     * @param x     the X coordinate of the newly constructed robot
     * @param y     the Y coordinate of the newly constructed robot
     */
    public Robot(KarelWorld world, int x, int y) {
        super(x, y);
        this.world = world;

        world.checkXCoordinate(x);
        world.checkYCoordinate(y);

        world.addRobot(this);
    }

    /**
     * Constructs and initializes a robot at the specified {@code (x,y)} location, viewing direction,
     * number of coins and places it on the given world.
     *
     * @param world         the world to place the newly constructed robot in
     * @param x             the X coordinate of the newly constructed robot
     * @param y             the Y coordinate of the newly constructed robot
     * @param direction     the viewing direction of the newly constructed robot
     * @param numberOfCoins the number of coins of the newly constructed robot
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
     * Constructs and initializes a robot with the properties of the specified robot.
     *
     * @param robot the robot to copy its properties
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
     * Constructs and initializes a robot at the specified {@code (x,y)} location, viewing direction
     * and number of coins.
     *
     * @param copy          if {@code true} then it is a copy constructor
     * @param x             the X coordinate of the newly constructed robot
     * @param y             the Y coordinate of the newly constructed robot
     * @param direction     the viewing direction of the newly constructed robot
     * @param numberOfCoins the number of coins of the newly constructed robot
     */
    protected Robot(boolean copy, int x, int y, Direction direction, int numberOfCoins) {
        super(x, y);
        this.numberOfCoins = numberOfCoins;
        this.direction = direction;
    }

    /**
     * Rotates this robot to the left so that after this call the viewing direction of this robot is
     * the old viewing direction by one to the left (e.g. {@code LEFT} to {@code UP}).
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
            default:
                throw new AssertionError();
        }
        if (printTrace) {
            printTrace();
        }
        world.triggerUpdate();
        world.sleep();
    }

    /**
     * Moves this robot one step forward, in its current viewing direction.
     *
     * @throws RuntimeException if this robot would move outside the world or collide against
     *                          something.
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
            default:
                throw new AssertionError();
        }

        if (printTrace) {
            printTrace();
        }
        world.updateRobotField(this, oldX, oldY);
        world.triggerUpdate();
        world.sleep();
    }

    /**
     * Places a coin at its current position.
     *
     * @throws RuntimeException if this robot has no coin
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
     * Picks up a coin from the current position.
     *
     * @throws RuntimeException if there is no coin on the current position
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
     * Outputs the robot tracing in the console.
     */
    private void printTrace() {
        String onOff = off ? "off" : "on";
        String trace = "Robot(" + this.getClass().getName() + ") is at (" + getX() + "," + getY() + ") facing "
            + direction.toString() + " with " + numberOfCoins + " coins (Turned " + onOff + ").";
        System.out.println(trace);
    }

    /**
     * Returns the current viewing direction of this robot.
     *
     * @return the current viewing direction of this robot
     */
    public Direction getDirection() {
        return direction;
    }

    /**
     * Returns the current number of coins this robot has.
     *
     * @return the current number of coins this robot has
     */
    public int getNumberOfCoins() {
        return numberOfCoins;
    }

    /**
     * Sets the current number of coins this robot has.
     *
     * @param coins The current number of coins this robot has
     */
    public void setNumberOfCoins(int coins) {
        world.trace(this, RobotAction.SET_NUMBER_OF_COINS);
        this.numberOfCoins = coins;
    }

    /**
     * Returns {@code true} if this robot has any coins.
     *
     * @return {@code true} if this robot has any coins
     */
    public boolean hasAnyCoins() {
        return numberOfCoins > 0;
    }

    /**
     * Returns {@code true} if the current viewing direction of this robot is up.
     *
     * @return {@code true} if the current viewing direction of this robot is up
     */
    public boolean isFacingUp() {
        return direction == Direction.UP;
    }

    /**
     * Returns {@code true} if the current viewing direction of this robot is down.
     *
     * @return {@code true} if the current viewing direction of this robot is down
     */
    public boolean isFacingDown() {
        return direction == Direction.DOWN;
    }

    /**
     * Returns {@code true} if the current viewing direction of this robot is left.
     *
     * @return {@code true} if the current viewing direction of this robot is left
     */
    public boolean isFacingLeft() {
        return direction == Direction.LEFT;
    }

    /**
     * Returns {@code true} if the current viewing direction of this robot is right.
     *
     * @return {@code true} if the current viewing direction of this robot is right
     */
    public boolean isFacingRight() {
        return direction == Direction.RIGHT;
    }

    /**
     * Enables or disables robot tracing in the console.
     *
     * @param printTrace if {@code true} enables the robot tracing
     */
    public void setPrintTrace(boolean printTrace) {
        this.printTrace = printTrace;
    }

    /**
     * Returns {@code true} if robot tracing is enabled.
     *
     * @return {@code true} if robot tracing is enabled
     */
    public boolean isPrintTraceEnabled() {
        return printTrace;
    }

    /**
     * Turn off this robot.
     */
    public void turnOff() {
        world.trace(this, RobotAction.TURN_OFF);
        off = true;
        world.triggerUpdate();
    }

    /**
     * Crashes the robot. Never terminates normally and always throws an exception.
     *
     * @throws RuntimeException this robot can no longer perform any action
     */
    protected void crash() {
        turnOff();
        System.err.println("Robot crashed!");
        throw new RuntimeException("Robot crashed!");
    }

    /**
     * Returns {@code true} if this robot is turned off.
     *
     * @return {@code true} if this robot is turned off
     */
    public boolean isTurnedOff() {
        return off;
    }

    /**
     * Returns {@code true} if this robot is turned on.
     *
     * @return {@code true} if this robot is turned on
     */
    public boolean isTurnedOn() {
        return !off;
    }

    /**
     * Returns {@code true} if this robot is on a coin, more precisely if the current position of this
     * robot is equal to the position of a coin.
     *
     * @return {@code true} if this robot is on a coin
     */
    public boolean isOnACoin() {
        return world.isCoinInField(getX(), getY());
    }

    /**
     * Returns {@code true} if this robot is on a coin, more precisely if the current position of this
     * robot is equal to the position of a coin.
     *
     * @return {@code true} if this robot is on a coin
     * @deprecated Confusing name, use {@link #isOnACoin()} instead.
     */
    @Deprecated(since = "0.3.0")
    public boolean isNextToACoin() {
        return isOnACoin();
    }

    /**
     * Returns {@code true} if this robot is on another robot, more precisely if the current position
     * of this robot is equal to the position of the other robot.
     *
     * @return {@code true} if this robot is on another robot
     */
    public boolean isOnAnotherRobot() {
        return world.isAnotherRobotInField(getX(), getY(), this);
    }

    /**
     * Returns {@code true} if this robot is on another robot, more precisely if the current position
     * of this robot is equal to the position of the other robot.
     *
     * @return {@code true} if this robot is on another robot
     * @deprecated Confusing name, use {@link #isOnAnotherRobot()} instead.
     */
    @Deprecated(since = "0.3.0")
    public boolean isNextToARobot() {
        return isOnAnotherRobot();
    }

    /**
     * Sets the identification of the robot to the specified identification.
     *
     * @param id the new identification for this robot
     */
    protected void setId(String id) {
        this.id = id;
    }

    /**
     * Returns the identification of this robot.
     *
     * @return the identification of this robot
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the image identification  of the robot to the specified image identification.
     *
     * @param id the new image identification for this robot
     */
    protected void setImageId(String id) {
        this.imageId = id;
    }

    /**
     * Returns the image identification of this robot.
     *
     * @return the image identification of this robot
     */
    protected String getImageId() {
        return imageId;
    }

    @Override
    public void setX(int x) {
        world.trace(this, RobotAction.SET_X);
        setXRobot(x);
    }

    /**
     * Sets the X coordinate of the robot to the specified X coordinate.
     *
     * @param x the new X coordinate for this robot
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

    @Override
    public void setY(int y) {
        world.trace(this, RobotAction.SET_Y);
        setYRobot(y);
    }

    /**
     * Sets the Y coordinate of the robot to the specified Y coordinate.
     *
     * @param y the new Y coordinate for this robot
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
     * Sets the X and Y coordinate of the robot to the specified X and Y coordinate.
     *
     * @param x the new X coordinate for this robot
     * @param y the new Y coordinate for this robot
     */
    public void setField(int x, int y) {
        world.trace(this, RobotAction.SET_X);
        world.trace(this, RobotAction.SET_Y);

        world.checkXCoordinate(x);
        world.checkYCoordinate(y);

        if (off) {
            return;
        }

        final int oldX = getX();
        final int oldY = getY();

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
     * Returns {@code true} if there is no object in front of this robot, which can collide with it at
     * the next step (e.g. walls).
     *
     * @return {@code true} if there is no object in front of this robot
     */
    public boolean isFrontClear() {
        final int x = getX();
        final int y = getY();

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
            default:
                throw new AssertionError();
        }

        return true;
    }

    /**
     * Sets the global world in which this robot is placed itself.
     *
     * @throws RuntimeException if there exists no global world
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
