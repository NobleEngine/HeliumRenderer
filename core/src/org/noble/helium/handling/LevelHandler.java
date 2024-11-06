package org.noble.helium.handling;

import org.noble.helium.Helium;
import org.noble.helium.screens.HeliumLevel;
import org.noble.helium.screens.tests.PhysicsTest;
import org.noble.helium.subsystems.telemetry.HeliumTelemetry;

public class LevelHandler {
  private static LevelHandler m_instance;
  private HeliumLevel m_currentLevel;
  private final Helium m_helium;
  private final ModelHandler m_modelHandler;
  private final ObjectHandler m_objectHandler;
  private final HeliumTelemetry m_telemetry;
  private String m_previousLevelName;


  private LevelHandler() {
    m_helium = Helium.getInstance();
    m_modelHandler = ModelHandler.getInstance();
    m_objectHandler = ObjectHandler.getInstance();
    m_telemetry = HeliumTelemetry.getInstance();
    changeScreen(new PhysicsTest());
  }

  public HeliumLevel getCurrentLevel() {
    return m_currentLevel;
  }

  public String getPreviousLevelName() {
    return m_previousLevelName;
  }

  public static LevelHandler getInstance() {
    if (m_instance == null) {
      m_instance = new LevelHandler();
    }
    return m_instance;
  }

  public void dispose() {
    m_currentLevel.dispose();
  }

  public void changeScreen(HeliumLevel level) {
    if (m_currentLevel != null) {
      m_telemetry.print("Changing screen to " + level.getClass().getSimpleName());
    } else {
      m_telemetry.print("Starting game on " + level.getClass().getSimpleName());
    }
//    m_userInterface.clear();
    m_modelHandler.clear();
    m_objectHandler.clear();
    m_helium.setScreen(level);
    if (m_currentLevel != null) {
      m_previousLevelName = m_currentLevel.getClass().getSimpleName();
      m_currentLevel.dispose();
    }
    m_currentLevel = level;
  }
}