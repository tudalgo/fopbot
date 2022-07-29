package fopbot;

/**
 * An enumeration of robot families.
 * A family of robots is uniquely in terms of their appearance.
 */
public class RobotFamily {

    public static RobotFamily TRIANGLEBOT = new RobotFamily("trianglebot");
    public static RobotFamily SQUARE_AQUA = new RobotFamily("square_aqua");
    public static RobotFamily SQUARE_BLUE = new RobotFamily("square_blue");
    public static RobotFamily SQUARE_GREEN = new RobotFamily("square_green");
    public static RobotFamily SQUARE_ORANGE = new RobotFamily("square_orange");
    public static RobotFamily SQUARE_PURPLE = new RobotFamily("square_purple");
    public static RobotFamily SQUARE_RED = new RobotFamily("square_red");
    public static RobotFamily SQUARE_YELLOW = new RobotFamily("square_yellow");


    final String identifier;

    /**
     * Constructs a robot family using the given identifier.
     *
     * @param identifier the identifier of the robot family
     */
    RobotFamily(String identifier) {
        this.identifier = identifier.toLowerCase();
    }

    /**
     * Returns the identifier of this robot family.
     * Two robot families are not allowed to have the same identifier.
     *
     * @return the identifier of this robot family
     */
    public String getIdentifier() {
        return identifier;
    }
}

