package org.noble.helium.actors;

import com.badlogic.gdx.math.Vector3;
import org.noble.helium.Helium;
import org.noble.helium.rendering.HeliumModelInstance;
import org.noble.helium.subsystems.telemetry.LogEntry;
import org.noble.helium.world.WorldObject;

import java.util.ArrayList;

public class Enemy extends Actor {
  private final WorldObject m_followingObject;

  public Enemy(Vector3 startingPos, int startingHealth, float speed, HeliumModelInstance model, WorldObject followingObject) {
    super(startingPos, startingHealth, speed, model);
    m_worldObject = new WorldObject(m_model, WorldObject.ShapeType.BOX, WorldObject.CollisionType.STANDARD);
    m_followingObject = followingObject;
  }

  public WorldObject getWorldObject() {
    return m_worldObject;
  }

  private void follow(WorldObject target) {
    Vector3 nextPosition = new Vector3(getPosition().x, getPosition().y, getPosition().z);
    float delta = Helium.getInstance().getDelta();

    if (target.getX() > getX()) {
      nextPosition.x += getSpeed() * delta;
    } else if (target.getX() < getX()) {
      nextPosition.x -= getSpeed() * delta;
    }

    if (target.getY() - target.getHeight() > getY() - this.getWorldObject().getHeight()) {
      nextPosition.y += getSpeed() * delta;
    } else if (target.getY() - target.getHeight() < getY() - this.getWorldObject().getHeight()) {
      nextPosition.y -= getSpeed() * delta;
    }

    if (target.getZ() > getZ()) {
      nextPosition.z += getSpeed() * delta;
    } else if (target.getZ() < getZ()) {
      nextPosition.z -= getSpeed() * delta;
    }

    setPosition(nextPosition);
  }

  @Override
  public void update() {
    follow(m_followingObject);
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
