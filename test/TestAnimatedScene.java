import foshbot.Direction;
import foshbot.Scene;
import foshbot.World;
import foshbot.anim.AnimatedSceneRunner;
import foshbot.anim.resources.Resources;

import java.io.IOException;

public class TestAnimatedScene implements Scene {
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
        var r = world.newRobot(4, 4, Direction.NORTH, 0);

        for (int i = 0; i < 20; i++) {
            r.turnLeft();
        }
    }

    public static void main(String[] args) throws IOException {
        Resources.loadAll();
        new AnimatedSceneRunner().run(new TestAnimatedScene());
    }
}
