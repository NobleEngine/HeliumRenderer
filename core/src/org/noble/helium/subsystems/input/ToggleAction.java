package org.noble.helium.subsystems.input;

import com.badlogic.gdx.Gdx;

public class ToggleAction extends Action {
  private final InputFunction m_normalFunction;
  private final InputFunction m_toggleFunction;
  private boolean m_toggled = true;
  public ToggleAction(int keyCode, InputFunction func1, InputFunction func2) {
    super(keyCode, func1, InputType.PRESSED);
    m_normalFunction = func1;
    m_toggleFunction = func2;
  }

  @Override
  public boolean shouldFire() {
    if(Gdx.input.isKeyJustPressed(m_keyCode)) {
      m_toggled = !m_toggled;
      return true;
    }
    return false;
  }

  @Override
  public InputFunction getFunction() {
    if(m_toggled) {
      return m_toggleFunction;
    }
    return m_normalFunction;
  }
}
