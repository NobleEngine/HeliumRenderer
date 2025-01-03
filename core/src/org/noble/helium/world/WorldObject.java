package org.noble.helium.world;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionObject;
import com.badlogic.gdx.physics.bullet.collision.btCollisionShape;
import org.noble.helium.math.Dimensions3;
import org.noble.helium.rendering.HeliumModelInstance;

public class WorldObject {
  private final HeliumModelInstance m_modelInstance;
  private btCollisionShape m_collisionShape;
  private final btCollisionObject m_collisionObject;
  private final int m_collisionType;

  public WorldObject(HeliumModelInstance modelInstance, ShapeType shape, int collision) {
    m_modelInstance = modelInstance;
    m_collisionType = collision;
    Dimensions3 modelDim = m_modelInstance.getDimensions();
    switch(shape) {
      case BOX -> m_collisionShape = new btBoxShape(new Vector3(modelDim.getWidth() / 2f, modelDim.getHeight() / 2f, modelDim.getDepth() / 2f));
      //TODO: More.
    }
    m_collisionObject = new btCollisionObject();
    m_collisionObject.setCollisionShape(m_collisionShape);
    m_collisionObject.setWorldTransform(m_modelInstance.transform);
  }

  public HeliumModelInstance getModelInstance() {
    return m_modelInstance;
  }

  public int getCollisionType() {
    return m_collisionType;
  }

  public void setPosition(Vector3 position) {
    m_modelInstance.setPosition(position);
  }

  public float getX() {
    return m_modelInstance.getPosition().x;
  }

  public float getY() {
    return m_modelInstance.getPosition().y;
  }

  public float getZ() {
    return m_modelInstance.getPosition().z;
  }

  public float getWidth() {
    return m_modelInstance.getDimensions().getWidth();
  }

  public float getHeight() {
    return m_modelInstance.getDimensions().getHeight();
  }

  public float getDepth() {
    return m_modelInstance.getDimensions().getDepth();
  }

  public btCollisionObject getBulletObject() {
    return m_collisionObject;
  }

  public void update() {
    m_collisionObject.setWorldTransform(m_modelInstance.transform);
  }

  public enum ShapeType { //TODO: Add more
    BOX, SPHERE
  }

  public interface CollisionType {
    int CLIMBABLE = 0;
    int STANDARD = 1;
    int NONE = 2;
  }

  public void dispose() {
    m_collisionObject.dispose();
    m_collisionShape.dispose();
  }
}
