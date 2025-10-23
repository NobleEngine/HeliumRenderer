package org.noble.helium.screens;

import com.badlogic.gdx.Gdx;
import com.google.gson.JsonElement;
import org.noble.helium.lda.LDAExtractor;
import org.noble.helium.lda.LDAParser;
import org.noble.helium.subsystems.scripting.HeliumScript;
import org.noble.helium.subsystems.scripting.ScriptRunner;
import org.noble.helium.HeliumIO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ParsedLevel extends HeliumLevel {
  private final ScriptRunner m_scriptRunner;
  private List<Class<?>> m_scripts;
  private final String m_name;
  private boolean m_warnForNoScipts = true;

  public ParsedLevel(String LDAName) {
    super();

    m_name = LDAName;
    m_scriptRunner = ScriptRunner.getInstance();
  }

  @Override
  public String getSimpleName() {
    return m_name;
  }

  @Override
  public void init() {
    super.init();

    List<Class<?>> scripts = LDAExtractor.getScripts(Gdx.files.internal("levels/" + m_name));
    if(scripts.isEmpty()) {
      HeliumIO.error("Level Handler",new NoSuchMethodException("No scripts found in level " + m_name),
          HeliumIO.ErrorType.FATAL_CLOSE_GRACEFUL, true);
    }
    Map<String, JsonElement> ldaElements;
    try {
      ldaElements = LDAExtractor.getLDAElements(Gdx.files.internal("levels/" + m_name));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    m_scripts = scripts;
    LDAParser.addWorldObjects(ldaElements);

    for (Class<?> script : m_scripts) {
      if (script.getSimpleName().equals("start")) {
        new HeliumScript(script).update();
      } else {
        m_scriptRunner.addScript(new HeliumScript(script));
      }
    }
  }

  @Override
  public void render(float delta) {
    super.render(delta);
    if (ScriptRunner.getInstance().m_scripts.isEmpty()) {
      if (m_warnForNoScipts) {
        HeliumIO.println("Scripts", "There are no scripts running while in a parsed level!", HeliumIO.printType.ERROR);
        m_warnForNoScipts = false;
      }
    }
  }
}
