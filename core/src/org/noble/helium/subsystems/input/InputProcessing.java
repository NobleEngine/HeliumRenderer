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
    bindKey(Input.Keys.A, InputFunction.STRAFE_LEFT, InputType.PRESSED);
    bindKey(Input.Keys.D, InputFunction.STRAFE_RIGHT, InputType.PRESSED);
    bindKey(Input.Keys.W, InputFunction.STRAFE_FORWARD, InputType.PRESSED);
    bindKey(Input.Keys.S, InputFunction.STRAFE_BACKWARD, InputType.PRESSED);
    bindKey(Input.Keys.SPACE, InputFunction.JUMP, InputType.PRESSED);

    //Misc.
    bindKey(Input.Keys.SHIFT_LEFT, InputFunction.MOVE_FASTER, InputType.PRESSED);
    bindKey(Input.Keys.SHIFT_LEFT, InputFunction.MOVE_STANDARD, InputType.RELEASED);
    bindKeyToggle(Input.Keys.F11, InputFunction.FULLSCREEN_MODE, InputFunction.WINDOWED_MODE);
    bindKeyToggle(Input.Keys.ESCAPE, InputFunction.PAUSE, InputFunction.RESUME);
  }

  public static InputProcessing getInstance() {
    if (m_instance == null) {
      m_instance = new InputProcessing();
    }
    return m_instance;
  }

  public void bindKeyToggle(int keyCode, InputFunction func1, InputFunction func2) {
    m_keyBindings.add(new ToggleAction(keyCode, func1, func2));
  }

  public void bindKey(int keyCode, InputFunction func, InputType type) {
    m_keyBindings.add(new Action(keyCode, func, type));
  }

  @Override
  public void update() {
    ArrayList<Action> pressedActions = getQueuedActions();
    for (Action action : pressedActions) {
      switch (action.getFunction()) {
        case MOVE_FASTER -> m_player.setSpeed(Constants.Player.k_fastSpeed);
        case MOVE_STANDARD -> m_player.setSpeed(Constants.Player.k_defaultSpeed);
        case PAUSE -> m_helium.setState(Helium.State.PAUSE);
        case RESUME -> m_helium.setState(Helium.State.PLAY);
        case FULLSCREEN_MODE -> m_helium.setWindowMode(Helium.WindowMode.FULLSCREEN);
        case WINDOWED_MODE -> m_helium.setWindowMode(Helium.WindowMode.WINDOWED);
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