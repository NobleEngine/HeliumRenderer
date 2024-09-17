package org.noble.helium.rendering;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import org.noble.helium.handling.ModelHandler;
import org.noble.helium.math.Dimensions3;
import org.noble.helium.math.EulerAngles;

public class HeliumModelInstance extends ModelInstance {
  private Vector3 m_position;
  private boolean m_render;
  public HeliumModelInstance(Model model, Vector3 startingPosition) {
    super(model);
    setPosition(startingPosition);
    m_render = true;
  }

  public void setPosition(Vector3 coords) {
    transform.setToTranslation(coords);
    m_position = coords;
  }

  public void setPosition(float x, float y, float z) {
    transform.setToTranslation(x, y, z);
    m_position = new Vector3(x, y, z);
  }

  public void setShouldRender(boolean shouldRender) {
    m_render = shouldRender;
  }

  public boolean shouldRender() {
    return m_render;
  }

  public Vector3 getPosition() {
    return m_position;
  }

  public Dimensions3 getDimensions() {
    if(ModelHandler.getInstance().getName(this) == null) {
      return new Dimensions3(0,0,0);
    }
    BoundingBox boundingBox = new BoundingBox();
    Vector3 dimensions = new Vector3();
    this.calculateBoundingBox(boundingBox);
    boundingBox.getDimensions(dimensions);
    return new Dimensions3(dimensions.x, dimensions.y, dimensions.z);
  }

  public EulerAngles getAngles() {
    Quaternion quaternion = new Quaternion();
    transform.getRotation(quaternion);
    return new EulerAngles(quaternion.getYaw(), quaternion.getPitch(), quaternion.getRoll());
  }
}
