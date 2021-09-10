package fopbot.anim;

import fopbot.Direction;
import fopbot.anim.paz.Vector;
import fopbot.anim.resources.Resources;
import fopbot.impl.AbstractRobot;

import static fopbot.anim.AnimatedWorldFrame.*;

public class AnimatedRobot extends AbstractRobot implements Animatable {

  public static final String RESOURCE = "/fopbot/trianglebot.png";

  private static final double UPDATE_EPSILON = 0.05;
  private static final double ANGLE_VEL_SCALAR = 0.003;
  private static final double VEL_SCALAR = 0.001;

  private final AnimatedWorld world;
  private final Vector pos;
  private double currentAngle;
  private Vector target;

  public AnimatedRobot(int x, int y, Direction dir, int numberOfCoins, AnimatedWorld world) {
    super(x, y, dir, numberOfCoins, world);
    this.currentAngle = getAngleOfDir(dir);
    setTarget(x, y);
    this.pos = target.copy();
    this.world = world;
  }

  private void setTarget(int x, int y) {
    this.target = new Vector(x, y)
      .mul(CELL_SIZE)
      .add(CELL_PADDING, CELL_PADDING);
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
  public void setField(int x, int y) {
    setTarget(x, y);
    super.setField(x, y);
  }

  @Override
  public boolean update(double dt) {
    boolean finished = updateAngle(dt);
    finished &= updatePos(dt);
    return finished;
  }

  private boolean updatePos(double dt) {
    var vel = target
      .copy()
      .sub(pos)
      .mul(VEL_SCALAR * dt);
    pos.add(vel);
    return target.dist(pos) < UPDATE_EPSILON;
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
    var w = CELL_SIZE - CELL_PADDING * 2;
    d.rotated(
      currentAngle,
      pos.x + w / 2,
      pos.y + w / 2,
      () -> d.image(
        Resources.getImages().get(RESOURCE),
        pos.x, pos.y,
        w, w));
  }
}
