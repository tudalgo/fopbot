package foshbot.anim;

import foshbot.Direction;
import foshbot.anim.resources.Resources;
import foshbot.impl.AbstractRobot;

public class AnimatedRobot extends AbstractRobot implements Animatable {

    public static final String RESOURCE = "/fopbot/trianglebot.png";

    private static final double UPDATE_EPSILON = 0.01;
    private static final double ANGLE_VEL_SCALAR = 0.003;
    private static final double VEL_SCALAR = 0.001;

    private final AnimatedWorld world;

    private double currentAngle;
    private double currentX;
    private double currentY;

    public AnimatedRobot(int x, int y, Direction dir, int numberOfCoins, AnimatedWorld world) {
        super(x, y, dir, numberOfCoins, world);
        this.currentAngle = getAngleOfDir(dir);
        this.currentX = x;
        this.currentY = y;
        this.world = world;
    }

    private double getAngleOfDir(Direction dir) {
        switch (dir) {
            case NORTH:
                return Math.PI * 2;
            case WEST:
                return 3 * Math.PI / 2;
            case SOUTH:
                return Math.PI;
            default:
                return Math.PI / 2;
        }
    }

    @Override
    public void turnLeft() {
        world.awaitUpdateFinish();
        super.turnLeft();
    }

    @Override
    public void move() {
        world.awaitUpdateFinish();
        super.move();
    }

    @Override
    public void putCoin() {
        world.awaitUpdateFinish();
        super.putCoin();
    }

    @Override
    public void pickCoin() {
        world.awaitUpdateFinish();
        super.pickCoin();
    }

    @Override
    public Direction getDirection() {
        world.awaitUpdateFinish();
        return super.getDirection();
    }

    @Override
    public int getNumberOfCoins() {
        world.awaitUpdateFinish();
        return super.getNumberOfCoins();
    }

    @Override
    public void turnOff() {
        world.awaitUpdateFinish();
        super.turnOff();
    }

    @Override
    public boolean isTurnedOff() {
        world.awaitUpdateFinish();
        return super.isTurnedOff();
    }

    @Override
    public boolean isNextToACoin() {
        world.awaitUpdateFinish();
        return super.isNextToACoin();
    }

    @Override
    public boolean isNextToARobot() {
        world.awaitUpdateFinish();
        return super.isNextToARobot();
    }

    @Override
    public boolean isFrontClear() {
        world.awaitUpdateFinish();
        return super.isFrontClear();
    }

    @Override
    public boolean update(double dt) {
        boolean finished = updateAngle(dt);
        finished &= updatePos(dt);
        return finished;
    }

    private boolean updatePos(double dt) {
        currentX += (x - currentX) * VEL_SCALAR * dt;
        currentY += (y - currentY) * VEL_SCALAR * dt;

        return Math.abs(x - currentX) < UPDATE_EPSILON
            && Math.abs(y - currentY) < UPDATE_EPSILON;
    }

    private boolean updateAngle(double dt) {
        var target = getAngleOfDir(dir);
        while (target > currentAngle) {
            currentAngle += Math.PI * 2;
        }

        currentAngle += (target - currentAngle) * ANGLE_VEL_SCALAR * dt;
        return Math.abs(target - currentAngle) < UPDATE_EPSILON;
    }

    @Override
    public void draw(Drawable d) {
        d.rotated(
            currentAngle,
        (currentX+0.5)*Frame.CELL_SIZE,
        (currentY+0.5)*Frame.CELL_SIZE,
            () -> d.image(
                Resources.getImages().get(RESOURCE),
                currentX * Frame.CELL_SIZE + Frame.CELL_PADDING,
                currentY * Frame.CELL_SIZE + Frame.CELL_PADDING,
                Frame.CELL_SIZE - Frame.CELL_PADDING * 2,
                Frame.CELL_SIZE - Frame.CELL_PADDING * 2));
    }
}