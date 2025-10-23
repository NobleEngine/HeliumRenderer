package org.noble.helium.screens.tests;

import com.badlogic.gdx.graphics.Color;

import com.badlogic.gdx.math.Vector3;
import org.noble.helium.actors.Enemy;
import org.noble.helium.handling.LevelHandler;
import org.noble.helium.math.Dimensions3;
import org.noble.helium.rendering.HeliumModelBuilder;
import org.noble.helium.screens.HeliumLevel;
import org.noble.helium.world.WorldObject;

public class PhysicsTest extends HeliumLevel {

  public PhysicsTest() {
    super();
  }

  @Override
  public void init() {
    super.init();
    m_actorHandler.addActor(new Enemy(new WorldObject(m_modelBuilder.create(HeliumModelBuilder.Shape.CUBE,
        m_textureHandler.getTexture(Color.RED), new Dimensions3(2f, 2f, 2f)),
        new Vector3(100f, 5f, 0f), WorldObject.CollisionType.NONE), 10,
        10, 5f, m_player));

    new WorldObject(m_modelBuilder.create(HeliumModelBuilder.Shape.CUBE,m_textureHandler.getTexture(Color.CORAL),
        new Dimensions3(50f, 100f, 5f)),new Vector3(0f, 10f, 20f),WorldObject.CollisionType.STANDARD);
    new WorldObject(m_modelBuilder.create(HeliumModelBuilder.Shape.CUBE, m_textureHandler.getTexture(Color.OLIVE),
        new Dimensions3(30f, 1f, 50f)),new Vector3(50f, 0f, 0f),WorldObject.CollisionType.STANDARD);
    new WorldObject(m_modelBuilder.create(HeliumModelBuilder.Shape.CUBE,m_textureHandler.getTexture(Color.FIREBRICK),
        new Dimensions3(30f, 10f, 50f)),new Vector3(),WorldObject.CollisionType.STANDARD);
    new WorldObject(m_modelBuilder.create(HeliumModelBuilder.Shape.CUBE, m_textureHandler.getTexture(Color.CHARTREUSE),
        new Dimensions3(30f, 1f, 50f)),new Vector3(90f, 0f, 0f),WorldObject.CollisionType.STANDARD);
    new WorldObject(m_modelBuilder.create(HeliumModelBuilder.Shape.CUBE, m_textureHandler.getTexture("textures/Sky/dusan-osmokrovic.jpg"),
        new Dimensions3(30f, 30f, 30f)),new Vector3(130f, 0f, 0f),WorldObject.CollisionType.STANDARD);
    new WorldObject(m_modelBuilder.create(HeliumModelBuilder.Shape.CUBE, m_textureHandler.getTexture(Color.WHITE),
        new Dimensions3(5f, 100.1f, 16f)),new Vector3(5f, 10f, 25f),WorldObject.CollisionType.CLIMBABLE);

    for(int i = 0; i < 200; i++) {
      new WorldObject(m_modelBuilder.create(HeliumModelBuilder.Shape.CUBE, m_textureHandler.getTexture(Color.BLUE),
          new Dimensions3(2f,1f, 30f)),new Vector3(170 + (i), i, 0),WorldObject.CollisionType.CLIMBABLE);
    }

    m_player.setPosition(new Vector3(0, 50, 0));
  }

  @Override
  public void render(float delta) {
    super.render(delta);

    if (m_player.getPosition().y < -100f) {
      m_player.setPosition(new Vector3(m_player.getX(), 150, m_player.getZ()));
      LevelHandler.getInstance().changeScreen("test.lda");
    }
  }
}
