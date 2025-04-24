package org.noble.helium.actors;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import org.noble.helium.rendering.HeliumModelInstance;
import org.noble.helium.world.WorldObject;

public abstract class Actor implements Disposable {
  private Vector3 m_position;
  private int m_health;
  private float m_speed;
  private boolean m_dead;
  public HeliumModelInstance m_model;
  public WorldObject m_worldObject;

  public Actor(Vector3 startingPos, int startingHealth, float speed, HeliumModelInstance model) {
    m_position = startingPos;
    m_health = startingHealth;
    m_speed = speed;
    m_dead = false;
    m_model = model;
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

  public WorldObject getWorldObject() {
    return m_worldObject;
  }

  public void die() {
    m_dead = true;
    m_model.setShouldRender(false);
  }

  public void setSpeed(float speed) {
    m_speed = speed;
  }

  public void setPosition(Vector3 newPosition) {
    m_position = newPosition;
  }

  public void setHealth(int health) {
    m_health = health;
  }

  public abstract void update();
}
