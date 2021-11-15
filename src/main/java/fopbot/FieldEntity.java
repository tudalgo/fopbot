package fopbot;

/**
 * This class represents a field entity on a graphical user interface and acts as a parent class of
 * every entity on the field.
 */
public abstract class FieldEntity {

  /**
   * The X coordinate of this {@code FieldEntity}.
   */
  private int x;

  /**
   * The Y coordinate of this {@code FieldEntity}.
   */
  private int y;

  /**
   * Constructs and initializes a field entity at the specified {@code (x,y)} location in the
   * coordinate space.
   *
   * @param x the X coordinate of the newly constructed field entity
   * @param y the Y coordinate of the newly constructed field entity
   */
  public FieldEntity(int x, int y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Sets the X coordinate of the field entity to the specified X coordinate.
   *
   * @param x the new X coordinate for this field entity
   */
  protected void setX(int x) {
    this.x = x;
  }

  /**
   * Sets the Y coordinate of the field entity to the specified Y coordinate.
   *
   * @param y the new Y coordinate for this field entity
   */
  protected void setY(int y) {
    this.y = y;
  }

  /**
   * Returns the X coordinate of this {@code FieldEntity}.
   *
   * @return the X coordinate of this {@code FieldEntity}
   */
  public int getX() {
    return this.x;
  }

  /**
   * Returns the Y coordinate of this {@code FieldEntity}.
   *
   * @return the Y coordinate of this {@code FieldEntity}
   */
  public int getY() {
    return this.y;
  }
}
