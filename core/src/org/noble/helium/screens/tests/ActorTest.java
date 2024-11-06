package org.noble.helium.screens.tests;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector3;
import org.noble.helium.handling.ModelHandler;
import org.noble.helium.math.Dimensions3;
import org.noble.helium.screens.HeliumLevel;
import org.noble.helium.world.WorldObject;

public class ActorTest extends HeliumLevel {
  public ActorTest() {
    super();
  }

  @Override
  public void init() {
    m_modelHandler.addNewShape("ground", ModelHandler.Shape.CUBE, Color.GOLDENROD, new Vector3(), new Dimensions3(1000, 10, 1000));
    m_objectHandler.add("ground", new WorldObject(m_modelHandler.get("ground"), WorldObject.ShapeType.BOX, WorldObject.CollisionType.STANDARD));
    m_player.setPosition(new Vector3(250,11,250));
    super.init();
  }

  @Override
  public void render(float delta) {
    super.render(delta);
  }
}
