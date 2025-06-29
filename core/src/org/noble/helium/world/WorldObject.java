package org.noble.helium.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import org.noble.helium.handling.ObjectHandler;
import org.noble.helium.handling.TextureHandler;
import org.noble.helium.math.Box3D;
import org.noble.helium.math.Dimensions3;
import org.noble.helium.HeliumIO;
import us.ihmc.euclid.tuple3D.interfaces.Point3DBasics;

public class WorldObject extends ModelInstance {
  private final Box3D m_box;
  private boolean m_shouldRender;
  private final int m_collisionType;

  public WorldObject(Model model, Vector3 position, int collision) {
    super(model);

    BoundingBox boundingBox = new BoundingBox();
    Vector3 dimensions = new Vector3();
    model.calculateBoundingBox(boundingBox);
    boundingBox.getDimensions(dimensions);
    m_box = new Box3D(position, new Dimensions3(dimensions.x, dimensions.y, dimensions.z));

    setPosition(position);
    m_collisionType = collision;
    m_shouldRender = true;
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
    return new Dimensions3((float) m_box.getSizeX(), (float) m_box.getSizeY(), (float) m_box.getSizeZ());
  }

  public Box3D getBox() {
    return m_box;
  }

  public void setShouldRender(boolean shouldRender) {
    m_shouldRender = shouldRender;
  }

  public void setTexture(Texture texture) {
    materials.forEach(material -> material.set(new Material(TextureAttribute.createDiffuse(texture))));
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
