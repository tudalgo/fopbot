package foshbot.impl;

import foshbot.Direction;
import foshbot.Entity;
import foshbot.World;

import java.util.Collection;

public abstract class AbstractWorld implements World {

    protected final Grid grid;

    public AbstractWorld(Grid grid) {
        this.grid = grid;
    }

    @Override
    public int getWidth() {
        return grid.getWidth();
    }

    @Override
    public int getHeight() {
        return grid.getHeight();
    }

    @Override
    public Collection<Entity> getEntities(int x, int y) {
        return grid.getEntities(x, y);
    }

    @Override
    public boolean hasBlockInField(int x, int y) {
        return false;
    }

    @Override
    public boolean fieldHasWallInDirection(int x, int y, Direction dir) {
        return grid.hasWall(x, y, dir);
    }

    @Override
    public boolean isCoinInField(int x, int y) {
        return false;
    }

    @Override
    public boolean isAnotherRobotInField(int x, int y, foshbot.Robot robot) {
        return false;
    }

    @Override
    public void placeCoinStack(int x, int y, int numberOfCoins) {

    }

    @Override
    public void placeBlock(int x, int y) {

    }

    @Override
    public void reset() {
        for (int x = 0; x < getWidth(); x++) {
            for (int y = 0; y < getHeight(); y++) {
                grid.getEntities(x, y).clear();
            }
        }
    }
}
