package org.noble.helium.actors;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;

public abstract class Actor implements Disposable {
  private Vector3 m_position;
  private int m_health;
  private final float m_speed;
  private boolean m_dead;

  public Actor(Vector3 startingPos, int startingHealth, float speed) {
    m_position = startingPos;
    m_health = startingHealth;
    m_speed = speed;
    m_dead = false;
  }

  public boolean isDead() {
    return m_dead;
  }

  public int getHealth() {
    return m_health;
  }

  public float getSpeed() {
    return m_speed;
  }

  public float getX() {
    return m_position.x;
  }

  public float getY() {
    return m_position.y;
  }

  public float getZ() {
    return m_position.z;
  }
  public Vector3 getPosition() {
    return m_position;
  }

  public void setPosition(Vector3 newPosition) {
    m_position = newPosition;
  }

  public void setHealth(int health) {
    m_health = health;
  }

  public abstract void update();

}
