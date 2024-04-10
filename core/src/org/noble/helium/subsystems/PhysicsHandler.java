package org.noble.helium.subsystems;

import com.badlogic.gdx.physics.bullet.*;
import com.badlogic.gdx.physics.bullet.collision.*;
import org.noble.helium.world.WorldObject;

import java.util.HashMap;

public class PhysicsHandler extends Subsystem {
  private static PhysicsHandler m_instance;
  private final btCollisionConfiguration  m_collisionConfig;
  private final btDispatcher m_dispatcher;
  private final HashMap<WorldObject, Boolean> m_objects;


  private PhysicsHandler() {
    Bullet.init();
    m_collisionConfig = new btDefaultCollisionConfiguration();
    m_dispatcher = new btCollisionDispatcher(m_collisionConfig);
    m_objects = new HashMap<>();
  }

  public static PhysicsHandler getInstance() {
    if(m_instance == null) {
      m_instance = new PhysicsHandler();
    }
    return m_instance;
  }

  public btDispatcher getDispatcher() {
    return m_dispatcher;
  }

  @Override
  public void update() {
  }

  @Override
  public void dispose() {
  }
}
