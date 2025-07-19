package org.noble.helium.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import org.noble.helium.Constants;
import org.noble.helium.Helium;
import org.noble.helium.HeliumIO;
import org.noble.helium.handling.TextureHandler;
import org.noble.helium.math.Dimensions3;
import org.noble.helium.rendering.HeliumModelBuilder;
import org.noble.helium.subsystems.input.Action;
import org.noble.helium.subsystems.input.InputProcessing;
import org.noble.helium.world.WorldObject;

import java.util.ArrayList;
import java.util.Vector;

public class PlayerController extends Actor {
  private final PerspectiveCamera m_camera;
  private final Helium m_engine;
  private static PlayerController m_instance;
  private float m_cameraPitch, m_cameraYaw;
  private final PlayerType m_playerType;
  private final Vector3 m_playerVelocity = new Vector3();

  private PlayerController() {
    super(null, 100, Constants.Player.k_defaultSpeed);
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

  public Vector3 getPlayerVelocity() {
    return m_playerVelocity;
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

    //TODO: If fly mode, set pitch to m_cameraPitch
    m_camera.direction.set(Vector3.Z).mul(new Quaternion().setEulerAngles(m_cameraYaw, 0, 0));

    // Move the camera based on keyboard input
    Vector3 nextPos = m_camera.position.cpy();
    Vector3 tmp = new Vector3();
    m_camera.update();
    m_worldObject.update();

    float delta = m_engine.getDelta();
    float speed = (getSpeed() / 0.01f * delta) * delta;
    ArrayList<Action> actions = InputProcessing.getInstance().getQueuedActions();

    for(Action action : actions) {
      //TODO: fix actions

      if (!InputProcessing.getInstance().isAnyPlayerMovementKeyPressed()) {
        float deceleration = Constants.World.k_defaultAcceleration * delta;
        m_playerVelocity.lerp(Vector3.Zero, deceleration * delta);
      }
      else {
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
          m_playerVelocity.x += speed;
        } else if (m_playerVelocity.x > 0.0f) {
          m_playerVelocity.x -= speed;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
          m_playerVelocity.x -= speed;
        } else if (m_playerVelocity.x < 0.0f) {
          m_playerVelocity.x += speed;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
          m_playerVelocity.z -= speed;
        } else if (m_playerVelocity.z < 0.0f) {
          m_playerVelocity.z += speed;
        }

        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
          m_playerVelocity.z += speed;
        } else if (m_playerVelocity.z > 0.0f) {
          m_playerVelocity.z -= speed;
        }

        m_playerVelocity.x = MathUtils.clamp(m_playerVelocity.x, -getSpeed() * delta, getSpeed() * delta);
        m_playerVelocity.z = MathUtils.clamp(m_playerVelocity.z, -getSpeed() * delta, getSpeed() * delta);

        if (action.getFunction() == Action.InputFunction.STRAFE_FORWARD && action.getFunction() == Action.InputFunction.STRAFE_BACKWARD) {
          m_playerVelocity.x = 0.0f;
        }
        if (action.getFunction() == Action.InputFunction.STRAFE_RIGHT && action.getFunction() == Action.InputFunction.STRAFE_LEFT) {
          m_playerVelocity.z = 0.0f;
        }
      }

      nextPos.add(tmp.set(m_camera.direction).scl(m_playerVelocity.x));
      nextPos.add(tmp.set(m_camera.direction).crs(m_camera.up).nor().scl(m_playerVelocity.z));
    }
    setPosition(nextPos);

    //TODO: If DOOM mode, don't do this
    m_camera.direction.set(Vector3.Z).mul(new Quaternion().setEulerAngles(m_cameraYaw, m_cameraPitch, 0));
    m_camera.update();

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
