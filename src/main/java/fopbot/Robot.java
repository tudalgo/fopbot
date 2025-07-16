package fopbot;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a robot entity placed on the field in the virtual world which can interact with its environment.
 */
public class Robot extends FieldEntity {

    /**
     * The default direction the robot faces when created.
     */
    private static final @NotNull Direction DEFAULT_DIRECTION = Direction.UP;

    /**
     * The default number of coins a robot starts with.
     */
    private static final int DEFAULT_COINS = 0;

    /**
     * The default visual representation of the robot.
     */
    private static final @NotNull RobotFamily DEFAULT_ROBOT_FAMILY = RobotFamily.TRIANGLE_BLUE;

    /**
     * The unique identifier of the robot.
     */
    private @Nullable String id;

    /**
     * The visual representation (appearance) of the robot.
     */
    private @NotNull RobotFamily robotFamily;

    /**
     * The number of coins currently held by the robot.
     */
    private int numberOfCoins = 0;

    /**
     * The direction the robot is currently facing.
     */
    private @NotNull Direction direction = DEFAULT_DIRECTION;

    /**
     * Indicates whether to print the robot's trace after each action.
     */
    private boolean printTrace;

    /**
     * Indicates whether  is on ({@code false}) or off ({@code true}).
     *
     * <p>A robot that is off does not perform any actions and cannot interact with the world.
     */
    private boolean off = false;

    /**
     * The virtual world this robot is placed in.
     */
    private @NotNull KarelWorld world;


    /**
     * Constructs a new {@code Robot} at the given position using the default {@link RobotFamily}.
     *
     * @param x the initial x-coordinate
     * @param y the initial y-coordinate
     */
    public Robot(final int x, final int y) {
        this(x, y, DEFAULT_ROBOT_FAMILY);
    }

    /**
     * Constructs a new {@code Robot} at the given position with the given {@link RobotFamily}.
     *
     * @param x           the initial x-coordinate
     * @param y           the initial y-coordinate
     * @param robotFamily the robot's visual representation
     */
    public Robot(final int x, final int y, final @NotNull RobotFamily robotFamily) {
        this(World.getGlobalWorld(), x, y, DEFAULT_DIRECTION, DEFAULT_COINS, robotFamily);
    }

    /**
     * Constructs a new {@code Robot} with the given direction and number of coins.
     *
     * @param x             the initial x-coordinate
     * @param y             the initial y-coordinate
     * @param direction     the initial facing direction
     * @param numberOfCoins the initial number of coins the robot carries
     */
    public Robot(final int x, final int y, final @NotNull Direction direction, final int numberOfCoins) {
        this(x, y, direction, numberOfCoins, DEFAULT_ROBOT_FAMILY);
    }

    /**
     * Constructs a new {@code Robot} with direction, coin count, and {@link RobotFamily}.
     *
     * @param x             the initial x-coordinate
     * @param y             the initial y-coordinate
     * @param direction     the initial facing direction
     * @param numberOfCoins the initial number of coins
     * @param robotFamily   the robot's appearance
     */
    public Robot(
        final int x,
        final int y,
        final @NotNull Direction direction,
        final int numberOfCoins,
        final @NotNull RobotFamily robotFamily
    ) {
        this(World.getGlobalWorld(), x, y, direction, numberOfCoins, robotFamily);
    }

    /**
     * Constructs a {@code Robot} assigned to a specific {@link KarelWorld}.
     *
     * @param world the world the robot belongs to
     * @param x     the x-coordinate
     * @param y     the y-coordinate
     */
    public Robot(final @NotNull KarelWorld world, final int x, final int y) {
        this(world, x, y, DEFAULT_ROBOT_FAMILY);
    }

    /**
     * Constructs a {@code Robot} with a given {@link KarelWorld} and {@link RobotFamily}.
     *
     * @param world       the world the robot is placed in
     * @param x           the x-coordinate
     * @param y           the y-coordinate
     * @param robotFamily the robot's appearance
     */
    public Robot(final @NotNull KarelWorld world, final int x, final int y, final @NotNull RobotFamily robotFamily) {
        this(world, x, y, DEFAULT_DIRECTION, DEFAULT_COINS, robotFamily);
    }

