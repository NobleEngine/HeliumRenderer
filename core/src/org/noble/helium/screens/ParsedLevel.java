package org.noble.helium.screens;

import org.noble.helium.subsystems.scripting.HeliumScript;
import org.noble.helium.subsystems.scripting.ScriptRunner;
import org.noble.helium.subsystems.telemetry.HeliumTelemetry;

import java.util.List;

public class ParsedLevel extends HeliumLevel {
  private final ScriptRunner m_scriptRunner;
  private final List<Class<?>> m_scripts;
  public ParsedLevel(List<Class<?>> scripts) {
    super();
    m_scriptRunner = ScriptRunner.getInstance();
    m_scripts = scripts;
  }

  @Override
  public void init() {
    super.init();

    for(Class<?> script : m_scripts) {
      if(script.getSimpleName().equals("start")) {
        new HeliumScript(script).update();
      } else {
        m_scriptRunner.addScript(new HeliumScript(script));
      }
    }
  }

  @Override
  public void render(float delta) {
    super.render(delta);
    if(ScriptRunner.getInstance().m_scripts.isEmpty()) {
      HeliumTelemetry.getInstance().printErrorln("There are no scripts running while in a parsed level!");
    }

  }
}
