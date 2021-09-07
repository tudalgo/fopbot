package foshbot.anim;

import foshbot.Direction;
import foshbot.Robot;
import foshbot.grid.Grid;

import java.awt.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static foshbot.anim.Frame.CELL_SIZE;

public class AnimatedWorld implements foshbot.World {

    private static final double UPDATE_TIMEOUT = 0.2;

    private final Lock lock = new ReentrantLock();
    private final Condition updateFinished = lock.newCondition();

    private double updateTimeout = 0.0;

    private boolean running = true;

    private final Grid grid;

    public AnimatedWorld(int width, int height) {
        grid = new Grid(width, height);
    }

    public void drawEntities(Drawable d) {
        for (int x = 0; x < grid.getWidth(); x++) {
            for (int y = 0; y < grid.getHeight(); y++) {
                for (var e : grid.getEntities(x, y)) {
                    e.draw(d);
                }
            }
        }
    }

    public void drawWalls(Drawable d) {
        d.fill(Color.BLACK);
        d.strokeWeight(8);

        for (int x = 0; x < grid.getWidth(); x++) {
            for (int y = 0; y < grid.getHeight(); y++) {
                if (grid.hasNorthWall(x, y)) {
                    d.line(
                        x*CELL_SIZE,
                        y*CELL_SIZE,
                        x*CELL_SIZE + CELL_SIZE,
                        y*CELL_SIZE);
                }
                if (grid.hasSouthWall(x, y)) {
                    d.line(
                        x*CELL_SIZE,
                        y*CELL_SIZE + CELL_SIZE,
                        x*CELL_SIZE + CELL_SIZE,
                        y*CELL_SIZE + CELL_SIZE);
                }
                if (grid.hasEastWall(x, y)) {
                    d.line(
                        x*CELL_SIZE + CELL_SIZE,
                        y*CELL_SIZE,
                        x*CELL_SIZE + CELL_SIZE,
                        y*CELL_SIZE + CELL_SIZE);
                }
                if (grid.hasWestWall(x, y)) {
                    d.line(
                        x*CELL_SIZE,
                        y*CELL_SIZE,
                        x*CELL_SIZE,
                        y*CELL_SIZE + CELL_SIZE);
                }
            }
        }
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
        return false;
    }

    @Override
    public boolean isCoinInField(int x, int y) {
        return false;
    }

    @Override
    public boolean isAnotherRobotInField(int x, int y, Robot robot) {
        return false;
    }

    @Override
    public void placeCoinStack(int x, int y, int numberOfCoins) {

    }

    @Override
    public void placeBlock(int x, int y) {

    }

    @Override
    public Robot newRobot(int x, int y, Direction dir, int numberOfCoins) {
        var r = new AnimatedRobot(x, y, dir, numberOfCoins, this);
        grid.getEntities(x, y).add(r);
        return r;
    }

    @Override
    public void placeWall(int x, int y, Direction dir, boolean wall) {

    }

    @Override
    public void reset() {

    }

    void update(double dt) {
        if (updateTimeout > 0) {
            updateTimeout -= dt;
            if (updateTimeout < 0) {
                lock.lock();
                try {
                    this.updateFinished.signal();
                } finally {
                    lock.unlock();
                }
            }

            return;
        }

        boolean updatesFinished = true;

        for (int x = 0; x < grid.getWidth(); x++) {
            for (int y = 0; y < grid.getHeight(); y++) {
                for (var e : grid.getEntities(x, y)) {
                    if (!e.update(dt)) {
                        updatesFinished = false;
                    }
                }
            }
        }

        if (updatesFinished) {
            updateTimeout = UPDATE_TIMEOUT;
        }
    }

    public void awaitUpdateFinish() {
        lock.lock();
        try {
            updateFinished.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }

    public boolean isRunning() {
        return running;
    }

    @Override
    public void stop() {
        running = false;
    }
}
