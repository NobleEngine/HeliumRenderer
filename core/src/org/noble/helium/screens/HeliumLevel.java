package org.noble.helium.screens;

public abstract class HeliumLevel extends BaseScreen {
  public boolean m_hasInit = false;
  public HeliumLevel() {
    super();
  }
  public void init() {
    m_hasInit = true;
  }
  public void render(float delta) {
    if(!m_hasInit) {
      init();
    }
    super.render(delta);
    m_batch.end();
  }
}
