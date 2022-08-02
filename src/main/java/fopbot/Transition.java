package fopbot;

/**
 * Defines the transition of a robot with its associated action. The transition will be used to
 * trace the actions of robots.
 */
public class Transition {

    /**
     * Defines the traceable action that a robot can do.
     *
     * @see Robot
     */
    public enum RobotAction {
        /**
         * The action that a robot can move.
         *
         * @see Robot#move()
         */
        MOVE,
        /**
         * The action that a robot can pick up  a coin.
         *
         * @see Robot#pickCoin()
         */
        PICK_COIN,
        /**
         * The action that a robot can put down a coin.
         *
         * @see Robot#putCoin()
         */
        PUT_COIN,
        /**
         * The action that a robot can turn left.
         *
         * @see Robot#turnLeft() ()
         */
        TURN_LEFT,
        /**
         * The action that a robot can be turned off.
         *
         * @see Robot#turnOff() ()
         */
        TURN_OFF,
        /**
         * The action that a robot can change its number of coins.
         *
         * @see Robot#setNumberOfCoins(int)
         */
        SET_NUMBER_OF_COINS,
        /**
         * The action that a robot can change its X coordinate.
         *
         * @see Robot#setX(int)
         */
        SET_X,
        /**
         * The action that a robot can change its Y coordinate.
         *
         * @see Robot#setY(int)
         */
        SET_Y,
        /**
         * The action only used for the last robot state, no change.
         */
        NONE,
    }

    /**
     * The execution count, which indicates the next available execution count.
     */
    private static int nextStep = 0;
    /**
     * The execution number when the robot action was executed.
     */
    public final int step;
    /**
     * The robot action of this transition.
     */
    public RobotAction action;
    /**
     * The robot that executed the action.
     */
    public Robot robot;

    /**
     * Constructs and initializes a field entity at the specified {@code (x,y)} location in the
     * coordinate space.
     *
     * @param action the action of the robot  of the newly constructed transition
     * @param robot  the robot that executed the action of the newly constructed transition
     */
    public Transition(RobotAction action, Robot robot) {
        this.action = action;
        this.robot = new Robot(robot);
        this.step = nextStep++;
    }

    @Override
    public String toString() {
        return "Transition{step=" + step + ", action=" + action + ", robot=" + robot + "}";
    }
}
