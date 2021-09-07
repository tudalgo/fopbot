package foshbot.anim;

import foshbot.Direction;
import foshbot.Robot;
import foshbot.Entity;
import foshbot.anim.resources.Resources;

public class AnimatedRobot extends Entity implements Robot {

    public static final String RESOURCE = "/fopbot/trianglebot.png";

    private static final double UPDATE_EPSILON = 0.01;
    private static final double ANGLE_VEL_SCALAR = 0.01;

    private final AnimatedWorld world;

    private Direction dir;
    private double angle;

    public AnimatedRobot(int x, int y, Direction dir, int numberOfCoins, AnimatedWorld world) {
        super(x, y);
        this.dir = dir;
        this.angle = getAngleOfDir(dir);
        this.world = world;
    }

    private double getAngleOfDir(Direction dir) {
        switch (dir) {
            case NORTH:
                return Math.PI / 2;
            case SOUTH:
                return -Math.PI / 2;
            case WEST:
                return Math.PI;
            default:
                return 0;
        }
    }

    @Override
    public void turnLeft() {
        world.awaitUpdateFinish();

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

    }

    @Override
    public void putCoin() {

    }

    @Override
    public void pickCoin() {

    }

    @Override
    public fopbot.Direction getDirection() {
        return null;
    }

    @Override
    public int getNumberOfCoins() {
        return 0;
    }

    @Override
    public void turnOff() {

    }

    @Override
    public boolean isTurnedOff() {
        return false;
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
        return false;
    }

    @Override
    public boolean update(double dt) {
        return updateAngle(dt);
    }

    private boolean updateAngle(double dt) {
        var target = getAngleOfDir(dir);
        angle += (target - angle) * ANGLE_VEL_SCALAR * dt;
        return Math.abs(target - angle) < UPDATE_EPSILON;
    }

    @Override
    public void draw(Drawable d) {
        d.rotated(angle, (x+0.5)*Frame.CELL_SIZE, (y+0.5)*Frame.CELL_SIZE, () ->
            d.image(
                Resources.getImages().get(RESOURCE),
                x * Frame.CELL_SIZE + Frame.CELL_PADDING,
                y * Frame.CELL_SIZE + Frame.CELL_PADDING,
                Frame.CELL_SIZE - Frame.CELL_PADDING * 2,
                Frame.CELL_SIZE - Frame.CELL_PADDING * 2));
    }
}
