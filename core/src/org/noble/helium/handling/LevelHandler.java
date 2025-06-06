package org.noble.helium.handling;

import com.badlogic.gdx.Gdx;
import com.google.gson.JsonElement;
import org.noble.helium.Helium;
import org.noble.helium.lda.LDAExtractor;
import org.noble.helium.lda.LDAParser;
import org.noble.helium.screens.HeliumLevel;
import org.noble.helium.screens.ParsedLevel;
import org.noble.helium.screens.tests.PhysicsTest;
import org.noble.helium.subsystems.telemetry.HeliumTelemetry;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class LevelHandler {
  private static LevelHandler m_instance;
  private HeliumLevel m_currentLevel;
  private final Helium m_helium;
  private final ModelHandler m_modelHandler;
  private final ObjectHandler m_objectHandler;
  private final HeliumTelemetry m_telemetry;

  private LevelHandler() {
    m_helium = Helium.getInstance();
    m_modelHandler = ModelHandler.getInstance();
    m_objectHandler = ObjectHandler.getInstance();
    m_telemetry = HeliumTelemetry.getInstance();
    changeScreen("test.lda");
    HeliumTelemetry.getInstance().println("Level handler initialized");
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

  public void changeScreen(String LDAName) {
    List<Class<?>> scripts = LDAExtractor.getScripts(Gdx.files.internal("levels/" + LDAName));
    changeScreen(new ParsedLevel(scripts));
    Map<String, JsonElement> ldaElements;
    try {
      ldaElements = LDAExtractor.getLDAElements(Gdx.files.internal("levels/" + LDAName));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    LDAParser.addWorldObjects(ldaElements);
  }

  public void changeScreen(HeliumLevel level) {
    if (m_currentLevel != null) {
      m_telemetry.println("Changing screen to " + level.getClass().getSimpleName());
    } else {
      m_telemetry.println("Starting game on " + level.getClass().getSimpleName());
    }
//    m_userInterface.clear();
    m_modelHandler.clear();
    m_objectHandler.clear();
    m_helium.setScreen(level);
    if (m_currentLevel != null) {
      m_currentLevel.dispose();
    }
    m_currentLevel = level;
  }
}