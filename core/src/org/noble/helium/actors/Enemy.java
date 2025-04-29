package org.noble.helium.actors;

import com.badlogic.gdx.math.Vector3;
import org.noble.helium.Helium;
import org.noble.helium.world.WorldObject;

public class Enemy extends Actor {
  private final Actor m_followingActor;
  private final int m_strength;
  private float m_timer;

  public Enemy(WorldObject object, int startingHealth, int strength, float speed, Actor followingActor) {
    super(object, startingHealth, speed);
    m_strength = strength;
    m_followingActor = followingActor;
    m_timer = 0.0f;
  }

  @Override
  public void update() {
    m_worldObject.update();
    Vector3 nextPosition = new Vector3(getPosition().x, getPosition().y, getPosition().z);
    float delta = Helium.getInstance().getDelta();
    WorldObject target = m_followingActor.getWorldObject();
    if (target == null) {
      return;
    }

    if (!m_worldObject.isColliding(target)) {
      if (target.getPosition().x > getX()) {
        nextPosition.x += getSpeed() * delta;
      } else if (target.getPosition().x < getX()) {
        nextPosition.x -= getSpeed() * delta;
      }

      if (target.getPosition().y - target.getDimensions().getDepth() > getY() - this.getWorldObject().getDimensions().getDepth()) {
        nextPosition.y += getSpeed() * delta;
      } else if (target.getPosition().y - target.getDimensions().getDepth() < getY() - this.getWorldObject().getDimensions().getDepth()) {
        nextPosition.y -= getSpeed() * delta;
      }

      if (target.getPosition().z > getZ()) {
        nextPosition.z += getSpeed() * delta;
      } else if (target.getPosition().z < getZ()) {
        nextPosition.z -= getSpeed() * delta;
      }
      m_timer = 1.0f;
    } else {
      m_timer += delta;
      if(m_timer >= 1.0f) {
        m_followingActor.setHealth(m_followingActor.getHealth() - m_strength);
        m_timer = 0.0f;
      }
    }

    if (getHealth() <= 0) {
      die();
    }

    setPosition(nextPosition);
  }

  @Override
  public void dispose() {
  }

  @Override
  public void setPosition(Vector3 position) {
    super.setPosition(position);
    m_worldObject.setPosition(position);
  }
}
