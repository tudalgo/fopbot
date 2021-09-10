package fopbot;

public abstract class Entity {

  protected int x;
  protected int y;

  public Entity(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int getX() {
    return this.x;
  }

  public int getY() {
    return this.y;
  }
}
