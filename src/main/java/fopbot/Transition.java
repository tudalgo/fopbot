package fopbot;

public class Transition {
  public enum RobotAction {
    MOVE,
    PICK_COIN,
    PUT_COIN,
    TURN_LEFT,
    TURN_OFF,
    SET_X,
    SET_Y,
    NONE // Only used for the last robot state, no change
  }

  private static int nextStep = 0;
  public final int step;
  public RobotAction action;
  public Robot robot;

  public Transition(RobotAction action, Robot robot) {
    this.action = action;
    this.robot = new Robot(robot);
    this.step = nextStep++;
  }

  @Override
  public String toString() {
    return "Transition{step=" + step + ", action=" + action + ", robot=" + robot + "}";
  }
}
