package org.noble.helium.actors;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import org.noble.helium.world.WorldObject;

public abstract class Actor implements Disposable {
  private int m_health;
  private float m_speed;
  private boolean m_dead;
  public WorldObject m_worldObject;

  public Actor(WorldObject object, int startingHealth, float speed) {
    m_health = startingHealth;
    m_speed = speed;
    m_dead = false;
    m_worldObject = object;
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
    return m_worldObject.getPosition().x;
  }

  public float getY() {
    return m_worldObject.getPosition().y;
  }

  public float getZ() {
    return m_worldObject.getPosition().z;
  }

  public Vector3 getPosition() {
    return m_worldObject.getPosition();
  }

  public WorldObject getWorldObject() {
    return m_worldObject;
  }

  public void die() {
    m_dead = true;
    m_worldObject.setShouldRender(false);
  }

  public void setSpeed(float speed) {
    m_speed = speed;
  }

  public void setPosition(Vector3 newPosition) {
    m_worldObject.setPosition(newPosition);
  }

  public void setHealth(int health) {
    m_health = health;
  }

  public abstract void update();
}
