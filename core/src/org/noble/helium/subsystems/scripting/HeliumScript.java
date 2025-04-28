package org.noble.helium.subsystems.scripting;

import com.badlogic.gdx.Gdx;
import org.noble.helium.subsystems.HeliumTelemetry;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class HeliumScript {
  private final Class<?> m_externalClass;

  public HeliumScript(Class<?> externalClass) {
    m_externalClass = externalClass;
  }

  public void update() {
    Method updateMethod;
    try {
      updateMethod = m_externalClass.getMethod("update", float.class);
    } catch (NoSuchMethodException e) {
      HeliumTelemetry.error("Script Handler", e, HeliumTelemetry.ErrorType.NONFATAL, true);
      return;
    }
    try {
      updateMethod.invoke(m_externalClass.getDeclaredConstructor().newInstance(), Gdx.graphics.getDeltaTime());
    } catch (IllegalAccessException | InvocationTargetException | InstantiationException | NoSuchMethodException e) {
      HeliumTelemetry.error("Script Handler", e, HeliumTelemetry.ErrorType.NONFATAL, true);
    }
  }
}
