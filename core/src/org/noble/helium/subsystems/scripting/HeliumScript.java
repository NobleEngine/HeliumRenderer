package org.noble.helium.subsystems.scripting;

import com.badlogic.gdx.Gdx;
import org.noble.helium.subsystems.telemetry.HeliumTelemetry;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HeliumScript {
  private final Class<?> m_externalClass;

  public HeliumScript(Class<?> externalClass) {
    m_externalClass = externalClass;
  }

  public void update() throws NoSuchMethodException {
    Method updateMethod = m_externalClass.getMethod("update", float.class);
    try {
      updateMethod.invoke(m_externalClass.getDeclaredConstructor().newInstance(), Gdx.graphics.getDeltaTime());
    } catch (IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException e) {
      HeliumTelemetry.getInstance().printErrorln(e.getMessage());
    }
  }
}
