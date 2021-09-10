package fopbot.anim.resources;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;

class ImageResourceCache extends ResourceCache<Image> {

  ImageResourceCache(String... resources) throws IOException {
    super(resources);
  }

  @Override
  protected Image loadFromStream(InputStream in) throws IOException {
    return ImageIO.read(in);
  }
}
