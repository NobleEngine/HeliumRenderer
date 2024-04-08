package org.noble.helium;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.bullet.*;
import com.badlogic.gdx.physics.bullet.collision.btBoxShape;
import com.badlogic.gdx.physics.bullet.collision.btCollisionConfiguration;
import com.badlogic.gdx.physics.bullet.collision.btDispatcher;
import com.badlogic.gdx.physics.bullet.dynamics.btDiscreteDynamicsWorld;
import com.badlogic.gdx.physics.bullet.dynamics.btRigidBody;
import com.badlogic.gdx.physics.bullet.linearmath.btDefaultMotionState;

import java.util.ArrayList;

public class PhysicsHandler {
  private static PhysicsHandler m_instance;
  private btCollisionConfiguration  m_collisionConfig;
  private btDispatcher m_dispatcher;


  private PhysicsHandler() {
    Bullet.init();
  }

  public static PhysicsHandler getInstance() {
    if(m_instance == null) {
      m_instance = new PhysicsHandler();
    }
    return m_instance;
  }

  public void update() {
  }

  public void dispose() {
  }
}
