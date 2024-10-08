package org.noble.helium.io;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import java.util.HashMap;
import java.util.Map;

public class KeyInput {
  private final HashMap<Integer, Integer> m_keyBindings;
  private static KeyInput m_instance;

  private KeyInput() { //Default binds go here
    m_keyBindings = new HashMap<>();

    //Player Movement
    bindKey(Input.Keys.A, Action.STRAFE_LEFT);
    bindKey(Input.Keys.D, Action.STRAFE_RIGHT);
    bindKey(Input.Keys.W, Action.STRAFE_FORWARD);
    bindKey(Input.Keys.S, Action.STRAFE_BACKWARD);
    bindKey(Input.Keys.SPACE, Action.JUMP);

    //Misc.
    bindKey(Input.Keys.F11, Action.TOGGLE_FULLSCREEN);
    bindKey(Input.Keys.ESCAPE, Action.PAUSE);
    bindKey(Input.Keys.SHIFT_LEFT, Action.MOVE_FASTER);
  }

  public static KeyInput getInstance() {
    if (m_instance == null) {
      m_instance = new KeyInput();
    }
    return m_instance;
  }

  public void bindKey(int keyCode, int action) {
    m_keyBindings.put(keyCode, action);
  }

  public boolean isKeyDown(int action, boolean noHold) {
    for (Map.Entry<Integer, Integer> entry : m_keyBindings.entrySet()) {
      if (entry.getValue() == action) {
        if (!noHold && Gdx.input.isKeyPressed(entry.getKey())) {
          return true;
        }
        if (noHold && Gdx.input.isKeyJustPressed(entry.getKey())) {
          return true;
        }
      }
    }
    return false;
  }

  public boolean isAnyKeyDown() {
    for (Map.Entry<Integer, Integer> entry : m_keyBindings.entrySet()) {
      if (Gdx.input.isKeyPressed(entry.getKey())) {
        return true;
      }
    }
    return false;
  }

  public interface Action {
    int STRAFE_LEFT = 0;
    int STRAFE_RIGHT = 1;
    int STRAFE_FORWARD = 2;
    int STRAFE_BACKWARD = 3;
    int MOVE_FASTER = 4;
    int PAUSE = 5;
    int TOGGLE_FULLSCREEN = 6;
    int JUMP = 7;
  }
}