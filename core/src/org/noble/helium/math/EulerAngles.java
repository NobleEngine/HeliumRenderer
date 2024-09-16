package org.noble.helium.math;

import com.badlogic.gdx.math.Vector3;

public class EulerAngles {
  private final Vector3 m_angles;
  public EulerAngles(float yaw, float pitch, float roll) {
    m_angles = new Vector3(yaw, pitch, roll);
  }

  public float getYaw() {
    return m_angles.x;
  }

  public float getPitch() {
    return m_angles.y;
  }

  public float getRoll() {
    return m_angles.z;
  }
}
