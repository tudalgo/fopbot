package fopbot;

/**
 * Represents a placeable graphical entity positioned on a field in a virtual world.
 *
 * <p>All placeable entities in the virtual world inherit from this class.
 */
public abstract class FieldEntity {

    /**
     * The x-coordinate of this entity on the field.
     */
    private int x;

    /**
     * The y-coordinate of this entity on the field.
     */
    private int y;

    /**
     * Constructs a new {@code FieldEntity} at the specified (x, y) coordinates.
     *
     * @param x the initial x-coordinate of this entity
     * @param y the initial y-coordinate of this entity
     */
    public FieldEntity(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Sets the x-coordinate of this entity.
     *
     * @param x the new x-coordinate of this entity
     */
    protected void setX(int x) {
        this.x = x;
    }

    /**
     * Sets the y-coordinate of this entity.
     *
     * @param y the new y-coordinate of this entity
     */
    protected void setY(int y) {
        this.y = y;
    }

    /**
     * Returns the current x-coordinate of this entity.
     *
     * @return the x-coordinate of this entity
     */
    public int getX() {
        return this.x;
    }

    /**
     * Returns the current y-coordinate of this entity.
     *
     * @return the y-coordinate of this entity
     */
    public int getY() {
        return this.y;
    }
}

