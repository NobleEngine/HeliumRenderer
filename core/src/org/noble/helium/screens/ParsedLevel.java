package org.noble.helium.screens;

import org.noble.helium.subsystems.scripting.HeliumScript;
import org.noble.helium.subsystems.scripting.ScriptRunner;
import org.noble.helium.subsystems.telemetry.HeliumTelemetry;

import java.util.Map;

public class ParsedLevel extends HeliumLevel {
  private final ScriptRunner m_scriptRunner;
  private final Map<String, Class<?>> m_scripts;
  public ParsedLevel(Map<String, Class<?>> scripts) {
    super();
    m_scriptRunner = ScriptRunner.getInstance();
    m_scripts = scripts;
  }

  @Override
  public void init() {
    super.init();

    m_scripts.forEach((key,value) -> {
      if(value.getSimpleName().equals("start")) {
        new HeliumScript(value).update();
      }
      m_scriptRunner.addScript(new HeliumScript(value));
    });
  }

  @Override
  public void render(float delta) {
    super.render(delta);
    if(ScriptRunner.getInstance().m_scripts.isEmpty()) {
      HeliumTelemetry.getInstance().printErrorln("There are no scripts running while in a parsed level!");
    }

  }
}
