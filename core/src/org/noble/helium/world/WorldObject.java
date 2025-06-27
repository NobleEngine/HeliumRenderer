package org.noble.helium.world;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import org.noble.helium.handling.ObjectHandler;
import org.noble.helium.math.Box3D;
import org.noble.helium.math.Dimensions3;
import org.noble.helium.math.EulerAngles;
import org.noble.helium.HeliumIO;
import us.ihmc.euclid.tuple3D.interfaces.Point3DBasics;

public class WorldObject extends ModelInstance {
  private final Box3D m_box;
  private Dimensions3 m_dimensions;
  private EulerAngles m_angles;
  private boolean m_shouldRender;
  private final int m_collisionType;

  public WorldObject(Model model, Vector3 position, int collision) {
    super(model);
    m_box = new Box3D(position, getDimensions());
    setPosition(position);
    m_collisionType = collision;
    m_shouldRender = true;
    m_angles = new EulerAngles(0f,0f,0f);
    ObjectHandler.getInstance().add(this);
  }

  public int getCollisionType() {
    return m_collisionType;
  }

  public void setPosition(Vector3 position) {
    m_box.setPosition(position);
    transform.setToTranslation(position);
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

  public Box3D getBox() {
    return m_box;
  }

  public EulerAngles getAngles() {
    return m_angles;
  }

  public void setPosition(float x, float y, float z) {
    setPosition(new Vector3(x, y, z));
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
    Point3DBasics position = m_box.getPosition();
    return new Vector3((float) position.getX(), (float) position.getY(), (float) position.getZ());
  }

  public boolean isColliding(WorldObject object) {
    if(object == null) {
      HeliumIO.println("Physics", "WorldObject is null", HeliumIO.printType.ERROR);
      return false;
    }

    if(m_collisionType == CollisionType.NONE || object.getCollisionType() == CollisionType.NONE) {
      return false;
    }

    return m_box.intersects(object.getBox());
  }

  public void update() {
    m_box.set(new Box3D(getPosition(), getDimensions()));
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
