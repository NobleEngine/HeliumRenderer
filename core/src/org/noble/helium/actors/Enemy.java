package org.noble.helium.actors;

import com.badlogic.gdx.math.Vector3;
import org.noble.helium.Helium;
import org.noble.helium.handling.InputHandler;
import org.noble.helium.handling.ObjectHandler;
import org.noble.helium.math.EulerAngles;
import org.noble.helium.rendering.HeliumModelInstance;
import org.noble.helium.subsystems.telemetry.HeliumTelemetry;
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

  public Vector3 calculateCollision(Vector3 position) {
    WorldObject thisObject = getWorldObject();
    ArrayList<WorldObject> objects = ObjectHandler.getInstance().getCollidingObjects(thisObject);
    for (WorldObject object : objects) {
      if (object.getCollisionType() == WorldObject.CollisionType.CLIMBABLE) {
        float topFaceOfObj = object.getY() + object.getHeight() / 2f;
        float translation = topFaceOfObj + thisObject.getHeight() / 2f;
        float movementSpeed = (Helium.getInstance().getDelta() * 2f);
        float yMovement = translation - position.y;
        if (translation > position.y) {
          if (yMovement < 3f) {
            movementSpeed *= 4f;
          }
          position.y += (yMovement) * movementSpeed;
        }
      }
      float extentA_x = thisObject.getWidth() / 2.0f;
      float extentA_y = thisObject.getHeight() / 2.0f;
      float extentA_z = thisObject.getDepth() / 2.0f;

      float extentB_x = object.getWidth() / 2.0f;
      float extentB_y = object.getHeight() / 2.0f;
      float extentB_z = object.getDepth() / 2.0f;

      float overlapX = (extentA_x + extentB_x) - Math.abs(thisObject.getX() - object.getX());
      float overlapY = (extentA_y + extentB_y) - Math.abs(thisObject.getY() - object.getY());
      float overlapZ = (extentA_z + extentB_z) - Math.abs(thisObject.getZ() - object.getZ());

      if (overlapX < overlapY && overlapX < overlapZ) {
        // Smallest overlap is in the x-axis
        if (thisObject.getX() < object.getX()) {
          position.x = object.getX() - (extentA_x + extentB_x);
        } else {
          position.x = object.getX() + (extentA_x + extentB_x);
        }
      } else if (overlapY < overlapX && overlapY < overlapZ) {
        // Smallest overlap is in the y-axis
        if (thisObject.getY() < object.getY()) {
          position.y = object.getY() - (extentA_y + extentB_y);
        } else {
          position.y = object.getY() + (extentA_y + extentB_y);
        }
        position.y -= 0.0005f;
      } else {
        // Smallest overlap is in the z-axis
        if (thisObject.getZ() < object.getZ()) {
          position.z = object.getZ() - (extentA_z + extentB_z);
        } else {
          position.z = object.getZ() + (extentA_z + extentB_z);
        }
      }
    }


    return position;
  }

  private void follow(WorldObject target) {
    Vector3 nextPosition = new Vector3(getPosition().x, getPosition().y, getPosition().z);
    float delta = Helium.getInstance().getDelta();

    if(!m_worldObject.isColliding(target)) {
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
    }

    setPosition(calculateCollision(nextPosition));
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
