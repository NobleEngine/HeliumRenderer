package org.noble.helium.world;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import org.noble.helium.handling.ObjectHandler;
import org.noble.helium.math.Dimensions3;
import org.noble.helium.math.EulerAngles;
import org.noble.helium.PrintUtils;

public class WorldObject extends ModelInstance {
  private AxisOrientedBoundingBox m_boundingBox;
  private Vector3 m_position;
  private Dimensions3 m_dimensions;
  private EulerAngles m_angles;
  private boolean m_shouldRender;
  private final int m_collisionType;

  public WorldObject(Model model, Vector3 position, int collision) {
    super(model);
    setPosition(position);
    m_collisionType = collision;
    m_shouldRender = true;
    m_angles = new EulerAngles(0f,0f,0f);
    m_boundingBox = new AxisOrientedBoundingBox(getPosition(), getDimensions(), getAngles());
    ObjectHandler.getInstance().add(this);
  }

  public AxisOrientedBoundingBox getBoundingBox() {
    return m_boundingBox;
  }

  public int getCollisionType() {
    return m_collisionType;
  }

  public void setPosition(Vector3 position) {
    m_position = position;
    transform.setToTranslation(m_position);
  }

  public Dimensions3 getDimensions() {
    if(m_dimensions != null) {
      return m_dimensions;
    }
    BoundingBox boundingBox = new BoundingBox();
    Vector3 dimensions = new Vector3();
    this.calculateBoundingBox(boundingBox);
    boundingBox.getDimensions(dimensions);
    m_dimensions = new Dimensions3(dimensions.x, dimensions.y, dimensions.z);
    return m_dimensions;
  }

  public EulerAngles getAngles() {
    return m_angles;
  }

  public void setPosition(float x, float y, float z) {
    transform.setToTranslation(x, y, z);
    m_position = new Vector3(x, y, z);
  }

  public void setShouldRender(boolean shouldRender) {
    m_shouldRender = shouldRender;
  }

  public void setRotation(EulerAngles angles) {
    m_angles = angles;
    transform.setFromEulerAnglesRad(angles.getYaw(), angles.getPitch(), angles.getRoll());
  }

  public boolean shouldRender() {
    return m_shouldRender;
  }

  public Vector3 getPosition() {
    return m_position;
  }

  public boolean isColliding(WorldObject object) {
    if(object == null) {
      PrintUtils.println("Physics", "WorldObject is null", PrintUtils.printType.ERROR);
      return false;
    }

    if(m_collisionType == CollisionType.NONE || object.getCollisionType() == CollisionType.NONE) {
      return false;
    }

    return m_boundingBox.isColliding(object.getBoundingBox());
  }

  public void update() {
    m_boundingBox = new AxisOrientedBoundingBox(getPosition(), getDimensions(), getAngles());
  }

  public void dispose() {
    this.model.dispose();
  }

  public interface CollisionType {
    int CLIMBABLE = 0;
    int STANDARD = 1;
    int NONE = 2;
  }
}
