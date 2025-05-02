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
import org.noble.helium.handling.ObjectHandler;
import org.noble.helium.handling.TextureHandler;
import org.noble.helium.math.Dimensions3;
import org.noble.helium.rendering.HeliumModelBuilder;
import org.noble.helium.world.WorldObject;

import java.util.ArrayList;

public class PlayerController extends Actor {
  //TODO: Refactor this class LAST
  private final PerspectiveCamera m_camera;
  private final Helium m_engine;
  private static PlayerController m_instance;
  private float m_cameraPitch, m_cameraYaw, m_verticalVelocity;
  private final PlayerType m_playerType;

  private boolean m_wantsToJump = false;
  String m_loggedName;

  private PlayerController() {
    super(null, 100, 8f);
    m_loggedName = "PlayerController";
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

  boolean m_strafeForward;
  boolean m_strafeBackward;
  boolean m_strafeLeft;
  boolean m_strafeRight;

  private void setVectorFromKeyboard(Vector3 nextPos, Vector3 tmp) {
    if (m_strafeForward) {
      nextPos.add(tmp.set(m_camera.direction).scl(getSpeed() * m_engine.getDelta()));
    }
    if (m_strafeBackward) {
      nextPos.add(tmp.set(m_camera.direction).scl(-getSpeed() * m_engine.getDelta()));
    }
    if (m_strafeLeft) {
      nextPos.add(tmp.set(m_camera.direction).crs(m_camera.up).nor().scl(-getSpeed() * m_engine.getDelta()));
    }
    if (m_strafeRight) {
      nextPos.add(tmp.set(m_camera.direction).crs(m_camera.up).nor().scl(getSpeed() * m_engine.getDelta()));
    }

    m_strafeForward = false;
    m_strafeBackward = false;
    m_strafeLeft = false;
    m_strafeRight = false;
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
        float topFaceOfObj = collision.getPosition().y + collision.getDimensions().getHeight() / 2f;
        float translation = topFaceOfObj + m_worldObject.getDimensions().getHeight() / 2f;
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
        m_wantsToJump = false;
      }
      float extentA_x = m_worldObject.getDimensions().getWidth() / 2.0f;
      float extentA_y = m_worldObject.getDimensions().getHeight() / 2.0f;
      float extentA_z = m_worldObject.getDimensions().getDepth() / 2.0f;

      float extentB_x = collision.getDimensions().getWidth() / 2.0f;
      float extentB_y = collision.getDimensions().getHeight() / 2.0f;
      float extentB_z = collision.getDimensions().getDepth() / 2.0f;

      float overlapX = (extentA_x + extentB_x) - Math.abs(m_worldObject.getPosition().x - collision.getPosition().x);
      float overlapY = (extentA_y + extentB_y) - Math.abs(m_worldObject.getPosition().y - collision.getPosition().y);
      float overlapZ = (extentA_z + extentB_z) - Math.abs(m_worldObject.getPosition().z - collision.getPosition().z);

      if (shouldCalculate) {
        if (overlapX < overlapY && overlapX < overlapZ) {
          // Smallest overlap is in the x-axis
          if (m_worldObject.getPosition().x < collision.getPosition().x) {
            nextPos.x = collision.getPosition().x - (extentA_x + extentB_x);
          } else {
            nextPos.x = collision.getPosition().x + (extentA_x + extentB_x);
          }
        } else if (overlapY < overlapX && overlapY < overlapZ) {
          // Smallest overlap is in the y-axis
          if (m_worldObject.getPosition().y < collision.getPosition().y) {
            nextPos.y = collision.getPosition().y - (extentA_y + extentB_y);
          } else {
            nextPos.y = collision.getPosition().y + (extentA_y + extentB_y);
          }
          nextPos.y -= 0.0005f;
          setVerticalVelocity(0);
          if(m_wantsToJump) {
            setVerticalVelocity(Constants.Player.k_jumpVerticalVelocity);
          }
          m_wantsToJump = false;
        } else {
          // Smallest overlap is in the z-axis
          if (m_worldObject.getPosition().z < collision.getPosition().z) {
            nextPos.z = collision.getPosition().z - (extentA_z + extentB_z);
          } else {
            nextPos.z = collision.getPosition().z + (extentA_z + extentB_z);
          }
        }
      }
    }
    m_wantsToJump = false;
  }

  public void jump() {
    m_wantsToJump = true;
  }

  public void strafeForward() {
    m_strafeForward = true;
  }

  public void strafeBackward() {
    m_strafeBackward = true;
  }

  public void strafeLeft() {
    m_strafeLeft = true;
  }

  public void strafeRight() {
    m_strafeRight = true;
  }

  public void update() {
    rotate();
    translate();
    m_camera.update();
    m_worldObject.update();
  }

  @Override
  public void dispose() {
  }

  public enum PlayerType {
    STANDARD, DOOM, FLY, GHOST
  }
}
