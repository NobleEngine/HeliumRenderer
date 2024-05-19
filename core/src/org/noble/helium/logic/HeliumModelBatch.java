package org.noble.helium.logic;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;

public class HeliumModelBatch extends ModelBatch {
  private boolean m_working;
  public HeliumModelBatch() {
    super();
  }

  public boolean isWorking() {
    return m_working;
  }

  @Override
  public void begin(Camera cam) {
    super.begin(cam);
    m_working = true;
  }

  @Override
  public void end() {
    super.end();
    m_working = false;
  }
}
