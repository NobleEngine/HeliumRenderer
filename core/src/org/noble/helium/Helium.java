package org.noble.helium;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.utils.ScreenUtils;
import org.noble.helium.actors.PlayerController;
import org.noble.helium.handling.ObjectHandler;
import org.noble.helium.handling.ScreenHandler;
import org.noble.helium.handling.SimpleModelHandler;
import org.noble.helium.io.KeyInput;
import org.noble.helium.rendering.HeliumModelBatch;
import org.noble.helium.subsystems.Physics;
import org.noble.helium.subsystems.Subsystem;
import org.noble.helium.subsystems.UserInterface;

import java.util.ArrayList;

public class Helium extends Game {
  private int m_status;
  private static Helium m_instance;
  private final ArrayList<Subsystem> m_subsystems;
  private SimpleModelHandler m_simpleModelHandler;
  private ObjectHandler m_objectHandler;
  private KeyInput m_input;
  private PlayerController m_player;
  private HeliumModelBatch m_modelBatch;
  private Physics m_physics;
  private UserInterface m_userInterface;
  private ScreenHandler m_screenHandler;

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

  @Override
  public void create() {
    m_simpleModelHandler = SimpleModelHandler.getInstance();
    m_input = KeyInput.getInstance();
    Gdx.input.setCursorCatched(true);
    m_physics = Physics.getInstance();
    m_subsystems.add(m_physics);
    m_objectHandler = ObjectHandler.getInstance();
    m_player = PlayerController.getInstance();
    m_userInterface = UserInterface.getInstance();
    m_screenHandler = ScreenHandler.getInstance();
    m_subsystems.add(m_userInterface);

    m_userInterface.addLabel("FPS", "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 10, 100, 25);

    m_modelBatch = new HeliumModelBatch();
  }

  @Override
  public void render() {
    Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
    ScreenUtils.clear(Color.WHITE);
    m_player.update();
    m_objectHandler.updateAllObjects();
    m_subsystems.forEach(Subsystem::update);

    m_userInterface.setLabel("FPS", "FPS: " + Gdx.graphics.getFramesPerSecond());

    //TODO: Change resolution on window resize
    if (m_input.isKeyDown(KeyInput.Action.TOGGLE_FULLSCREEN, true)) {
      if (Gdx.graphics.isFullscreen()) {
        Gdx.graphics.setWindowedMode(1600, 900);
      } else {
        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
      }
    }

    if(m_input.isKeyDown(KeyInput.Action.DEBUG_KILL, true)) {
      Gdx.app.exit();
    }

    super.render();
  }

  @Override
  public void dispose() {
    m_simpleModelHandler.clear();
    m_subsystems.forEach(Subsystem::dispose);
    m_screenHandler.dispose();
  }
}
