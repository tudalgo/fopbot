package fopbot;

/**
 * A field entity that represents a wall on a graphical user interface.
 */
public class Wall extends FieldEntity {

  /**
   * The orientation of this wall (horizontal or vertical).
   */
  private final boolean horizontal;

  /**
   * Constructs and initializes a wall at the specified {@code (x,y)} location and orientation.
   *
   * @param x          the X coordinate of the newly constructed wall
   * @param y          the Y coordinate of the newly constructed wall
   * @param horizontal if {@code true} a horizontal wall will be constructed
   */
  public Wall(int x, int y, boolean horizontal) {
    super(x, y);
    this.horizontal = horizontal;
  }

  /**
   * Returns {@code true} if the orientation of this wall was horizontal.
   *
   * @return {@code true} if the orientation of this wall was horizontal
   */
  protected boolean isHorizontal() {
    return horizontal;
  }
}
