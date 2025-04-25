package org.noble.helium;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL32;
import com.kotcrab.vis.ui.widget.VisLabel;
import org.noble.helium.actors.PlayerController;
import org.noble.helium.handling.LevelHandler;
import org.noble.helium.handling.ModelHandler;
import org.noble.helium.handling.TextureHandler;
import org.noble.helium.math.Dimensions2;
import org.noble.helium.subsystems.input.InputProcessing;
import org.noble.helium.rendering.HeliumModelBatch;
import org.noble.helium.subsystems.scripting.ScriptRunner;
import org.noble.helium.subsystems.telemetry.HeliumTelemetry;
import org.noble.helium.subsystems.Subsystem;
import org.noble.helium.subsystems.ui.UserInterface;

import java.util.ArrayList;

public class Helium extends Game {
  private State m_state;
  private float m_delta;
  private static Helium m_instance;
  private final ArrayList<Subsystem> m_subsystems;
  private ModelHandler m_modelHandler;
  private PlayerController m_player;
  private HeliumModelBatch m_modelBatch;
  private UserInterface m_userInterface;
  private LevelHandler m_screenHandler;
  private HeliumTelemetry m_telemetry;

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
    m_telemetry = HeliumTelemetry.getInstance();
    m_modelHandler = ModelHandler.getInstance();
    m_player = PlayerController.getInstance();
    m_userInterface = UserInterface.getInstance();
    m_screenHandler = LevelHandler.getInstance();
    m_subsystems.add(ScriptRunner.getInstance());
    m_subsystems.add(InputProcessing.getInstance());
    m_subsystems.add(m_userInterface);

    m_userInterface.addLabel("Engine-FPS", "FPS: " + Gdx.graphics.getFramesPerSecond(), 0, 0, 100, 25, Color.WHITE);
    m_userInterface.addLabel("PlayerController-Position", "", 0, 30, 100, 25, Color.WHITE);
    m_userInterface.addLabel("Engine-Status", "", 0, 60, 100, 25, Color.WHITE);
    m_userInterface.addLabel("Engine-FrametimeMS", "", 0, 90, 100, 25, Color.WHITE);
    m_userInterface.addLabel("PlayerController-Health", "", 0, 120, 100, 25, Color.WHITE);

    m_modelBatch = new HeliumModelBatch();
    m_telemetry.println("Helium", "Ready to render!");
  }

  @Override
  public void render() {
    m_delta = Gdx.graphics.getDeltaTime();
    Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    Gdx.gl.glClear(GL32.GL_COLOR_BUFFER_BIT | GL32.GL_DEPTH_BUFFER_BIT);
    if(getStatus() == State.PLAY) {
      m_player.update();
    }

    VisLabel FPSLabel = m_userInterface.getLabel("Engine-FPS");
    m_userInterface.setLabel("Engine-FPS", "FPS: " + Gdx.graphics.getFramesPerSecond(), FPSLabel.getX(),
        FPSLabel.getY(), new Dimensions2(FPSLabel.getWidth(), FPSLabel.getHeight()), FPSLabel.getColor());
    VisLabel StatusLabel = m_userInterface.getLabel("Engine-Status");
    m_userInterface.setLabel("Engine-Status", "Status: " + getStatus(), StatusLabel.getX(),
        StatusLabel.getY(), new Dimensions2(StatusLabel.getWidth(), StatusLabel.getHeight()), StatusLabel.getColor());
    VisLabel FrametimeMSLabel = m_userInterface.getLabel("Engine-FrametimeMS");
    m_userInterface.setLabel("Engine-FrametimeMS", "Frame time: " + getDelta(), FrametimeMSLabel.getX(),
        FrametimeMSLabel.getY(), new Dimensions2(FrametimeMSLabel.getWidth(), FrametimeMSLabel.getHeight()), FrametimeMSLabel.getColor());

    super.render();
    m_subsystems.forEach(Subsystem::update);
  }

  public void setWindowMode(WindowMode windowMode) {
    switch(windowMode) {
      //TODO: Implement these modes, go into Windowed or Fullscreen for now
      case WINDOWED -> Gdx.graphics.setWindowedMode(1280, 720);
      case BORDERLESS -> Gdx.graphics.setWindowedMode(1280, 720);
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

    m_telemetry.println("Helium", "Game state set to " + state);
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
    m_modelHandler.clear();
    m_subsystems.forEach(Subsystem::dispose);
    m_screenHandler.dispose();
  }
}
