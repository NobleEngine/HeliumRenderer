package org.noble.helium.helpers;

public class Dimensions {
  float m_length;
  float m_depth;
  float m_height;

  public Dimensions(float x, float y, float z) {
    m_length = x;
    m_depth = y;
    m_height = z;
  }

  public float getDepth() {
    return m_length;
  }

  public float getWidth() {
    return m_depth;
  }

  public float getHeight() {
    return m_height;
  }
}
