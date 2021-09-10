package fopbot.anim;

import java.awt.*;

public interface Drawable {

  void fill(Color c);

  void strokeWeight(double w);

  void rect(double x, double y, double w, double h);

  void ellipse(double x, double y, double w, double h);

  void triangle(double x1, double y1, double x2, double y2, double x3, double y3);

  void line(double x1, double y1, double x2, double y2);

  void image(Image image, double x, double y, double w, double h);

  void rotated(double theta, double x, double y, Runnable r);

  void translated(double x, double y, Runnable r);

  void scaled(double scale, Runnable r);

  void text(double x, double y, String text);

  void textSize(int size);
}
