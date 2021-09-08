import foshbot.Direction;
import foshbot.Scene;
import foshbot.World;
import foshbot.anim.AnimatedSceneRunner;
import foshbot.anim.resources.Resources;

import java.io.IOException;
import java.util.List;

public class TestAnimatedTurning implements Scene {
    @Override
    public int getWidth() {
        return 20;
    }

    @Override
    public int getHeight() {
        return 10;
    }

    @Override
    public void init(World world) {
    }

    @Override
    public void run(World world) {
        var robots = List.of(
            world.newRobot(0, 0, Direction.NORTH, 0),
            world.newRobot(1, 1, Direction.EAST, 0),
            world.newRobot(2, 2, Direction.SOUTH, 0),
            world.newRobot(3, 3, Direction.WEST, 0));

        while (true) {
            for (var r : robots) {
                r.turnLeft();
                r.turnLeft();
                r.turnLeft();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Resources.loadAll();
        new AnimatedSceneRunner().run(new TestAnimatedTurning());
    }
}
