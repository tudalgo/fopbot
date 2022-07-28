package fopbot;

/**
 * An enumeration of robot families.
 * A family of robots is uniquely in terms of their appearance.
 */
public enum RobotFamily {

    TRIANGLEBOT,
    SQUARE_AQUA,
    SQUARE_BLUE,
    SQUARE_GREEN,
    SQUARE_ORANGE,
    SQUARE_PURPLE,
    SQUARE_RED,
    SQUARE_YELLOW;


    final String identifier;

    /**
     * Constructs a robot family using the name of the item as the identifier.
     */
    RobotFamily() {
        this.identifier = name().toLowerCase();
    }

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

