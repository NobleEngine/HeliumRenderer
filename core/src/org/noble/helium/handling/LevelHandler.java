package org.noble.helium.handling;

import com.badlogic.gdx.Gdx;
import com.google.gson.JsonElement;
import org.noble.helium.Helium;
import org.noble.helium.lda.LDAExtractor;
import org.noble.helium.lda.LDAParser;
import org.noble.helium.screens.HeliumLevel;
import org.noble.helium.screens.ParsedLevel;
import org.noble.helium.screens.tests.PhysicsTest;
import org.noble.helium.HeliumIO;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class LevelHandler {
  private static LevelHandler m_instance;
  private HeliumLevel m_currentLevel;
  private final Helium m_helium;
  private final ObjectHandler m_objectHandler;

  private LevelHandler() {
    m_helium = Helium.getInstance();
    m_objectHandler = ObjectHandler.getInstance();
//    changeScreen("test.lda");
    changeScreen(new PhysicsTest());
    HeliumIO.println("Level Handler","Level handler initialized");
  }

  public static LevelHandler getInstance() {
    if (m_instance == null) {
      m_instance = new LevelHandler();
    }
    return m_instance;
  }

  public String getLevelName() {
    return m_currentLevel.getSimpleName();
  }

  public void dispose() {
    m_currentLevel.dispose();
  }

  public void changeScreen(String LDAName) {
    changeScreen(new ParsedLevel(LDAName));
  }

  public void changeScreen(HeliumLevel level) {
    if (m_currentLevel != null) {
      HeliumIO.println("Level Handler", "Changing screen to " + level.getSimpleName());
    } else {
      HeliumIO.println("Level Handler","Starting game on " + level.getSimpleName());
    }
    TextureHandler.getInstance().clear();
    m_objectHandler.clear();
    m_helium.setScreen(level);
    if (m_currentLevel != null) {
      m_currentLevel.dispose();
    }
    m_currentLevel = level;
  }
}