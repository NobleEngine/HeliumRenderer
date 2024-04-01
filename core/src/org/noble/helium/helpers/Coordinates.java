package org.noble.helium.helpers;

public class Coordinates {
  float m_x;
  float m_y;
  float m_z;

  public Coordinates(float x,float y,float z) {
    m_x = x;
    m_y = y;
    m_z = z;
  }

  public float getX() {
    return m_x;
  }

  public float getY() {
    return m_y;
  }

  public float getZ() {
    return m_z;
  }
}
