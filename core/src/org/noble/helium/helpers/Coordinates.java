package org.noble.helium.helpers;

import com.badlogic.gdx.math.Vector3;

public class Coordinates {
  float m_x;
  float m_y;
  float m_z;

  public Coordinates(float x,float y,float z) {
    m_x = x;
    m_y = y;
    m_z = z;
  }

  public Coordinates(Vector3 vector3) {
    m_x = vector3.x;
    m_y = vector3.y;
    m_z = vector3.z;
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

  public Vector3 getVector3() {
    return new Vector3(getX(), getY(), getZ());
  }
}
