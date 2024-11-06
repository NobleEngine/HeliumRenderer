package org.noble.helium;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.kotcrab.vis.ui.widget.VisLabel;
import org.noble.helium.actors.PlayerController;
import org.noble.helium.handling.LevelHandler;
import org.noble.helium.handling.ModelHandler;
import org.noble.helium.handling.TextureHandler;
import org.noble.helium.math.Dimensions2;
import org.noble.helium.io.KeyInput;
import org.noble.helium.rendering.HeliumModelBatch;
import org.noble.helium.subsystems.telemetry.HeliumTelemetry;
import org.noble.helium.subsystems.Subsystem;
import org.noble.helium.subsystems.ui.UserInterface;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.util.ArrayList;

public class Helium extends Game {
  private int m_status;
  private float m_delta;
  private static Helium m_instance;
  private final ArrayList<Subsystem> m_subsystems;
  private ModelHandler m_modelHandler;
  private KeyInput m_input;
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

  public void setStatus(int status) {
    m_status = status;
  }

  public int getStatus() {
    return m_status;
  }

  public float getDelta() {
    return m_delta;
  }

  @Override
  public void create() {
    m_telemetry = HeliumTelemetry.getInstance();
    OperatingSystemMXBean osBean = ManagementFactory.getOperatingSystemMXBean();
    String osName = osBean.getName();
    String osArch = osBean.getArch();
    m_telemetry.print(Constants.Engine.prettyName + " starting up on " + osName + " " + osArch);
    m_modelHandler = ModelHandler.getInstance();
    m_input = KeyInput.getInstance();
    Gdx.input.setCursorCatched(true);
    m_player = PlayerController.getInstance();
    m_userInterface = UserInterface.getInstance();
    m_screenHandler = LevelHandler.getInstance();
    m_subsystems.add(m_userInterface);
    m_subsystems.add(m_telemetry);

    m_userInterface.addLabel("Engine-FPS", "FPS: " + Gdx.graphics.getFramesPerSecond(), 0, 0, 100, 25, Color.WHITE);
    m_userInterface.addLabel("PlayerController-Position", "", 0, 30, 100, 25, Color.WHITE);
    m_userInterface.addLabel("Engine-Status", "", 0, 60, 100, 25, Color.WHITE);

    m_modelBatch = new HeliumModelBatch();
    m_telemetry.print("Ready to render!");
  }

  @Override
  public void render() {
    m_delta = Gdx.graphics.getDeltaTime();
    Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
    if(getStatus() == Status.PLAY) {
      m_player.update();
    }

    VisLabel FPSLabel = m_userInterface.getLabel("Engine-FPS");
    m_userInterface.setLabel("Engine-FPS", "FPS: " + Gdx.graphics.getFramesPerSecond(), FPSLabel.getX(),
        FPSLabel.getY(), new Dimensions2(FPSLabel.getWidth(), FPSLabel.getHeight()), FPSLabel.getColor());
    VisLabel StatusLabel = m_userInterface.getLabel("Engine-Status");
    m_userInterface.setLabel("Engine-Status", "Status: " + getStatus(), StatusLabel.getX(),
        StatusLabel.getY(), new Dimensions2(StatusLabel.getWidth(), StatusLabel.getHeight()), StatusLabel.getColor());

    //TODO: Change resolution on window resize
    if (m_input.isKeyDown(KeyInput.Action.TOGGLE_FULLSCREEN, true)) {
      if (Gdx.graphics.isFullscreen()) {
        Gdx.graphics.setWindowedMode(1600, 900);
      } else {
        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
      }
    }

    if(m_input.isKeyDown(KeyInput.Action.PAUSE, true)) {
      if(getStatus() == Status.PLAY) {
        setStatus(Status.PAUSE);
        Gdx.input.setCursorCatched(false);
      } else {
        setStatus(Status.PLAY);
        Gdx.input.setCursorCatched(true);
      }
    }

    super.render();
    m_subsystems.forEach(Subsystem::update);
  }

  public interface Status {
    int PLAY = 0;
    int PAUSE = 1;
  }

  @Override
  public void dispose() {
    TextureHandler.getInstance().clear();
    m_modelHandler.clear();
    m_subsystems.forEach(Subsystem::dispose);
    m_screenHandler.dispose();
  }
}
