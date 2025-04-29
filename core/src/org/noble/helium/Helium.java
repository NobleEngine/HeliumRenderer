package org.noble.helium;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL32;
import org.noble.helium.actors.PlayerController;
import org.noble.helium.handling.LevelHandler;
import org.noble.helium.handling.ObjectHandler;
import org.noble.helium.handling.TextureHandler;
import org.noble.helium.math.Units;
import org.noble.helium.subsystems.input.InputProcessing;
import org.noble.helium.rendering.HeliumModelBatch;
import org.noble.helium.subsystems.scripting.ScriptRunner;
import org.noble.helium.subsystems.Subsystem;

import java.lang.management.OperatingSystemMXBean;
import java.util.ArrayList;
import java.util.Objects;

public class Helium extends Game {
  private State m_state;
  private float m_delta;
  private double m_targetTime;
  private String m_windowTitle;
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

  public State getStatus() {
    return m_state;
  }

  public float getDelta() {
    return m_delta;
  }

  @Override
  public void create() {
    OperatingSystemMXBean osBean = java.lang.management.ManagementFactory.getOperatingSystemMXBean();
    PrintUtils.println(Constants.Engine.k_prettyName, Constants.Engine.k_build + " running for " + osBean.getArch() + " " + osBean.getVersion());
    PrintUtils.println(Constants.Engine.k_prettyName, Gdx.graphics.getGLVersion().getRendererString()); //TODO: Move this to UI (an F3 menu like Minecraft?)
    PrintUtils.println("Telemetry", "Warnings look like this", PrintUtils.printType.WARNING);
    PrintUtils.println("Telemetry", "Errors look like this", PrintUtils.printType.ERROR);

    m_player = PlayerController.getInstance();
    m_levelHandler = LevelHandler.getInstance();
    m_subsystems.add(ScriptRunner.getInstance());
    m_subsystems.add(InputProcessing.getInstance());
    setTargetFPS(Gdx.graphics.getDisplayMode().refreshRate);

    m_modelBatch = new HeliumModelBatch();
    PrintUtils.println(Constants.Engine.k_prettyName, "Ready to render!");
  }

  @Override
  public void render() {
    setTitle(Constants.Engine.k_prettyName + " - " + m_levelHandler.getLevelName() + " - " + getStatus());
    m_delta = Gdx.graphics.getDeltaTime();
    double startTime = Units.nanosecondsToSeconds(System.nanoTime()); //in seconds

    if(m_delta > m_targetTime + 0.1) {
      PrintUtils.println(Constants.Engine.k_prettyName, "Loop overrun by " +
          String.valueOf(m_delta - m_targetTime).substring(0,6) + " seconds!", PrintUtils.printType.WARNING);
    }

    Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    Gdx.gl.glClear(GL32.GL_COLOR_BUFFER_BIT | GL32.GL_DEPTH_BUFFER_BIT);

    if(getStatus() == State.PLAY) {
      m_player.update();
    }

    super.render();
    m_subsystems.forEach(Subsystem::update);

    while (Units.nanosecondsToSeconds(System.nanoTime()) - startTime < m_targetTime) {
      try {
        // This warning is not problematic.
        Thread.sleep(0);
      } catch (InterruptedException e) {
        PrintUtils.error(Constants.Engine.k_prettyName, e, PrintUtils.ErrorType.FATAL, true);
      }
    }
  }

  public void setWindowMode(WindowMode windowMode) {
    switch(windowMode) {
      case WINDOWED -> Gdx.graphics.setWindowedMode(1280, 720);
      case FULLSCREEN -> Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
      case MAXIMIZED -> Gdx.graphics.setWindowedMode(Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);
    }
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

    PrintUtils.println("Helium", "Game state set to " + state);
  }

  public void setTargetFPS(int fps) {
    m_targetTime = 1.0 / fps;
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
