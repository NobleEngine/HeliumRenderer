package org.noble.helium.world;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.*;
import org.noble.helium.rendering.HeliumModelInstance;

public class WorldObject {
  private final btCollisionObject m_body;
  private final HeliumModelInstance m_modelInstance;

  public WorldObject(HeliumModelInstance modelInstance, ShapeType shape) {
    m_modelInstance = modelInstance;
    m_body = new btCollisionObject();
    m_body.setCollisionShape(getShape(shape));
    m_body.setWorldTransform(m_modelInstance.transform);
  }

  public HeliumModelInstance getModelInstance() {
    return m_modelInstance;
  }

  private btCollisionShape getShape(ShapeType shape) {
    switch(shape) {
      case ShapeType.BOX -> {
        return new btBoxShape(m_modelInstance.getDimensions().scl(0.5f));
      }
      case ShapeType.PLAYER -> {
        return new btBoxShape(new Vector3(5,5,5).scl(0.5f));
      }
    }

    return null;
  }

  public btCollisionObject getBody() {
    return m_body;
  }

  public void setPosition(float x, float y, float z) {
    m_modelInstance.setPosition(x,y,z);
    m_body.setWorldTransform(m_modelInstance.transform);
  }

  public float getX() {
    return m_modelInstance.getPosition().getX();
  }

  public float getY() {
    return m_modelInstance.getPosition().getY();
  }

  public float getZ() {
    return m_modelInstance.getPosition().getZ();
  }

  public float getDimX() {
    return m_body.getWorldTransform().getScaleX();
  }
  public float getDimY() {
    return m_body.getWorldTransform().getScaleY();
  }
  public float getDimZ() {
    return m_body.getWorldTransform().getScaleZ();
  }

  public void update() {
    m_body.setWorldTransform(m_modelInstance.transform);
  }

  public void dispose() {
    m_body.dispose();
  }

  public enum ShapeType { //TODO: Add more
    BOX, PLAYER
  }
}
