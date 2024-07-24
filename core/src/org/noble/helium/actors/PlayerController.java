package org.noble.helium.actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import org.noble.helium.handling.ObjectHandler;
import org.noble.helium.handling.SimpleModelHandler;
import org.noble.helium.helpers.Dimensions;
import org.noble.helium.io.KeyInput;
import org.noble.helium.world.WorldObject;

import java.util.ArrayList;

public class PlayerController extends Actor {
  private final PerspectiveCamera m_camera;
  private final KeyInput m_input;
  private final WorldObject m_playerWObject;
  private static PlayerController m_instance;
  private float m_cameraPitch, m_cameraYaw, m_verticalVelocity;

  private PlayerController() {
    super(new Vector3(),100,0.5f);
    m_input = KeyInput.getInstance();

    m_camera = new PerspectiveCamera();
    m_camera.fieldOfView = 67;
    m_camera.viewportWidth = Gdx.graphics.getWidth();
    m_camera.viewportHeight = Gdx.graphics.getHeight();
    m_camera.position.set(10f, 10f, 10f);
    m_camera.lookAt(10f,10f,10f);
    m_camera.near = 0.1f;
    m_camera.far = 5000f;

    m_cameraYaw = 0.0f;
    m_cameraPitch = 45.0f;

    SimpleModelHandler modelHandler = SimpleModelHandler.getInstance();
    modelHandler.addNewShape("player", SimpleModelHandler.Shape.CUBE, Color.BLACK,
        new Vector3(), new Dimensions(5,15,5));
    m_playerWObject = new WorldObject(modelHandler.get("player"), WorldObject.ShapeType.BOX, WorldObject.CollisionType.NONE);
  }

  public static PlayerController getInstance() {
    if(m_instance == null) {
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
    m_playerWObject.setPosition(pos.x, pos.y, pos.z);
  }

  private ArrayList<WorldObject> getCollisions() {
    ArrayList<WorldObject> collisions = new ArrayList<>();
    for(WorldObject object : ObjectHandler.getInstance().getAllObjects().values()) {
      if (!object.equals(m_playerWObject) && m_playerWObject.isColliding(object)) {
        collisions.add(object);
      }
    }
    return collisions;
  }

  private void rotate() {
    // Update camera rotation based on mouse movement
    m_cameraYaw += -Gdx.input.getDeltaX() * Gdx.graphics.getDeltaTime() * 10f;
    m_cameraPitch += Gdx.input.getDeltaY() * Gdx.graphics.getDeltaTime() * 10f;

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

    if(m_input.isKeyDown(KeyInput.Action.MOVE_FASTER, false)) {
      speed = 0.5f * 20f;
    } else {
      speed = 0.5f * 10f;
    }

    if (m_input.isKeyDown(KeyInput.Action.MOVE_FORWARD, false)) {
      nextPos.add(tmp.set(m_camera.direction).scl(speed * Gdx.graphics.getDeltaTime()));
    }
    if (m_input.isKeyDown(KeyInput.Action.MOVE_BACKWARD, false)) {
      nextPos.add(tmp.set(m_camera.direction).scl(-speed * Gdx.graphics.getDeltaTime()));
    }
    if (m_input.isKeyDown(KeyInput.Action.MOVE_LEFT, false)) {
      nextPos.add(tmp.set(m_camera.direction).crs(m_camera.up).nor().scl(-speed * Gdx.graphics.getDeltaTime()));
    }
    if (m_input.isKeyDown(KeyInput.Action.MOVE_RIGHT, false)) {
      nextPos.add(tmp.set(m_camera.direction).crs(m_camera.up).nor().scl(speed * Gdx.graphics.getDeltaTime()));
    }

    nextPos.y = getY() + (getVerticalVelocity() * Gdx.graphics.getDeltaTime());

    ArrayList<WorldObject> collisions = getCollisions();
    if(collisions.isEmpty()) {
      setVerticalVelocity(getVerticalVelocity() - (8f * Gdx.graphics.getDeltaTime()));
    } else if(KeyInput.getInstance().isKeyDown(KeyInput.Action.JUMP, false)) {
      setVerticalVelocity(1600f / 144f);
    } else {
      setVerticalVelocity(0);

      for (WorldObject collision : collisions) {
        switch (collision.getCollisionType()) {
          case WorldObject.CollisionType.FLOOR -> {
            float topFaceOfObj = collision.getY() + collision.getHeight() / 2;
            nextPos.y = topFaceOfObj + m_playerWObject.getHeight() / 2;
          }
        }
      }
    }


    setPosition(nextPos);
  }

  public void update() {
    rotate();
    translate();
    m_camera.update();
  }

  @Override
  public void dispose() {
  }
}
