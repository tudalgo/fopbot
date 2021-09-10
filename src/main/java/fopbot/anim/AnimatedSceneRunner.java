package fopbot.anim;

import fopbot.Scene;
import fopbot.SceneRunner;

public class AnimatedSceneRunner implements SceneRunner {

    @Override
    public void run(Scene scene) {
        var frame = new Frame(scene.getGrid());
        var world = frame.getWorld();

        scene.init(world);
        world.start();

        frame.startUpdateThread();
        scene.run(world);
    }
}