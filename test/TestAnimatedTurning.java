import foshbot.Direction;
import foshbot.Scene;
import foshbot.World;
import foshbot.anim.AnimatedSceneRunner;
import foshbot.anim.resources.Resources;
import foshbot.impl.Grid;

import java.io.IOException;
import java.util.List;

public class TestAnimatedTurning implements Scene {
    @Override
    public Grid getGrid() {
        return new Grid(20, 10);
    }

    @Override
    public void init(World world) {
    }

    @Override
    public void run(World world) {
        var robots = List.of(
            world.newRobot(4, 4, Direction.NORTH, 0),
            world.newRobot(5, 5, Direction.EAST, 0),
            world.newRobot(6, 6, Direction.SOUTH, 0),
            world.newRobot(7, 7, Direction.WEST, 0));

        while (true) {
            for (var r : robots) {
                r.turnLeft();
                r.move();
            }
        }
    }

    public static void main(String[] args) throws IOException {
        Resources.loadAll();
        new AnimatedSceneRunner().run(new TestAnimatedTurning());
    }
}
