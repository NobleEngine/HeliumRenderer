package org.noble.helium.screens;

import org.noble.helium.actors.PlayerController;
import org.noble.helium.subsystems.scripting.ScriptRunner;

public abstract class HeliumLevel extends BaseScreen {
  public boolean m_hasInit = false;
  public HeliumLevel() {
    super();
  }
  public void init() {
    ScriptRunner.getInstance().m_scripts.clear();
    PlayerController.getInstance().reset();
    m_hasInit = true;
  }

  public String getSimpleName() {
    return getClass().getSimpleName();
  }

  public void render(float delta) {
    if(!m_hasInit) {
      init();
    }
    super.render(delta);
    m_batch.end();
  }
}