    /**
     * Constructs a {@code Robot} with direction and coin count in a specific world.
     *
     * @param world         the world the robot is placed in
     * @param x             the x-coordinate
     * @param y             the y-coordinate
     * @param direction     the initial direction
     * @param numberOfCoins the initial number of coins
     */
    public Robot(
        final @NotNull KarelWorld world,
        final int x,
        final int y,
        final @NotNull Direction direction,
        final int numberOfCoins
    ) {
        this(world, x, y, direction, numberOfCoins, DEFAULT_ROBOT_FAMILY);
    }

    /**
     * Constructs a {@code Robot} with full configuration in a specific world.
     *
     * @param world         the world the robot is placed in
     * @param x             the x-coordinate
     * @param y             the y-coordinate
     * @param direction     the initial direction
     * @param numberOfCoins the initial number of coins
     * @param robotFamily   the robot's appearance
     */
    public Robot(
        final @NotNull KarelWorld world,
        final int x,
        final int y,
        final @NotNull Direction direction,
        final int numberOfCoins,
        final @NotNull RobotFamily robotFamily
    ) {
        super(x, y);
        this.world = world;
        this.direction = direction;
        this.numberOfCoins = numberOfCoins;
        this.robotFamily = robotFamily;
        setRobotFamily(robotFamily);

        world.checkXCoordinate(x);
        world.checkYCoordinate(y);
        world.checkNumberOfCoins(numberOfCoins);
        world.addRobot(this);
    }

    /**
     * Creates a copy of another {@code Robot}.
     *
     * @param robot the robot to copy
     */
    protected Robot(final @NotNull Robot robot) {
        super(robot.getX(), robot.getY());
        this.numberOfCoins = robot.numberOfCoins;
        this.direction = robot.direction;
        this.id = robot.id;
        this.printTrace = robot.printTrace;
        this.off = robot.off;
        this.world = robot.world;
        this.robotFamily = robot.robotFamily;
    }

    /**
     * Internal constructor for copying configuration manually.
     * Used to make a copy of a roboto without visual representation.
     *
     * @param copy          flag indicating if this is a copy operation
     * @param x             the x-coordinate
     * @param y             the y-coordinate
     * @param direction     the initial direction
     * @param numberOfCoins the number of coins
     */
    protected Robot(final boolean copy, final int x, final int y, final @NotNull Direction direction, final int numberOfCoins) {
        super(x, y);
        this.numberOfCoins = numberOfCoins;
        this.direction = direction;
    }

    /**
     * Causes the robot to crash.
     *
     * <p>This is called when the robot performs an invalid action.
     *
     * @throws RuntimeException always, indicating the robot has crashed
     */
    protected void crash() {
        turnOff();
        System.err.println("Robot crashed!");
        throw new RuntimeException("Robot crashed!");
    }

