package org.noble.helium.math;

import com.badlogic.gdx.math.Vector3;

public class Dimensions3 {
  private final Vector3 m_dim;
  public Dimensions3(float width, float height, float depth) {
    m_dim = new Vector3(width, height, depth);
  }
  public Dimensions3(Vector3 dim) {
    m_dim = dim;
  }

  public float getWidth() {
    return m_dim.x;
  }

  public float getHeight() {
    return m_dim.y;
  }

  public float getDepth() {
    return m_dim.z;
  }

  @Override
  public String toString() {
    return m_dim.toString();
  }
}
