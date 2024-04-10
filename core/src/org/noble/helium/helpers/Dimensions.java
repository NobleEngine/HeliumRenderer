package org.noble.helium.helpers;

public class Dimensions {
  float m_width;
  float m_depth;
  float m_height;

  public Dimensions(float height, float width, float depth) {
    m_height = height;
    m_width = width;
    m_depth = depth;
  }

  public float getDepth() {
    return m_depth;
  }

  public float getWidth() {
    return m_width;
  }

  public float getHeight() {
    return m_height;
  }
}
