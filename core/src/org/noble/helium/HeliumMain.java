package org.noble.helium;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import org.noble.helium.Actors.PlayerController;
import org.noble.helium.helpers.Coordinates;
import org.noble.helium.helpers.Dimensions;
import org.noble.helium.io.KeyInput;
import org.noble.helium.logic.CopperValues;

import java.util.Map;

public class HeliumMain extends Game {
  private Environment m_environment;
  private PlayerController m_player;
  private SimpleModelHandler m_simpleModelHandler;
  private KeyInput m_input;
  private ModelBatch m_modelBatch;
  private PhysicsHandler m_physics;

  @Override
  public void create() {
    m_simpleModelHandler = SimpleModelHandler.getInstance();
    m_input = KeyInput.getInstance();
    m_player = PlayerController.getInstance();
    m_physics = PhysicsHandler.getInstance();

    m_simpleModelHandler.addNewShape(
        "cube-01", SimpleModelHandler.Shape.CUBE, new Texture(Gdx.files.internal("textures/dirt.png")),
        new Coordinates(0f,0f,0f), new Dimensions(100f,100f,10f));

    m_simpleModelHandler.addNewShape(
        "cube-physical", SimpleModelHandler.Shape.CUBE, Color.BLUE,
        new Coordinates(5f, 50f, 5f), new Dimensions(5f, 5f, 5f));

    m_simpleModelHandler.addNewShape(
        "sphere-01", SimpleModelHandler.Shape.SPHERE, Color.RED,
        new Coordinates(20f,20f,20f), new Dimensions(10f,10f,10f));
    m_simpleModelHandler.addNewGLTFModel("slime", "models/Alien Slime/Alien Slime.gltf",
        new Coordinates(10f, 10f, 10f));
    m_simpleModelHandler.addNewOBJModel("pawn", "models/Pawn/pawn.obj",
        new Coordinates(15f, 15f, 15f));

    m_simpleModelHandler.setTexture("pawn", "textures/dirt.png");
    m_simpleModelHandler.setColor("pawn", Color.SCARLET);
    m_simpleModelHandler.setColor("cube-01", Color.FIREBRICK);

    m_environment = new Environment();
    m_environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1.0f));
    m_environment.add(new DirectionalLight().set(0.8f,0.8f,0.8f,-1f,-0.8f,-0.2f));

    m_modelBatch = new ModelBatch();

    CopperValues values = new CopperValues();
    values.setObjectVariable("test-01", "health", 3);
    values.setObjectVariable("test-01", "dead", true);
    values.setObjectVariable("test-02", "health", 2);
    values.setObjectVariable("test-02", "dead", false);

    System.out.println(values.getObjectVariable("test-01", "health"));
    System.out.println(values.getObjectVariable("test-01", "dead"));
    System.out.println(values.getObjectVariable("test-02", "health"));
    System.out.println(values.getObjectVariable("test-02", "dead"));

//    m_physics.addCubeModel(m_simpleModelHandler.get("cube-physical"));
  }

  @Override
  public void render() {
    Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
    m_player.update();
    m_physics.update();

    m_modelBatch.begin(m_player.getCamera());

    for (Map.Entry<String, HeliumModelInstance> entry : m_simpleModelHandler.getModelInstances().entrySet()) {
      m_modelBatch.render(entry.getValue(), m_environment);
    }

    m_modelBatch.end();

    //TODO: Change resolution on window resize
    if (m_input.isKeyDown(KeyInput.Action.TOGGLE_FULLSCREEN, true)) {
      if (Gdx.graphics.isFullscreen()) {
        Gdx.graphics.setWindowedMode(1600, 900);
      } else {
        Gdx.graphics.setFullscreenMode(Gdx.graphics.getDisplayMode());
      }
    }

    if(m_input.isKeyDown(KeyInput.Action.DEBUG_KILL, true)) {
      Gdx.app.exit();
    }

    Gdx.input.setCursorCatched(true);
  }

  @Override
  public void dispose() {
    m_simpleModelHandler.clear();
    m_physics.dispose();
  }
}
