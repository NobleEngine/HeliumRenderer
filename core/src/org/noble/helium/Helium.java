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
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.kotcrab.vis.ui.VisUI;
import com.kotcrab.vis.ui.widget.VisLabel;
import org.noble.helium.actors.PlayerController;
import org.noble.helium.handling.ObjectHandler;
import org.noble.helium.handling.SimpleModelHandler;
import org.noble.helium.helpers.Coordinates;
import org.noble.helium.helpers.Dimensions;
import org.noble.helium.io.KeyInput;
import org.noble.helium.subsystems.PhysicsHandler;
import org.noble.helium.subsystems.Subsystem;
import org.noble.helium.world.WorldObject;

import java.util.ArrayList;
import java.util.Map;

public class Helium extends Game {
  private static Helium m_instance = null;
  private final ArrayList<Subsystem> m_subsystems;
  private Environment m_environment;
  private SimpleModelHandler m_simpleModelHandler;
  private ObjectHandler m_objectHandler;
  private KeyInput m_input;
  private PlayerController m_player;
  private ModelBatch m_modelBatch;
  private PhysicsHandler m_physics;

  private VisLabel m_fpsLabel;
  private Stage m_2dStage;

  private Helium() {
    m_subsystems = new ArrayList<>();
  }

  public static Helium getInstance() {
    if(m_instance == null) {
      m_instance = new Helium();
    }
    return m_instance;
  }

  @Override
  public void create() {
    System.out.println("Helium Renderer started!");
    System.out.print("Setting up model handler... ");
    m_simpleModelHandler = SimpleModelHandler.getInstance();
    System.out.println("Done!");
    System.out.print("Setting up keyboard inputs... ");
    m_input = KeyInput.getInstance();
    System.out.println("Done!");
    System.out.print("Setting up physics subsystem... ");
    m_physics = PhysicsHandler.getInstance();
    m_subsystems.add(m_physics);
    System.out.println("Done!");
    System.out.print("Setting up object handler... ");
    m_objectHandler = ObjectHandler.getInstance();
    System.out.println("Done!");
    System.out.print("Setting up player controller... ");
    m_player = PlayerController.getInstance();
    System.out.println("Done!");
    System.out.print("Setting up user interface... ");

    VisUI.load(); //TODO: Make some handler or helper for this
    m_fpsLabel = new VisLabel();
    m_fpsLabel.setPosition(10,10);
    m_2dStage = new Stage(new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight()));
    m_2dStage.addActor(m_fpsLabel);

    System.out.println("Done!");
    System.out.println("\nInit complete");

    m_simpleModelHandler.addNewShape(
        "cube-01", SimpleModelHandler.Shape.CUBE, new Texture(Gdx.files.internal("textures/dirt.png")),
        new Coordinates(0f,0f,0f), new Dimensions(100f,100f,10f));
    m_simpleModelHandler.addNewShape(
        "cube-physical", SimpleModelHandler.Shape.CUBE, Color.BLUE,
        new Coordinates(5f, 50f, 5f), new Dimensions(5f, 5f, 5f));

    m_objectHandler.add("cube-01",
        new WorldObject(m_simpleModelHandler.get("cube-01"),
            WorldObject.ShapeType.BOX, WorldObject.ObjectType.STATIC));
    m_objectHandler.add("cube-physical",
        new WorldObject(m_simpleModelHandler.get("cube-physical"),
            WorldObject.ShapeType.BOX, WorldObject.ObjectType.PHYSICS));

    m_environment = new Environment();
    m_environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1.0f));
    m_environment.add(new DirectionalLight().set(0.8f,0.8f,0.8f,-1f,-0.8f,-0.2f));

    m_modelBatch = new ModelBatch();
  }

  @Override
  public void render() {
    float delta = Gdx.graphics.getDeltaTime();
    Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
    m_player.update();
    m_subsystems.forEach(Subsystem::update);

    WorldObject cube01 = m_objectHandler.get("cube-01");
    WorldObject cubePhysical = m_objectHandler.get("cube-physical");

    if(!cubePhysical.isColliding(cube01)) {
      cubePhysical.setPosition(cubePhysical.getX(), cubePhysical.getY() - (10f * delta), cubePhysical.getZ());
    }

    m_modelBatch.begin(m_player.getCamera());
    for (Map.Entry<String, WorldObject> entry : m_objectHandler.getAllObjects().entrySet()) {
      m_modelBatch.render(entry.getValue().getModelInstance(), m_environment);
    }
    m_modelBatch.end();

    m_fpsLabel.setText("FPS: " + Gdx.graphics.getFramesPerSecond());
    m_2dStage.act();
    m_2dStage.draw();

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
    m_subsystems.forEach(Subsystem::dispose);
  }
}
