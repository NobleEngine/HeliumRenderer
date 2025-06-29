package org.noble.helium.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import org.noble.helium.Helium;
import org.noble.helium.HeliumIO;
import org.noble.helium.handling.TextureHandler;
import org.noble.helium.math.Dimensions3;
import org.noble.helium.rendering.HeliumModelBuilder;
import org.noble.helium.subsystems.input.Action;
import org.noble.helium.subsystems.input.InputProcessing;
import org.noble.helium.world.WorldObject;

import java.util.ArrayList;

public class PlayerController extends Actor {
  private final PerspectiveCamera m_camera;
  private final Helium m_engine;
  private static PlayerController m_instance;
  private float m_cameraPitch, m_cameraYaw;
  private final PlayerType m_playerType;

  private PlayerController() {
    super(null, 100, 8f);
    m_playerType = PlayerType.STANDARD;
    m_engine = Helium.getInstance();

    HeliumModelBuilder modelBuilder = HeliumModelBuilder.getInstance();
    TextureHandler textureHandler = TextureHandler.getInstance();
    Model model = modelBuilder.create(HeliumModelBuilder.Shape.CUBE, textureHandler.getTexture(Color.WHITE), new Dimensions3(5f,15f,5f));
    m_worldObject = new WorldObject(model, new Vector3(), WorldObject.CollisionType.STANDARD);

    m_camera = new PerspectiveCamera();
    m_camera.fieldOfView = 95;
    m_camera.viewportWidth = Gdx.graphics.getWidth();
    m_camera.viewportHeight = Gdx.graphics.getHeight();
    m_camera.position.set(10f, 10f, 10f);

    m_camera.near = 0.1f;
    m_camera.far = 5000f;

    m_cameraYaw = 0.0f;
    m_cameraPitch = 0.0f;
  }

  public static PlayerController getInstance() {
    if (m_instance == null) {
      m_instance = new PlayerController();
    }
    return m_instance;
  }

  public PerspectiveCamera getCamera() {
    return m_camera;
  }

  @Override
  public void setPosition(Vector3 pos) {
    super.setPosition(pos);
    m_camera.position.set(pos);
    m_worldObject.setPosition(pos);
  }

  public void update() {
    // Update camera rotation based on mouse movement
    m_cameraYaw += -Gdx.input.getDeltaX() * m_engine.getDelta() * 10f;
    m_cameraPitch += Gdx.input.getDeltaY() * m_engine.getDelta() * 10f;

    // Clamp pitch angle to prevent flipping
    m_cameraPitch = MathUtils.clamp(m_cameraPitch, -89f, 89f);
    if(m_playerType == PlayerType.DOOM) {
      m_cameraPitch = 0.0f;
    }

    Quaternion quaternion = new Quaternion().setEulerAngles(m_cameraYaw, m_cameraPitch, 0);
    m_camera.direction.set(Vector3.Z).mul(quaternion);

    // Move the camera based on keyboard input
    Vector3 nextPos = m_camera.position.cpy();
    Vector3 tmp = new Vector3();
    m_camera.update();
    m_worldObject.update();

    ArrayList<Action> actions = InputProcessing.getInstance().getQueuedActions();
    for(Action action : actions) {
      if(action.getFunction() == Action.InputFunction.STRAFE_FORWARD) {
        nextPos.add(tmp.set(m_camera.direction).scl(getSpeed() * m_engine.getDelta()));
      }
      if(action.getFunction() == Action.InputFunction.STRAFE_BACKWARD) {
        nextPos.add(tmp.set(m_camera.direction).scl(-getSpeed() * m_engine.getDelta()));
      }
      if(action.getFunction() == Action.InputFunction.STRAFE_LEFT) {
        nextPos.add(tmp.set(m_camera.direction).crs(m_camera.up).nor().scl(-getSpeed() * m_engine.getDelta()));
      }
      if(action.getFunction() == Action.InputFunction.STRAFE_RIGHT) {
        nextPos.add(tmp.set(m_camera.direction).crs(m_camera.up).nor().scl(getSpeed() * m_engine.getDelta()));
      }
    }
    setPosition(nextPos);

    if(getHealth() <= 0) {
      HeliumIO.notify("Player", "You Died!");
      setHealth(100);
    }
  }

  @Override
  public void dispose() {
  }

  public enum PlayerType {
    STANDARD, DOOM, FLY, GHOST
  }
}
