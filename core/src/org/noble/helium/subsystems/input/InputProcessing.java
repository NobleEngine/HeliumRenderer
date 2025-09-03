package org.noble.helium.subsystems.input;

import com.badlogic.gdx.Input;
import org.noble.helium.Constants;
import org.noble.helium.Helium;
import org.noble.helium.actors.PlayerController;
import org.noble.helium.subsystems.Subsystem;
import org.noble.helium.subsystems.input.Action.InputFunction;

import java.util.ArrayList;

public class InputProcessing extends Subsystem {
  private final ArrayList<Action> m_keyBindings;
  private static InputProcessing m_instance;
  private final PlayerController m_player;
  private final Helium m_helium;

  private InputProcessing() { //Default binds go here
    m_keyBindings = new ArrayList<>();
    m_player = PlayerController.getInstance();
    m_helium = Helium.getInstance();

    //Player Movement
    bindKey(Input.Keys.A, InputFunction.STRAFE_LEFT, Action.InputType.PRESSED);
    bindKey(Input.Keys.D, InputFunction.STRAFE_RIGHT, Action.InputType.PRESSED);
    bindKey(Input.Keys.W, InputFunction.STRAFE_FORWARD, Action.InputType.PRESSED);
    bindKey(Input.Keys.S, InputFunction.STRAFE_BACKWARD, Action.InputType.PRESSED);
    bindKey(Input.Keys.SPACE, InputFunction.JUMP, Action.InputType.PRESSED);

    //Misc.
    bindKey(Input.Keys.SHIFT_LEFT, InputFunction.MOVE_FASTER, Action.InputType.PRESSED);
    bindKey(Input.Keys.SHIFT_LEFT, InputFunction.MOVE_STANDARD, Action.InputType.RELEASED);
    bindKey(Input.Keys.F11, InputFunction.TOGGLE_FULLSCREEN, Action.InputType.JUST_PRESSED);
    bindKey(Input.Keys.ESCAPE, InputFunction.TOGGLE_PAUSE, Action.InputType.JUST_PRESSED);
  }

  public static InputProcessing getInstance() {
    if (m_instance == null) {
      m_instance = new InputProcessing();
    }
    return m_instance;
  }

  public void bindKey(int keyCode, InputFunction func, Action.InputType type) {
    m_keyBindings.add(new Action(keyCode, func, type));
  }

  @Override
  public void update() {
    ArrayList<Action> pressedActions = getQueuedActions();
    for (Action action : pressedActions) {
      switch (action.getFunction()) {
        case MOVE_FASTER -> m_player.setSpeed(Constants.Player.k_fastSpeed);
        case MOVE_STANDARD -> m_player.setSpeed(Constants.Player.k_defaultSpeed);
        case TOGGLE_PAUSE -> {
          if(m_helium.getState() == Helium.State.PAUSE) {
            m_helium.setState(Helium.State.PLAY);
          } else {
            m_helium.setState(Helium.State.PAUSE);
          }
        }
        case TOGGLE_FULLSCREEN -> {
          if(m_helium.getWindowMode() == Helium.WindowMode.WINDOWED) {
            m_helium.setWindowMode(Helium.WindowMode.FULLSCREEN);
          } else {
            m_helium.setWindowMode(Helium.WindowMode.WINDOWED);
          }
        }
      }
    }
  }

  @Override
  public void dispose() {
  }

  public ArrayList<Action> getQueuedActions() {
    ArrayList<Action> pressedActions = new ArrayList<>();
    for (Action action : m_keyBindings) {
      if (action.shouldFire()) {
        pressedActions.add(action);
      }
    }
    return pressedActions;
  }
}