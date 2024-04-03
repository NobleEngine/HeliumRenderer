package org.noble.helium.Actors;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import org.noble.helium.helpers.Coordinates;

public class PlayerController extends Actor {
  private final PerspectiveCamera m_camera;
  private static PlayerController m_instance;
  private float m_cameraPitch, m_cameraYaw;

  private PlayerController() {
    super(new Coordinates(0f, 0f, 0f), 100, 5);
    m_camera = new PerspectiveCamera();
    m_camera.fieldOfView = 67;
    m_camera.viewportWidth = Gdx.graphics.getWidth();
    m_camera.viewportHeight = Gdx.graphics.getHeight();
    m_camera.position.set(10f, 10f, 10f);
    m_camera.lookAt(10f,10f,10f);

    m_cameraYaw = 0.0f;
    m_cameraPitch = 45.0f;
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

  private void rotateCameraByMouseMovement() {
    // Update camera rotation based on mouse movement
    m_cameraYaw += -Gdx.input.getDeltaX() * 0.5f;
    m_cameraPitch += Gdx.input.getDeltaY() * 0.5f;

    // Clamp pitch angle to prevent flipping
    m_cameraPitch = MathUtils.clamp(m_cameraPitch, -89f, 89f);

    Quaternion quaternion = new Quaternion().setEulerAngles(m_cameraYaw, m_cameraPitch, 0);
    m_camera.direction.set(Vector3.Z).mul(quaternion);

    // Update the camera
    m_camera.update();
  }

  public void update() {
    rotateCameraByMouseMovement();
    m_camera.update();
  }
}
