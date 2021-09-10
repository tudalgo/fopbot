package fopbot.anim.paz;

import fopbot.anim.Drawable;

import java.awt.event.*;

public class PanningAndZooming implements MouseListener, MouseWheelListener, MouseMotionListener {

  private final PanningAndZoomingTarget target;

  private final Vector offset;
  private Vector maxTopLeft = null;
  private Vector minTopLeft = null;

  private double scale;
  private double maxScale = Double.POSITIVE_INFINITY;
  private double minScale = 0;

  private Vector prevMouse = null;

  public PanningAndZooming(PanningAndZoomingTarget target) {
    this.target = target;
    this.offset = new Vector(0, 0);
    this.scale = 1;
  }

  public void pan(Vector v) {
    pan(v.x, v.y);
  }

  public void pan(double x, double y) {
    offset.add(x / scale, y / scale);
    clampInBounce();
  }

  private void clampInBounce() {
    var topLeft = screenToWorld(0, 0);

    if (minTopLeft != null) {
      if (topLeft.x < minTopLeft.x) {
        offset.x += topLeft.x - minTopLeft.x;
      }
      if (topLeft.y < minTopLeft.y) {
        offset.y += topLeft.y - minTopLeft.y;
      }
    }

    if (maxTopLeft != null) {
      if (topLeft.x > maxTopLeft.x) {
        offset.x += topLeft.x - maxTopLeft.x;
      }
      if (topLeft.y > maxTopLeft.y) {
        offset.y += topLeft.y - maxTopLeft.y;
      }
    }
  }

  public void zoom(double x, double y, double s) {
    var newScale = scale * s;
    if (newScale > maxScale || newScale < minScale) {
      return;
    }

    var before = screenToWorld(x, y);
    scale = newScale;
    var after = screenToWorld(x, y);

    var d = before
      .sub(after)
      .mul(scale);
    offset.sub(d);

    clampInBounce();
  }

  public void zoom(double s) {
    zoom(target.getWidth() / 2.0, target.getHeight() / 2.0, s);
  }

  public Vector screenToWorld(double x, double y) {
    return new Vector(x, y)
      .sub(offset)
      .div(scale);
  }

  public Vector worldToScreen(double x, double y) {
    return new Vector(x, y)
      .mul(scale)
      .add(offset);
  }

  public Vector toScreenScale(double x, double y) {
    return new Vector(x, y)
      .mul(scale);
  }

  public void draw(Drawable d, Runnable r) {
    d.translated(offset.x, offset.y, () ->
      d.scaled(scale, r));
  }

  @Override
  public void mouseClicked(MouseEvent e) {
  }

  @Override
  public void mousePressed(MouseEvent e) {
    if (e.getButton() == MouseEvent.BUTTON2) {
      prevMouse = new Vector(e.getX(), e.getY());
    }

    var v = screenToWorld(e.getX(), e.getY());
    target.onMousePressed(e.getButton(), v);
  }

  @Override
  public void mouseReleased(MouseEvent e) {
    if (e.getButton() == MouseEvent.BUTTON2) {
      prevMouse = null;
    }
  }

  @Override
  public void mouseEntered(MouseEvent e) {
  }

  @Override
  public void mouseExited(MouseEvent e) {
  }

  @Override
  public void mouseWheelMoved(MouseWheelEvent e) {
    var s = e.getWheelRotation() < 0 ? 1.1 : 0.9;
    zoom(e.getX(), e.getY(), s);
  }

  @Override
  public void mouseDragged(MouseEvent e) {
    if (prevMouse != null) {
      var v = new Vector(e.getX(), e.getY());
      offset.sub(prevMouse.sub(v));
      clampInBounce();
      prevMouse = v;
    }
  }

  @Override
  public void mouseMoved(MouseEvent e) {
  }

  public void setOffset(double x, double y) {
    offset.x = x;
    offset.y = y;
  }

  public void setScale(double scale) {
    this.scale = scale;
  }

  public void setMaxScale(double maxScale) {
    this.maxScale = maxScale;
  }

  public void setMinScale(double minScale) {
    this.minScale = minScale;
  }

  public void setMaxTopLeft(Vector maxTopLeft) {
    this.maxTopLeft = maxTopLeft;
  }

  public void setMinTopLeft(Vector minTopLeft) {
    this.minTopLeft = minTopLeft;
  }
}
