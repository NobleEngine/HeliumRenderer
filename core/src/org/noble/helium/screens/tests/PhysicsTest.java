package org.noble.helium.screens.tests;

import com.badlogic.gdx.graphics.Color;

import org.noble.helium.handling.SimpleModelHandler;
import org.noble.helium.helpers.Coordinates;
import org.noble.helium.helpers.Dimensions;
import org.noble.helium.screens.BaseScreen;
import org.noble.helium.world.WorldObject;

public class PhysicsTest extends BaseScreen {
  public PhysicsTest() {
    super();
    m_simpleModelHandler.addNewShape(
        "cube-01", SimpleModelHandler.Shape.CUBE, Color.RED,
        new Coordinates(0f,0f,0f), new Dimensions(10f,30f,100f));
    m_simpleModelHandler.addNewShape(
        "cube-02", SimpleModelHandler.Shape.CUBE, Color.RED,
        new Coordinates(50f,0f,0f), new Dimensions(10f,30f,100f));
    m_simpleModelHandler.addNewShape(
        "cube-03", SimpleModelHandler.Shape.CUBE, Color.RED,
        new Coordinates(90f,0f,0f), new Dimensions(10f,30f,100f));
    m_simpleModelHandler.addNewShape(
        "cube-04", SimpleModelHandler.Shape.CUBE, Color.RED,
        new Coordinates(130f,0f,0f), new Dimensions(10f,30f,100f));

    m_objectHandler.add("floor1", new WorldObject(m_simpleModelHandler.get("cube-01"), WorldObject.ShapeType.BOX));
    m_objectHandler.add("floor2", new WorldObject(m_simpleModelHandler.get("cube-02"), WorldObject.ShapeType.BOX));
    m_objectHandler.add("floor3", new WorldObject(m_simpleModelHandler.get("cube-03"), WorldObject.ShapeType.BOX));
    m_objectHandler.add("floor4", new WorldObject(m_simpleModelHandler.get("cube-04"), WorldObject.ShapeType.BOX));

    m_player.setDebug(false);
    m_player.setPosition(new Coordinates(0,100,0));
  }

  @Override
  public void render(float delta) {
    super.render(delta);

//    if(!cube.isColliding(floor)) {
//      cube.setPosition(cube.getX(), cube.getY() - (delta * 8f), cube.getZ());
//    } else {
//      System.out.println("Cube X: " + cube.getX() + ", Y: " + cube.getY() + ", Z: " + cube.getZ());
//      System.out.println("Floor X: " + floor.getX() + ", Y: " + floor.getY() + ", Z: " + floor.getZ());
//    }

    m_batch.end();
  }
}
