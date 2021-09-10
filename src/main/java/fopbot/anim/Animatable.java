package fopbot.anim;

public interface Animatable {

  void draw(Drawable d);

  /**
   * Update internal state
   *
   * @param dt Times passed since last update
   * @return True iff done updating
   */
  boolean update(double dt);
}
