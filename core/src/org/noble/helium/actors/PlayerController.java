package org.noble.helium.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.kotcrab.vis.ui.widget.VisLabel;
import org.noble.helium.Helium;
import org.noble.helium.handling.ModelHandler;
import org.noble.helium.handling.ObjectHandler;
import org.noble.helium.math.Dimensions2;
import org.noble.helium.handling.InputHandler;
import org.noble.helium.math.Dimensions3;
import org.noble.helium.subsystems.telemetry.LogEntry;
import org.noble.helium.subsystems.ui.UserInterface;
import org.noble.helium.world.WorldObject;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;

public class PlayerController extends Actor {
  private final PerspectiveCamera m_camera;
  private final InputHandler m_input;
  private final Helium m_engine;
  private static PlayerController m_instance;
  private float m_cameraPitch, m_cameraYaw, m_verticalVelocity;
  private final PlayerType m_playerType;

  private PlayerController() {
    super(new Vector3(), 100, 8f, null);
    m_loggedName = "PlayerController";
    m_playerType = PlayerType.STANDARD;
    m_input = InputHandler.getInstance();
    m_engine = Helium.getInstance();

    ModelHandler.getInstance().addNewShape(m_loggedName + "-model", ModelHandler.Shape.SPHERE, Color.WHITE,
        new Vector3(), new Dimensions3(5f,15f,5f));
    m_model = ModelHandler.getInstance().get(m_loggedName + "-model");

    m_worldObject = new WorldObject(m_model, WorldObject.CollisionType.STANDARD);

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

  public float getVerticalVelocity() {
    return m_verticalVelocity;
  }

  public void setVerticalVelocity(float velocity) {
    m_verticalVelocity = velocity;
  }

  @Override
  public void setPosition(Vector3 pos) {
    super.setPosition(pos);
    m_camera.position.set(pos);
    m_worldObject.setPosition(pos);
  }

  private ArrayList<WorldObject> getCollisions() {
    ArrayList<WorldObject> collisions = new ArrayList<>();
    for (WorldObject object : ObjectHandler.getInstance().getAllObjects().values()) {
      if (!object.equals(m_worldObject) && m_worldObject.getBoundingBox().isColliding(object.getBoundingBox())) {
        collisions.add(object);
      }
    }
    return collisions;
  }

  private void rotate() {
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
  }

  private void setVectorFromKeyboard(Vector3 nextPos, Vector3 tmp) {
    if (m_input.isActionDown(InputHandler.Action.STRAFE_FORWARD, false)) {
      nextPos.add(tmp.set(m_camera.direction).scl(getSpeed() * m_engine.getDelta()));
    }
    if (m_input.isActionDown(InputHandler.Action.STRAFE_BACKWARD, false)) {
      nextPos.add(tmp.set(m_camera.direction).scl(-getSpeed() * m_engine.getDelta()));
    }
    if (m_input.isActionDown(InputHandler.Action.STRAFE_LEFT, false)) {
      nextPos.add(tmp.set(m_camera.direction).crs(m_camera.up).nor().scl(-getSpeed() * m_engine.getDelta()));
    }
    if (m_input.isActionDown(InputHandler.Action.STRAFE_RIGHT, false)) {
      nextPos.add(tmp.set(m_camera.direction).crs(m_camera.up).nor().scl(getSpeed() * m_engine.getDelta()));
    }
  }

  private void translate() {
    // Move the camera based on keyboard input
    Vector3 nextPos = m_camera.position.cpy();
    Vector3 tmp = new Vector3();
    ArrayList<WorldObject> collisions = getCollisions();

    switch(m_playerType) {
      case STANDARD, DOOM -> {
        calculateCollisions(nextPos, collisions);
        setVerticalVelocity(getVerticalVelocity() - 15f * m_engine.getDelta());

        float tempY = nextPos.y;
        setVectorFromKeyboard(nextPos, tmp);
        nextPos.y = tempY + (getVerticalVelocity() * m_engine.getDelta());
      }
      case FLY -> {
        calculateCollisions(nextPos, collisions);
        setVectorFromKeyboard(nextPos, tmp);
      }
      case GHOST -> setVectorFromKeyboard(nextPos, tmp);
    }

    setPosition(nextPos);

  }

  private void calculateCollisions(Vector3 nextPos, ArrayList<WorldObject> collisions) {
    boolean shouldCalculate = true;
    for (WorldObject collision : collisions) {
      if (collision.getCollisionType() == WorldObject.CollisionType.CLIMBABLE) {
        float topFaceOfObj = collision.getY() + collision.getHeight() / 2f;
        float translation = topFaceOfObj + m_worldObject.getHeight() / 2f;
        float movementSpeed = (m_engine.getDelta() * 2f);
        float yMovement = translation - nextPos.y;
        if (translation > nextPos.y) {
          if (yMovement < 3f) {
            movementSpeed *= 4f;
            shouldCalculate = false;
          }
          nextPos.y += (yMovement) * movementSpeed;
        }
        setVerticalVelocity(0f);
        if (m_input.isActionDown(InputHandler.Action.JUMP, false)) {
          setVerticalVelocity(10f);
        }
      }
      float extentA_x = m_worldObject.getWidth() / 2.0f;
      float extentA_y = m_worldObject.getHeight() / 2.0f;
      float extentA_z = m_worldObject.getDepth() / 2.0f;

      float extentB_x = collision.getWidth() / 2.0f;
      float extentB_y = collision.getHeight() / 2.0f;
      float extentB_z = collision.getDepth() / 2.0f;

      float overlapX = (extentA_x + extentB_x) - Math.abs(m_worldObject.getX() - collision.getX());
      float overlapY = (extentA_y + extentB_y) - Math.abs(m_worldObject.getY() - collision.getY());
      float overlapZ = (extentA_z + extentB_z) - Math.abs(m_worldObject.getZ() - collision.getZ());

      if (shouldCalculate) {
        if (overlapX < overlapY && overlapX < overlapZ) {
          // Smallest overlap is in the x-axis
          if (m_worldObject.getX() < collision.getX()) {
            nextPos.x = collision.getX() - (extentA_x + extentB_x);
          } else {
            nextPos.x = collision.getX() + (extentA_x + extentB_x);
          }
        } else if (overlapY < overlapX && overlapY < overlapZ) {
          // Smallest overlap is in the y-axis
          if (m_worldObject.getY() < collision.getY()) {
            nextPos.y = collision.getY() - (extentA_y + extentB_y);
          } else {
            nextPos.y = collision.getY() + (extentA_y + extentB_y);
          }
          nextPos.y -= 0.0005f;
          setVerticalVelocity(0);
          if (m_input.isActionDown(InputHandler.Action.JUMP, false)) {
            setVerticalVelocity(10f);
          }
        } else {
          // Smallest overlap is in the z-axis
          if (m_worldObject.getZ() < collision.getZ()) {
            nextPos.z = collision.getZ() - (extentA_z + extentB_z);
          } else {
            nextPos.z = collision.getZ() + (extentA_z + extentB_z);
          }
        }
      }
    }
  }

  public void update() {
    if(m_input.isActionDown(InputHandler.Action.MOVE_FASTER, false)) {
      setSpeed(16f);
    } else {
      setSpeed(8f);
    }

    rotate();
    translate();
    m_camera.update();
    m_worldObject.update();
    
    VisLabel PosLabel = UserInterface.getInstance().getLabel("PlayerController-Position");
    UserInterface.getInstance().setLabel("PlayerController-Position", "Position: " + getX() + ", " + getY() +
        ", " + getZ(), PosLabel.getX(), PosLabel.getY(), new Dimensions2(PosLabel.getWidth(), PosLabel.getHeight()), PosLabel.getColor());
    VisLabel HealthLabel = UserInterface.getInstance().getLabel("PlayerController-Health");
    UserInterface.getInstance().setLabel("PlayerController-Health", "Health: " + getHealth(), HealthLabel.getX(), HealthLabel.getY(),
        new Dimensions2(HealthLabel.getWidth(), HealthLabel.getHeight()), HealthLabel.getColor());
  }

  @Override
  public void dispose() {
  }

  @Override
  public ArrayList<LogEntry> getLogEntries() {
    ArrayList<LogEntry> logs = new ArrayList<>();
    logs.add(new LogEntry(Timestamp.from(Instant.now()), "Player Position", getPosition().toString()));
    logs.add(new LogEntry(Timestamp.from(Instant.now()), "Player Direction", getCamera().direction.toString()));
    return logs;
  }

  public enum PlayerType {
    STANDARD, DOOM, FLY, GHOST
  }
}
