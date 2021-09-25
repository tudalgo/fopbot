package fopbot.global;

import fopbot.examples.TracingExample;
import fopbot.trace.RobotAction;
import fopbot.trace.TracingRobot;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TracingGlobalTest {

  private static List<TracingRobot> traces;

  @BeforeAll
  public static void setup() {
    var tracer = new TracingRunner(new HeadlessRunner());

    GlobalScene.INSTANCE.setRunner(tracer);
    SimpleExample.main(new String[0]);

    traces = tracer.getTraces();
  }

  @Test
  void showTraces() {
    TracingExample.showTraces(traces);
  }

  @Test
  void testThat_noRobotTeleported() {
    for (var trace: traces) {
      for (var state : trace) {
        assertNotEquals(RobotAction.SET_FIELD, state.getLastAction());
      }
    }
  }
}
