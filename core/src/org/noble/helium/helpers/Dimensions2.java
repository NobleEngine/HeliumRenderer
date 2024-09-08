package org.noble.helium.helpers;

import com.badlogic.gdx.math.Vector2;

public class Dimensions2 {
  private final Vector2 dim;
  public Dimensions2(float width, float height) {
    dim = new Vector2(width, height);
  }

  public float getWidth() {
    return dim.x;
  }

  public float getHeight() {
    return dim.y;
  }
}
