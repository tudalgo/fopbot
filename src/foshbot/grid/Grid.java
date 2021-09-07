package foshbot.grid;

import foshbot.*;
import foshbot.Entity;

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
        return hasWall(x, y, Direction.NORTH.ordinal());
    }

    public void setNorthWall(int x, int y, boolean wall) {
        setWall(x, y, Direction.NORTH.ordinal(), wall);
        if (y > 0) {
            setWall(x, y-1, Direction.SOUTH.ordinal(), wall);
        }
    }

    public boolean hasSouthWall(int x, int y) {
        return hasWall(x, y, Direction.SOUTH.ordinal());
    }

    public void setSouthWall(int x, int y, boolean wall) {
        setWall(x, y, Direction.SOUTH.ordinal(), wall);
        if (y < height-2) {
            setWall(x, y+1, Direction.NORTH.ordinal(), wall);
        }
    }

    public boolean hasEastWall(int x, int y) {
        return hasWall(x, y, Direction.EAST.ordinal());
    }

    public void setEastWall(int x, int y, boolean wall) {
        setWall(x, y, Direction.EAST.ordinal(), wall);
        if (x < width-2) {
            setWall(x+1, y, Direction.WEST.ordinal(), wall);
        }
    }

    public boolean hasWestWall(int x, int y) {
        return hasWall(x, y, Direction.WEST.ordinal());
    }

    public void setWestWall(int x, int y, boolean wall) {
        setWall(x, y, Direction.WEST.ordinal(), wall);
        if (x > 0) {
            setWall(x-1, y, Direction.EAST.ordinal(), wall);
        }
    }

    private boolean hasWall(int x, int y, int side) {
        checkCoords(x, y);
        return grid[x][y].walls[side];
    }

    private void setWall(int x, int y, int side, boolean wall) {
        checkCoords(x, y);
        grid[x][y].walls[side] = wall;
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