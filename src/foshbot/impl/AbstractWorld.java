package foshbot.impl;

import foshbot.*;

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
    public void putCoins(int x, int y, int numberOfCoins) {
        var c = getEntities(x, y)
            .stream()
            .filter(CoinStack.class::isInstance)
            .map(CoinStack.class::cast)
            .findFirst()
            .orElseGet(() -> newCoinStack(x, y));

        c.numberOfCoins += numberOfCoins;
    }

    protected abstract CoinStack newCoinStack(int x, int y);

    @Override
    public boolean hasBlockInField(int x, int y) {
        return getEntities(x, y)
            .stream()
            .anyMatch(Block.class::isInstance);
    }

    @Override
    public boolean fieldHasWallInDirection(int x, int y, Direction dir) {
        return grid.hasWall(x, y, dir);
    }

    @Override
    public boolean hasCoinInField(int x, int y) {
        return getEntities(x, y)
            .stream()
            .anyMatch(CoinStack.class::isInstance);
    }

    @Override
    public boolean hasAnotherRobotInField(int x, int y, Robot robot) {
        return getEntities(x, y)
            .stream()
            .anyMatch(r ->
               r instanceof Robot && r != robot);
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
