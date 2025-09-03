package org.noble.helium.subsystems.scripting;

import org.noble.helium.Helium;
import org.noble.helium.subsystems.Subsystem;

import java.util.ArrayList;

public class ScriptRunner extends Subsystem {
  private static ScriptRunner m_instance;
  public final ArrayList<HeliumScript> m_scripts;
  private ScriptRunner() {
    m_scripts = new ArrayList<>();
  }

  public static ScriptRunner getInstance() {
    if(m_instance == null) {
      m_instance = new ScriptRunner();
    }
    return m_instance;
  }

  public void addScript(HeliumScript script) {
    m_scripts.add(script);
  }

  @Override
  public void update() {
    if(Helium.getInstance().getState() == Helium.State.PLAY) {
      for (HeliumScript script : m_scripts) {
        script.update();
      }
    }
  }

  @Override
  public void dispose() {

  }
}
