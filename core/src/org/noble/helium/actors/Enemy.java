package org.noble.helium.actors;

import com.badlogic.gdx.math.Vector3;
import org.noble.helium.Helium;
import org.noble.helium.rendering.HeliumModelInstance;
import org.noble.helium.subsystems.telemetry.LogEntry;
import org.noble.helium.world.WorldObject;

import java.util.ArrayList;

public class Enemy extends Actor {
  private final Actor m_followingActor;

  public Enemy(Vector3 startingPos, int startingHealth, float speed, HeliumModelInstance model, Actor followingActor) {
    super(startingPos, startingHealth, speed, model);
    m_worldObject = new WorldObject(m_model, WorldObject.ShapeType.BOX, WorldObject.CollisionType.STANDARD);
    m_followingActor = followingActor;
  }

  private void follow(Actor followingActor) {
    Vector3 nextPosition = new Vector3(getPosition().x, getPosition().y, getPosition().z);
    float delta = Helium.getInstance().getDelta();
    WorldObject target = followingActor.getWorldObject();
    if (target == null) {
      return;
    }

    if (!m_worldObject.isColliding(target)) {
      if (target.getX() > getX()) {
        nextPosition.x += getSpeed() * delta;
      } else if (target.getX() < getX()) {
        nextPosition.x -= getSpeed() * delta;
      }

      if (target.getY() - target.getDepth() > getY() - this.getWorldObject().getDepth()) {
        nextPosition.y += getSpeed() * delta;
      } else if (target.getY() - target.getDepth() < getY() - this.getWorldObject().getDepth()) {
        nextPosition.y -= getSpeed() * delta;
      }

      if (target.getZ() > getZ()) {
        nextPosition.z += getSpeed() * delta;
      } else if (target.getZ() < getZ()) {
        nextPosition.z -= getSpeed() * delta;
      }
    } else {
      followingActor.setHealth(followingActor.getHealth() - 1);
    }

    if (getHealth() <= 0) {
      die();
    }

    setPosition(nextPosition);
  }

  @Override
  public void update() {
    follow(m_followingActor);
  }

  @Override
  public void dispose() {
  }

  @Override
  public void setPosition(Vector3 position) {
    super.setPosition(position);
    m_model.setPosition(position);
    m_worldObject.setPosition(position);
  }

  @Override
  public ArrayList<LogEntry> getLogEntries() {
    return null;
  }
}
