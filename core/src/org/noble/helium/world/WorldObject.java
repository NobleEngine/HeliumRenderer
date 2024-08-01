package org.noble.helium.world;

import com.badlogic.gdx.math.Vector3;
import org.noble.helium.helpers.Dimensions;
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

  public void setPosition(float x, float y, float z) {
    m_modelInstance.setPosition(x,y,z);
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
    if(object.getCollisionType() == CollisionType.NONE) {
      return false;
    }

    Dimensions thisDimensions = m_modelInstance.getDimensions();
    Vector3 thisPosition = m_modelInstance.getPosition();
    Dimensions comparedDimensions = object.getModelInstance().getDimensions();
    Vector3 comparedPosition = object.getModelInstance().getPosition();

    double extentA_x = thisDimensions.getWidth() / 2.0;
    double extentA_y = thisDimensions.getHeight() / 2.0;
    double extentA_z = thisDimensions.getDepth() / 2.0;

    double extentB_x = comparedDimensions.getWidth() / 2.0;
    double extentB_y = comparedDimensions.getHeight() / 2.0;
    double extentB_z = comparedDimensions.getDepth() / 2.0;

    boolean xOverlap = Math.abs(thisPosition.x - comparedPosition.x) <= (extentA_x + extentB_x);
    boolean yOverlap = Math.abs(thisPosition.y - comparedPosition.y) <= (extentA_y + extentB_y);
    boolean zOverlap = Math.abs(thisPosition.z - comparedPosition.z) <= (extentA_z + extentB_z);

    return xOverlap && yOverlap && zOverlap;
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
