package fopbot;

import java.util.Collection;

public interface World {

  /**
   * @return the worlds width
   */
  int getWidth();

  /**
   * @return the worlds height
   */
  int getHeight();

  Collection<Entity> getEntities(int x, int y);

  /**
   * @return true if a block is in field (x, y)
   */
  boolean hasBlockInField(int x, int y);

  /**
   * @return true if there is a wall at field (x, y) in direction dir
   */
  boolean fieldHasWallInDirection(int x, int y, Direction dir);

  /**
   * @return true if at least one coin is in field (x, y)
   */
  boolean hasCoinInField(int x, int y);

  /**
   * @return true if another robot is in field (x, y)
   */
  boolean hasAnotherRobotInField(int x, int y, Robot robot);

  /**
   * Puts down N coins at field (x, y)
   */
  void putCoins(int x, int y, int numberOfCoins);

  /**
   * Tries to remove one coin from the field (x,y)
   *
   * @return true if a  coin got removed
   */
  boolean pickCoin(int x, int y);

  /**
   * Places a block at field (x, y)
   */
  void putBlock(int x, int y);

  /**
   * Adds a robot to the world at field (x, y) looking at direction dir
   */
  Robot newRobot(int x, int y, Direction dir, int numberOfCoins);

  /**
   * Reset the world (remove all entities)
   */
  void reset();

  /**
   * Indicate that the controlling script starts action in this world
   */
  void start();

  /**
   * @return If the world is still running a script
   */
  boolean isRunning();

  /**
   * Indicate that the controlling script as finished executing
   */
  void stop();
}
