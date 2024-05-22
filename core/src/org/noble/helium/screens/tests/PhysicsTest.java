package org.noble.helium.screens.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import org.noble.helium.handling.SimpleModelHandler;
import org.noble.helium.helpers.Coordinates;
import org.noble.helium.helpers.Dimensions;
import org.noble.helium.screens.BaseScreen;
import org.noble.helium.world.WorldObject;

public class PhysicsTest extends BaseScreen {
  public PhysicsTest() {
    super();
    m_simpleModelHandler.addNewShape(
        "cube-01", SimpleModelHandler.Shape.CUBE, new Texture(Gdx.files.internal("textures/dirt.png")),
        new Coordinates(0f,0f,0f), new Dimensions(20f,100f,100f));
    m_simpleModelHandler.addNewShape(
        "cube-physical", SimpleModelHandler.Shape.CUBE, Color.BLUE,
        new Coordinates(5f, 50f, 5f), new Dimensions(5f, 5f, 5f));
    m_objectHandler.add("floor", new WorldObject(m_simpleModelHandler.get("cube-01"), WorldObject.ShapeType.BOX, WorldObject.ObjectType.STATIC));
    m_objectHandler.add("cube", new WorldObject(m_simpleModelHandler.get("cube-physical"), WorldObject.ShapeType.BOX, WorldObject.ObjectType.PHYSICS));
    m_player.setDebug(true);
  }

  @Override
  public void render(float delta) {
    super.render(delta);
    WorldObject cube = m_objectHandler.get("cube");
    WorldObject floor = m_objectHandler.get("floor");
    cube.update();
    floor.update();

    if(!cube.isColliding(floor)) {
      cube.setPosition(cube.getX(), cube.getY() - (delta * 8f), cube.getZ());
    } else {
      System.out.println("Cube X: " + cube.getX() + ", Y: " + cube.getY() + ", Z: " + cube.getZ());
      System.out.println("Floor X: " + floor.getX() + ", Y: " + floor.getY() + ", Z: " + floor.getZ());
    }

    m_batch.end();
  }
}
