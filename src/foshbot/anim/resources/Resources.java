package foshbot.anim.resources;


import foshbot.anim.AnimatedRobot;

import java.awt.*;
import java.io.IOException;

public class Resources {

    private static ResourceCache<Image> images;

    public static void loadAll() throws IOException {
        images = new ImageResourceCache(AnimatedRobot.RESOURCE);
    }

    public static ResourceCache<Image> getImages() {
        return images;
    }
}
