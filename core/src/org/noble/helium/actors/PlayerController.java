package org.noble.helium.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import org.noble.helium.Constants;
import org.noble.helium.Helium;
import org.noble.helium.HeliumIO;
import org.noble.helium.handling.ObjectHandler;
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
  private float m_cameraPitch, m_cameraYaw, m_verticalVelocity;
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

  public float getVerticalVelocity() {
    return m_verticalVelocity;
  }

  public void setVerticalVelocity(float velocity) {
    if(!(Math.abs(velocity) > 25)) {
      m_verticalVelocity = velocity;
    }
  }

  @Override
  public void setPosition(Vector3 pos) {
    super.setPosition(pos);
    m_camera.position.set(pos);
    m_worldObject.setPosition(pos);
  }

  private ArrayList<WorldObject> getCollisions() {
    ArrayList<WorldObject> collisions = new ArrayList<>();
    for (WorldObject object : ObjectHandler.getInstance().getAllObjects()) {
      if (!object.equals(m_worldObject) && m_worldObject.isColliding(object)) {//getBoundingBox().isColliding(object.getBoundingBox())) {
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
    ArrayList<Action> actions = InputProcessing.getInstance().getQueuedActions();
    m_camera.direction.set(Vector3.Z).mul(new Quaternion().setEulerAngles(m_cameraYaw, 0, 0));
    for(Action a : actions) {
      if(a.getFunction() == Action.InputFunction.STRAFE_FORWARD) {
        nextPos.add(tmp.set(m_camera.direction).scl(getSpeed() * m_engine.getDelta()));
      }
      if(a.getFunction() == Action.InputFunction.STRAFE_BACKWARD) {
        nextPos.add(tmp.set(m_camera.direction).scl(-getSpeed() * m_engine.getDelta()));
      }
      if(a.getFunction() == Action.InputFunction.STRAFE_LEFT) {
        nextPos.add(tmp.set(m_camera.direction).crs(m_camera.up).nor().scl(-getSpeed() * m_engine.getDelta()));
      }
      if(a.getFunction() == Action.InputFunction.STRAFE_RIGHT) {
        nextPos.add(tmp.set(m_camera.direction).crs(m_camera.up).nor().scl(getSpeed() * m_engine.getDelta()));
      }
    }
    m_camera.direction.set(Vector3.Z).mul(new Quaternion().setEulerAngles(m_cameraYaw, m_cameraPitch, 0));
  }

  private void translate() {
    // Move the camera based on keyboard input
    Vector3 nextPos = m_camera.position.cpy();
    Vector3 tmp = new Vector3();
    ArrayList<WorldObject> collisions = getCollisions();
    boolean shouldJump = false;

    ArrayList<Action> actions = InputProcessing.getInstance().getQueuedActions();
    for(Action a : actions) {
      if(a.getFunction() == Action.InputFunction.JUMP) {
        shouldJump = true;
        break;
      }
    }

    switch(m_playerType) {
      case STANDARD, DOOM -> {
        calculateCollisions(nextPos, collisions, shouldJump);
        setVerticalVelocity(getVerticalVelocity() - 15f * m_engine.getDelta());

        setVectorFromKeyboard(nextPos, tmp);
        nextPos.y += getVerticalVelocity() * m_engine.getDelta();
      }
      case FLY -> {
        calculateCollisions(nextPos, collisions, shouldJump);
        setVectorFromKeyboard(nextPos, tmp);
      }
      case GHOST -> setVectorFromKeyboard(nextPos, tmp);
    }

    setPosition(nextPos);

  }

  private void calculateCollisions(Vector3 nextPos, ArrayList<WorldObject> collisions, boolean wantsToJump) {
    boolean shouldCalculate = true;
    boolean hasClimbable = false;

    for (WorldObject collision : collisions) {
      Vector3 objPos = collision.getPosition();
      Dimensions3 objDims = collision.getDimensions();
      Vector3 myPos = m_worldObject.getPosition();
      Dimensions3 myDims = m_worldObject.getDimensions();

      // Handle climbable surfaces
      if (collision.getCollisionType() == WorldObject.CollisionType.CLIMBABLE) {
        hasClimbable = true;

        float topOfObject = objPos.y + objDims.getHeight() / 2f;
        float targetY = topOfObject + myDims.getHeight() / 2f;
        float yMovement = targetY - nextPos.y;

        if (targetY > nextPos.y && yMovement < 3f) {
          float speed = m_engine.getDelta() * 8f; // 2f * 4f
          nextPos.y += yMovement * speed;
          if (wantsToJump) {
            setVerticalVelocity(Constants.Player.k_jumpVerticalVelocity);
          } else {
            setVerticalVelocity(0f);
          }
          shouldCalculate = false;
        }
      }

      // Calculate extents
      float extentA_x = myDims.getWidth() / 2f;
      float extentB_x = objDims.getWidth() / 2f;

      float extentA_y = myDims.getHeight() / 2f;
      float extentB_y = objDims.getHeight() / 2f;

      float extentA_z = myDims.getDepth() / 2f;
      float extentB_z = objDims.getDepth() / 2f;

      // Calculate overlaps
      float overlapX = (extentA_x + extentB_x) - Math.abs(myPos.x - objPos.x);
      float overlapY = (extentA_y + extentB_y) - Math.abs(myPos.y - objPos.y);
      float overlapZ = (extentA_z + extentB_z) - Math.abs(myPos.z - objPos.z);

      if (!shouldCalculate) {
        continue;
      }

      // Resolve the smallest axis of overlap
      if (overlapX < overlapY && overlapX < overlapZ) {
        if (myPos.x < objPos.x) {
          nextPos.x = objPos.x - (extentA_x + extentB_x);
        } else {
          nextPos.x = objPos.x + (extentA_x + extentB_x);
        }
      } else if (overlapY < overlapX && overlapY < overlapZ) {
        if (myPos.y < objPos.y) {
          nextPos.y = objPos.y - (extentA_y + extentB_y);
        } else {
          nextPos.y = objPos.y + (extentA_y + extentB_y);
        }

        nextPos.y -= 0.0005f;
        setVerticalVelocity(0f);
      } else {
        if (myPos.z < objPos.z) {
          nextPos.z = objPos.z - (extentA_z + extentB_z);
        } else {
          nextPos.z = objPos.z + (extentA_z + extentB_z);
        }
      }

      if (wantsToJump && (getVerticalVelocity() == 0.0f || hasClimbable)) {
        setVerticalVelocity(Constants.Player.k_jumpVerticalVelocity);
      }
    }
  }

  public void update() {
    rotate();
    translate();
    m_camera.update();
    m_worldObject.update();

    if(getHealth() <= 0) {
      HeliumIO.notify("Player", "You Died!");
      setHealth(100);
    }
  }

  public void reset() {
    setVerticalVelocity(0f);
    setPosition(new Vector3(0f, 0f, 0f));
  }

  @Override
  public void dispose() {
  }

  public enum PlayerType {
    STANDARD, DOOM, FLY, GHOST
  }
}
