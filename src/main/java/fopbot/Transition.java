package fopbot;

/**
 * Represents a transition that captures a robot's action within the simulation.
 *
 * <p>A {@code Transition} records the robot, the type of action performed, and the execution step at which the action
 * occurred. These transitions can be used to trace robot behavior for debugging or playback purposes.
 */
public class Transition {

    /**
     * Enumerates the possible actions a robot can perform that are traceable.
     *
     * @see Robot
     */
    public enum RobotAction {

        /**
         * The action of moving forward.
         *
         * @see Robot#move()
         */
        MOVE,

        /**
         * The action of picking up a coin.
         *
         * @see Robot#pickCoin()
         */
        PICK_COIN,

        /**
         * The action of putting down a coin.
         *
         * @see Robot#putCoin()
         */
        PUT_COIN,

        /**
         * The action of turning left.
         *
         * @see Robot#turnLeft()
         */
        TURN_LEFT,

        /**
         * The action of turning the robot off.
         *
         * @see Robot#turnOff()
         */
        TURN_OFF,

        /**
         * The action of setting the number of coins.
         *
         * @see Robot#setNumberOfCoins(int)
         */
        SET_NUMBER_OF_COINS,

        /**
         * The action of setting the X-coordinate.
         *
         * @see Robot#setX(int)
         */
        SET_X,

        /**
         * The action of setting the Y-coordinate.
         *
         * @see Robot#setY(int)
         */
        SET_Y,

        /**
         * No action. Used as a placeholder for the final robot state.
         */
        NONE
    }

    /**
     * Global execution counter, used to assign step numbers to transitions.
     */
    private static int nextStep = 0;

    /**
     * The step number at which the robot performed the action.
     */
    public final int step;

    /**
     * The type of action the robot performed.
     */
    public RobotAction action;

    /**
     * The robot that performed the action.
     *
     * <p>A copy of the original robot is stored to preserve the exact state at this step.
     */
    public Robot robot;

    /**
     * Constructs a new {@code Transition} that represents a robot action at a specific moment.
     *
     * @param action the type of action performed
     * @param robot  the robot performing the action
     */
    public Transition(RobotAction action, Robot robot) {
        this.action = action;
        this.robot = new Robot(robot); // capture snapshot of the robot's state
        this.step = nextStep++;
    }

    @Override
    public String toString() {
        return "Transition{step=" + step + ", action=" + action + ", robot=" + robot + "}";
    }
}

