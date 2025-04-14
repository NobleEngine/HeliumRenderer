package org.noble.helium.rendering;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.utils.ShaderProvider;
import org.noble.helium.handling.ShaderHandler;

public class HeliumModelBatch extends ModelBatch {
  private boolean m_working;

  public HeliumModelBatch() {
    super();
  }

  public HeliumModelBatch(ShaderProvider shaderProvider) {
    super(shaderProvider);
  }

  public boolean isWorking() {
    return m_working;
  }

  @Override
  public void begin(Camera cam) {
    ShaderHandler.getInstance().begin();
    super.begin(cam);
    m_working = true;
  }

  @Override
  public void end() {
    super.end();
    ShaderHandler.getInstance().end();
    m_working = false;
  }
}