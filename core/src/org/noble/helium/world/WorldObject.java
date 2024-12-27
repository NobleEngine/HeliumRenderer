package org.noble.helium.world;

import com.badlogic.gdx.math.Vector3;
import org.noble.helium.rendering.HeliumModelInstance;

public class WorldObject {
  private final HeliumModelInstance m_modelInstance;
  private final int m_collisionType;

  public WorldObject(HeliumModelInstance modelInstance, ShapeType shape, int collision) {
    m_modelInstance = modelInstance;
    m_collisionType = collision;
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

  public void update() {
  }

  public enum ShapeType { //TODO: Add more
    BOX
  }

  public interface CollisionType {
    int CLIMBABLE = 0;
    int STANDARD = 1;
    int NONE = 2;
  }
}
