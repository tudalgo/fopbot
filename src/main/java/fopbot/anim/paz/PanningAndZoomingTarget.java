package fopbot.anim.paz;

public interface PanningAndZoomingTarget {

  int getWidth();

  int getHeight();

  void onMousePressed(int button, Vector v);
}
