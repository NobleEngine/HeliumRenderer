package org.noble.helium.subsystems.input;

import com.badlogic.gdx.Gdx;
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
    Gdx.input.setCursorCatched(true);
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
    bindKey(Input.Keys.F11, InputFunction.FULLSCREEN_MODE, InputType.PRESSED);
    bindKey(Input.Keys.F12, InputFunction.WINDOWED_MODE, InputType.RELEASED);
    bindKey(Input.Keys.ESCAPE, InputFunction.PAUSE, InputType.PRESSED);
    bindKey(Input.Keys.SHIFT_LEFT, InputFunction.MOVE_FASTER, InputType.PRESSED);
    bindKey(Input.Keys.ESCAPE, InputFunction.RESUME, InputType.RELEASED);
  }

  public static InputProcessing getInstance() {
    if (m_instance == null) {
      m_instance = new InputProcessing();
    }
    return m_instance;
  }

  public void bindKey(int keyCode, InputFunction action, InputType type) {
    m_keyBindings.add(new Action(keyCode, action, type));
  }

  @Override
  public void update() {
    ArrayList<Action> pressedActions = getQueuedActions();
    for (Action action : pressedActions) {
      switch (action.getFunction()) {
        case JUMP -> m_player.jump();
        case STRAFE_LEFT -> m_player.strafeLeft();
        case STRAFE_RIGHT -> m_player.strafeRight();
        case STRAFE_FORWARD -> m_player.strafeForward();
        case STRAFE_BACKWARD -> m_player.strafeBack();
        case MOVE_FASTER -> m_player.setSpeed(Constants.Player.k_fastSpeed);
        case MOVE_STANDARD -> m_player.setSpeed(Constants.Player.k_defaultSpeed);
        case PAUSE -> m_helium.setState(Helium.State.PAUSE);
        case RESUME -> m_helium.setState(Helium.State.PLAY);
        case FULLSCREEN_MODE -> m_helium.setWindowMode(WindowMode.EXCLUSIVE_FULLSCREEN);
        case WINDOWED_MODE -> m_helium.setWindowMode(WindowMode.WINDOWED);
      }
    }
  }

  @Override
  public void dispose() {
  }

  public ArrayList<Action> getQueuedActions() {
    ArrayList<Action> pressedActions = new ArrayList<>();
    for(Action action : m_keyBindings) {
      if(action.shouldFire()) {
        pressedActions.add(action);
      }
    }
    return pressedActions;
  }
}