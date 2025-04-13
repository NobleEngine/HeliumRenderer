package org.noble.helium.rendering;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import org.noble.helium.math.Dimensions3;
import org.noble.helium.math.EulerAngles;

public class HeliumModelInstance extends ModelInstance {
  private Vector3 m_position;
  private EulerAngles m_angles;
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

  public void setRotation(EulerAngles angles) {
    transform.setFromEulerAnglesRad(angles.getYaw(), angles.getPitch(), angles.getRoll());
  }

  public boolean shouldRender() {
    return m_render;
  }

  public Vector3 getPosition() {
    return m_position;
  }

  public Dimensions3 getDimensions() {
    BoundingBox boundingBox = new BoundingBox();
    Vector3 dimensions = new Vector3();
    this.calculateBoundingBox(boundingBox);
    boundingBox.getDimensions(dimensions);
    return new Dimensions3(dimensions.x, dimensions.y, dimensions.z);
  }

  public EulerAngles getAngles() {
    if(m_angles == null) {
      return new EulerAngles(0,0,0);
    }
    return m_angles;
  }
}
