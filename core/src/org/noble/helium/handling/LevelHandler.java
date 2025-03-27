package org.noble.helium.handling;

import com.badlogic.gdx.Gdx;
import com.google.gson.JsonElement;
import org.noble.helium.Helium;
import org.noble.helium.lda.LDAExtractor;
import org.noble.helium.lda.LDAParser;
import org.noble.helium.screens.HeliumLevel;
import org.noble.helium.screens.ParsedLevel;
import org.noble.helium.screens.tests.PhysicsTest;
import org.noble.helium.subsystems.scripting.HeliumScript;
import org.noble.helium.subsystems.scripting.ScriptRunner;
import org.noble.helium.subsystems.telemetry.HeliumTelemetry;

import javax.tools.ToolProvider;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

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
    try {
      changeScreen("test.lda");
    } catch (IOException | ClassNotFoundException | NoSuchMethodException e) {
      changeScreen(new PhysicsTest());
      throw new RuntimeException(e);
    }
//    HeliumTelemetry.getInstance().println("Level handler initialized");
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

  public void changeScreen(String LDAName) throws IOException, ClassNotFoundException, NoSuchMethodException {
    changeScreen(new ParsedLevel());
    Map<String, JsonElement> ldaElements = LDAExtractor.getLDAElements(Gdx.files.internal("levels/" + LDAName));
    LDAParser.addWorldObjects(ldaElements);
    LDAParser.usePlayerStartingConfiguration(ldaElements);

    Map<String, Class<?>> scripts = LDAExtractor.getScripts(Gdx.files.internal("levels/" + LDAName));

    scripts.forEach((key,value) -> {
      ScriptRunner.getInstance().addScript(new HeliumScript(value));
    });
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
      m_previousLevelName = m_currentLevel.getClass().getSimpleName();
      m_currentLevel.dispose();
    }
    m_currentLevel = level;
  }
}