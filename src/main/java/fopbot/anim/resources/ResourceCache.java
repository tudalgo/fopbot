package fopbot.anim.resources;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Objects;

public abstract class ResourceCache<T> {

  private final HashMap<String, T> cache = new HashMap<>();

  ResourceCache(String... resources) throws IOException {
    for (var r : resources) {
      load(r);
    }
  }

  public T load(String resource) throws IOException {
    if (!cache.containsKey(resource)) {
      var in = Objects.requireNonNull(
        getClass().getResourceAsStream(resource),
        "Resource `" + resource + "` not found");

      cache.put(resource, loadFromStream(in));
    }
    return cache.get(resource);
  }

  public T get(String resource) {
    return cache.get(resource);
  }

  protected abstract T loadFromStream(InputStream in) throws IOException;
}
