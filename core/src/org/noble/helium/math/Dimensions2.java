package org.noble.helium.math;

import com.badlogic.gdx.math.Vector2;

public class Dimensions2 {
  private final Vector2 m_dim;
  public Dimensions2(float width, float height) {
    m_dim = new Vector2(width, height);
  }

  public float getWidth() {
    return m_dim.x;
  }

  public float getHeight() {
    return m_dim.y;
  }
}
