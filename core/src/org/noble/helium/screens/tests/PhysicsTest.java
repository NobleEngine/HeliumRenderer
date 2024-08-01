package org.noble.helium.screens.tests;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import org.noble.helium.handling.SimpleModelHandler;
import org.noble.helium.helpers.Dimensions;
import org.noble.helium.screens.BaseScreen;
import org.noble.helium.world.WorldObject;

public class PhysicsTest extends BaseScreen {
  public PhysicsTest() {
    super();
    m_simpleModelHandler.addNewShape(
        "wall-01", SimpleModelHandler.Shape.CUBE, Color.CORAL,
        new Vector3(0, 10, 20), new Dimensions(50,100,5));
    m_simpleModelHandler.addNewShape(
        "cube-01", SimpleModelHandler.Shape.CUBE, Color.FIREBRICK,
        new Vector3(), new Dimensions(30f,10f,50f));
    m_simpleModelHandler.addNewShape(
        "cube-02", SimpleModelHandler.Shape.CUBE, Color.OLIVE,
        new Vector3(50f,0f,0f), new Dimensions(30f,1f,50f));
    m_simpleModelHandler.addNewShape(
        "cube-03", SimpleModelHandler.Shape.CUBE, Color.CHARTREUSE,
        new Vector3(90f,0f,0f), new Dimensions(30f,1f,50f));
    m_simpleModelHandler.addNewShape(
        "cube-04", SimpleModelHandler.Shape.CUBE, new Texture(Gdx.files.internal("textures/dirt.png")),
        new Vector3(130f,0f,0f), new Dimensions(30f,1f,50f));
    m_simpleModelHandler.addNewShape(
        "ladder", SimpleModelHandler.Shape.CUBE, Color.WHITE,
        new Vector3(5,10,25), new Dimensions(5f,100.1f,16f));

    for(int i = 0; i < 200; i++) {
      m_simpleModelHandler.addNewShape(
          "stair-" + i, SimpleModelHandler.Shape.CUBE, Color.BLUE,
          new Vector3(170 + (i), i, 0), new Dimensions(2, 1, 30));
      m_objectHandler.add("stair" + i, new WorldObject(m_simpleModelHandler.get("stair-" + i),
          WorldObject.ShapeType.BOX, WorldObject.CollisionType.CLIMBABLE));
    }

    m_objectHandler.add("wall1", new WorldObject(m_simpleModelHandler.get("wall-01"),
        WorldObject.ShapeType.BOX, WorldObject.CollisionType.STANDARD));
    m_objectHandler.add("ladder", new WorldObject(m_simpleModelHandler.get("ladder"),
        WorldObject.ShapeType.BOX, WorldObject.CollisionType.CLIMBABLE));
    m_objectHandler.add("floor1", new WorldObject(m_simpleModelHandler.get("cube-01"),
        WorldObject.ShapeType.BOX, WorldObject.CollisionType.STANDARD));
    m_objectHandler.add("floor2", new WorldObject(m_simpleModelHandler.get("cube-02"),
        WorldObject.ShapeType.BOX, WorldObject.CollisionType.STANDARD));
    m_objectHandler.add("floor3", new WorldObject(m_simpleModelHandler.get("cube-03"),
        WorldObject.ShapeType.BOX, WorldObject.CollisionType.NONE));
    m_objectHandler.add("floor4", new WorldObject(m_simpleModelHandler.get("cube-04"),
        WorldObject.ShapeType.BOX, WorldObject.CollisionType.STANDARD));

    m_player.setPosition(new Vector3(0,50,0));
  }

  @Override
  public void render(float delta) {
    super.render(delta);

    if(m_player.getPosition().y < -100f) {
      m_player.setPosition(new Vector3(m_player.getX(),150,m_player.getZ()));
//      m_player.setVerticalVelocity(0);
    }

    m_batch.end();
  }
}
