package org.noble.helium;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import org.noble.helium.helpers.Coordinates;
import org.noble.helium.helpers.Dimensions;

import java.util.Map;

public class HeliumMain extends Game {
  private Environment m_environment;
	private PerspectiveCamera m_camera;
  private CameraInputController m_cameraInput;
  private ModelHandler m_modelHandler;
  private ModelBatch m_modelBatch;

  @Override
  public void create() {
    m_modelHandler = ModelHandler.getInstance();

    m_camera = new PerspectiveCamera();
    m_camera.fieldOfView = 67;
    m_camera.viewportWidth = Gdx.graphics.getWidth();
    m_camera.viewportHeight = Gdx.graphics.getHeight();
    m_camera.position.set(10f, 10f, 10f);
    m_camera.lookAt(0,0,0);
    m_camera.near = 1f;
    m_camera.far = 300f;

    m_cameraInput = new CameraInputController(m_camera);
    Gdx.input.setInputProcessor(m_cameraInput);

    m_modelHandler.addNewShape(
        "cube-01", ModelHandler.Shape.CUBE, Color.YELLOW,
        new Coordinates(0f,0f,0f), new Dimensions(20f,10f,10f));
    m_modelHandler.addNewShape(
        "sphere-01", ModelHandler.Shape.SPHERE, Color.RED,
        new Coordinates(20f,20f,20f), new Dimensions(10f,10f,10f));

    m_environment = new Environment();
    m_environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1.0f));
    m_environment.add(new DirectionalLight().set(0.8f,0.8f,0.8f,-1f,-0.8f,-0.2f));

    m_modelBatch = new ModelBatch();

    m_camera.update();
  }

  float a = 0.0f;
  @Override
  public void render() {
    m_cameraInput.update();

    Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

    m_modelBatch.begin(m_camera);

    for (Map.Entry<String, ModelInstance> entry : m_modelHandler.getModelInstances().entrySet()) {
      m_modelBatch.render(entry.getValue(), m_environment);
    }

    a += 1.0f;
    m_modelBatch.end();
  }

  @Override
  public void dispose() {
  }
}