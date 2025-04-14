package org.noble.helium.rendering;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.utils.ShaderProvider;
import org.noble.helium.subsystems.telemetry.HeliumTelemetry;

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
    try {
      super.begin(cam);
    } catch (Exception e) {
      HeliumTelemetry.getInstance().printErrorln(e.getMessage());
      return;
    }
    m_working = true;
  }

  @Override
  public void end() {
    try {
      super.end();
    } catch(Exception e) {
      HeliumTelemetry.getInstance().printErrorln(e.getMessage());
      return;
    }
    m_working = false;
  }
}