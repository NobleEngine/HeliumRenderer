package org.noble.helium.helpers;

import com.badlogic.gdx.math.Vector3;

public class Dimensions {
  private final Vector3 dim;
  public Dimensions(float width, float height, float depth) {
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

  public Vector3 getVector3() {
    return dim;
  }
}