    /**
     * Moves the robot to a new field, updating both x and y coordinates.
     *
     * @param x the new x-coordinate
     * @param y the new y-coordinate
     */
    public void setField(final int x, final int y) {
        world.trace(this, Transition.RobotAction.SET_X);
        world.trace(this, Transition.RobotAction.SET_Y);

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
     * Prints the robot's current state to the console in human-readable form.
     * This is used internally when {@link #printTrace} is enabled.
     */
    private void printTrace() {
        final String onOff = off ? "off" : "on";
        final String trace = "Robot(" + this.getClass().getName() + ") is at (" + getX() + "," + getY()
            + ") facing " + direction + " with " + numberOfCoins + " coins (Turned " + onOff + ").";
        System.out.println(trace);
    }

    @Override
    public void setX(final int x) {
        world.trace(this, Transition.RobotAction.SET_X);
        setXRobot(x);
    }

    /**
     * Updates the robot’s x-coordinate internally, with full validation and redraw.
     *
     * @param x the new x-coordinate
     */
    private void setXRobot(final int x) {
        world.checkXCoordinate(x);
        if (off) {
            return;
        }

        final int oldX = getX();
        super.setX(x);

        if (printTrace) {
            printTrace();
        }

        world.updateRobotField(this, oldX, getY());
        world.triggerUpdate();
        world.sleep();
    }

    @Override
    public void setY(final int y) {
        world.trace(this, Transition.RobotAction.SET_Y);
        setYRobot(y);
    }

    /**
     * Turns the robot 90 degrees to the left (counter-clockwise).
     */
    public void turnLeft() {
        world.trace(this, Transition.RobotAction.TURN_LEFT);
        if (off) {
            return;
        }

        direction = switch (direction) {
            case UP -> Direction.LEFT;
            case LEFT -> Direction.DOWN;
            case DOWN -> Direction.RIGHT;
            case RIGHT -> Direction.UP;
        };

        if (printTrace) {
            printTrace();
        }

        world.triggerUpdate();
        world.sleep();
    }

    /**
     * Updates the robot’s y-coordinate internally, with full validation and redraw.
     *
     * @param y the new y-coordinate
     */
    private void setYRobot(final int y) {
        world.checkYCoordinate(y);
        if (off) {
            return;
        }

        final int oldY = getY();
        super.setY(y);

        if (printTrace) {
            printTrace();
        }

        world.updateRobotField(this, getX(), oldY);
        world.triggerUpdate();
        world.sleep();
    }

    /**
     * Places a coin on the current field and decreases the robot's coin count by one.
     *
     * @throws RuntimeException if the robot has no coins to place
     */
    public void putCoin() {
        world.trace(this, Transition.RobotAction.PUT_COIN);
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
     * Picks up a coin from the current field and increases the robot's coin count by one.
     *
     * @throws RuntimeException if there is no coin on the field
     */
    public void pickCoin() {
        world.trace(this, Transition.RobotAction.PICK_COIN);
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
     * Checks whether the field in front of the robot is free.
     *
     * <p>A field is considered free if it is passable and within the world boundaries.
     *
     * @return {@code true} if the robot can move forward; {@code false} otherwise
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
     * Checks whether the robot is currently on a field that contains at least one coin.
     *
     * @return {@code true} if there is a coin at the robot's position
     */
    public boolean isOnACoin() {
        return world.isCoinInField(getX(), getY());
    }

    /**
     * Checks whether the robot is currently on a field that contains at least one coin.
     *
     * @return {@code true} if there is a coin at the robot's position
     * @deprecated Confusing method name; use {@link #isOnACoin()} instead.
     */
    @Deprecated(since = "0.3.0")
    public boolean isNextToACoin() {
        return isOnACoin();
    }

    /**
     * Checks whether there is another robot occupying the same field.
     *
     * @return {@code true} if another robot is on the same field
     */
    public boolean isOnAnotherRobot() {
        return world.isAnotherRobotInField(getX(), getY(), this);
    }

    /**
     * Checks whether there is another robot occupying the same field.
     *
     * @return {@code true} if another robot is on the same field
     * @deprecated Confusing method name; use {@link #isOnAnotherRobot()} instead.
     */
    @Deprecated(since = "0.3.0")
    public boolean isNextToARobot() {
        return isOnAnotherRobot();
    }

    /**
     * Returns the number of coins the robot is currently carrying.
     *
     * @return the number of coins the robot is currently carrying
     */
    public int getNumberOfCoins() {
        return numberOfCoins;
    }

    /**
     * Moves the robot one field forward in the direction it is currently facing.
     *
     * @throws RuntimeException if the robot cannot move forward due to a non-passable field or is out of world bounds
     */
    public void move() {
        world.trace(this, Transition.RobotAction.MOVE);
        if (off) {
            return;
        }

        if (!isFrontClear()) {
            crash();
        }

        final int oldX = getX();
        final int oldY = getY();
        final int newX = switch (direction) {
            case UP -> oldX;
            case LEFT -> oldX - 1;
            case DOWN -> oldX;
            case RIGHT -> oldX + 1;
        };
        final int newY = switch (direction) {
            case UP -> oldY + 1;
            case LEFT -> oldY;
            case DOWN -> oldY - 1;
            case RIGHT -> oldY;
        };
        setXRobot(newX);
        setYRobot(newY);

        if (printTrace) {
            printTrace();
        }

        world.updateRobotField(this, oldX, oldY);
        world.triggerUpdate();
        world.sleep();
    }

    /**
     * Checks whether the robot currently holds at least one coin.
     *
     * @return {@code true} if the robot has one or more coins, otherwise {@code false}
     */
    public boolean hasAnyCoins() {
        return numberOfCoins > 0;
    }

    /**
     * Returns the robot's ID.
     *
     * @return the ID of the robot
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the number of coins the robot carries.
     *
     * @param coins the new coin count
     *
     * @throws IllegalArgumentException if the number of coins is negative
     */
    public void setNumberOfCoins(final int coins) {
        world.checkNumberOfCoins(coins);
        world.trace(this, Transition.RobotAction.SET_NUMBER_OF_COINS);
        this.numberOfCoins = coins;
    }

    /**
     * Checks whether the robot is currently turned off.
     *
     * @return {@code true} if the robot is off, otherwise {@code false}
     */
    public boolean isTurnedOff() {
        return off;
    }

    /**
     * Checks whether the robot is currently turned on.
     *
     * @return {@code true} if the robot is on, otherwise {@code false}
     */
    public boolean isTurnedOn() {
        return !off;
    }

    /**
     * Turns the robot off. It can no longer move or interact.
     */
    public void turnOff() {
        world.trace(this, Transition.RobotAction.TURN_OFF);
        off = true;
        world.triggerUpdate();
    }

    /**
     * Sets the robot's ID.
     *
     * @param id a string identifier for the robot
     */
    protected void setId(final @Nullable String id) {
        this.id = id;
    }

    /**
     * Returns the robot's visual family (its shape and color).
     *
     * @return the robot's family
     * @deprecated Use {@link #getRobotFamily()} instead.
     */
    @Deprecated(since = "0.5.0")
    public @NotNull RobotFamily getFamily() {
        return getRobotFamily();
    }

    /**
     * Returns the robot's visual family (its shape and color).
     *
     * @return the robot's family
     */
    public @NotNull RobotFamily getRobotFamily() {
        return robotFamily;
    }

    /**
     * Sets the robot's visual family.
     *
     * @param robotFamily the new visual family of the robot
     */
    public void setRobotFamily(final @NotNull RobotFamily robotFamily) {
        this.robotFamily = robotFamily;
        world.triggerUpdate();
        world.sleep();
    }

    /**
     * Sets the robot's visual family.
     *
     * @param robotFamily the new visual family of the robot
     *
     * @deprecated Use {@link #setRobotFamily(RobotFamily)} instead.
     */
    @Deprecated(since = "0.5.0")
    public void setFamily(final @NotNull RobotFamily robotFamily) {
        setRobotFamily(robotFamily);
    }

    /**
     * Checks if the robot is facing upward.
     *
     * @return {@code true} if facing {@link Direction#UP}, otherwise {@code false}
     */
    public boolean isFacingUp() {
        return direction == Direction.UP;
    }

    /**
     * Checks if the robot is facing downward.
     *
     * @return {@code true} if facing {@link Direction#DOWN}, otherwise {@code false}
     */
    public boolean isFacingDown() {
        return direction == Direction.DOWN;
    }

    /**
     * Checks if the robot is facing left.
     *
     * @return {@code true} if facing {@link Direction#LEFT}, otherwise {@code false}
     */
    public boolean isFacingLeft() {
        return direction == Direction.LEFT;
    }

    /**
     * Checks if the robot is facing right.
     *
     * @return {@code true} if facing {@link Direction#RIGHT}, otherwise {@code false}
     */
    public boolean isFacingRight() {
        return direction == Direction.RIGHT;
    }

    /**
     * Returns the direction the robot is currently facing.
     *
     * @return the current {@link Direction}
     */
    public @NotNull Direction getDirection() {
        return direction;
    }

    /**
     * Checks whether trace output is enabled for this robot.
     *
     * @return {@code true} if trace is enabled, otherwise {@code false}
     */
    public boolean isPrintTraceEnabled() {
        return printTrace;
    }

    /**
     * Enables or disables trace output for the robot.
     *
     * @param printTrace {@code true} to enable tracing, {@code false} to disable
     */
    public void setPrintTrace(final boolean printTrace) {
        this.printTrace = printTrace;
    }

    @Override
    public String toString() {
        return "Robot{"
            + "id='" + id + '\''
            + ", at=[" + getX() + '/' + getY()
            + "], numberOfCoins=" + numberOfCoins
            + ", direction=" + direction
            + '}';
    }
}
