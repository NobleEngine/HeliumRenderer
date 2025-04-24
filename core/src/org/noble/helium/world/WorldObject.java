package org.noble.helium.world;

import com.badlogic.gdx.math.Vector3;
import org.noble.helium.rendering.HeliumModelInstance;
import org.noble.helium.subsystems.telemetry.HeliumTelemetry;

public class WorldObject {
  private final HeliumModelInstance m_modelInstance;
  private AxisOrientedBoundingBox m_boundingBox;
  private final int m_collisionType;

  public WorldObject(HeliumModelInstance modelInstance, int collision) {
    m_modelInstance = modelInstance;
    m_collisionType = collision;

    m_boundingBox = new AxisOrientedBoundingBox(modelInstance.getPosition(), modelInstance.getDimensions(), modelInstance.getAngles());
  }

  public HeliumModelInstance getModelInstance() {
    return m_modelInstance;
  }

  public AxisOrientedBoundingBox getBoundingBox() {
    return m_boundingBox;
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

  public boolean isColliding(WorldObject object) {
    if(object == null) {
      HeliumTelemetry.getInstance().printErrorln("WorldObject is null");
      return false;
    }

    if(m_collisionType == CollisionType.NONE || object.getCollisionType() == CollisionType.NONE) {
      return false;
    }

    return m_boundingBox.isColliding(object.getBoundingBox());
  }

  public void update() {
    m_boundingBox = new AxisOrientedBoundingBox(m_modelInstance.getPosition(), m_modelInstance.getDimensions(), m_modelInstance.getAngles());
  }

  public interface CollisionType {
    int CLIMBABLE = 0;
    int STANDARD = 1;
    int NONE = 2;
  }
}
