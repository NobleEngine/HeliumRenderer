package org.noble.helium.screens.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import org.noble.helium.handling.SimpleModelHandler;
import org.noble.helium.helpers.Coordinates;
import org.noble.helium.helpers.Dimensions;
import org.noble.helium.screens.BaseScreen;
import org.noble.helium.subsystems.Physics;
import org.noble.helium.world.WorldObject;

public class PhysicsTest extends BaseScreen {
  Physics m_physics = Physics.getInstance();

  public PhysicsTest() {
    super();
    m_simpleModelHandler.addNewShape(
        "cube-01", SimpleModelHandler.Shape.CUBE, new Texture(Gdx.files.internal("textures/dirt.png")),
        new Coordinates(0f,0f,0f), new Dimensions(10f,100f,100f));
    m_simpleModelHandler.addNewShape(
        "cube-02", SimpleModelHandler.Shape.CUBE, new Texture(Gdx.files.internal("textures/dirt.png")),
        new Coordinates(0f,60f,0f), new Dimensions(10f,50f,50f));

    m_objectHandler.add("floor1", new WorldObject(m_simpleModelHandler.get("cube-01"), WorldObject.ShapeType.BOX));
    m_objectHandler.add("floor2", new WorldObject(m_simpleModelHandler.get("cube-02"), WorldObject.ShapeType.BOX));
    m_player.setDebug(false);
    m_player.setPosition(new Coordinates(0,100,0));
  }

  @Override
  public void render(float delta) {
    super.render(delta);
    WorldObject floor1 = m_objectHandler.get("floor1");
    WorldObject floor2 = m_objectHandler.get("floor2");
    floor1.update();
    floor2.update();

//    if(!cube.isColliding(floor)) {
//      cube.setPosition(cube.getX(), cube.getY() - (delta * 8f), cube.getZ());
//    } else {
//      System.out.println("Cube X: " + cube.getX() + ", Y: " + cube.getY() + ", Z: " + cube.getZ());
//      System.out.println("Floor X: " + floor.getX() + ", Y: " + floor.getY() + ", Z: " + floor.getZ());
//    }

    if(!(m_physics.checkCollision(floor1.getBody(), m_player.getObject().getBody()) || m_physics.checkCollision(floor2.getBody(), m_player.getObject().getBody()))) {
      m_player.setPosition(new Coordinates(m_player.getX(), m_player.getY() - (8f * delta), m_player.getZ()));
    }

    m_batch.end();
  }
}
