package org.noble.helium;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;

import java.util.ArrayList;

public class HeliumMain extends Game {
  private Environment m_environment;
	private PerspectiveCamera m_camera;
  private CameraInputController m_cameraInput;
  private ModelBuilder m_modelBuilder;
  private ModelBatch m_modelBatch;
  private ArrayList<Model> m_models;
  private ArrayList<ModelInstance> m_modelInstances;

  @Override
  public void create() {
    m_models = new ArrayList<>();
    m_modelInstances = new ArrayList<>();

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

    m_modelBuilder = new ModelBuilder();
    m_models.add(m_modelBuilder.createBox(5f, 5f, 5f,
        new Material(ColorAttribute.createDiffuse(Color.GREEN)),
        VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal));

    m_models.forEach(model -> m_modelInstances.add(new ModelInstance(model)));

    m_environment = new Environment();
    m_environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1.0f));
    m_environment.add(new DirectionalLight().set(0.8f,0.8f,0.8f,-1f,-0.8f,-0.2f));

    m_modelBatch = new ModelBatch();

    m_camera.update();
  }

  @Override
  public void render() {
    m_cameraInput.update();

    Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

    m_modelBatch.begin(m_camera);
		m_modelInstances.forEach(instance -> m_modelBatch.render(instance,m_environment));
    m_modelBatch.end();
  }

  @Override
  public void dispose() {
  }
}
