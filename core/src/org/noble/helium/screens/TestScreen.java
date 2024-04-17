package org.noble.helium.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Environment;
import com.badlogic.gdx.graphics.g3d.ModelBatch;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import org.noble.helium.Helium;
import org.noble.helium.actors.PlayerController;
import org.noble.helium.handling.ObjectHandler;
import org.noble.helium.handling.SimpleModelHandler;
import org.noble.helium.helpers.Coordinates;
import org.noble.helium.helpers.Dimensions;
import org.noble.helium.subsystems.UserInterface;
import org.noble.helium.world.WorldObject;

import java.util.Map;

public class TestScreen implements Screen {
  private final SimpleModelHandler m_simpleModelHandler;
  private final ObjectHandler m_objectHandler;
  private final Environment m_environment;
  private final UserInterface m_userInterface;
  private final PlayerController m_player;
  private final ModelBatch m_modelBatch;
  public TestScreen() {
    m_simpleModelHandler = SimpleModelHandler.getInstance();
    m_objectHandler = ObjectHandler.getInstance();
    m_userInterface = UserInterface.getInstance();
    m_player = PlayerController.getInstance();
    m_modelBatch = Helium.getInstance().getModelBatch();

    m_simpleModelHandler.addNewShape(
        "cube-01", SimpleModelHandler.Shape.CUBE, new Texture(Gdx.files.internal("textures/dirt.png")),
        new Coordinates(0f,0f,0f), new Dimensions(10f,100f,100f));
    m_simpleModelHandler.addNewShape(
        "cube-physical", SimpleModelHandler.Shape.CUBE, Color.BLUE,
        new Coordinates(5f, 50f, 5f), new Dimensions(5f, 5f, 5f));

    m_objectHandler.add("cube-01",
        new WorldObject(m_simpleModelHandler.get("cube-01"),
            WorldObject.ShapeType.BOX, WorldObject.ObjectType.STATIC));
    m_objectHandler.add("cube-physical",
        new WorldObject(m_simpleModelHandler.get("cube-physical"),
            WorldObject.ShapeType.BOX, WorldObject.ObjectType.PHYSICS));

    m_userInterface.addLabel("FPS", "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, 10, 100, 25);

    m_environment = new Environment();
    m_environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1.0f));
    m_environment.add(new DirectionalLight().set(0.8f,0.8f,0.8f,-1f,-0.8f,-0.2f));;
  }

  @Override
  public void show() {

  }

  @Override
  public void render(float delta) {
//    WorldObject cube01 = m_objectHandler.get("cube-01");
//    WorldObject cubePhysical = m_objectHandler.get("cube-physical");
//    if (!cubePhysical.isColliding(cube01)) {
//      cubePhysical.setPosition(cubePhysical.getX(), cubePhysical.getY() - (10f * delta), cubePhysical.getZ());
//    }

    m_modelBatch.begin(PlayerController.getInstance().getCamera());
//    for (Map.Entry<String, WorldObject> entry : ObjectHandler.getInstance().getAllObjects().entrySet()) {
//      m_modelBatch.render(entry.getValue().getModelInstance(), m_environment);
//    }
    m_modelBatch.end();

    m_userInterface.setLabel("FPS", "FPS " + Gdx.graphics.getFramesPerSecond());
  }

  @Override
  public void resize(int width, int height) {

  }

  @Override
  public void pause() {

  }

  @Override
  public void resume() {

  }

  @Override
  public void hide() {

  }

  @Override
  public void dispose() {

  }
}
