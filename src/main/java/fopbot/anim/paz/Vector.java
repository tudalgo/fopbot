package fopbot.anim.paz;

import java.awt.*;

public class Vector {

  public double x, y;

  public Vector(double x, double y) {
    this.x = x;
    this.y = y;
  }

  public Vector copy() {
    return new Vector(x, y);
  }

  public Vector add(Vector v) {
    return add(v.x, v.y);
  }

  public Vector add(double x, double y) {
    this.x += x;
    this.y += y;
    return this;
  }

  public Vector sub(Vector v) {
    x -= v.x;
    y -= v.y;
    return this;
  }

  public Vector mul(double d) {
    x *= d;
    y *= d;
    return this;
  }

  public Vector div(double d) {
    x /= d;
    y /= d;
    return this;
  }

  double dot(Vector v) {
    return x * v.x + y * v.y;
  }

  public double mag() {
    return Math.sqrt(dot(this));
  }

  public Vector norm() {
    return div(mag());
  }

  Vector setMag(double mag) {
    return mul(mag / this.mag());
  }

  public double dist(Vector v) {
    return dist(v.x, v.y);
  }

  public double dist(double x, double y) {
    var dx = this.x - x;
    var dy = this.y - y;
    return Math.sqrt(dx * dx + dy * dy);
  }


  public double angle() {
    return Math.atan2(y, x);
  }

  public Dimension toDimension() {
    return new Dimension((int) x, (int) y);
  }

  public Point toPoint() {
    return new Point((int) x, (int) y);
  }

  @Override
  public String toString() {
    return "(" + x + ", " + y + ')';
  }
}
