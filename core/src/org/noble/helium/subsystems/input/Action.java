package org.noble.helium.subsystems.input;

import com.badlogic.gdx.Gdx;

public class Action {
  final int m_keyCode;
  private final InputFunction m_function;
  private final InputType m_inputType;

  public Action(int keyCode, InputFunction action, InputType inputType) {
    m_keyCode = keyCode;
    m_function = action;
    m_inputType = inputType;
  }

  public boolean shouldFire() {
    if(m_inputType == InputType.PRESSED) {
      return Gdx.input.isKeyPressed(m_keyCode);
    } else if(m_inputType == InputType.RELEASED) {
      return !Gdx.input.isKeyPressed(m_keyCode);
    } else if(m_inputType == InputType.JUST_PRESSED) {
      return Gdx.input.isKeyJustPressed(m_keyCode);
    }
    return false;
  }

  public InputFunction getFunction() {
    return m_function;
  }

  public enum InputFunction {
    STRAFE_LEFT, STRAFE_RIGHT, STRAFE_FORWARD, STRAFE_BACKWARD, MOVE_FASTER, MOVE_STANDARD, TOGGLE_PAUSE,
    TOGGLE_FULLSCREEN, JUMP
  }

  public enum InputType {
    PRESSED, RELEASED, JUST_PRESSED
  }
}
