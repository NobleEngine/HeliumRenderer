package org.noble.helium.helpers;

import com.badlogic.gdx.math.Vector3;

public class Dimensions3 {
  private final Vector3 dim;
  public Dimensions3(float width, float height, float depth) {
    dim = new Vector3(width, height, depth);
  }

  public float getWidth() {
    return dim.x;
  }

  public float getHeight() {
    return dim.y;
  }

  public float getDepth() {
    return dim.z;
  }
}
