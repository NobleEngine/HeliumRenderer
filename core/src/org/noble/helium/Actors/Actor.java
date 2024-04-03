package org.noble.helium.Actors;

import org.noble.helium.helpers.Coordinates;

public abstract class Actor {
  public Coordinates m_position;
  public int m_health;
  public final float m_speed;
  public boolean m_dead;

  public Actor(Coordinates startingPos, int startingHealth, float speed) {
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

  public float getX() {
    return m_position.getX();
  }

  public float getY() {
    return m_position.getY();
  }

  public float getZ() {
    return m_position.getZ();
  }

  public Coordinates getPosition() {
    return m_position;
  }

  public void setPosition(Coordinates newPosition) {
    m_position = newPosition;
  }

  public abstract void update();

}
