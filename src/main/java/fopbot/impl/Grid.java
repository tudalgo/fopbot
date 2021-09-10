package fopbot.impl;

import fopbot.Direction;
import fopbot.Entity;

import java.util.Collection;

public class Grid {

  private final int width;
  private final int height;

  private final Cell[][] grid;

  public Grid(int width, int height) {
    this.width = width;
    this.height = height;

    grid = new Cell[width][height];
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        grid[x][y] = new Cell();
      }
    }
  }

  public boolean hasNorthWall(int x, int y) {
    return hasWall(x, y, Direction.NORTH);
  }

  public void setNorthWall(int x, int y, boolean wall) {
    setWall(x, y, Direction.NORTH, wall);
    if (y > 0) {
      setWall(x, y - 1, Direction.SOUTH, wall);
    }
  }

  public boolean hasSouthWall(int x, int y) {
    return hasWall(x, y, Direction.SOUTH);
  }

  public void setSouthWall(int x, int y, boolean wall) {
    setWall(x, y, Direction.SOUTH, wall);
    if (y < height - 1) {
      setWall(x, y + 1, Direction.NORTH, wall);
    }
  }

  public boolean hasEastWall(int x, int y) {
    return hasWall(x, y, Direction.EAST);
  }

  public void setEastWall(int x, int y, boolean wall) {
    setWall(x, y, Direction.EAST, wall);
    if (x < width - 1) {
      setWall(x + 1, y, Direction.WEST, wall);
    }
  }

  public boolean hasWestWall(int x, int y) {
    return hasWall(x, y, Direction.WEST);
  }

  public void setWestWall(int x, int y, boolean wall) {
    setWall(x, y, Direction.WEST, wall);
    if (x > 0) {
      setWall(x - 1, y, Direction.EAST, wall);
    }
  }

  public boolean hasWall(int x, int y, Direction dir) {
    checkCoords(x, y);
    return grid[x][y].walls[dir.ordinal()];
  }

  private void setWall(int x, int y, Direction dir, boolean wall) {
    checkCoords(x, y);
    grid[x][y].walls[dir.ordinal()] = wall;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public Collection<Entity> getEntities(int x, int y) {
    checkCoords(x, y);
    return grid[x][y].entities;
  }

  private void checkCoords(int x, int y) {
    if (x < 0 || x >= width) {
      throw new IllegalArgumentException("X coordinate invalid: " + x);
    }
    if (y < 0 || y >= height) {
      throw new IllegalArgumentException("Y coordinate invalid: " + y);
    }
  }
}
