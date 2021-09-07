package foshbot;

import fopbot.Direction;

public interface Robot {

    /**
     * Turns the robots body to the left
     */
    void turnLeft();


    /**
     * Move the robot one step forward in the current direction
     */
    void move();


    /**
     * Puts down one coin at the current position
     */
    void putCoin();

    /**
     * Picks up one coin at the current position
     */
    void pickCoin();


    /**
     * @return the current direction of the robot
     */
    Direction getDirection();


    /**
     * @return the number of coins the robot possesses
     */
    int getNumberOfCoins();

    /**
     * @return true if the robot has any coins
     */
    default boolean hasAnyCoins() {
        return getNumberOfCoins() > 0;
    }

    /**
     * @return true if the robot is facing up
     */
    default boolean isFacingUp() {
        return getDirection() == Direction.UP;
    }

    /**
     * @return true if the robot is facing down
     */
    default boolean isFacingDown() {
        return getDirection() == Direction.DOWN;
    }

    /**
     * @return true if the robot is facing left
     */
    default boolean isFacingLeft() {
        return getDirection() == Direction.LEFT;
    }

    /**
     * @return true if the robot is facing right
     */
    default boolean isFacingRight() {
        return getDirection() == Direction.RIGHT;
    }

    /**
     * Turn off the robot
     */
    void turnOff();

    /**
     * @return true if the robot is turned off
     */
    boolean isTurnedOff();

    /**
     * @return true if the robot is turned on
     */
    default boolean isTurnedOn() {
        return !isTurnedOff();
    }

    /**
     * @return true if the robot is standing on a coin/if at least one coin is at
     *         the same position as the robot's position
     */
    boolean isNextToACoin();

    /**
     * @return true if at least another robot is at the same position as the robot's
     *         position
     */
    boolean isNextToARobot();

    /**
     * Sets the robot's x-coordinate
     */
    void setX(int x);

    /**
     * Sets the robot's y-coordinate
     */
    void setY(int y);

    /**
     * Sets the robot's field (x,y)
     */
    void setField(int x, int y);

    /**
     * @return true if the robot is not facing an object that is standing in the way
     *         (e.g. a wall)
     */
    boolean isFrontClear();
}