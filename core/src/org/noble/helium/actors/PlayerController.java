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
import org.noble.helium.math.Dimensions3;
import org.noble.helium.io.KeyInput;
import org.noble.helium.math.EulerAngles;
import org.noble.helium.subsystems.telemetry.LogEntry;
import org.noble.helium.subsystems.telemetry.Loggable;
import org.noble.helium.subsystems.ui.UserInterface;
import org.noble.helium.world.WorldObject;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;

public class PlayerController extends Actor {
  private final PerspectiveCamera m_camera;
  private final KeyInput m_input;
  private final Helium m_engine;
  private final ObjectHandler m_objectHandler;
  private static PlayerController m_instance;
  private float m_cameraPitch, m_cameraYaw, m_verticalVelocity;

  private PlayerController() {
    super(new Vector3(), 100, 0.5f);
    m_loggedName = "PlayerController";
    m_input = KeyInput.getInstance();
    m_engine = Helium.getInstance();
    m_objectHandler = ObjectHandler.getInstance();

    m_camera = new PerspectiveCamera();
    m_camera.fieldOfView = 67;
    m_camera.viewportWidth = Gdx.graphics.getWidth();
    m_camera.viewportHeight = Gdx.graphics.getHeight();
    m_camera.position.set(10f, 10f, 10f);
    m_camera.lookAt(10f, 10f, 10f);
    m_camera.near = 0.1f;
    m_camera.far = 5000f;

    m_cameraYaw = 0.0f;
    m_cameraPitch = 45.0f;

    createWorldObject();
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

  public WorldObject getWorldObject() {
    return m_objectHandler.get("PlayerController-object");
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
    m_objectHandler.get("PlayerController-object").setPosition(pos);
  }

  private void createWorldObject() {
    ModelHandler modelHandler = ModelHandler.getInstance();
    modelHandler.addNewShape("PlayerController-model", ModelHandler.Shape.CUBE, Color.WHITE, new Vector3(),
        new Dimensions3(5,15,5));
    m_objectHandler.add("PlayerController-object", new WorldObject(modelHandler.get("PlayerController-model"),
        WorldObject.ShapeType.BOX, WorldObject.CollisionType.NONE));
  }

  private ArrayList<WorldObject> getCollisions() {
    ArrayList<WorldObject> collisions = new ArrayList<>();
    WorldObject playerWObject = m_objectHandler.get("PlayerController-object");
    for (WorldObject object : ObjectHandler.getInstance().getAllObjects().values()) {
      if (!object.equals(playerWObject) && playerWObject.isColliding(object)) {
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

    Quaternion quaternion = new Quaternion().setEulerAngles(m_cameraYaw, m_cameraPitch, 0);
    m_camera.direction.set(Vector3.Z).mul(quaternion);
  }

  private void translate() {
    // Move the camera based on keyboard input
    Vector3 nextPos = m_camera.position.cpy();
    Vector3 tmp = new Vector3();
    float speed;
    ArrayList<WorldObject> collisions = getCollisions();
    WorldObject playerWObject = m_objectHandler.get("PlayerController-object");

    setVerticalVelocity(getVerticalVelocity() - 15f * m_engine.getDelta());

    boolean shouldCalculate = true;
    for (WorldObject collision : collisions) {
      if (collision.getCollisionType() == WorldObject.CollisionType.CLIMBABLE) {
        float topFaceOfObj = collision.getY() + collision.getHeight() / 2f;
        float translation = topFaceOfObj + playerWObject.getHeight() / 2f;
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
        if (m_input.isKeyDown(KeyInput.Action.JUMP, false)) {
          setVerticalVelocity(10f);
        }
      }
      float extentA_x = playerWObject.getWidth() / 2.0f;
      float extentA_y = playerWObject.getHeight() / 2.0f;
      float extentA_z = playerWObject.getDepth() / 2.0f;

      float extentB_x = collision.getWidth() / 2.0f;
      float extentB_y = collision.getHeight() / 2.0f;
      float extentB_z = collision.getDepth() / 2.0f;

      float overlapX = (extentA_x + extentB_x) - Math.abs(playerWObject.getX() - collision.getX());
      float overlapY = (extentA_y + extentB_y) - Math.abs(playerWObject.getY() - collision.getY());
      float overlapZ = (extentA_z + extentB_z) - Math.abs(playerWObject.getZ() - collision.getZ());

      if (shouldCalculate) {
        if (overlapX < overlapY && overlapX < overlapZ) {
          // Smallest overlap is in the x-axis
          if (playerWObject.getX() < collision.getX()) {
            nextPos.x = collision.getX() - (extentA_x + extentB_x);
          } else {
            nextPos.x = collision.getX() + (extentA_x + extentB_x);
          }
        } else if (overlapY < overlapX && overlapY < overlapZ) {
          // Smallest overlap is in the y-axis
          if (playerWObject.getY() < collision.getY()) {
            nextPos.y = collision.getY() - (extentA_y + extentB_y);
          } else {
            nextPos.y = collision.getY() + (extentA_y + extentB_y);
          }
          nextPos.y -= 0.0005f;
          setVerticalVelocity(0);
          if (m_input.isKeyDown(KeyInput.Action.JUMP, false)) {
            setVerticalVelocity(10f);
          }
        } else {
          // Smallest overlap is in the z-axis
          if (playerWObject.getZ() < collision.getZ()) {
            nextPos.z = collision.getZ() - (extentA_z + extentB_z);
          } else {
            nextPos.z = collision.getZ() + (extentA_z + extentB_z);
          }
        }
      }
    }

    if (m_input.isKeyDown(KeyInput.Action.MOVE_FASTER, false)) {
      speed = 0.5f * 20f;
    } else {
      speed = 0.5f * 10f;
    }

    float tempY = nextPos.y;

    if (m_input.isKeyDown(KeyInput.Action.STRAFE_FORWARD, false)) {
      nextPos.add(tmp.set(m_camera.direction).scl(speed * m_engine.getDelta()));
    }
    if (m_input.isKeyDown(KeyInput.Action.STRAFE_BACKWARD, false)) {
      nextPos.add(tmp.set(m_camera.direction).scl(-speed * m_engine.getDelta()));
    }
    if (m_input.isKeyDown(KeyInput.Action.STRAFE_LEFT, false)) {
      nextPos.add(tmp.set(m_camera.direction).crs(m_camera.up).nor().scl(-speed * m_engine.getDelta()));
    }
    if (m_input.isKeyDown(KeyInput.Action.STRAFE_RIGHT, false)) {
      nextPos.add(tmp.set(m_camera.direction).crs(m_camera.up).nor().scl(speed * m_engine.getDelta()));
    }

    nextPos.y = tempY + (getVerticalVelocity() * m_engine.getDelta());

    setPosition(nextPos);
  }

  public void update() {
    if(m_objectHandler.get("PlayerController-object") == null) {
      createWorldObject();
    }
    rotate();
    translate();
    m_camera.update();
    VisLabel PosLabel = UserInterface.getInstance().getLabel("PlayerController-Position");
    UserInterface.getInstance().setLabel("PlayerController-Position", "Position: " + getX() + ", " + getY() +
        ", " + getZ(), PosLabel.getX(), PosLabel.getY(), new Dimensions2(PosLabel.getWidth(), PosLabel.getHeight()), PosLabel.getColor());
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
}
