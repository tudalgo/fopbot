package foshbot.impl;

import foshbot.Direction;
import foshbot.World;

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
    public boolean isBlockInField(int x, int y) {
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

    }
}
