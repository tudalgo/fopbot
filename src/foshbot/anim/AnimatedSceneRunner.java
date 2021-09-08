package foshbot.anim;

import foshbot.Scene;
import foshbot.SceneRunner;

public class AnimatedSceneRunner implements SceneRunner {

    @Override
    public void run(Scene scene) {
        var frame = new Frame(scene.getGrid());
        var world = frame.getWorld();
        scene.init(world);
        frame.startUpdateThread();
        scene.run(world);
    }
}