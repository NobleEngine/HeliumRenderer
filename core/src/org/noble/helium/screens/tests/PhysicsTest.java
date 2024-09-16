package org.noble.helium.screens.tests;

import com.badlogic.gdx.graphics.Color;

import com.badlogic.gdx.math.Vector3;
import org.noble.helium.handling.LevelHandler;
import org.noble.helium.handling.ModelHandler;
import org.noble.helium.helpers.Dimensions3;
import org.noble.helium.screens.HeliumLevel;
import org.noble.helium.world.WorldObject;

import java.util.Objects;

public class PhysicsTest extends HeliumLevel {
  public PhysicsTest() {
    super();
  }

  @Override
  public void init() {
    m_modelHandler.addNewShape(
        "wall-01", ModelHandler.Shape.CUBE, Color.CORAL,
        new Vector3(0, 10, 20), new Dimensions3(50,100,5));
    m_modelHandler.addNewShape(
        "cube-01", ModelHandler.Shape.CUBE, Color.FIREBRICK,
        new Vector3(), new Dimensions3(30f,10f,50f));
    m_modelHandler.addNewShape(
        "cube-02", ModelHandler.Shape.CUBE, Color.OLIVE,
        new Vector3(50f,0f,0f), new Dimensions3(30f,1f,50f));
    m_modelHandler.addNewShape(
        "cube-03", ModelHandler.Shape.CUBE, Color.CHARTREUSE,
        new Vector3(90f,0f,0f), new Dimensions3(30f,1f,50f));
    m_modelHandler.addNewShape(
        "cube-04", ModelHandler.Shape.CUBE, "textures/dirt.png",
        new Vector3(130f,0f,0f), new Dimensions3(30f,1f,50f));
    m_modelHandler.addNewShape(
        "ladder", ModelHandler.Shape.CUBE, Color.WHITE,
        new Vector3(5,10,25), new Dimensions3(5f,100.1f,16f));

    for(int i = 0; i < 200; i++) {
      m_modelHandler.addNewShape(
          "stair-" + i, ModelHandler.Shape.CUBE, Color.BLUE,
          new Vector3(170 + (i), i, 0), new Dimensions3(2, 1, 30));
      m_objectHandler.add("stair" + i, new WorldObject(m_modelHandler.get("stair-" + i),
          WorldObject.ShapeType.BOX, WorldObject.CollisionType.CLIMBABLE));
    }

    m_objectHandler.add("wall1", new WorldObject(m_modelHandler.get("wall-01"),
        WorldObject.ShapeType.BOX, WorldObject.CollisionType.STANDARD));
    m_objectHandler.add("ladder", new WorldObject(m_modelHandler.get("ladder"),
        WorldObject.ShapeType.BOX, WorldObject.CollisionType.CLIMBABLE));
    m_objectHandler.add("floor1", new WorldObject(m_modelHandler.get("cube-01"),
        WorldObject.ShapeType.BOX, WorldObject.CollisionType.STANDARD));
    m_objectHandler.add("floor2", new WorldObject(m_modelHandler.get("cube-02"),
        WorldObject.ShapeType.BOX, WorldObject.CollisionType.STANDARD));
    m_objectHandler.add("floor3", new WorldObject(m_modelHandler.get("cube-03"),
        WorldObject.ShapeType.BOX, WorldObject.CollisionType.STANDARD));
    m_objectHandler.add("floor4", new WorldObject(m_modelHandler.get("cube-04"),
        WorldObject.ShapeType.BOX, WorldObject.CollisionType.STANDARD));

    m_player.setPosition(new Vector3(0,50,0));
    super.init();
  }

  @Override
  public void render(float delta) {
    super.render(delta);

    if(m_player.getPosition().y < -100f) {
      m_player.setPosition(new Vector3(m_player.getX(),150,m_player.getZ()));
//      m_player.setVerticalVelocity(0);
    }


    m_batch.end();
    if(m_player.getX() > 50f && !Objects.equals(LevelHandler.getInstance().getPreviousLevelName(), "PhysicsTest")) {
      LevelHandler.getInstance().changeScreen(new PhysicsTest());
    }
  }
}
