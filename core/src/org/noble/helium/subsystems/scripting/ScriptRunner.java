package org.noble.helium.subsystems.scripting;

import org.noble.helium.subsystems.Subsystem;

import java.util.ArrayList;

public class ScriptRunner extends Subsystem {
  private static ScriptRunner m_instance;
  public ArrayList<HeliumScript> m_scripts;
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

  public void clear() {
    m_scripts.clear();
  }

  @Override
  public void update() {
    for(HeliumScript script : m_scripts) {
      try {
        script.update();
      } catch (NoSuchMethodException e) {
        throw new RuntimeException(e);
      }
    }
  }

  @Override
  public void dispose() {

  }
}
