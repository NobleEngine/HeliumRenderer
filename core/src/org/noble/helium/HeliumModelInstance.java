package org.noble.helium;

import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import org.noble.helium.helpers.Coordinates;

public class HeliumModelInstance extends ModelInstance {
  private Coordinates m_position;
  public HeliumModelInstance(Model model, Coordinates startingPosition) {
    super(model);
    setPosition(startingPosition);
  }

  public void setPosition(Coordinates coords) {
    m_position = coords;
    transform.setToTranslation(coords.getVector3());
  }

  public void setPosition(float x, float y, float z) {
    transform.setToTranslation(x, y, z);
    m_position = new Coordinates(x, y, z);
  }

  public Coordinates getPosition() {
    return m_position;
  }

  public Vector3 getDimensions() {
    BoundingBox box = new BoundingBox();
    Vector3 dimensions = new Vector3();
    this.calculateBoundingBox(box);
    box.getDimensions(dimensions);
    return dimensions;
  }
}
