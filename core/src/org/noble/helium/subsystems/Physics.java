package org.noble.helium.subsystems;

import com.badlogic.gdx.physics.bullet.*;
import com.badlogic.gdx.physics.bullet.collision.*;
import org.noble.helium.world.WorldObject;

import java.util.HashMap;

public class Physics extends Subsystem {
  private static Physics m_instance;
  private final btCollisionConfiguration  m_collisionConfig;
  private final btDispatcher m_dispatcher;
  private final HashMap<WorldObject, Boolean> m_objects;


  private Physics() {
    Bullet.init();
    m_collisionConfig = new btDefaultCollisionConfiguration();
    m_dispatcher = new btCollisionDispatcher(m_collisionConfig);
    m_objects = new HashMap<>();
  }

  public static Physics getInstance() {
    if(m_instance == null) {
      m_instance = new Physics();
    }
    return m_instance;
  }

  public btDispatcher getDispatcher() {
    return m_dispatcher;
  }

  public void reset() {
    m_objects.clear();
  }

  @Override
  public void update() {
  }

  @Override
  public void dispose() {
  }
}
