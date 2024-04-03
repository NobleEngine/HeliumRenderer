package org.noble.helium.helpers;

public class Dimensions {
  float m_length;
  float m_depth;
  float m_height;

  public Dimensions(float length, float depth, float height) {
    m_length = length;
    m_depth = depth;
    m_height = height;
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
