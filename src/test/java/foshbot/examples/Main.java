package foshbot.examples;

import foshbot.anim.AnimatedSceneRunner;
import foshbot.anim.resources.Resources;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        Resources.loadAll();
        var example = new BlockMazeExample();
        new AnimatedSceneRunner().run(example);
    }
}
