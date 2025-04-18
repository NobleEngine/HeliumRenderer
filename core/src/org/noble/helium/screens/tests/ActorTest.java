package org.noble.helium.screens.tests;

import com.badlogic.gdx.math.Vector3;
import org.noble.helium.actors.Enemy;
import org.noble.helium.handling.ModelHandler;
import org.noble.helium.math.Dimensions3;
import org.noble.helium.screens.HeliumLevel;
import org.noble.helium.world.WorldObject;

public class ActorTest extends HeliumLevel {
  Enemy enemy;
  public ActorTest() {
    super();
  }

  @Override
  public void init() {
    m_modelHandler.addNewOBJModel("enemy", "models/Pawn/pawn.obj", new Vector3(200,11,200));

    enemy = new Enemy(new Vector3(200,12,200),10,10,5, m_modelHandler.get("enemy"), m_player);

    m_modelHandler.addNewShape("ground", ModelHandler.Shape.CUBE, "textures/dirt.png", new Vector3(), new Dimensions3(1000, 10, 1000));
    m_objectHandler.add("ground", new WorldObject(m_modelHandler.get("ground"), WorldObject.CollisionType.STANDARD));
    m_player.setPosition(new Vector3(250,11,250));
    m_player.setSpeed(m_player.getSpeed() * 3);
    super.init();
  }

  @Override
  public void render(float delta) {
    if(enemy != null && !enemy.isDead()) {
      enemy.update();
    }
    super.render(delta);
  }
}
