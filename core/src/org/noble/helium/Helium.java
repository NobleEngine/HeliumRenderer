package org.noble.helium;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL32;
import com.badlogic.gdx.utils.ScreenUtils;
import org.noble.helium.actors.PlayerController;
import org.noble.helium.handling.LevelHandler;
import org.noble.helium.handling.ObjectHandler;
import org.noble.helium.handling.TextureHandler;
import org.noble.helium.subsystems.ui.UserInterface;
import org.noble.helium.subsystems.input.InputProcessing;
import org.noble.helium.rendering.HeliumModelBatch;
import org.noble.helium.subsystems.scripting.ScriptRunner;
import org.noble.helium.subsystems.Subsystem;

import java.util.ArrayList;
import java.util.Objects;

public class Helium extends Game {
  private State m_state;
  private WindowMode m_windowMode;
  private float m_delta;
  private double m_targetTime;
  private String m_windowTitle;
  private Color m_backgroundColor;
  private static Helium m_instance;
  private final ArrayList<Subsystem> m_subsystems;
  private PlayerController m_player;
  private HeliumModelBatch m_modelBatch;
  private LevelHandler m_levelHandler;

  private Helium() {
    m_subsystems = new ArrayList<>();
  }

  public static Helium getInstance() {
    if(m_instance == null) {
      m_instance = new Helium();
    }
    return m_instance;
  }

  public HeliumModelBatch getModelBatch() {
    if(m_modelBatch == null) {
      m_modelBatch = new HeliumModelBatch();
    }
    return m_modelBatch;
  }

  public WindowMode getWindowMode() {
    return m_windowMode;
  }

  public State getState() {
    return m_state;
  }

  public float getDelta() {
    return m_delta;
  }

  public float getFPS() {
    return Gdx.graphics.getFramesPerSecond();
  }

  public Color getBackgroundColor() {
    return m_backgroundColor;
  }

  @Override
  public void create() {
    HeliumIO.println("Telemetry", "Warnings look like this", HeliumIO.printType.WARNING);
    HeliumIO.println("Telemetry", "Errors look like this", HeliumIO.printType.ERROR);
    setBackgroundColor(Color.BLACK);
    setWindowMode(WindowMode.WINDOWED);

    SystemInformation.getInstance();
    m_player = PlayerController.getInstance();
    m_levelHandler = LevelHandler.getInstance();
    m_subsystems.add(ScriptRunner.getInstance());
    m_subsystems.add(InputProcessing.getInstance());
    m_subsystems.add(UserInterface.getInstance());

    m_modelBatch = new HeliumModelBatch();
    HeliumIO.println(Constants.Engine.k_prettyName, "Ready to render!");
  }

  @Override
  public void render() {
    setTitle(Constants.Engine.k_prettyName + " - " + m_levelHandler.getLevelName() + " - " + getState());
    m_delta = Gdx.graphics.getDeltaTime();

    Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    Gdx.gl.glClear(GL32.GL_COLOR_BUFFER_BIT | GL32.GL_DEPTH_BUFFER_BIT);
    ScreenUtils.clear(getBackgroundColor());
//    Gdx.gl.glClearColor(1,0,0,0);

    if(getState() == State.PLAY) {
      m_player.update();
    }

    super.render();
    m_subsystems.forEach(Subsystem::update);
  }

  public void setWindowMode(WindowMode windowMode) {
    switch(windowMode) {
      case WINDOWED -> Gdx.graphics.setWindowedMode(1280, 720);
      case FULLSCREEN -> Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
      case MAXIMIZED -> Gdx.graphics.setWindowedMode(Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);
    }

    m_windowMode = windowMode;
  }

  public void setState(State state) {
    if(state == m_state) {
      return;
    }

    m_state = state;

    switch(m_state) {
      case PLAY -> Gdx.input.setCursorCatched(true);
      case PAUSE -> Gdx.input.setCursorCatched(false);
    }

    HeliumIO.println("Helium", "Game state set to " + state);
  }

  public void setBackgroundColor(Color color) {
    m_backgroundColor = color;
  }

  public void setTitle(String title) {
    if(!Objects.equals(title, m_windowTitle)) {
      m_windowTitle = title;
      Gdx.graphics.setTitle(m_windowTitle);
    }
  }

  public enum State {
    PLAY, PAUSE
  }

  public enum WindowMode {
    WINDOWED, BORDERLESS, FULLSCREEN, MAXIMIZED
  }

  @Override
  public void dispose() {
    TextureHandler.getInstance().clear();
    ObjectHandler.getInstance().clear();
    m_subsystems.forEach(Subsystem::dispose);
    m_levelHandler.dispose();
  }
}
