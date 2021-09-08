package foshbot.impl;

import foshbot.*;

public abstract class AbstractRobot extends Entity implements Robot {

    protected boolean on;
    protected int numberOfCoins;
    protected Direction dir;
    private final World world;

    public AbstractRobot(int x, int y, Direction dir, int numberOfCoins, World world) {
        super(x, y);
        this.on = true;
        this.dir = dir;
        this.numberOfCoins = numberOfCoins;
        this.world = world;
    }

    @Override
    public void turnLeft() {
        switch (dir) {
            case NORTH:
                dir = Direction.WEST;
                break;
            case EAST:
                dir = Direction.NORTH;
                break;
            case SOUTH:
                dir = Direction.EAST;
                break;
            case WEST:
                dir = Direction.SOUTH;
                break;
        }
    }

    @Override
    public void move() {
        if (!on) {
            return;
        }

        if (!isFrontClear()) {
            throw new RobotException("Robot crashed!");
        }

        world.getEntities(x, y).remove(this);

        x += getDx();
        y += getDy();

        world.getEntities(x, y).add(this);
    }

    @Override
    public void putCoin() {

    }

    @Override
    public void pickCoin() {

    }

    @Override
    public Direction getDirection() {
        return dir;
    }

    @Override
    public int getNumberOfCoins() {
        return numberOfCoins;
    }

    @Override
    public void turnOff() {
        this.on = false;
    }

    @Override
    public boolean isTurnedOff() {
        return !on;
    }

    @Override
    public boolean isNextToACoin() {
        return false;
    }

    @Override
    public boolean isNextToARobot() {
        return false;
    }

    @Override
    public void setField(int x, int y) {

    }

    @Override
    public boolean isFrontClear() {
        var dx = getDx();
        var dy = getDy();
        return x+dx >= 0
            && x+dx < world.getWidth()
            && y+dy >= 0
            && y+dy < world.getHeight()
            && !world.fieldHasWallInDirection(x, y, dir)
            && !world.hasBlockInField(x+dx, y+dy);
    }

    private int getDx() {
        switch (dir) {
            case EAST:
                return +1;
            case WEST:
                return -1;
            default:
                return 0;
        }
    }

    private int getDy() {
        switch (dir) {
            case NORTH:
                return -1;
            case SOUTH:
                return +1;
            default:
                return 0;
        }
    }
}